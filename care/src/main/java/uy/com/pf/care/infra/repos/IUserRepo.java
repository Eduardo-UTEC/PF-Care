package uy.com.pf.care.infra.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepo extends MongoRepository<User, String> {
    Optional<User> findByIdentificationDocument(Integer identificationDocument);
    //User findByUserName(String userName);
    List<User> findByResidenceZone_CountryName(String countryName);
    List<User> findByResidenceZone_CountryNameAndResidenceZone_DepartmentName(String countryName, String departmentName);
    List<User> findByResidenceZone_CountryNameAndResidenceZone_DepartmentNameAndResidenceZone_CityName(
            String countryName, String departmentName, String cityName);
}
