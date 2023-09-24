package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.VolunteerPerson;
import uy.com.pf.care.model.objects.DayTimeRangeObject;

import java.util.List;
import java.util.Optional;

public interface IVolunteerPersonService {

    String save(VolunteerPerson volunteerPerson);
    Boolean update(VolunteerPerson newVolunteerPerson);
    Boolean addVolunteerActivitiesId(String volunteerPersonId, List<String> volunteerActivitiesId);
    Boolean changeVolunteerActivityId(String volunteerPersonId, String oldVolunteerActivityId, String newVolunteerActivityId);
    Boolean delVolunteerActivitiesId(String volunteerPersonId, List<String> volunteerActivitiesId);
    Boolean setAvailability(String id, Boolean isAvailable);
    Boolean setValidation(String id, Boolean isValidated);
    Boolean setDeletion(String id, Boolean isDeleted);
    Optional<VolunteerPerson> findId(String id);
    VolunteerPerson findIdentificationNumber(String identificationDocument, String countryName);
    List<VolunteerPerson> findAll(Boolean withoutValidate, Boolean includeDeleted, String countryName);
    VolunteerPerson findMail(String mail);
    List<VolunteerPerson> findName(Boolean withoutValidate, Boolean includeDeleted, String countryName, String name1);
    List<VolunteerPerson> findNameLike(Boolean withoutValidate, Boolean includeDeleted, String countryName, String name1);

    List<VolunteerPerson> findInterestZones_Neighborhood(
            Boolean withoutValidate,
            Boolean includeDeleted,
            String interestNeighborhoodName,
            String interestCityName,
            String interestDepartmentName,
            String countryName);

    List<VolunteerPerson> findInterestZones_City(
            Boolean validateCity,
            Boolean withoutValidate,
            Boolean includeDeleted,
            String interestCityName,
            String interestDepartmentName,
            String countryName);

    List<VolunteerPerson> findInterestZones_Department(
            Boolean validateInterestDepartment,
            Boolean withoutValidate,
            Boolean includeDeleted,
            String interestDepartmentName,
            String countryName);

    List<VolunteerPerson> findDateTimeRange(
            List<DayTimeRangeObject> dayTimeRange,
            String interestNeighborhoodName,
            String interestCityName,
            String interestDepartmentName,
            String countryName);

}
