package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.Patient;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPatientRepo extends MongoRepository<Patient, String> {

    //**** Busquedas por indices ****

    Optional<Patient> findByIdentificationDocumentAndZone_CountryName(Integer identificationDocument, String countryName);

    // Busqueda por indice completo
    List<Patient> findByName1AndZone_CityNameAndZone_DepartmentNameAndZone_CountryNameAndZone_neighborhoodName(
            String name1, String cityName, String departmentName, String countryName, String neighborhoodName);

    // Busqueda por indice parcial (se excluye el barrio)
    List<Patient> findByName1AndZone_CityNameAndZone_DepartmentNameAndZone_CountryName(
            String name1, String cityName, String departmentName, String countryName);

    Optional<Patient> findByMail(String mail);


    //**** Busquedas sin indice ****

    // Pacientes de un pais, excluyendo los borrados
    List<Patient> findByZone_CountryNameAndDeletedFalse(String countryName);

    // Pacientes de un pais (todos)
    List<Patient> findByZone_CountryName(String countryName);


    // Pacientes de una ciudad+departamento+pais, excluyendo los borrados
    List<Patient> findByZone_CityNameAndZone_DepartmentNameAndZone_CountryNameAndDeletedFalse(
            String cityName, String departmentName, String countryName);

    // Pacientes de una ciudad+departamento+pais, incluyendo los borrados
    List<Patient> findByZone_CityNameAndZone_DepartmentNameAndZone_CountryName(
            String cityName, String departmentName, String countryName);

    // Pacientes de un departamento+pais, excluyendo los borrados
    List<Patient> findByZone_DepartmentNameAndZone_CountryNameAndDeletedFalse(String departmentName, String countryName);

    // Pacientes de un departamento+pais, incluyendo los borrados
    List<Patient> findByZone_DepartmentNameAndZone_CountryName(String departmentName, String countryName);


// Busqueda exacta por documento de identidad (indice activo: documento+pais)
//    Optional<Patient> findByIdentificationDocumentAndZone_CountryNameAndDeletedFalse(
//            Integer identificationDocument, String countryName);

    // Busqueda aproximada por primer nombre
/*
    List<Patient> findByName1LikeAndZone_CityNameAndZone_DepartmentNameAndZone_CountryNameAndDeletedFalse(
            String name1, String cityName, String departmentName, String countryName
    );
*/
    // Busqueda aproximada por primer apellido
/*
    List<Patient> findBySurname1LikeAndZone_CityNameAndZone_DepartmentNameAndZone_CountryNameAndDeletedFalse(
            String surname1, String cityName, String departmentName, String countryName
    );

    // Busqueda aproximada por primer nombre y primer apellido
    List<Patient> findByName1LikeAndSurname1LikeAndZone_CityNameAndZone_DepartmentNameAndZone_CountryNameAndDeletedFalse(
            String name1, String surname1, String cityName, String departmentName, String countryName
    );
*/

}
