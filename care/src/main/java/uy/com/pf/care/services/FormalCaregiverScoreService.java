package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uy.com.pf.care.exceptions.FormalCaregiverRatingSaveException;
import uy.com.pf.care.infra.config.ParamConfig;
import uy.com.pf.care.model.documents.FormalCaregiverScore;
import uy.com.pf.care.model.objects.VoteObject;
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
    MongoTemplate mongoTemplate;
    @Autowired
    private ParamConfig paramConfig;

    //private static final Logger log = LoggerFactory.getLogger(CuidadosApplication.class);

    @Override
    public FormalCaregiverScore save(FormalCaregiverScore formalCaregiverScore) {
        RestTemplate restTemplate = new RestTemplate();
        try{
            FormalCaregiverScore newFormalCaregiverScore = formalCaregiverScoreRepo.save(formalCaregiverScore);

            // Actualizo scoreData en FormalCaregiver
            Boolean updatedScoreData = restTemplate.postForEntity(
                    this.getUrlUpdateVotes(
                            formalCaregiverScore.getFormalCaregiverId(),
                            -1, // No hay un score previo porque es un nuevo score para este cuidador
                            formalCaregiverScore.getScore()
                    ),
                    formalCaregiverScore, Boolean.class).getBody();
            
            if (Boolean.TRUE.equals(updatedScoreData))
                log.info("*** Rating del Cuidador Formal guardado con exito: " + LocalDateTime.now());
            else{
                log.info("No se pudo actualizar el scoreSum del Cuidador Formal con id " +
                        formalCaregiverScore.getFormalCaregiverId() +
                        " del paciente  con id " + formalCaregiverScore.getPatientId());
            }

            return newFormalCaregiverScore;

        }catch(Exception e){
            log.warning("*** ERROR GUARDANDO RATING DEL CUIDADOR FORMAL: " + e);
            throw new FormalCaregiverRatingSaveException(formalCaregiverScore);
        }
    }

    @Override
    public Boolean updateScore(String formalCaregiverId, String patientId, VoteObject voteObject) {

        return null;
    }

    @Override
    public Optional<FormalCaregiverScore> findId(String formalCaregiverScoreId) {
        return formalCaregiverScoreRepo.findById(formalCaregiverScoreId);
    }

    @Override
    public FormalCaregiverScore findRatingOfPatient(String formalCaregiverId, String patientId) {
        return formalCaregiverScoreRepo.findByFormalCaregiverIdAndPatientId(formalCaregiverId, patientId);
    }

    @Override
    public List<FormalCaregiverScore> findAll(String formalCaregiverId) {
        return formalCaregiverScoreRepo.findByFormalCaregiverId(formalCaregiverId);
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
}
