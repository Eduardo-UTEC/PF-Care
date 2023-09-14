package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.Patient;

import java.util.List;
import java.util.Optional;

public interface IPatientService {
    String save(Patient patient);
    Boolean update(Patient newPatient);
    Boolean setValidation(String id, Boolean isValidated);
    Boolean setDeletion(String id, Boolean isDeleted);
    Optional<Patient> findId(String id);
    Optional<Patient> findIdentificationDocument(Integer identificationDocument, String countryName);
    Optional<Patient> findMail(String mail);
    List<Patient> findName1(
            String name1, String neighborhoodName, String cityName, String departmentName, String countryName);
    List<Patient> findCity(Boolean includeDeleted, String cityName, String departmentName, String countryName);
    List<Patient> findDepartment(Boolean includeDeleted, String departmentName, String countryName);
    List<Patient> findAll(Boolean includeDeleted, String countryName);

//    Optional<Patient> findIdentificationDocument(Integer identificationDocument, String countryName);
//    List<Patient> findName1Like(String name1, String cityName, String departmentName, String countryName);
//    List<Patient> findSurname1Like(String surname1, String cityName, String departmentName, String countryName);
//    List<Patient> findName1Surname1Likes(String name1, String surname1, String cityName, String departmentName, String countryName);


}
