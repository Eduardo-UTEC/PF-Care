package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.Patient;

import java.util.List;
import java.util.Optional;

public interface IPatientService {
    Patient save(Patient patient);
    Boolean logicalDelete(String id);

    //**** Busquedas con indice ****
    // Nota: los metodos que comienzan con "findWithIndex_" implica que buscan por los campos del indice, y no
    // contemplan otros campos como "deleted", por ej.

    Optional<Patient> findId(String id);
    Optional<Patient> findWithIndex_IdentificationDocument(Integer identificationDocument, String countryName);
    Optional<Patient> findWithIndex_Mail(String mail);
    List<Patient> findWithIndex_Name1(
            String name1, String cityName, String departmentName, String countryName, String neighborhoodName);

    //**** Busquedas sin indice ****

    List<Patient> findAll(Boolean includeDeleted, String countryName);
    List<Patient> findByCity(Boolean includeDeleted, String cityName, String departmentName, String countryName);
    List<Patient> findByDepartment(Boolean includeDeleted, String departmentName, String countryName);

//    Optional<Patient> findIdentificationDocument(Integer identificationDocument, String countryName);
//    List<Patient> findName1Like(String name1, String cityName, String departmentName, String countryName);
//    List<Patient> findSurname1Like(String surname1, String cityName, String departmentName, String countryName);
//    List<Patient> findName1Surname1Likes(String name1, String surname1, String cityName, String departmentName, String countryName);


}
