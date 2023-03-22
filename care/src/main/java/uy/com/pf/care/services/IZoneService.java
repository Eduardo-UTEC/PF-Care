package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.Zone;
import uy.com.pf.care.model.objects.NeighborhoodObject;

import java.util.List;
import java.util.Optional;

public interface IZoneService {
    Zone save(Zone zone);
    List<Zone> findAllZones(Boolean includeDeleted, String countryName);
    Optional<Zone> findId(String id);

    // Barrios de una ciudad+departamento+pais
    List<NeighborhoodObject> findAllNeighborhoods(
            Boolean includeDeleted, String cityName, String departmentName, String countryName);

    // Ciudades de un departamento+pais
    List<String> findAllCities(Boolean includeDeleted, String departmentName, String countryName);

    // Departamentos de un pais
    List<String> findAllDepartments(Boolean includeDeleted, String countryName);

    // Paises
    List<String> findAllCountries(Boolean includeDeleted);

}
