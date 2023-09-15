package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.Patient;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPatientRepo extends MongoRepository<Patient, String> {

    Optional<Patient> findByIdentificationDocumentAndZone_CountryName(Integer identificationDocument, String countryName);
    Optional<Patient> findByMailIgnoreCase(String mail);
    List<Patient> findByZone_CountryNameAndValidateTrueOrderByName1(String countryName);
    List<Patient> findByZone_CountryNameAndValidateTrueAndDeletedFalseOrderByName1(String countryName);
    List<Patient> findByZone_CountryNameAndValidateFalseAndDeletedFalseOrderByName1(String countryName);
    List<Patient> findByZone_CountryNameAndValidateFalseOrderByName1(String countryName);
    List<Patient> findByZone_CountryNameAndZone_DepartmentNameAndValidateTrueOrderByName1(
            String countryName, String departmentName);
    List<Patient> findByZone_CountryNameAndZone_DepartmentNameAndValidateTrueAndDeletedFalseOrderByName1(
            String countryName, String departmentName);
    List<Patient> findByZone_CountryNameAndZone_DepartmentNameAndValidateFalseAndDeletedFalseOrderByName1(
            String countryName, String departmentName);
    List<Patient> findByZone_CountryNameAndZone_DepartmentNameAndValidateFalseOrderByName1(
            String countryName, String departmentName);
    List<Patient> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndValidateTrueOrderByName1(
            String countryName, String departmentName, String cityName);
    List<Patient> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndValidateTrueAndDeletedFalseOrderByName1(
            String countryName, String departmentName, String cityName);
    List<Patient> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndValidateFalseAndDeletedFalseOrderByName1(
            String countryName, String departmentName, String cityName);
    List<Patient> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndValidateFalseOrderByName1(
            String countryName, String departmentName, String cityName);
    List<Patient> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndName1IgnoreCaseAndValidateTrueAndDeletedFalseOrderByName1(
            String countryName, String departmentName, String cityName, String name1);
    //List<Patient> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndName1IgnoreCaseAndValidateFalseAndDeletedFalseOrderByName1(
    //        String countryName, String departmentName, String cityName, String name1);
    List<Patient> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndZone_NeighborhoodNameAndName1IgnoreCaseAndValidateTrueAndDeletedFalse(
            String countryName, String departmentName, String cityName, String neighborhoodName, String name1);
    List<Patient> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndZone_NeighborhoodNameAndName1IgnoreCaseAndValidateFalseAndDeletedFalse(
            String countryName, String departmentName, String cityName, String neighborhoodName, String name1);
    List<Patient> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndName1IgnoreCaseAndValidateFalseOrderByName1(
            String countryName, String departmentName, String cityName, String name1);
    List<Patient> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndZone_NeighborhoodNameAndName1IgnoreCaseAndValidateFalse(
            String countryName, String departmentName, String cityName, String neighborhoodName, String name1);
    List<Patient> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndName1IgnoreCaseAndValidateFalseAndDeletedFalse(
            String countryName, String departmentName, String cityName, String name1);
    List<Patient> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndName1IgnoreCaseAndValidateTrueOrderByName1(
            String countryName, String departmentName, String cityName, String name1);


// Busqueda exacta por documento de identidad (indice activo: documento+pais)
//    Optional<Patient> findByIdentificationDocumentAndZone_CountryNameAndDeletedFalse(
//            Integer identificationDocument, String countryName);

    // Busqueda aproximada por primer nombre
/*
    List<Patient> findByName1LikeAndZone_CityNameAndZone_DepartmentNameAndZone_CountryNameAndValidateTrueAndDeletedFalse(
            String name1, String cityName, String departmentName, String countryName
    );
*/
    // Busqueda aproximada por primer apellido
/*
    List<Patient> findBySurname1LikeAndZone_CityNameAndZone_DepartmentNameAndZone_CountryNameAndValidateTrueAndDeletedFalse(
            String surname1, String cityName, String departmentName, String countryName
    );

    // Busqueda aproximada por primer nombre y primer apellido
    List<Patient> findByName1LikeAndSurname1LikeAndZone_CityNameAndZone_DepartmentNameAndZone_CountryNameAndValidateTrueAndDeletedFalse(
            String name1, String surname1, String cityName, String departmentName, String countryName
    );
*/

}
