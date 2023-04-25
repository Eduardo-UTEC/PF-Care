package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.FormalCaregiverScore;
import uy.com.pf.care.model.objects.VoteObject;

import java.util.List;
import java.util.Optional;

public interface IFormalCaregiverScoreService {
    FormalCaregiverScore save(FormalCaregiverScore formalCaregiverScore);
    Boolean updateScore(String formalCaregiverId, String patientId, VoteObject voteObject);
    Optional<FormalCaregiverScore> findId(String formalCaregiverScoreId);
    FormalCaregiverScore findRatingOfPatient(String formalCaregiverId, String patientId);
    List<FormalCaregiverScore> findAll(String formalCaregiverId);
}
