package uy.com.pf.care.infra.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.Residential;

import java.util.List;

@Repository
public interface IResidentialRepo extends MongoRepository<Residential, String> {

    List<Residential> findByCountryName(String countryName);
    List<Residential> findByCountryNameAndDeletedFalse(String countryName);
    List<Residential> findByCountryNameAndDepartmentName(String countryName, String departmentName);
    List<Residential> findByCountryNameAndDepartmentNameAndDeletedFalse(String countryName, String departmentName);
    List<Residential> findByCountryNameAndDepartmentNameAndCityName(
            String countryName,  String departmentName, String cityName);
    List<Residential> findByCountryNameAndDepartmentNameAndCityNameAndDeletedFalse(
            String countryName,  String departmentName, String cityName);
    List<Residential> findByCountryNameAndDepartmentNameAndCityNameAndNameIgnoreCase(
            String countryName,  String departmentName, String cityName, String name);
    List<Residential> findByCountryNameAndDepartmentNameAndCityNameAndNameIgnoreCaseAndDeletedFalse(
            String countryName,  String departmentName, String cityName, String name);

    /*
    List<Residential> findByCountryNameAndDepartmentNameAndCityNameAndNameLike(
            String countryName, String departmentName, String cityName, String nameLike);
    List<Residential> findByCountryNameAndDepartmentNameAndCityNameAndNameLikeAndDeletedFalse(
            String countryName, String departmentName, String cityName, String nameLike);
    */
}
