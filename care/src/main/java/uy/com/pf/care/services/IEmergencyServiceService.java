package uy.com.pf.care.services;
import uy.com.pf.care.model.documents.EmergencyService;

import java.util.List;
import java.util.Optional;

public interface IEmergencyServiceService {
    EmergencyService save(EmergencyService emergencyService);
    boolean logicalDelete(String id);

    List<EmergencyService> findAll(Boolean includeDeleted, String countryName);
    Optional<EmergencyService> findId(String id);
    List<EmergencyService> findByCity(Boolean includeDeleted, String cityName, String departmentName, String countryName);
    List<EmergencyService> findByDepartment(Boolean includeDeleted, String departmentName, String countryName);
    EmergencyService findByName(
            Boolean includeDeleted, String name, String cityName, String departmentName, String countryName);
}
