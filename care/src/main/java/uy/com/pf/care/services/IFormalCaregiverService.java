package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.FormalCaregiver;
import uy.com.pf.care.model.objects.DayTimeRangeObject;

import java.util.List;
import java.util.Optional;

public interface IFormalCaregiverService {
    String save(FormalCaregiver formalCaregiver);
    Boolean update(FormalCaregiver newFormalCaregiver);
    Boolean setAvailability(String id, Boolean isAvailable);
    Boolean setValidation(String id, Boolean isValidated);
    Boolean setDeletion(String id, Boolean isDeleted);
    Boolean updateVotes(String formalCaregiverId, int previousScore, int currentScore);
    Optional<FormalCaregiver> findId(String id);
    List<FormalCaregiver> findAll(Boolean withoutValidate, Boolean includeDeleted, String countryName);
    FormalCaregiver findMail(String mail);
    List<FormalCaregiver> findName(Boolean withoutValidate, Boolean includeDeleted, String countryName, String name);
    List<FormalCaregiver> findNameLike(Boolean withoutValidate, Boolean includeDeleted, String countryName, String name);
    List<FormalCaregiver> findInterestZones_Neighborhood(
            Boolean withoutValidate,
            Boolean includeDeleted,
            String interestNeighborhoodName,
            String interestCityName,
            String interestDepartmentName,
            String countryName);
    List<FormalCaregiver> findInterestZones_City(
            Boolean validateCity,
            Boolean withoutValidate,
            Boolean includeDeleted,
            String interestCityName,
            String interestDepartmentName,
            String countryName);
    List<FormalCaregiver> findInterestZones_Department(
            Boolean validateInterestDepartment,
            Boolean withoutValidate,
            Boolean includeDeleted,
            String interestDepartmentName,
            String countryName);
    List<FormalCaregiver> findPriceRange(
            Integer maxPrice,
            String interestNeighborhoodName,
            String interestCityName,
            String interestDepartmentName,
            String countryName);
    List<FormalCaregiver> findDateTimeRange(
            List<DayTimeRangeObject> dayTimeRange,
            String interestNeighborhoodName,
            String interestCityName,
            String interestDepartmentName,
            String countryName);

}
