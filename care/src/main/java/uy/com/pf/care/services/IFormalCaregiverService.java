package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.FormalCaregiver;
import uy.com.pf.care.model.objects.DayTimeRangeObject;

import java.util.List;
import java.util.Optional;

public interface IFormalCaregiverService {
    FormalCaregiver save(FormalCaregiver formalCaregiver);
    Boolean setAvailability(String id, Boolean isAvailable);
    Boolean setDeletion(String id, Boolean isDeleted);
    Optional<FormalCaregiver> findId(String id);
    List<FormalCaregiver> findAll(Boolean includeDeleted, String countryName);
    FormalCaregiver findWithIndex_Mail(String mail);
    List<FormalCaregiver> findWithIndex_Name(Boolean includeDeleted, String name, String countryName);
    List<FormalCaregiver> findByNameLike(Boolean includeDeleted, String name, String countryName);
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
            Boolean validateInterestDepartment,
            Boolean includeDeleted,
            String interestDepartmentName,
            String countryName);
    List<FormalCaregiver> findByPriceRange(
            Integer maxPrice,
            String interestNeighborhoodName,
            String interestCityName,
            String interestDepartmentName,
            String countryName);
    List<FormalCaregiver> findByDateTimeRange(
            List<DayTimeRangeObject> dayTimeRange,
            String interestNeighborhoodName,
            String interestCityName,
            String interestDepartmentName,
            String countryName);

}
