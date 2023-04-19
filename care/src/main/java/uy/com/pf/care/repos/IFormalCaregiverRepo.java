package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.FormalCaregiver;

import java.util.List;

@Repository
public interface IFormalCaregiverRepo extends MongoRepository<FormalCaregiver, String> {

    /*
        Busqueda por rangos... ejemplo:
          List<User> findByAgeBetween(Integer startAge, Integer endAge);

        Contar:
             Long countByLastname(String lastname);
     */


    FormalCaregiver findByMail(String mail);
    List<FormalCaregiver> findByCountryNameOrderByInterestZones_DepartmentName(String countryName);
    List<FormalCaregiver> findByCountryNameAndDeletedFalseOrderByInterestZones_DepartmentName(String countryName);
    List<FormalCaregiver> findByCountryNameAndNameOrderByInterestZones_DepartmentName(String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndNameAndDeletedFalseOrderByInterestZones_DepartmentName(
        String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndNameLikeOrderByInterestZones_DepartmentName(
            String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndNameLikeAndDeletedFalseOrderByInterestZones_DepartmentName(
            String countryName, String name);

}
