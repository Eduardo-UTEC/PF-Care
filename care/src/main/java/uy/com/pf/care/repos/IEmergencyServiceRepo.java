package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.EmergencyService;

import java.util.List;

@Repository
public interface IEmergencyServiceRepo extends MongoRepository<EmergencyService, String> {

    EmergencyService findByCountryNameIgnoreCaseAndDepartmentNameIgnoreCaseAndCityNameIgnoreCaseAndNameIgnoreCase(
            String countryName,
            String departmentName,
            String cityName,
            String name);
    EmergencyService findByCountryNameIgnoreCaseAndDepartmentNameIgnoreCaseAndCityNameIgnoreCaseAndNameIgnoreCaseAndDeletedFalse(
            String countryName,
            String departmentName,
            String cityName,
            String name);
    //
    List<EmergencyService> findByCountryNameIgnoreCaseAndDepartmentNameIgnoreCaseAndCityNameIgnoreCaseOrderByName(
            String countryName,
            String departmentName,
            String cityName);
    List<EmergencyService> findByCountryNameIgnoreCaseAndDepartmentNameIgnoreCaseAndCityNameIgnoreCaseAndDeletedFalseOrderByName(
            String countryName,
            String departmentName,
            String cityName);
    //
    List<EmergencyService> findByCountryNameIgnoreCaseAndDepartmentNameIgnoreCaseOrderByName(String countryName, String departmentName);
    List<EmergencyService> findByCountryNameIgnoreCaseAndDepartmentNameIgnoreCaseAndDeletedFalseOrderByName(
            String countryName, String departmentName);
    //
    List<EmergencyService> findByCountryNameIgnoreCaseOrderByName(String countryName);
    List<EmergencyService> findByCountryNameIgnoreCaseAndDeletedFalseOrderByName(String countryName);

}
