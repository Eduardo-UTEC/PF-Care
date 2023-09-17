package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.infra.config.ParamConfig;
import uy.com.pf.care.model.documents.Patient;
import uy.com.pf.care.model.enums.RoleEnum;
import uy.com.pf.care.repos.IPatientRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class PatientService implements IPatientService{

    @Autowired
    private IPatientRepo patientRepo;
    @Autowired
    private ParamConfig paramConfig;

    @Override
    public String save(Patient patient) {

        //Se define aqui para controlar la excepcion, estableciendo si se debe eliminar el Paciente
        String newPatientId = null;

        try{
            this.defaultValues(patient);
            newPatientId = patientRepo.save(patient).getPatientId();

            //Actualizo el entityId del rol de usuario (documento Users) con el newPatientId de este nuevo Paciente

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Boolean> response = restTemplate.exchange(
                    getUpdateEntityIdUrl(patient.getUserId(), newPatientId),
                    HttpMethod.PUT,
                    null,
                    Boolean.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("*** Paciente guardado con exito: " + LocalDateTime.now());
                return newPatientId;
            }

            String msg = "Error actualizando entityId en el rol Paciente, en documento Users";
            log.warning(msg);
            throw new UserUpdateEntityIdInRolesListException(msg);

        //En caso de excepción, si el nuevo paciente fue persistido, se elimina, evitando inconsistencia de la bbdd
        }catch (UserUpdateEntityIdInRolesListException e){
            if (newPatientId != null)
                patientRepo.deleteById(newPatientId);
            throw new UserUpdateEntityIdInRolesListException(e.getMessage());

        }catch(Exception e){
            if (newPatientId != null)
                patientRepo.deleteById(newPatientId);
            String msg = "*** ERROR GUARDANDO PACIENTE: " + e.getMessage();
            log.warning(msg);
            throw new PatientSaveException(msg);
        }
    }

    @Override
    public Boolean update(Patient newPatient) {
        try {
            Optional<Patient> entityFound = patientRepo.findById(newPatient.getPatientId());
            if (entityFound.isPresent()) {
                this.defaultValues(entityFound.get(), newPatient);
                patientRepo.save(newPatient);
                log.info("Paciente actualizado con exito");
                return true;
            }
            String msg = "No se encontro el paciente con id " + newPatient.getPatientId();
            log.info(msg);
            throw new PatientNotFoundException(msg);

        }catch (PatientNotFoundException e){
            throw new PatientNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR ACTUALIZANDO PACIENTE";
            log.warning(msg +  ": " + e.getMessage());
            throw new PatientUpdateException(msg);
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

    private String getStartUrl(){
        return paramConfig.getProtocol() + "://" + paramConfig.getSocket() + "/";
    }

    private String getUpdateEntityIdUrl(String userId, String patientId){
        return getStartUrl() +
                "users/updateEntityId/" +
                userId + "/" +
                RoleEnum.PATIENT.getOrdinal() + "/" +
                patientId;
    }

}

