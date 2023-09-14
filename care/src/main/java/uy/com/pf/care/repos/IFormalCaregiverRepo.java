package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.FormalCaregiver;

import java.util.List;

@Repository
public interface IFormalCaregiverRepo extends MongoRepository<FormalCaregiver, String> {
    FormalCaregiver findByMailIgnoreCase(String mail);
    List<FormalCaregiver> findByCountryNameAndValidateTrue(String countryName);
    List<FormalCaregiver> findByCountryNameAndValidateTrueDeletedFalse(String countryName);
    List<FormalCaregiver> findByCountryNameAndValidateFalseAndDeletedFalse(String countryName);
    List<FormalCaregiver> findByCountryNameAndValidateTrueAndNameIgnoreCase(String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndNameIgnoreCaseAndValidateTrueAndDeletedFalse(String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndNameLikeIgnoreCaseAndValidateTrue(String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndNameLikeIgnoreCaseAndValidateTrueAndDeletedFalse(String countryName, String name);
}
