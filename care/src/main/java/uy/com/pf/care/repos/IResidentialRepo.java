package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.Residential;

import java.util.List;

@Repository
public interface IResidentialRepo extends MongoRepository<Residential, String> {

    // Todos los recienciales de un pais
    List<Residential> findByCountryName(String countryName);

    // Todos los recienciales de un departamento+pais
    List<Residential> findByDepartmentNameAndCountryName(String departmentName, String countryName);

    // Todos los recienciales de una ciudad+departamento+pais
    List<Residential> findByCityNameAndDepartmentNameAndCountryName(String cityName, String departmentName, String countryName);

    // Busqueda aproximada por nombre nombre de residenciales de una ciudad+Departamento+Pais
    List<Residential> findByNameLikeAndCityNameAndDepartmentNameAndCountryName(
            String name, String cityName, String departmentName, String countryName
    );

    // Busqueda aproximada por nombre nombre de residenciales de un Departamento+Pais
    List<Residential> findByNameLikeAndDepartmentNameAndCountryName(
            String name, String departmentName, String countryName
    );
}
