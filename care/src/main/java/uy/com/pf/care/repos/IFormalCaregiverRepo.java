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
    List<FormalCaregiver> findByCountryNameIgnoreCase(String countryName);
    List<FormalCaregiver> findByCountryNameIgnoreCaseAndDeletedFalse(String countryName);
    List<FormalCaregiver> findByCountryNameIgnoreCaseAndNameIgnoreCase(String countryName, String name);
    List<FormalCaregiver> findByCountryNameIgnoreCaseAndNameIgnoreCaseAndDeletedFalse(String countryName, String name);
    List<FormalCaregiver> findByCountryNameIgnoreCaseAndNameLikeIgnoreCase(String countryName, String name);
    List<FormalCaregiver> findByCountryNameIgnoreCaseAndNameLikeIgnoreCaseAndDeletedFalse(String countryName, String name);

}
