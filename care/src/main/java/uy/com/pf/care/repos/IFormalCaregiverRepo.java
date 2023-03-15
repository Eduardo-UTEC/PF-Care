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

    //TODO: busquedas por zonas de interes:
    //  * por ciudad + departamento (de interes, no el de residencia),
    //  * por barrio + ciudad + departamento


}
