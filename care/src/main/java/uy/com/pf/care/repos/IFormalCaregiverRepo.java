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
    List<FormalCaregiver> findByNameAndCountryNameOrderByInterestZones_DepartmentName(
            String name, String countryName);
    List<FormalCaregiver> findByNameAndCountryNameAndDeletedFalseOrderByInterestZones_DepartmentName(
            String name, String countryName);

    //**** Busquedas sin indices ****

    List<FormalCaregiver> findByNameLikeAndCountryNameOrderByInterestZones_DepartmentName(
            String name, String countryName);
    List<FormalCaregiver> findByNameLikeAndCountryNameAndDeletedFalseOrderByInterestZones_DepartmentName(
            String name, String countryName);

}
