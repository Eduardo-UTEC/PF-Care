package uy.com.pf.care.services;
import uy.com.pf.care.model.documents.EmergencyService;

import java.util.List;
import java.util.Optional;

public interface IEmergencyServiceService {
    EmergencyService save(EmergencyService emergencyService);
    List<EmergencyService> findAll(String countryName);
    Optional<EmergencyService> findId(String id);
    List<EmergencyService> findByCity(String cityName, String departmentName, String countryName);
    List<EmergencyService> findByDepartment(String departmentName, String countryName);
    EmergencyService findByName(String name, String cityName, String departmentName, String countryName);
}
