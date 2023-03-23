package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.FormalCaregiver;

import java.util.List;
import java.util.Optional;

public interface IFormalCaregiverService {
    FormalCaregiver save(FormalCaregiver formalCaregiver);
    Boolean setAvailability(String id, Boolean isAvailable);
    Boolean setDeletion(String id, Boolean isDeleted);
    List<FormalCaregiver> findAll(Boolean includeDeleted, String countryName);
    Optional<FormalCaregiver> findId(String id);
    FormalCaregiver findWithIndex_Mail(String mail);
    FormalCaregiver findByName(
            Boolean includeDeleted, String name, String telephone, String departmentName, String countryName);
    List<FormalCaregiver> findByNameLike(
            Boolean includeDeleted, String name, String departmentName, String countryName);
    List<FormalCaregiver> findByDepartment(Boolean includeDeleted, String departmentName, String countryName);
    List<FormalCaregiver> findByAvailable(String departmentName, String countryName);
    List<FormalCaregiver> findByInterestZones_Neighborhood(
            Boolean includeDeleted,
            String interestNeighborhoodName,
            String interestCityName,
            String interestDepartmentName,
            String countryName);

    List<FormalCaregiver> findByInterestZones_City(
            Boolean validateCity,
            Boolean includeDeleted,
            String interestCityName,
            String interestDepartmentName,
            String countryName);

    List<FormalCaregiver> findByInterestZones_Department(
            Boolean validateInterestDepartment, Boolean includeDeleted, String interestDepartmentName, String countryName);

}
