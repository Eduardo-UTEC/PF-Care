package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.Zone;

import java.util.List;

@Repository
public interface IZoneRepo extends MongoRepository<Zone, String> {
    List<Zone> findByCountryName(String countryName);
    List<Zone> findByCountryNameAndDeletedFalse(String countryName);

    @Query("{'countryName': :#{#countryName}, 'departmentName': :#{#departmentName}}")
    //@Query("{'countryName': ?0, 'departmentName': ?1}")
    List<Zone> myFind(String countryName, String departmentName);
    List<Zone> findByCountryNameAndDepartmentNameAndDeletedFalse(String countryName);

}
