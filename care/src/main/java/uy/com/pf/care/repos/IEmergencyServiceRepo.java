package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.EmergencyService;

import java.util.List;

@Repository
public interface IEmergencyServiceRepo extends MongoRepository<EmergencyService, String> {
    List<EmergencyService> findByCityNameAndDepartmentNameAndCountryNameAndDeletedFalse(String cityName,
                                                                                        String departmentName,
                                                                                        String countryName);
    List<EmergencyService> findByDepartmentNameAndCountryNameAndDeletedFalse(String departmentName,
                                                                             String countryName);
    List<EmergencyService> findByCountryNameAndDeletedFalse(String countryName);
    EmergencyService findByNameAndCityNameAndDepartmentNameAndCountryNameAndDeletedFalse(String name,
                                                                                         String cityName,
                                                                                         String departmentName,
                                                                                         String countryName);

}
