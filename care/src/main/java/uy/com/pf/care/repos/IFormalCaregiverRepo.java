package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.FormalCaregiver;

import java.util.List;

@Repository
public interface IFormalCaregiverRepo extends MongoRepository<FormalCaregiver, String> {

    //**** Busquedas por indices ****

    FormalCaregiver findByMail(String mail);
    FormalCaregiver findByNameAndTelephoneAndDepartmentNameAndCountryName(
            String name, String telephone, String departmentName, String countryName);

    //**** Busquedas sin indices ****

    FormalCaregiver findByNameAndTelephoneAndDepartmentNameAndCountryNameAndDeletedFalse(
            String name, String telephone, String departmentName, String countryName);
    List<FormalCaregiver> findByNameLikeAndDepartmentNameAndCountryName(
            String name, String departmentName, String countryName);
    List<FormalCaregiver> findByNameLikeAndDepartmentNameAndCountryNameAndDeletedFalse(
            String name, String departmentName, String countryName);
    List<FormalCaregiver> findByDepartmentNameAndCountryName(String departmentName, String countryName);
    List<FormalCaregiver> findByDepartmentNameAndCountryNameAndDeletedFalse(String departmentName, String countryName);
    List<FormalCaregiver> findByCountryName(String countryName);
    List<FormalCaregiver> findByCountryNameAndDeletedFalse(String countryName);
    List<FormalCaregiver> findByAvailableTrueAndDepartmentNameAndCountryNameAndDeletedFalse(
            String departmentName, String countryName);

    // Zonas de interes: Departamento de interes + Pais de residencia
    List<FormalCaregiver> findByInterestZones_DepartmentNameAndCountryName(
            String interestDepartmentName, String countryName);

    List<FormalCaregiver> findByInterestZones_DepartmentNameAndCountryNameAndDeletedFalse(
            String interestDepartmentName, String countryName);

    //TODO: modificar consultas para que si busca, por ej, por barrio Prieto, ademas busque los que tengan marcado
    // todos los barrios (neighborhoodNames=[])

    // Zonas de interes: Ciudad de interes + Departamento de interes + Pais de residencia
    List<FormalCaregiver> findByInterestZones_cities_CityNameAndInterestZones_DepartmentNameAndCountryName(
            String interestCityName, String interestDepartmentName, String countryName);

    List<FormalCaregiver> findByInterestZones_cities_CityNameAndInterestZones_DepartmentNameAndCountryNameAndDeletedFalse(
            String interestCityName, String interestDepartmentName, String countryName);

    // Zonas de interes: Barrio de interes + Ciudad de interes + Departamento de interes + Pais de residencia
    List<FormalCaregiver> findByInterestZones_cities_neighborhoodNamesAndInterestZones_cities_CityNameAndInterestZones_DepartmentNameAndCountryName(
            String interestNeighborhoodName, String interestCityName, String interestDepartmentName, String countryName);

    List<FormalCaregiver> findByInterestZones_cities_neighborhoodNamesAndInterestZones_cities_CityNameAndInterestZones_DepartmentNameAndCountryNameAndDeletedFalse(
            String interestNeighborhoodName, String interestCityName, String interestDepartmentName, String countryName);

}
