package uy.com.pf.care.infra.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.Material;

import java.util.List;

@Repository
public interface IMaterialRepo extends MongoRepository<Material, String> {

    /* Armar querys nativas...

        Por nombre de parametros:
        @Query("{'countryName': :#{#countryName}, 'departmentName': :#{#departmentName}}")

        Por parametros posicionales:
        //@Query("{'countryName': ?0, 'departmentName': ?1}")
     */

    List<Material> findByCountryName(String countryName);
    List<Material> findByCountryNameAndDeletedFalse(String countryName);
    List<Material> findByCountryNameAndNameIgnoreCase(String countryName, String name);
    List<Material> findByCountryNameAndNameIgnoreCaseLike(String countryName, String name);
    List<Material> findByCountryNameAndNameIgnoreCaseAndDeletedFalse(String countryName, String name);
    List<Material> findByCountryNameAndNameIgnoreCaseLikeAndDeletedFalse(String countryName, String name);


}
