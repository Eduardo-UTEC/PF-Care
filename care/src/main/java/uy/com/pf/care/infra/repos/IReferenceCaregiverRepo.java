package uy.com.pf.care.infra.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.ReferenceCaregiver;

import java.util.List;
import java.util.Optional;

@Repository
public interface IReferenceCaregiverRepo extends MongoRepository<ReferenceCaregiver, String> {
    Optional<ReferenceCaregiver> findByIdentificationDocumentAndZone_CountryName(
            Integer identificationDocument, String countryName);
    List<ReferenceCaregiver>
    findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndZone_NeighborhoodNameAndName1IgnoreCaseOrderByName1(
            String countryName, String departmentName, String cityName, String neighborhoodName, String name1);
    List<ReferenceCaregiver> findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameOrderByName1(
            String countryName, String departmentName, String cityName);
    List<ReferenceCaregiver> findByZone_CountryNameAndZone_DepartmentNameOrderByName1(
            String countryName, String departmentName);
    List<ReferenceCaregiver> findByZone_CountryNameOrderByName1(String countryName);


}
