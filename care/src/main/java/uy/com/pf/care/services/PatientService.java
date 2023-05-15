package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.PatientSaveException;
import uy.com.pf.care.model.documents.Patient;
import uy.com.pf.care.repos.IPatientRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class PatientService implements IPatientService{

    @Autowired
    private IPatientRepo patientRepo;

   // private static final Logger log = LoggerFactory.getLogger(CuidadosApplication.class);

    @Override
    public String save(Patient patient) {
        try{
            Patient newPatient = patientRepo.save(patient);
            log.info("*** Paciente guardado con exito: " + LocalDateTime.now());
            return newPatient.getPatientId();

        }catch(Exception e){
            log.warning("*** ERROR GUARDANDO PACIENTE: " + e);
            throw new PatientSaveException(patient);
        }
    }

    @Override
    public Boolean setDeletion(String id, Boolean isDeleted) {
        Optional<Patient> patient = this.findId(id);
        if (patient.isPresent()) {
            patient.get().setDeleted(isDeleted);
            this.save(patient.get());
            return true;
        }
        return false;
    }

    @Override
    public List<Patient> findAll(Boolean includeDeleted, String countryName) {
        if (includeDeleted)
            return patientRepo.findByZone_CountryNameOrderByName1(countryName);

        return patientRepo.findByZone_CountryNameAndDeletedFalseOrderByName1(countryName);
    }

    @Override
    public Optional<Patient> findId(String id) {
        return patientRepo.findById(id);
    }

    @Override
    public Optional<Patient> findIdentificationDocument(Integer identificationDocument, String countryName) {
        return patientRepo.findByIdentificationDocumentAndZone_CountryName(identificationDocument, countryName);
    }

    @Override
    public Optional<Patient> findMail(String mail) {
        return patientRepo.findByMail(mail);
    }

    @Override
    public List<Patient> findName1(
            String name1, String neighborhoodName, String cityName, String departmentName, String countryName) {

        if (neighborhoodName == null)
            return patientRepo.
                    findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndName1AndDeletedFalseOrderByName1(
                            countryName, departmentName, cityName, name1);
        return patientRepo.
                findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndZone_NeighborhoodNameAndName1AndDeletedFalseOrderByName1(
                        countryName, departmentName, cityName, neighborhoodName, name1);
    }

    @Override
    public List<Patient> findCity(Boolean includeDeleted, String cityName, String departmentName, String countryName) {
        if (includeDeleted)
            return patientRepo.findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameOrderByName1(
                    countryName, departmentName, cityName);

        return patientRepo.findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndDeletedFalseOrderByName1(
                countryName, departmentName, cityName);
    }

    @Override
    public List<Patient> findDepartment(Boolean includeDeleted, String departmentName, String countryName) {
        if (includeDeleted)
            return patientRepo.findByZone_CountryNameAndZone_DepartmentNameOrderByName1(countryName, departmentName);

        return patientRepo.findByZone_CountryNameAndZone_DepartmentNameAndDeletedFalseOrderByName1(
                    countryName, departmentName);
    }

/*

    @Override
    public Optional<Patient> findIdentificationDocument(Integer identificationDocument, String countryName) {
        return patientRepo.findByIdentificationDocumentAndZone_CountryNameAndDeletedFalse(
                identificationDocument, countryName);
    }

    @Override
    public List<Patient> findName1Like(String name1, String cityName, String departmentName, String countryName) {
        return patientRepo.findByName1LikeAndZone_CityNameAndZone_DepartmentNameAndZone_CountryNameAndDeletedFalse(
                name1, cityName, departmentName, countryName);
    }


    @Override
    public List<Patient> findSurname1Like(String surname1, String cityName, String departmentName, String countryName) {
        return patientRepo.findBySurname1LikeAndZone_CityNameAndZone_DepartmentNameAndZone_CountryNameAndDeletedFalse(
                surname1, cityName, departmentName, countryName
        );
    }

    @Override
    public List<Patient> findName1Surname1Likes(String name1, String surname1, String cityName, String departmentName, String countryName) {
        return patientRepo.findByName1LikeAndSurname1LikeAndAddress_CityNameAndAddress_DepartmentNameAndAddress_CountryName(
                name1, surname1, cityName, departmentName, countryName
        );
        return patientRepo.findByName1LikeAndSurname1LikeAndZone_CityNameAndZone_DepartmentNameAndZone_CountryNameAndDeletedFalse(
                name1, surname1, cityName, departmentName, countryName
        );
    }
*/

}
