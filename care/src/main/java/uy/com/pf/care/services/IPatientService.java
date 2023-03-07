package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.Patient;

import java.util.List;
import java.util.Optional;

public interface IPatientService {
    Patient save(Patient patient);
    List<Patient> findAll(String countryName);
    Optional<Patient> findId(String id);
    Optional<Patient> findIdentificationDocument(Integer identificationDocument, String countryName);
    List<Patient> findName1Like(String name1, String cityName, String departmentName, String countryName);
    List<Patient> findSurname1Like(String surname1, String cityName, String departmentName, String countryName);
    List<Patient> findName1Surname1Likes(String name1, String surname1, String cityName, String departmentName, String countryName);
    List<Patient> findByCity(String cityName, String departmentName, String countryName);
    List<Patient> findByDepartment(String departmentName, String countryName);
    boolean logicalDelete(String id);
}
