package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.EmergencyService;

import java.util.List;

@Repository
public interface IEmergencyServiceRepo extends MongoRepository<EmergencyService, String> {

    EmergencyService findByCountryNameAndDepartmentNameAndCityNameAndName(
            String countryName,
            String departmentName,
            String cityName,
            String name);
    EmergencyService findByCountryNameAndDepartmentNameAndCityNameAndNameAndDeletedFalse(
            String countryName,
            String departmentName,
            String cityName,
            String name);
    //
    List<EmergencyService> findByCountryNameAndDepartmentNameAndCityNameOrderByName(
            String countryName,
            String departmentName,
            String cityName);
    List<EmergencyService> findByCountryNameAndDepartmentNameAndCityNameAndDeletedFalseOrderByName(
            String countryName,
            String departmentName,
            String cityName);
    //
    List<EmergencyService> findByCountryNameAndDepartmentNameOrderByName(String countryName, String departmentName);
    List<EmergencyService> findByCountryNameAndDepartmentNameAndDeletedFalseOrderByName(
            String countryName, String departmentName);
    //
    List<EmergencyService> findByCountryNameOrderByName(String countryName);
    List<EmergencyService> findByCountryNameAndDeletedFalseOrderByName(String countryName);

}
