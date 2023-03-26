package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.FormalCaregiver;

import java.util.List;

@Repository
public interface IFormalCaregiverRepo extends MongoRepository<FormalCaregiver, String> {

    //**** Busquedas por indices ****

    FormalCaregiver findByMail(String mail);
    List<FormalCaregiver> findByCountryName(String countryName);
    List<FormalCaregiver> findByCountryNameAndDeletedFalse(String countryName);
    FormalCaregiver findByNameAndCountryName(String name, String countryName);
    FormalCaregiver findByNameAndCountryNameAndDeletedFalse(String name, String countryName);

    //**** Busquedas sin indices ****

    List<FormalCaregiver> findByNameLikeAndCountryName(String name, String countryName);
    List<FormalCaregiver> findByNameLikeAndCountryNameAndDeletedFalse(String name, String countryName);

}
