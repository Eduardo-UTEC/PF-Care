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

        Ordenar:
            List<User> findByNameOrderByName(String name);
            List<User> findByNameOrderByNameDesc(String name);

        Contar:
             Long countByLastname(String lastname);
     */


    //**** Busquedas por indices ****

    FormalCaregiver findByMail(String mail);
    List<FormalCaregiver> findByCountryNameOrderByInterestZones_DepartmentName(String countryName);
    List<FormalCaregiver> findByCountryNameAndDeletedFalseOrderByInterestZones_DepartmentName(String countryName);
    List<FormalCaregiver> findByNameAndCountryName(String name, String countryName);
    List<FormalCaregiver> findByNameAndCountryNameAndDeletedFalse(String name, String countryName);

    //**** Busquedas sin indices ****

    List<FormalCaregiver> findByNameLikeAndCountryNameOrderByName(String name, String countryName);
    List<FormalCaregiver> findByNameLikeAndCountryNameAndDeletedFalseOrderByName(String name, String countryName);

}
