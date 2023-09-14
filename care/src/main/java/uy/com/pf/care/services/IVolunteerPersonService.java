package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.FormalCaregiver;
import uy.com.pf.care.model.documents.VolunteerActivity;
import uy.com.pf.care.model.documents.VolunteerPerson;
import uy.com.pf.care.model.objects.DayTimeRangeObject;

import java.util.List;
import java.util.Optional;

public interface IVolunteerPersonService {
    String save(VolunteerPerson volunteerPerson);
    Boolean update(VolunteerPerson newVolunteerPerson);
    Optional<VolunteerPerson> findId(String id);
    Boolean exist(String identificationDocument, String countryName);
    List<VolunteerPerson> findAll(Boolean includeDeleted, String countryName);
    Optional<VolunteerPerson> findMail(String mail);
    List<VolunteerPerson> findName(Boolean includeDeleted, String countryName, String name1);
    List<VolunteerPerson> findNameLike(Boolean includeDeleted, String countryName, String name1);
    List<VolunteerPerson> findInterestZones_Neighborhood(
            Boolean includeDeleted,
            String interestNeighborhoodName,
            String interestCityName,
            String interestDepartmentName,
            String countryName);
    List<VolunteerPerson> findInterestZones_City(
            Boolean validateCity,
            Boolean includeDeleted,
            String interestCityName,
            String interestDepartmentName,
            String countryName);
    List<VolunteerPerson> findInterestZones_Department(
            Boolean validateInterestDepartment,
            Boolean includeDeleted,
            String interestDepartmentName,
            String countryName);
    List<VolunteerPerson> findDateTimeRange(
            List<DayTimeRangeObject> dayTimeRange,
            String interestNeighborhoodName,
            String interestCityName,
            String interestDepartmentName,
            String countryName);
    Boolean setAvailability(String id, Boolean isAvailable);
    Boolean setValidation(String id, Boolean isValidated);
    Boolean setDeletion(String id, Boolean isDeleted);

}
