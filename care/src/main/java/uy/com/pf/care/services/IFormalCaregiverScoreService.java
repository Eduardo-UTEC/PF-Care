package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.FormalCaregiverScore;
import uy.com.pf.care.model.objects.ScoreObject;

import java.util.List;
import java.util.Optional;

public interface IFormalCaregiverScoreService {
    String save(FormalCaregiverScore formalCaregiverScore);
    List<FormalCaregiverScore> findAll(String formalCaregiverId);
    Optional<FormalCaregiverScore> findId(String formalCaregiverScoreId);
    FormalCaregiverScore findScore(String formalCaregiverId, String patientId);
    Boolean updateScore(FormalCaregiverScore formalCaregiverScore);
}
