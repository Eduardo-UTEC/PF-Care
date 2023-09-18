package uy.com.pf.care.infra.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.Role;

import java.util.List;

@Repository
public interface IRoleRepo extends MongoRepository<Role, String> {
    //Role findByRolNameAndCountryNameAndDepartmentName(String rolName, String departmentName, String countryName);
    List<Role> findByCountryNameAndDepartmentNameOrderByDepartmentName(String countryName, String departmentName);
}
