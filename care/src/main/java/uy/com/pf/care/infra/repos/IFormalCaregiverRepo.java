package uy.com.pf.care.infra.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.FormalCaregiver;

import java.util.List;
import java.util.Optional;

@Repository
public interface IFormalCaregiverRepo extends MongoRepository<FormalCaregiver, String> {
    Optional<FormalCaregiver> findByMailIgnoreCase(String mail);
    Optional<FormalCaregiver> findByTelephone(String telephone);
    List<FormalCaregiver> findByCountryNameAndValidateTrueAndAvailableTrue(String countryName);
    List<FormalCaregiver> findByCountryNameAndValidateFalseAndAvailableTrue(String countryName);
    List<FormalCaregiver> findByCountryNameAndValidateTrueAndDeletedFalseAndAvailableTrue(String countryName);
    List<FormalCaregiver> findByCountryNameAndValidateFalseAndDeletedFalseAndAvailableTrue(String countryName);
    List<FormalCaregiver> findByCountryNameAndValidateTrueAndAvailableTrueAndName1IgnoreCase(
            String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndName1IgnoreCaseAndValidateTrueAndAvailableTrueAndDeletedFalse(
            String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndName1LikeIgnoreCaseAndValidateTrueAndAvailableTrue(
            String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndName1LikeIgnoreCaseAndValidateTrueAndDeletedFalseAndAvailableTrue(
            String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndValidateFalseAndAvailableTrueAndName1IgnoreCase(
            String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndName1IgnoreCaseAndValidateFalseAndDeletedFalseAndAvailableTrue(
            String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndName1LikeIgnoreCaseAndValidateFalseAndAvailableTrue(
            String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndName1LikeIgnoreCaseAndValidateFalseAndDeletedFalseAndAvailableTrue(
            String countryName, String name);
}
