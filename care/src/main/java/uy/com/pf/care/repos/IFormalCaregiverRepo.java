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


    FormalCaregiver findByMailIgnoreCase(String mail);
    List<FormalCaregiver> findByCountryName(String countryName);
    List<FormalCaregiver> findByCountryNameAndDeletedFalse(String countryName);
    List<FormalCaregiver> findByCountryNameAndNameIgnoreCase(String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndNameIgnoreCaseAndDeletedFalse(String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndNameLikeIgnoreCase(String countryName, String name);
    List<FormalCaregiver> findByCountryNameAndNameLikeIgnoreCaseAndDeletedFalse(String countryName, String name);

}
