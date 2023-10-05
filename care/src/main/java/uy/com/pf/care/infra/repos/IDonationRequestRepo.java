package uy.com.pf.care.infra.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.DonationRequest;

import java.util.List;

@Repository
public interface IDonationRequestRepo extends MongoRepository<DonationRequest, String> {

    List<DonationRequest> findByCountryNameAndDepartmentName(String countryName, String departmentName);
    List<DonationRequest> findByCountryNameAndDepartmentNameAndActiveTrue(String countryName, String departmentName);


}
