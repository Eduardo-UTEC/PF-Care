package uy.com.pf.care.infra.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.VolunteerPerson;

import java.util.List;
import java.util.Optional;

@Repository
public interface IVolunteerPersonRepo extends MongoRepository<VolunteerPerson, String> {
    Optional<VolunteerPerson> findByCountryNameAndIdentificationDocument(
            String countryName, String identificationDocument);
    Optional<VolunteerPerson> findByMailIgnoreCase(String mail);
    //List<VolunteerPerson> findByCountryName(String countryName);
    List<VolunteerPerson> findByCountryNameAndValidateFalseOrderByName1(String countryName);
    List<VolunteerPerson> findByCountryNameAndValidateTrueOrderByName1(String countryName);
    //List<VolunteerPerson> findByCountryNameAndDeletedFalse(String countryName);
    List<VolunteerPerson> findByCountryNameAndValidateFalseAndDeletedFalseOrderByName1(String countryName);
    List<VolunteerPerson> findByCountryNameAndValidateTrueAndDeletedFalseOrderByName1(String countryName);
    //List<VolunteerPerson> findByCountryNameAndName1IgnoreCase(String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndValidateFalseAndName1IgnoreCase(String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndValidateTrueAndName1IgnoreCase(String countryName, String name1);
    //List<VolunteerPerson> findByCountryNameAndName1IgnoreCaseAndDeletedFalse(String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndValidateTrueAndName1IgnoreCaseAndDeletedFalse(
            String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndValidateFalseAndName1IgnoreCaseAndDeletedFalse(
            String countryName, String name1);
    //List<VolunteerPerson> findByCountryNameAndName1LikeIgnoreCase(String countryName, String name1);
    //List<VolunteerPerson> findByCountryNameAndName1LikeIgnoreCaseAndDeletedFalse(String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndValidateFalseAndName1LikeIgnoreCase(String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndValidateFalseAndName1LikeIgnoreCaseAndDeletedFalse(
            String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndValidateTrueAndName1LikeIgnoreCase(
            String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndValidateTrueAndName1LikeIgnoreCaseAndDeletedFalse(
            String countryName, String name1);
}