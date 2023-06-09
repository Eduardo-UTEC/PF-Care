package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.EmergencyService;

import java.util.List;

@Repository
public interface IEmergencyServiceRepo extends MongoRepository<EmergencyService, String> {

    EmergencyService findByCountryNameAndDepartmentNameAndCityNameAndNameIgnoreCase(
            String countryName,
            String departmentName,
            String cityName,
            String name);
    EmergencyService findByCountryNameAndDepartmentNameAndCityNameAndNameIgnoreCaseAndDeletedFalse(
            String countryName,
            String departmentName,
            String cityName,
            String name);
    //
    List<EmergencyService> findByCountryNameAndDepartmentNameAndCityName(
            String countryName,
            String departmentName,
            String cityName);
    List<EmergencyService> findByCountryNameAndDepartmentNameAndCityNameAndDeletedFalse(
            String countryName,
            String departmentName,
            String cityName);
    //
    List<EmergencyService> findByCountryNameAndDepartmentName(String countryName, String departmentName);
    List<EmergencyService> findByCountryNameAndDepartmentNameAndDeletedFalse(
            String countryName, String departmentName);
    //
    List<EmergencyService> findByCountryName(String countryName);
    List<EmergencyService> findByCountryNameAndDeletedFalse(String countryName);

}
