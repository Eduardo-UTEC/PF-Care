package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.FormalCaregiver;

import java.util.List;

@Repository
public interface IFormalCaregiverRepo extends MongoRepository<FormalCaregiver, String> {
    List<FormalCaregiver> findByDepartmentNameAndCountryNameAndDeletedFalse(String departmentName, String countryName);
    List<FormalCaregiver> findByCountryNameAndDeletedFalse(String countryName);
    FormalCaregiver findByMail(String mail);
}
