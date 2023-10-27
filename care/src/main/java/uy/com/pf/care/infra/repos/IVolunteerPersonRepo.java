package uy.com.pf.care.infra.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.FormalCaregiver;
import uy.com.pf.care.model.documents.VolunteerPerson;

import java.util.List;
import java.util.Optional;

@Repository
public interface IVolunteerPersonRepo extends MongoRepository<VolunteerPerson, String> {
    Optional<VolunteerPerson> findByCountryNameAndTelephone(String countryName, String telephone);
    Optional<VolunteerPerson> findByCountryNameAndIdentificationDocument(
            String countryName, String identificationDocument);
    Optional<VolunteerPerson> findByMailIgnoreCase(String mail);
    List<VolunteerPerson> findByCountryNameAndValidateFalseAndAvailableTrueOrderByName1(String countryName);
    List<VolunteerPerson> findByCountryNameAndValidateTrueAndAvailableTrueOrderByName1(String countryName);
    //List<VolunteerPerson> findByCountryNameAndDeletedFalse(String countryName);
    List<VolunteerPerson> findByCountryNameAndValidateFalseAndDeletedFalseAndAvailableTrueOrderByName1(String countryName);
    List<VolunteerPerson> findByCountryNameAndValidateTrueAndDeletedFalseAndAvailableTrueOrderByName1(String countryName);
    //List<VolunteerPerson> findByCountryNameAndName1IgnoreCase(String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndValidateFalseAndAvailableTrueAndName1IgnoreCase(String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndValidateTrueAndAvailableTrueAndName1IgnoreCase(String countryName, String name1);
    //List<VolunteerPerson> findByCountryNameAndName1IgnoreCaseAndDeletedFalse(String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndValidateTrueAndName1IgnoreCaseAndDeletedFalseAndAvailableTrue(
            String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndValidateFalseAndName1IgnoreCaseAndDeletedFalseAndAvailableTrue(
            String countryName, String name1);
    //List<VolunteerPerson> findByCountryNameAndName1LikeIgnoreCase(String countryName, String name1);
    //List<VolunteerPerson> findByCountryNameAndName1LikeIgnoreCaseAndDeletedFalse(String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndValidateFalseAndAvailableTrueAndName1LikeIgnoreCase(String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndValidateFalseAndName1LikeIgnoreCaseAndDeletedFalseAndAvailableTrue(
            String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndValidateTrueAndAvailableTrueAndName1LikeIgnoreCase(
            String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndValidateTrueAndName1LikeIgnoreCaseAndDeletedFalseAndAvailableTrue(
            String countryName, String name1);
}