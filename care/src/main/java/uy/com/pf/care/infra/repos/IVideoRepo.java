package uy.com.pf.care.infra.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.Role;
import uy.com.pf.care.model.documents.Video;

import java.util.List;

@Repository
public interface IVideoRepo extends MongoRepository<Video, String> {
    List<Video> findByCountryNameAndDepartmentName(String countryName, String departmentName);
}
