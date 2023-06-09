package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import uy.com.pf.care.model.documents.HealthProvider;

import java.util.List;

public interface IHealthProviderRepo extends MongoRepository<HealthProvider, String> {

    List<HealthProvider> findByCountryNameOrderByName(String countryName);
    List<HealthProvider> findByCountryNameAndDeletedFalseOrderByName(String countryName);
    List<HealthProvider> findByCountryNameAndDepartmentNameOrderByName(String countryName, String departmentName);
    List<HealthProvider> findByCountryNameAndDepartmentNameAndDeletedFalseOrderByName(
            String countryName, String departmentName);
    List<HealthProvider> findByCountryNameAndDepartmentNameAndCityName(
            String countryName, String departmentName, String cityName);
    List<HealthProvider> findByCountryNameAndDepartmentNameAndCityNameAndDeletedFalse(
            String countryName, String departmentName, String cityName);
    HealthProvider findByCountryNameAndDepartmentNameAndCityNameAndNameAndDeletedFalse(
            String countryName, String departmentName, String cityName, String name);

}
