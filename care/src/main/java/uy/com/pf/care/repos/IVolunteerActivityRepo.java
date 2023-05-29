package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.Video;
import uy.com.pf.care.model.documents.VolunteerActivity;

import java.util.List;

@Repository
public interface IVolunteerActivityRepo extends MongoRepository<VolunteerActivity, String> {
    List<VolunteerActivity> findByCountryName(String countryName);
    List<VolunteerActivity> findByCountryNameAndDeletedFalse(String countryName);
    List<VolunteerActivity> findByCountryNameAndDepartmentName(String countryName, String departmentName);
    List<VolunteerActivity> findByCountryNameAndDepartmentNameAndDeletedFalse(
            String countryName, String departmentName);
}
