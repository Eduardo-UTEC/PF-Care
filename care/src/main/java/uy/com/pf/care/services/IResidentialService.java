package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.Residential;

import java.util.List;
import java.util.Optional;

public interface IResidentialService {
    Residential save(Residential residential);
    Boolean setDeletion(String id, Boolean isDeleted);
    Optional<Residential> findId(String id);
    List<Residential> findCountry(Boolean includeDeleted, String countryName);
    List<Residential> findDepartment(Boolean includeDeleted, String countryName, String departmentName);
    List<Residential> findCity(Boolean includeDeleted, String countryName, String departmentName, String cityName);
    List<Residential> findName(
            Boolean includeDeleted, String countryName, String departmentName, String cityName, String name);
}
