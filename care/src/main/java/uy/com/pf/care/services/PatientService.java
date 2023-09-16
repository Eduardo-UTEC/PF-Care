package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.PatientSaveException;
import uy.com.pf.care.exceptions.PatientUpdateException;
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
            this.defaultValues(patient);
            String id = patientRepo.save(patient).getPatientId();
            log.info("*** Paciente guardado con exito: " + LocalDateTime.now());
            return id;

        }catch(Exception e){
            log.warning("*** ERROR GUARDANDO PACIENTE: " + e);
            throw new PatientSaveException("*** ERROR GUARDANDO PACIENTE");
        }
    }

    @Override
    public Boolean update(Patient newPatient) {
        try{
            Optional<Patient> entityFound = patientRepo.findById(newPatient.getPatientId());
            if (entityFound.isPresent()){
                this.defaultValues(entityFound.get(), newPatient);
                patientRepo.save(newPatient);
                log.info("Paciente actualizado con exito");
                return true;
            }
            log.info("No se encontro el paciente con id " + newPatient.getPatientId());
            return false;

        }catch(Exception e){
            log.warning("*** ERROR ACTUALIZANDO PACIENTE: " + e);
            throw new PatientUpdateException("*** ERROR ACTUALIZANDO PACIENTE");
        }
    }

    @Override
    public Boolean setValidation(String id, Boolean isValidated) {
        Optional<Patient> patient = this.findId(id);
        if (patient.isPresent()) {
            patient.get().setValidate(isValidated);
            patientRepo.save(patient.get());
            return true;
        }
        return false;
    }

    @Override
    public Boolean setDeletion(String id, Boolean isDeleted) {
        Optional<Patient> patient = this.findId(id);
        if (patient.isPresent()) {
            patient.get().setDeleted(isDeleted);
            patientRepo.save(patient.get());
            return true;
        }
        return false;
    }

    @Override
    public List<Patient> findAll(Boolean withoutValidate, Boolean includeDeleted, String countryName) {
        /*if (includeDeleted)
            return patientRepo.findByZone_CountryNameAndValidateTrueOrderByName1(countryName);

        return patientRepo.findByZone_CountryNameAndValidateTrueAndDeletedFalseOrderByName1(countryName);

         */
        if (withoutValidate) {
            if (includeDeleted)
                return patientRepo.findByZone_CountryNameAndValidateFalseOrderByName1(countryName);

            return patientRepo.findByZone_CountryNameAndValidateFalseAndDeletedFalseOrderByName1(countryName);
        } else {
            //solo los validados
            if (includeDeleted)
                return patientRepo.findByZone_CountryNameAndValidateTrueOrderByName1(countryName);

            return patientRepo.findByZone_CountryNameAndValidateTrueAndDeletedFalseOrderByName1(countryName);
        }
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
        return patientRepo.findByMailIgnoreCase(mail);
    }

    @Override
    public List<Patient> findName1(
            String name1,
            Boolean withoutValidate,
            Boolean includeDeleted,
            String neighborhoodName,
            String cityName,
            String departmentName,
            String countryName) {

        /*if (neighborhoodName == null)
            return patientRepo.
                    findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndName1IgnoreCaseAndValidateTrueAndDeletedFalseOrderByName1(
                            countryName, departmentName, cityName, name1);
        return patientRepo.
                findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndZone_NeighborhoodNameAndName1IgnoreCaseAndValidateTrueAndDeletedFalse(
                        countryName, departmentName, cityName, neighborhoodName, name1);
        */

        if (withoutValidate) {
            if (includeDeleted)
                if (neighborhoodName == null)
                    return patientRepo.
                            findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndName1IgnoreCaseAndValidateFalseOrderByName1(
                                    countryName, departmentName, cityName, name1);
                else
                    return patientRepo.
                            findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndZone_NeighborhoodNameAndName1IgnoreCaseAndValidateFalse(
                                    countryName, departmentName, cityName, neighborhoodName, name1);
            else
                if (neighborhoodName == null)
                    return patientRepo.
                            findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndName1IgnoreCaseAndValidateFalseAndDeletedFalse(
                                countryName, departmentName, cityName, name1);
                else
                    return patientRepo.
                            findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndZone_NeighborhoodNameAndName1IgnoreCaseAndValidateFalseAndDeletedFalse(
                                countryName, departmentName, cityName, neighborhoodName, name1);
        }
        // solo los validados
        if (includeDeleted) {
            if (neighborhoodName == null)
                return patientRepo.
                        findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndName1IgnoreCaseAndValidateTrueOrderByName1(
                                countryName, departmentName, cityName, name1);
        }
        else if (neighborhoodName == null)
                return patientRepo.
                    findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndName1IgnoreCaseAndValidateTrueAndDeletedFalseOrderByName1(
                            countryName, departmentName, cityName, name1);

        return patientRepo.
                findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndZone_NeighborhoodNameAndName1IgnoreCaseAndValidateTrueAndDeletedFalse(
                        countryName, departmentName, cityName, neighborhoodName, name1);
    }

    @Override
    public List<Patient> findCity(Boolean withoutValidate, Boolean includeDeleted, String cityName, String departmentName, String countryName) {
        if (withoutValidate) {
            if (includeDeleted)
                return patientRepo.findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndValidateFalseOrderByName1(
                        countryName, departmentName, cityName);

            return patientRepo.findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndValidateFalseAndDeletedFalseOrderByName1(
                    countryName, departmentName, cityName);
        } else {
            //solo validados
            if (includeDeleted)
                return patientRepo.findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndValidateTrueOrderByName1(
                        countryName, departmentName, cityName);

            return patientRepo.findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndValidateTrueAndDeletedFalseOrderByName1(
                    countryName, departmentName, cityName);
        }
    }

    @Override
    public List<Patient> findDepartment(Boolean withoutValidate, Boolean includeDeleted, String departmentName, String countryName) {
        /*if (includeDeleted)
            return patientRepo.findByZone_CountryNameAndZone_DepartmentNameAndValidateTrueOrderByName1(countryName, departmentName);

        return patientRepo.findByZone_CountryNameAndZone_DepartmentNameAndValidateTrueAndDeletedFalseOrderByName1(
                    countryName, departmentName);

         */
        if (withoutValidate) {
            if (includeDeleted)
                return patientRepo.findByZone_CountryNameAndZone_DepartmentNameAndValidateFalseOrderByName1(
                        countryName, departmentName);

            return patientRepo.findByZone_CountryNameAndZone_DepartmentNameAndValidateFalseAndDeletedFalseOrderByName1(
                    countryName, departmentName);
        } else {
            //solo validados
            if (includeDeleted)
                return patientRepo.findByZone_CountryNameAndZone_DepartmentNameAndValidateTrueOrderByName1(
                        countryName, departmentName);

            return patientRepo.findByZone_CountryNameAndZone_DepartmentNameAndValidateTrueAndDeletedFalseOrderByName1(
                    countryName, departmentName);
        }

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

    // Asigna los valores por default a la entidad
    private void defaultValues(Patient patient){
        if (patient.getValidate() == null) patient.setValidate(false);
        patient.setDeleted(false);
    }

    // Asigna los valores a la nueva entitdad, tomados de la vieja entidad (de la persistida)
    private void defaultValues(Patient oldPatient, Patient newPatient){
        newPatient.setValidate(oldPatient.getValidate());
        newPatient.setDeleted(oldPatient.getDeleted());
    }

}
