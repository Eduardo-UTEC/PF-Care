package uy.com.pf.care.infra.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.VolunteerActivity;

import java.util.List;
import java.util.Optional;

@Repository
public interface IVolunteerActivityRepo extends MongoRepository<VolunteerActivity, String> {
    Optional<VolunteerActivity> findByCountryNameAndDepartmentNameAndNameIgnoreCase(
            String countryName, String departmentName, String name);
    List<VolunteerActivity> findByCountryNameOrderByName(String countryName);
    List<VolunteerActivity> findByCountryNameAndDeletedFalseOrderByName(String countryName);
    List<VolunteerActivity> findByCountryNameAndDepartmentNameOrderByName(
            String countryName, String departmentName);
    List<VolunteerActivity> findByCountryNameAndDepartmentNameAndDeletedFalseOrderByName(
            String countryName, String departmentName);
}
