package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.Residential;

import java.util.List;
import java.util.Optional;

public interface IResidentialService {
    Residential save(Residential residential);
    Boolean setDeletion(String id, Boolean isDeleted);
    Optional<Residential> findId(String id);
    List<Residential> findByCountry(String countryName);
    List<Residential> findByDepartment(String departmentName, String countryName);
    List<Residential> findByCity(String cityName, String departmentName, String countryName);
}
