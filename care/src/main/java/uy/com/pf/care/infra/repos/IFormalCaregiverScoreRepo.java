package uy.com.pf.care.infra.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.FormalCaregiverScore;

import java.util.List;

@Repository
public interface IFormalCaregiverScoreRepo extends MongoRepository<FormalCaregiverScore, String> {

    FormalCaregiverScore findByFormalCaregiverIdAndPatientId(String formalCaregiverId, String patientId);

    // Busco en el indice formalCaregiver + Date
    List<FormalCaregiverScore> findByFormalCaregiverIdOrderByDateDesc(String formalCaregiverId);
}
