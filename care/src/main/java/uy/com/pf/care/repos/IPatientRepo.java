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
    List<Patient> findByZone_CountryNameOrderByName1(String countryName);
    List<Patient> findByZone_CountryNameAndDeletedFalseOrderByName1(String countryName);
    List<Patient> findByZone_CountryNameAndZone_DepartmentNameOrderByName1(
            String countryName, String departmentName);
    List<Patient> findByZone_CountryNameAndZone_DepartmentNameAndDeletedFalseOrderByName1(
            String countryName, String departmentName);
    List<Patient> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameOrderByName1(
            String countryName, String departmentName, String cityName);
    List<Patient> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndDeletedFalseOrderByName1(
            String countryName, String departmentName, String cityName);
    List<Patient> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndName1IgnoreCaseAndDeletedFalseOrderByName1(
            String countryName, String departmentName, String cityName, String name1);
    List<Patient> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndZone_NeighborhoodNameAndName1IgnoreCaseAndDeletedFalse(
            String countryName, String departmentName, String cityName, String neighborhoodName, String name1);



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
