package uy.com.pf.care.services;

import com.mongodb.client.result.UpdateResult;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.infra.config.ParamConfig;
import uy.com.pf.care.infra.repos.IFormalCaregiverScoreRepo;
import uy.com.pf.care.model.documents.FormalCaregiverScore;

import java.util.List;
import java.util.Optional;

@Service
@Log
public class FormalCaregiverScoreService implements IFormalCaregiverScoreService {

    @Autowired
    private IFormalCaregiverScoreRepo formalCaregiverScoreRepo;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ParamConfig paramConfig;

    @Override
    public String save(FormalCaregiverScore formalCaregiverScore) {

        String formalCaregiverScoreId = null;
        try{

            //********************************* prueba con CoreNPL ******************

            //Ejemplo 1:
            //SentimentAnalyzer sent = new SentimentAnalyzer();
            //log.info(String.valueOf(sent.stanfordScore("Frida es una buena perra")));

            //Ejemplo 2:
            /*Properties props = new Properties();
            props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, sentiment");
            //props.setProperty("pos.model", "edu/stanford/nlp/models/pos-tagger/spanish/spanish-distsim.tagger");
            StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
            Annotation document = new Annotation("Este es un texto para analizar.");
            pipeline.annotate(document);

            List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

            //sentiments.add(sentence.get(CoreAnnotations.SentimentCoreAnnotations.SentimentAnnotatedTree.class));
            List<CoreMap> sentiments = new ArrayList<>(sentences);

            for (CoreMap sentiment : sentiments) {
                System.out.println(sentiment.toString());
            }*/

            //****************************************

            // Agrego el puntaje a la colección FormalCaregiverScore
            formalCaregiverScoreId = formalCaregiverScoreRepo.save(formalCaregiverScore).getFormalCaregiverScoreId();

            // Actualizo votos en coleccion FormalCaregivers
            //      * previousScore = -1: indica que no hay un score previo, ya que es un nuevo documento
            if (this.updateVotesFormalCaregiver(formalCaregiverScore, -1))
                return formalCaregiverScoreId;
            return null;

            // el cuidador formal ya tiene un voto registrado del paciente
        } catch (DuplicateKeyException e) {
            String msg = "El paciente ya habia calificado al Cuidador Formal";
            log.warning(msg);
            throw new FormalCaregiverScoreDuplicateKeyException(msg);

        }catch (FormalCaregiverScoreUpdateVotesException e){
            try{
                // Borro fisicamente el puntaje dado de alta en la colección FormalCaregiversScores para mantener la
                // integridad de la bbdd
                this.physicallyDeleteScore(formalCaregiverScoreId);

            }catch (FormalCaregiverScorePhysicallyDeleteException e1){
                log.warning(e1.getMessage());
                throw new FormalCaregiverScorePhysicallyDeleteException(e1.getMessage());
            }
            throw new FormalCaregiverScoreUpdateVotesException(e.getMessage());

        } catch(Exception e){
            String msg = "Error guardando puntaje del cuidador formal.";
            log.warning(msg + " " + formalCaregiverScore + e.getMessage());
            throw new FormalCaregiverScoreSaveException(msg);
        }
    }

    @Override
    public List<FormalCaregiverScore> findAll(String formalCaregiverId) {
        return formalCaregiverScoreRepo.findByFormalCaregiverIdOrderByDateDesc(formalCaregiverId);
    }

    @Override
    public Optional<FormalCaregiverScore> findId(String formalCaregiverScoreId) {
        //return formalCaregiverScoreRepo.findById(formalCaregiverScoreId);
        Optional<FormalCaregiverScore> found = formalCaregiverScoreRepo.findById(formalCaregiverScoreId);
        if (found.isPresent())
            return found;

        String msg = "No se encontro un score con id " + formalCaregiverScoreId;
        log.warning(msg);
        throw new FormalCaregiverScoreNotFoundException(msg);
    }

    @Override
    public FormalCaregiverScore findScore(String formalCaregiverId, String patientId) {
        return formalCaregiverScoreRepo.findByFormalCaregiverIdAndPatientId(formalCaregiverId, patientId);
    }

    // Actualiza el score + comentario en la colección FormalCaregiversScores, y luego actualiza los votos
    // del Cuidador Formal
    @Override
    public Boolean updateScore(FormalCaregiverScore formalCaregiverScore) {

        // Obtengo el documento almacenado
        FormalCaregiverScore formalCaregiverScoreFound = this.findScore(
                formalCaregiverScore.getFormalCaregiverId(),
                formalCaregiverScore.getPatientId()
        );

        if (formalCaregiverScoreFound == null) {
            String msg = "El cuidador formal no ha sido calificado por el paciente";
            log.info(msg);
            throw new FormalCaregiverScoreNotFoundException(msg);
        }

        try{
            //Actualizo el puntaje en el documento FormalCaregiversScores
            if (! this.updateScoreFormalCaregiver(formalCaregiverScore, ""))
                return false;
            // Actualizo el voto en el documento FormalCaregivers
            return this.updateVotesFormalCaregiver(formalCaregiverScore, formalCaregiverScoreFound.getScore());

        }catch (FormalCaregiverScoreUpdateVotesException e){
            try{
                // Deshago la actualización de la calificacion en la coleccion FormalCaregiverScores
                this.updateScoreFormalCaregiver(formalCaregiverScoreFound,
                        "No se pudo deshacer la actualización de la calificación otorgada al Cuidador Formal " +
                                "con Id " + formalCaregiverScore.getFormalCaregiverId() + " por parte del Paciente " +
                                "con Id " + formalCaregiverScore.getPatientId());
                log.info("Actualización de calificación deshecha con éxito");
                throw new FormalCaregiverScoreUpdateVotesException(e.getMessage());

            }catch (FormalCaregiverScoreUpdateScoreException e1){
                throw new FormalCaregiverScoreUpdateScoreException(e1.getMessage());
            }

        }catch (Exception e){
            throw new FormalCaregiverScoreUpdateScoreException(e.getMessage());
        }
    }

    private String getUrlUpdateVotes(String formalCaregiverId, Integer previousScore, Integer currentScore){
        return getStartUrl() +
                "formal_caregivers/updateVotes/" +
                formalCaregiverId + "/" +
                previousScore + "/" +
                currentScore;
    }

    private String getStartUrl(){
        return paramConfig.getProtocol() + "://" + paramConfig.getSocket() + "/";
    }

    private boolean updateScoreFormalCaregiver(FormalCaregiverScore formalCaregiverScore, String errorMsg){

        Query query = new Query(Criteria.where("formalCaregiverId").is(formalCaregiverScore.getFormalCaregiverId())
                .and("patientId").is(formalCaregiverScore.getPatientId()));
        // Obtengo solo campos "score", "comment" y "date"
        //query.fields().include("score").include("comment").include("date").

        Update update = new Update()
                .set("score", formalCaregiverScore.getScore())
                .set("comment", formalCaregiverScore.getComment())
                .set("date", formalCaregiverScore.getDate())
                .set("feeling", formalCaregiverScore.getFeeling());

        try {
            //Actualizacion atomica
            UpdateResult updateResult = mongoTemplate.updateFirst(query, update, FormalCaregiverScore.class);
            return updateResult.wasAcknowledged();

        }catch (Exception e) {
            if (errorMsg.isEmpty()) {
                log.warning("Error al intentar actualizar la calificación del Cuidador Formal con id " +
                        formalCaregiverScore.getFormalCaregiverId() + " otorgada por el Paciente con id " +
                        formalCaregiverScore.getPatientId() + ". " + e.getMessage());
                throw new FormalCaregiverScoreUpdateScoreException(
                        "Error al intentar actualizar la calificación del Cuidador Formal.");
            } else {
                log.warning(errorMsg + ". " + e.getMessage());
                throw new FormalCaregiverScoreUpdateScoreException(errorMsg);
            }
        }
    }

    private boolean updateVotesFormalCaregiver(FormalCaregiverScore formalCaregiverScore, Integer previousScore){

        WebClient webClient = WebClient.create();

        Mono<Boolean> updateVotesResponse = webClient.put()
                .uri(this.getUrlUpdateVotes(
                        formalCaregiverScore.getFormalCaregiverId(),
                        previousScore,
                        formalCaregiverScore.getScore()))
                .retrieve()
                .bodyToMono(Boolean.class);

        return updateVotesResponse.flatMap(response -> {
            if (response) {
                log.info("Voto actualizado con éxito del Cuidador Formal con Id " +
                        formalCaregiverScore.getFormalCaregiverId());
                return Mono.just(true);
            } else {
                log.info("No se pudo actualizar el voto al Cuidador Formal con id " +
                        formalCaregiverScore.getFormalCaregiverId() +
                        " del paciente  con id " + formalCaregiverScore.getPatientId());
                return Mono.error(new FormalCaregiverScoreUpdateVotesException(
                        "Error al intentar actualizar votos del Cuidador Formal"));
            }
        }).onErrorResume(error -> {
            log.info("Ocurrió un error al intentar actualizar el voto al Cuidador Formal: "
                    + formalCaregiverScore.getFormalCaregiverId() + ". " + error.getMessage());
            return Mono.error(new FormalCaregiverScoreUpdateVotesException(
                    "Error al intentar actualizar votos del Cuidador Formal" + ". " + error.getMessage()));
        }).blockOptional().orElse(false);
    }

    private void physicallyDeleteScore(String formalCaregiverScoreId){
        try{
            formalCaregiverScoreRepo.deleteById(formalCaregiverScoreId);
            log.info("Se ha eliminado la calificación del Cuidador Formal con id: " + formalCaregiverScoreId);

        }catch(IllegalArgumentException e){
            log.warning("No se pudo eliminar la calificación del Cuidador Formal con Id: "
                    + formalCaregiverScoreId +
                    ". Verifique posible inconsistencia de la base de datos en las colecciones FormalCaregiversScores " +
                    "y FormalCaregivers. La clave 'votes' del cuidador formal debe representar la suma de sus " +
                    "calificaciones");
            throw new FormalCaregiverScorePhysicallyDeleteException(
                    "No se pudo eliminar la calificación del Cuidador Formal con Id: " + formalCaregiverScoreId);
        }
    }

}
