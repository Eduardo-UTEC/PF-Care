package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.Zone;
import uy.com.pf.care.model.objects.NeighborhoodObject;

import java.util.List;
import java.util.Optional;

public interface IZoneService {
    Zone save(Zone zone);
    List<Zone> findAll(Boolean includeDeleted, String countryName);
    Optional<Zone> findId(String id);
    List<NeighborhoodObject> findNeighborhoods(
            Boolean includeDeleted, String cityName, String departmentName, String countryName);
    List<String> findCities(Boolean includeDeleted, String departmentName, String countryName);
    List<String> findDepartments(Boolean includeDeleted, String countryName);
    List<String> findCountries(Boolean includeDeleted);
    Boolean setDeletion(String id, Boolean isDeleted);
}
