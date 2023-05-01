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
import uy.com.pf.care.exceptions.FormalCaregiverScoreDuplicateKeyException;
import uy.com.pf.care.exceptions.FormalCaregiverScoreNotFoundException;
import uy.com.pf.care.exceptions.FormalCaregiverScoreSaveException;
import uy.com.pf.care.exceptions.FormalCaregiverScoreUpdateVotesException;
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
            formalCaregiverScoreId = formalCaregiverScoreRepo.save(formalCaregiverScore).getFormalCaregiverScoreId();

            // previousScore = -1: indica que no hay un score previo, ya que es un nuevo documento
            if (this.updateVotesFormalCaregiver(formalCaregiverScore, -1))
                return formalCaregiverScoreId;
            return null;

            // el cuidador formal ya tiene un voto registrado del paciente
        } catch (DuplicateKeyException e){
            log.warning("DuplicateKeyException: El paciente ya habia calificado al Cuidador Formal");
            throw new FormalCaregiverScoreDuplicateKeyException("El paciente ya había calificado al Cuidador Formal");

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

        if (formalCaregiverScoreFound == null){
            log.info("No existe la calificación para modificar");
            throw new FormalCaregiverScoreNotFoundException("No existe la calificación para modificar");
        }

        Query query = new Query(Criteria.where("formalCaregiverId").is(formalCaregiverScore.getFormalCaregiverId())
                .and("patientId").is(formalCaregiverScore.getPatientId()));
        query.fields().include("score").include("comment"); // Obtengo solo campos "score" y "comment"

        Update update = new Update()
                .set("score", formalCaregiverScore.getScore())
                .set("comment", formalCaregiverScore.getComment());
        //Actualizacion atomica
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, FormalCaregiverScore.class);

        if (!updateResult.wasAcknowledged())
            return false;

        return this.updateVotesFormalCaregiver(formalCaregiverScore, formalCaregiverScoreFound.getScore());
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

    private boolean updateVotesFormalCaregiver(FormalCaregiverScore formalCaregiverScore, Integer previousScore){

        try {
            WebClient webClient = WebClient.create();

            // Actualizo los votos  del cuidador formal
            Mono<Boolean> updateVotesResponse = webClient.post()
                    .uri(this.getUrlUpdateVotes(
                            formalCaregiverScore.getFormalCaregiverId(),
                            previousScore,
                            formalCaregiverScore.getScore()))
                    .retrieve()
                    .bodyToMono(Boolean.class);

            // Manejo la respuesta y un posible error
            updateVotesResponse.subscribe(response -> {
                        if (response)
                            log.info("*** Puntaje asignado con exito al Cuidador Formal: " + LocalDateTime.now());
                        else
                            log.info("No se pudo asignar el puntaje al Cuidador Formal con id " +
                                    formalCaregiverScore.getFormalCaregiverId() +
                                    " del paciente  con id " + formalCaregiverScore.getPatientId());
                    },
                    error -> log.info("Ocurrió un error al intentar asignar el puntaje al Cuidador Formal: "
                            + error.getMessage())
            );
            return true;

        } catch (Exception e) {
            log.warning("Error al intentar actualizar votos del Cuidador Formal: " + e.getMessage());
            throw new FormalCaregiverScoreUpdateVotesException("Error al intentar actualizar votos del Cuidador Formal");
        }
    }
}
