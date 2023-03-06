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

    /*@Override
    public void save(Patient patient) {
        try{
            patientRepo.save(patient);
            log.info("*** Paciente guardado con exito: " + LocalDateTime.now());
        }catch(Exception e){
            log.warning("*** ERROR GUARDANDO PACIENTE: " + e);
            throw new PatientSaveException(patient);
        }
    }*/
    @Override
    public Patient save(Patient patient) {
        try{
            Patient newPatient = patientRepo.save(patient);
            log.info("*** Paciente guardado con exito: " + LocalDateTime.now());
            return newPatient;

        }catch(Exception e){
            log.warning("*** ERROR GUARDANDO PACIENTE: " + e);
            throw new PatientSaveException(patient);
        }
    }

    @Override
    public List<Patient> findAll(String countryName) {
        return patientRepo.findByZone_CountryNameAndDeletedFalse(countryName);
    }

    @Override
    public Optional<Patient> findId(String id) {
        return patientRepo.findById(id);
    }

    @Override
    public Optional<Patient> findIdentificationDocument(Integer identificationDocument, String countryName) {
        //return patientRepo.findByIdentificationDocumentAndAddress_CountryName(identificationDocument, countryName);
        return patientRepo.findByIdentificationDocumentAndZone_CountryNameAndDeletedFalse(identificationDocument, countryName);

    }

    /*@Override
    public List<Patient> findName1(String name1, String cityName, String departmentName, String countryName) {
        return patientRepo.findByName1AndAddress_CityNameAndAddress_DepartmentNameAndAddress_CountryName(
                name1, cityName, departmentName, countryName
        );
    }*/

   /* @Override
    public List<Patient> findSurname1(String surname1, String cityName, String departmentName, String countryName) {
        return patientRepo.findBySurname1AndAddress_CityNameAndAddress_DepartmentNameAndAddress_CountryName(
                surname1, cityName, departmentName, countryName
        );
    }*/

   /* @Override
    public List<Patient> findName1Surname1(String name1, String surname1, String cityName, String departmentName, String countryName) {
        return patientRepo.findByName1AndSurname1AndAddress_CityNameAndAddress_DepartmentNameAndAddress_CountryName(
                name1, surname1, cityName, departmentName, countryName
        );
    }*/

    @Override
    public List<Patient> findName1Like(String name1, String cityName, String departmentName, String countryName) {
//        return patientRepo.findByName1LikeAndAddress_CityNameAndAddress_DepartmentNameAndAddress_CountryName(
//                name1, cityName, departmentName, countryName
//        );
        return patientRepo.findByName1LikeAndZone_CityNameAndZone_DepartmentNameAndZone_CountryNameAndDeletedFalse(
                name1, cityName, departmentName, countryName
        );
    }

    @Override
    public List<Patient> findSurname1Like(String surname1, String cityName, String departmentName, String countryName) {
//        return patientRepo.findBySurname1LikeAndAddress_CityNameAndAddress_DepartmentNameAndAddress_CountryName(
//                surname1, cityName, departmentName, countryName
//        );
        return patientRepo.findBySurname1LikeAndZone_CityNameAndZone_DepartmentNameAndZone_CountryNameAndDeletedFalse(
                surname1, cityName, departmentName, countryName
        );
    }

    @Override
    public List<Patient> findName1Surname1Likes(String name1, String surname1, String cityName, String departmentName, String countryName) {
//        return patientRepo.findByName1LikeAndSurname1LikeAndAddress_CityNameAndAddress_DepartmentNameAndAddress_CountryName(
//                name1, surname1, cityName, departmentName, countryName
//        );
        return patientRepo.findByName1LikeAndSurname1LikeAndZone_CityNameAndZone_DepartmentNameAndZone_CountryNameAndDeletedFalse(
                name1, surname1, cityName, departmentName, countryName
        );
    }

    @Override
    public List<Patient> findByCity(String cityName, String departmentName, String countryName) {
        return patientRepo.findByZone_CityNameAndZone_DepartmentNameAndZone_CountryNameAndDeletedFalse(
                cityName, departmentName, countryName
        );
    }

    @Override
    public List<Patient> findByDepartment(String departmentName, String countryName) {
        return patientRepo.findByZone_DepartmentNameAndZone_CountryNameAndDeletedFalse(departmentName, countryName);
    }

    @Override
    public boolean logicalDelete(String id) {
        Optional<Patient> patient = this.findId(id);
        if (patient.isPresent()) {
            patient.get().setDeleted(true);
            this.save(patient.get());
            return true;
        }
        return false;
    }

}
