package uy.com.pf.care.infra.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.ReferenceCaregiver;

import java.util.List;
import java.util.Optional;

@Repository
public interface IReferenceCaregiverRepo extends MongoRepository<ReferenceCaregiver, String> {
    Optional<ReferenceCaregiver> findByIdentificationDocumentAndZone_CountryName(
            Integer identificationDocument, String countryName);
    List<ReferenceCaregiver> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndZone_NeighborhoodNameAndName1IgnoreCase(
            String countryName, String departmentName, String cityName, String neighborhoodName, String name1);

    //List<ReferenceCaregiver> findByZone_CountryNameAndValidateTrueOrderByName1(String countryName);
    //List<ReferenceCaregiver> findByZone_CountryNameAndValidateTrueAndDeletedFalseOrderByName1(String countryName);
    //List<ReferenceCaregiver> findByZone_CountryNameAndValidateFalseAndDeletedFalseOrderByName1(String countryName);
    //List<ReferenceCaregiver> findByZone_CountryNameAndValidateFalseOrderByName1(String countryName);
    //List<ReferenceCaregiver> findByZone_CountryNameAndZone_DepartmentNameAndValidateTrueOrderByName1(String countryName, String departmentName);
    //List<ReferenceCaregiver> findByZone_CountryNameAndZone_DepartmentNameAndValidateTrueAndDeletedFalseOrderByName1(String countryName, String departmentName);
    //List<ReferenceCaregiver> findByZone_CountryNameAndZone_DepartmentNameAndValidateFalseAndDeletedFalseOrderByName1(String countryName, String departmentName);
    //List<ReferenceCaregiver> findByZone_CountryNameAndZone_DepartmentNameAndValidateFalseOrderByName1(String countryName, String departmentName);
    //List<ReferenceCaregiver> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndValidateTrueOrderByName1(String countryName, String departmentName, String cityName);
    //List<ReferenceCaregiver> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndValidateTrueAndDeletedFalseOrderByName1(String countryName, String departmentName, String cityName);
    //List<ReferenceCaregiver> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndValidateFalseAndDeletedFalseOrderByName1(String countryName, String departmentName, String cityName);
    //List<ReferenceCaregiver> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndValidateFalseOrderByName1(String countryName, String departmentName, String cityName);
    //List<ReferenceCaregiver> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndName1IgnoreCaseAndValidateTrueAndDeletedFalseOrderByName1(String countryName, String departmentName, String cityName, String name1);
    //List<ReferenceCaregiver> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndName1IgnoreCaseAndValidateFalseAndDeletedFalseOrderByName1(String countryName, String departmentName, String cityName, String name1);

}
