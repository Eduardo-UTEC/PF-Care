package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.ReferenceCaregiver;
import uy.com.pf.care.model.enums.RelationshipEnum;

import java.util.List;
import java.util.Optional;

public interface IReferenceCaregiverService {

    String save(ReferenceCaregiver referenceCaregiver);
    Boolean update(ReferenceCaregiver newReferenceCaregiver);
    Boolean addPatient(String referenceCaregiverId, String patientId);
    //Boolean changeRelationshipPatient(String referenceCaregiverId, String patientId, RelationshipEnum relationship);
    Optional<ReferenceCaregiver> findId(String id);
    Optional<ReferenceCaregiver> findMail(String mail);
    Optional<ReferenceCaregiver> findIdentificationDocument(Integer identificationDocument, String countryName);
    List<ReferenceCaregiver> findName1(
            String name1,
            String neighborhoodName,
            String cityName,
            String departmentName,
            String countryName);
    List<ReferenceCaregiver> findCity(String cityName, String departmentName, String countryName);
    List<ReferenceCaregiver> findDepartment(String departmentName, String countryName);
    List<ReferenceCaregiver> findAll(String countryName);
}
