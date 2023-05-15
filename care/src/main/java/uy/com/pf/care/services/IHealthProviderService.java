package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.HealthProvider;

import java.util.List;
import java.util.Optional;

public interface IHealthProviderService {
    String save(HealthProvider healthProvider);
    Optional<HealthProvider> findId(String id);
    HealthProvider findByName(String cityName, String departmentName, String countryName, String name);
    List<HealthProvider> findAll(Boolean includeDeleted, String countryName);
    List<HealthProvider> findByCity(Boolean includeDeleted, String cityName, String departmentName, String countryName);
    List<HealthProvider> findByDepartment(Boolean includeDeleted, String departmentName, String countryName);
    Boolean setDeletion(String id, Boolean isDeleted);
}
