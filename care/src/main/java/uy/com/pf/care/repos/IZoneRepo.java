package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.Zone;

import java.util.List;

@Repository
public interface IZoneRepo extends MongoRepository<Zone, String> {

    /* Armar querys nativas...

        Por nombre de parametros:
        @Query("{'countryName': :#{#countryName}, 'departmentName': :#{#departmentName}}")

        Por parametros posicionales:
        //@Query("{'countryName': ?0, 'departmentName': ?1}")
     */


    List<Zone> findByCountryName(String countryName);
    List<Zone> findByCountryNameAndDeletedFalse(String countryName);
}
