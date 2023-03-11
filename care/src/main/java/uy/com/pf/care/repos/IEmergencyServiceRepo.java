package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.EmergencyService;

import java.util.List;

@Repository
public interface IEmergencyServiceRepo extends MongoRepository<EmergencyService, String> {

    //**** Busquedas por indice ****
    EmergencyService findByNameAndCityNameAndDepartmentNameAndCountryName(String name,
                                                                          String cityName,
                                                                          String departmentName,
                                                                          String countryName);

    //**** Busquedas sin indice ****

    EmergencyService findByNameAndCityNameAndDepartmentNameAndCountryNameAndDeletedFalse(String name,
                                                                                         String cityName,
                                                                                         String departmentName,
                                                                                         String countryName);
    List<EmergencyService> findByCityNameAndDepartmentNameAndCountryName(String cityName,
                                                                         String departmentName,
                                                                         String countryName);
    List<EmergencyService> findByCityNameAndDepartmentNameAndCountryNameAndDeletedFalse(String cityName,
                                                                                        String departmentName,
                                                                                        String countryName);
    List<EmergencyService> findByDepartmentNameAndCountryName(String departmentName, String countryName);
    List<EmergencyService> findByDepartmentNameAndCountryNameAndDeletedFalse(String departmentName, String countryName);
    List<EmergencyService> findByCountryName(String countryName);
    List<EmergencyService> findByCountryNameAndDeletedFalse(String countryName);

}
