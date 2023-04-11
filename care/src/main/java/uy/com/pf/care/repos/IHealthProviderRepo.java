package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import uy.com.pf.care.model.documents.HealthProvider;

import java.util.List;

public interface IHealthProviderRepo extends MongoRepository<HealthProvider, String> {

    // Proveedores de Salud de un Pais
    List<HealthProvider> findByCountryName(String countryName);

    // Proveedores de Salud de un Departamento+Pais
    List<HealthProvider> findByDepartmentNameAndCountryName(String departmentName, String countryName);

    // Proveedores de Salud de una Ciudad+Departamento+Pais
    List<HealthProvider> findByCityNameAndDepartmentNameAndCountryName(
            String cityName, String departmentName, String countryName);

}
