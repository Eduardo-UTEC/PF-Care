package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.EmergencyService;
import uy.com.pf.care.model.documents.Patient;
import uy.com.pf.care.model.documents.User;

import java.util.List;

@Repository
public interface IUserRepo extends MongoRepository<User, String> {
    //User findByIdentificationDocumentAndZone_CountryName(Integer identificationDocument, String countryName);
    User findByUserName(String userName);
    List<User> findByZone_CountryName(String countryName);
    List<User> findByZone_CountryNameAndZone_DepartmentName(String countryName, String departmentName);
    List<User> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityName(
            String countryName, String departmentName, String cityName);
}
