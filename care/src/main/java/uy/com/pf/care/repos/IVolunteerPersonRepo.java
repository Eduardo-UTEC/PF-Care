package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.FormalCaregiver;
import uy.com.pf.care.model.documents.VolunteerActivity;
import uy.com.pf.care.model.documents.VolunteerPerson;

import java.util.List;
import java.util.Optional;

@Repository
public interface IVolunteerPersonRepo extends MongoRepository<VolunteerPerson, String> {
    Optional<VolunteerPerson> findByCountryNameAndIdentificationDocument(
            String countryName, String identificationDocument);
    Optional<VolunteerPerson> findByMail(String mail);
    List<VolunteerPerson> findByCountryName(String countryName);
    List<VolunteerPerson> findByCountryNameAndDeletedFalse(String countryName);
    List<VolunteerPerson> findByCountryNameAndName1(String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndName1AndDeletedFalse(String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndName1Like(String countryName, String name1);
    List<VolunteerPerson> findByCountryNameAndName1LikeAndDeletedFalse(String countryName, String name1);

}