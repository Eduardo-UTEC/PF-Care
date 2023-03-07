package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.HealthProvider;

import java.util.List;
import java.util.Optional;

public interface IHealthProviderService {
    HealthProvider save(HealthProvider healthProvider);
    Optional<HealthProvider> findId(String id);
    List<HealthProvider> findAll(String countryName);
    List<HealthProvider> findByCity(String cityName, String departmentName, String countryName);
    List<HealthProvider> findByDepartment(String departmentName, String countryName);

}
