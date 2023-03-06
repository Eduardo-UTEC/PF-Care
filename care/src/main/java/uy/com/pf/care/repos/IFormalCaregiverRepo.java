package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.FormalCaregiver;

import java.util.List;

@Repository
public interface IFormalCaregiverRepo extends MongoRepository<FormalCaregiver, String> {
    List<FormalCaregiver> findByDepartmentNameAndCountryName(String departmentName, String countryName);
    List<FormalCaregiver> findByCountryName(String countryName);
}
