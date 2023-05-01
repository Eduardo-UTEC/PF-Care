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
import uy.com.pf.care.model.documents.FormalCaregiverScore;
import uy.com.pf.care.repos.IFormalCaregiverScoreRepo;

import java.time.LocalDateTime;
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

    //private static final Logger log = LoggerFactory.getLogger(CuidadosApplication.class);

    @Override
    public String save(FormalCaregiverScore formalCaregiverScore) {

        String formalCaregiverScoreId = null;
        try{
            // Agrego el puntaje a la colección FormalCaregiverScore
            formalCaregiverScoreId = formalCaregiverScoreRepo.save(formalCaregiverScore).getFormalCaregiverScoreId();

            // Actualizo votos en coleccion FormalCaregiver
            //      * previousScore = -1: indica que no hay un score previo, ya que es un nuevo documento
            if (this.updateVotesFormalCaregiver(formalCaregiverScore, -1))
                return formalCaregiverScoreId;
            return null;

            // el cuidador formal ya tiene un voto registrado del paciente
        } catch (DuplicateKeyException e) {
            log.warning("DuplicateKeyException: El paciente ya habia calificado al Cuidador Formal");
            throw new FormalCaregiverScoreDuplicateKeyException("El paciente ya había calificado al Cuidador Formal");

        }catch (FormalCaregiverScoreUpdateVotesException e){
            try{
                // Borro fisicamente el puntaje dado de alta en la colección FormalCaregiversScores para mantener la
                // integridad de la bbdd
                this.physicallyDeleteScore(formalCaregiverScoreId);

            }catch (FormalCaregiverScorePhysicallyDeleteException e1){
                throw new FormalCaregiverScorePhysicallyDeleteException(e.getMessage());
            }
            throw new FormalCaregiverScoreUpdateVotesException(e.getMessage());

        } catch(Exception e1){
            log.warning("Error guardando puntaje del cuidador formal: " + e1.getMessage() + ". "
                    + formalCaregiverScore);
            throw new FormalCaregiverScoreSaveException("Error guardando puntaje del cuidador formal:");
        }
    }

    @Override
    public List<FormalCaregiverScore> findAll(String formalCaregiverId) {
        return formalCaregiverScoreRepo.findByFormalCaregiverId(formalCaregiverId);
    }

    @Override
    public Optional<FormalCaregiverScore> findId(String formalCaregiverScoreId) {
        return formalCaregiverScoreRepo.findById(formalCaregiverScoreId);
    }

    @Override
    public FormalCaregiverScore findScore(String formalCaregiverId, String patientId) {
        return formalCaregiverScoreRepo.findByFormalCaregiverIdAndPatientId(formalCaregiverId, patientId);
    }

    // Actualiza el score + comentario en la colección FormalCaregiversScores, y luego actualiza los votos
    // del Cuidador Formal
    @Override
    public Boolean updateScore(FormalCaregiverScore formalCaregiverScore) {

        // Obtengo el documento "original"
        FormalCaregiverScore formalCaregiverScoreFound = this.findScore(
                formalCaregiverScore.getFormalCaregiverId(),
                formalCaregiverScore.getPatientId()
        );

        if (formalCaregiverScoreFound == null) {
            log.info("No existe la calificación para modificar");
            throw new FormalCaregiverScoreNotFoundException("No existe la calificación para modificar");
        }

        try{
            if (! this.updateScoreFormalCaregiver(formalCaregiverScore, ""))
                return false;
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
        query.fields().include("score").include("comment"); // Obtengo solo campos "score" y "comment"

        Update update = new Update()
                .set("score", formalCaregiverScore.getScore())
                .set("comment", formalCaregiverScore.getComment());

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

        Mono<Boolean> updateVotesResponse = webClient.post()
                .uri(this.getUrlUpdateVotes(
                        formalCaregiverScore.getFormalCaregiverId(),
                        previousScore,
                        formalCaregiverScore.getScore()))
                .retrieve()
                .bodyToMono(Boolean.class);

        return updateVotesResponse.flatMap(response -> {
            if (response) {
                log.info("Voto actualizado con éxito del Cuidador Formal con Id " +
                        formalCaregiverScore.getFormalCaregiverId() + ". " + LocalDateTime.now());
                return Mono.just(true);
            } else {
                log.info("No se pudo actualizar el voto al Cuidador Formal con id " +
                        formalCaregiverScore.getFormalCaregiverId() +
                        " del paciente  con id " + formalCaregiverScore.getPatientId() + ". "
                        + LocalDateTime.now());
                return Mono.error(new FormalCaregiverScoreUpdateVotesException(
                        "Error al intentar actualizar votos del Cuidador Formal"));
            }
        }).onErrorResume(error -> {
            log.info("Ocurrió un error al intentar actualizar el voto al Cuidador Formal: "
                    + formalCaregiverScore.getFormalCaregiverId() + ". "
                    + error.getMessage());
            return Mono.error(new FormalCaregiverScoreUpdateVotesException(
                    "Error al intentar actualizar votos del Cuidador Formal"));
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
