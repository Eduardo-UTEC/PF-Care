package uy.com.pf.care.infra.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.FormalCaregiver;

import java.util.List;

@Repository
public interface IFormalCaregiverRepo extends MongoRepository<FormalCaregiver, String> {
    FormalCaregiver findByMailIgnoreCase(String mail);
    List<FormalCaregiver> findByCountryNameAndValidateTrueAndAvailableTrue(String countryName);
    List<FormalCaregiver> findByCountryNameAndValidateFalseAndAvailableTrue(String countryName);
    List<FormalCaregiver> findByCountryNameAndValidateTrueAndDeletedFalseAndAvailableTrue(String countryName);
    List<FormalCaregiver> findByCountryNameAndValidateFalseAndDeletedFalseAndAvailableTrue(String countryName);
    List<FormalCaregiver> findByCountryNameAndValidateTrueAndAvailableTrueAndNameIgnoreCase(
            String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndNameIgnoreCaseAndValidateTrueAndAvailableTrueAndDeletedFalse(
            String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndNameLikeIgnoreCaseAndValidateTrueAndAvailableTrue(
            String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndNameLikeIgnoreCaseAndValidateTrueAndDeletedFalseAndAvailableTrue(
            String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndValidateFalseAndAvailableTrueAndNameIgnoreCase(
            String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndNameIgnoreCaseAndValidateFalseAndDeletedFalseAndAvailableTrue(
            String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndNameLikeIgnoreCaseAndValidateFalseAndAvailableTrue(
            String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndNameLikeIgnoreCaseAndValidateFalseAndDeletedFalseAndAvailableTrue(
            String countryName, String name);
}
