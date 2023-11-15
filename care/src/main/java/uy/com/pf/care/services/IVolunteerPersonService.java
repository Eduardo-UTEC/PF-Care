package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.Patient;
import uy.com.pf.care.model.documents.VolunteerPerson;
import uy.com.pf.care.model.globalFunctions.MostDemandedServicesVolunteerRequestStats;
import uy.com.pf.care.model.objects.DayTimeRangeObject;

import java.util.List;
import java.util.Map;
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
    Boolean receivePatientRequest(String volunteerPersonId, String patientId);
    Boolean setMatchPatient(String volunteerPersonId, String patientId, Boolean isMatch);
    Optional<VolunteerPerson> findId(String id);
    List<VolunteerPerson> findIds(List<String> volunteersPersonId);
    VolunteerPerson findIdentificationNumber(String identificationDocument, String countryName);
    Optional<VolunteerPerson> findTelephone(String countryName, String telephone);
    List<VolunteerPerson> findAll(Boolean withoutValidate, Boolean includeDeleted, String countryName);
    VolunteerPerson findMail(String mail);
    //getMostDemandedVolunteerActivities: Uso interno. No se expone en el Controller
    Map<String, MostDemandedServicesVolunteerRequestStats> getMostDemandedVolunteerActivities(
            List<Patient> patients, long monthsAgo, long limit);
    List<VolunteerPerson> findName(Boolean withoutValidate, Boolean includeDeleted, String countryName, String name1);
    List<VolunteerPerson> findNameLike(Boolean withoutValidate, Boolean includeDeleted, String countryName, String name1);

    List<VolunteerPerson> findInterestZones_Neighborhood(
            Boolean withoutValidate,
            Boolean includeDeleted,
            String interestNeighborhoodName,
            String interestCityName,
            String interestDepartmentName,
            String countryName,
            List<String> excludedVolunteerIds);

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
            String countryName,
            List<String> excludedVolunteerIds);

}
