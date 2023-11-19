package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.Patient;
import uy.com.pf.care.model.dtos.MostDemandedServicesVolunteerDTO;
import uy.com.pf.care.model.dtos.StatisticPatientWithOthersDTO;

import java.util.List;
import java.util.Optional;

public interface IPatientService {
    String save(Patient patient);
    String update(Patient newPatient);
    List<String> updateReferenceCaregiverOnPatients(List<String> patientsId, String referenceCaregiverId);
    Boolean sendRequestVolunteerPerson(String patientId, String volunteerPersonId);
    Boolean setMatchVolunteerPerson(String patientId, String volunteerPersonId, Boolean isMatch);
    Boolean setValidation(String id, Boolean isValidated);
    Boolean setDeletion(String id, Boolean isDeleted);
    Optional<Patient> findId(String id);
    List<Patient> findIds(List<String> patientsId);
    Optional<Patient> findIdentificationDocument(Integer identificationDocument, String countryName);
    Optional<Patient> findMail(String mail);
    List<MostDemandedServicesVolunteerDTO> getMostDemandedVolunteerActivities(
            Boolean withoutValidate, Boolean includeDeleted, String departmentName, String countryName);
    List<Patient> findName1(
        String name1,
        Boolean withoutValidate,
        Boolean includeDeleted,
        String neighborhoodName,
        String cityName,
        String departmentName,
        String countryName);
    List<Patient> findCity(
            Boolean withoutValidate,
            Boolean includeDeleted,
            String cityName,
            String departmentName,
            String countryName);
    List<Patient> findDepartment(Boolean withoutValidate, Boolean includeDeleted, String departmentName, String countryName);
    List<Patient> findAll(Boolean withoutValidate, Boolean includeDeleted, String countryName);
    List<StatisticPatientWithOthersDTO> getMonthlyRequestStatsForLastSixMonths(
            Boolean withoutValidate, Boolean includeDeleted, String departmentName, String countryName);

//    Optional<Patient> findIdentificationDocument(Integer identificationDocument, String countryName);
//    List<Patient> findName1Like(String name1, String cityName, String departmentName, String countryName);
//    List<Patient> findSurname1Like(String surname1, String cityName, String departmentName, String countryName);
//    List<Patient> findName1Surname1Likes(String name1, String surname1, String cityName, String departmentName, String countryName);


}
