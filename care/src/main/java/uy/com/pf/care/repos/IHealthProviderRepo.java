package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import uy.com.pf.care.model.documents.HealthProvider;

import java.util.List;

public interface IHealthProviderRepo extends MongoRepository<HealthProvider, String> {

    //*** Busquedas con indice ***

    List<HealthProvider> findByCountryNameOrderByName(String countryName);
    List<HealthProvider> findByCountryNameAndDeletedFalseOrderByName(String countryName);
    List<HealthProvider> findByCountryNameAndDepartmentNameOrderByName(String countryName, String departmentName);
    List<HealthProvider> findByCountryNameAndDepartmentNameAndDeletedFalseOrderByName(
            String countryName, String departmentName);
    List<HealthProvider> findByCountryNameAndDepartmentNameAndCityNameOrderByName(
            String countryName, String departmentName, String cityName);
    List<HealthProvider> findByCountryNameAndDepartmentNameAndCityNameAndDeletedFalseOrderByName(
            String countryName, String departmentName, String cityName);
    HealthProvider findByCountryNameAndDepartmentNameAndCityNameAndNameAndDeletedFalseOrderByName(
            String countryName, String departmentName, String cityName, String name);

}
