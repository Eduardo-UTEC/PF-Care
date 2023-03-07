package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.FormalCaregiver;

import java.util.List;
import java.util.Optional;

public interface IFormalCaregiverService {
    FormalCaregiver save(FormalCaregiver formalCaregiver);
    List<FormalCaregiver> findAll(String countryName);
    Optional<FormalCaregiver> findId(String id);
    List<FormalCaregiver> findByDepartment(String departmentName, String countryName);
}
