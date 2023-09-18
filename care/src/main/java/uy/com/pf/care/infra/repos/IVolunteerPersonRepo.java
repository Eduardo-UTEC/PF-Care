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
    List<VolunteerPerson> findByCountryName(String countryName);
    List<VolunteerPerson> findByCountryNameAndDeletedFalse(String countryName);
    List<VolunteerPerson> findByCountryNameAndName1IgnoreCase(String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndName1IgnoreCaseAndDeletedFalse(String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndName1LikeIgnoreCase(String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndName1LikeIgnoreCaseAndDeletedFalse(String countryName, String name1);

}