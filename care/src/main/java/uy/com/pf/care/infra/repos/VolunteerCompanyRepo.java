package uy.com.pf.care.infra.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.VolunteerCompany;

import java.util.List;

@Repository
public interface VolunteerCompanyRepo extends MongoRepository<VolunteerCompany, String> {
    List<VolunteerCompany> findByCountryName(String countryName);
    List<VolunteerCompany> findByCountryNameAndValidateTrue(String countryName);
    List<VolunteerCompany> findByCountryNameAndValidateTrueAndDeletedFalse(String countryName);
    List<VolunteerCompany> findByCountryNameAndDeletedFalse(String countryName);

}
