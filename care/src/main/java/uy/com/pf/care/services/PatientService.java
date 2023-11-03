package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.infra.repos.IPatientRepo;
import uy.com.pf.care.model.documents.Patient;
import uy.com.pf.care.model.enums.RoleEnum;
import uy.com.pf.care.model.globalFunctions.ForceEnumsToPatient;
import uy.com.pf.care.model.globalFunctions.SetMatch;
import uy.com.pf.care.model.globalFunctions.UpdateEntityId;
import uy.com.pf.care.model.objects.VolunteerPersonMatchObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class PatientService implements IPatientService{

    @Autowired
    private IPatientRepo patientRepo;
    @Autowired
    private UpdateEntityId updateEntityId;
    @Autowired
    private SetMatch setMatch;

    @Override
    public String save(Patient patient) {

        //Valido keys necesarias para el Save del nuevo paciente
        this.saveValidate(patient);

        //Se define aqui para controlar la excepcion, estableciendo si se debe eliminar el Paciente
        String newPatientId = null;

        try{
            this.defaultValues(patient);
            ForceEnumsToPatient.execute(patient);
            newPatientId = patientRepo.save(patient).getPatientId();

            ResponseEntity<Boolean> response = updateEntityId.execute(
                    patient.getUserId(), RoleEnum.PATIENT.getOrdinal(), newPatientId);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("*** Paciente guardado con exito");
                return newPatientId;
            }

            String msg = "Error actualizando entityId en el rol Paciente, en documento Users";
            log.warning(msg);
            throw new UserUpdateEntityIdInRolesListException(msg);

        //En caso de excepción, si el nuevo paciente fue persistido, se elimina, evitando inconsistencia de la bbdd
        }catch (UserUpdateEntityIdInRolesListException e){
            if (newPatientId != null)
                this.physicallyDeletePatient(newPatientId);
            throw new UserUpdateEntityIdInRolesListException(e.getMessage());
        }catch(Exception e){
            if (newPatientId != null)
                this.physicallyDeletePatient(newPatientId);

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
                ForceEnumsToPatient.execute(newPatient);
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

    //Devuelve un lista con los pacientes cuya Id no existe, con lo cual no pudieron actualizarse.
    @Override
    public List<String> updateReferenceCaregiverOnPatients(List<String> patientsId, String referenceCaregiverId) {
        List<String> ret = new ArrayList<>();
        for (String patientId : patientsId){
            Optional<Patient> patient = patientRepo.findById(patientId);
            if (patient.isPresent()) {
                patient.get().setReferenceCaregiverId(referenceCaregiverId);
                patientRepo.save(patient.get());
            }else
                ret.add(patientId);
        }
        return ret;
    }

    @Override
    public Boolean sendRequestVolunteerPerson(String patientId, String volunteerPersonId) {
        Optional<Patient> patient = patientRepo.findById(patientId);
        int ordinalVolunteer;

        try {
            if (patient.isPresent()) {
                //Envía solicitud de contacto al voluntario
                ResponseEntity<Boolean> response =
                        setMatch.execute(patientId, volunteerPersonId, RoleEnum.PATIENT, null);

                if (response.getStatusCode() == HttpStatus.OK) {
                    ordinalVolunteer = existVolunteer(volunteerPersonId, patient.get().getVolunteerPeople());
                    if (ordinalVolunteer == -1) {
                        patient.get().getVolunteerPeople().add(
                                new VolunteerPersonMatchObject(volunteerPersonId, false));
                        patientRepo.save(patient.get());
                        log.info("Solicitud de contacto del paciente " + patientId + " enviada con éxito al " +
                                "voluntario " + volunteerPersonId);

                    } else {
                        log.info("Se reenvió con éxito la solicitud de contacto del paciente " + patientId +
                                "al voluntario " + volunteerPersonId);
                    }
                    return true;

                } else
                    return false;

            } else {
                String msg = "No se encontró el paciente con id " + patientId;
                log.info(msg);
                return false;
            }


            /*if (patient.isPresent()) {
                ordinalVolunteer = existVolunteer(volunteerPersonId, patient.get().getVolunteerPeople());
                if (ordinalVolunteer == -1) {
                    //Envía solicitud de contacto al voluntario
                    ResponseEntity<Boolean> response =
                            setMatch.execute(patientId, volunteerPersonId, RoleEnum.PATIENT, null);
                    if (response.getStatusCode() == HttpStatus.OK) {
                        patient.get().getVolunteerPeople().add(
                                new VolunteerPersonMatchObject(volunteerPersonId, false));
                        patientRepo.save(patient.get());
                        log.info("Solicitud de contacto del paciente " + patientId + " enviada con éxito al " +
                                "voluntario " + volunteerPersonId);
                        return true;

                    } else
                        return false;

                } else {
                    String msg = "El paciente " + patientId +
                            " ya había enviado una solicitud de contacto al voluntario " + volunteerPersonId;
                    log.info(msg);
                    throw new SendRequestVolunteerPersonException(msg);
                }

            } else {
                String msg = "No se encontró el paciente con id " + patientId;
                log.info(msg);
                return false;
            }*/

        } catch (SendRequestVolunteerPersonException e) {
            throw new SendRequestVolunteerPersonException(e.getMessage());
        } catch (SetMatchException e) {
            throw new SetMatchException(e.getMessage());
        }
    }

    @Override
    public Boolean setMatchVolunteerPerson(String patientId, String volunteerPersonId, Boolean isMatch) {
        int ordinalVolunteer;
        try {
            Optional<Patient> patient = this.findId(patientId);
            if (patient.isPresent()) {
                ordinalVolunteer = existVolunteer(volunteerPersonId, patient.get().getVolunteerPeople());
                if ( ordinalVolunteer != -1) {
                    patient.get().getVolunteerPeople().get(ordinalVolunteer).setMatch(isMatch);
                    patientRepo.save(patient.get());
                    log.info("Match " + (isMatch ? "realizado" : "quitado") + " con éxito del lado del paciente");
                    return true;
                }
                String msg = "El paciente " + patientId +  " no tiene una solicitud de contacto con la persona " +
                        "voluntaria " + volunteerPersonId;
                log.warning(msg);
                return false;

            } else {
                String msg = "No se encontro el paciente con id " + patientId;
                log.warning(msg);
                throw new PatientNotFoundException(msg);
            }

        } catch (PatientNotFoundException e) {
            throw new PatientNotFoundException(e.getMessage());
        } catch (Exception e) {
            String msg = "Error seteando el match de la persona voluntaria en el paciente";
            log.warning(msg + ": " + e.getMessage());
            throw new PatientSetMatchException(e.getMessage());
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
        String msg = "No se encontro el paciente con id " + id;
        log.warning(msg);
        throw new PatientNotFoundException(msg);
    }

    @Override
    public Boolean setDeletion(String id, Boolean isDeleted) {
        Optional<Patient> patient = this.findId(id);
        if (patient.isPresent()) {
            patient.get().setDeleted(isDeleted);
            patientRepo.save(patient.get());
            return true;
        }
        String msg = "No se encontro el paciente con id " + id;
        log.warning(msg);
        throw new PatientNotFoundException(msg);
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
    public List<Patient> findIds(List<String> patientsId) {
        return patientRepo.findAllById(patientsId);
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
        patient.setValidate(false);
        patient.setDeleted(false);
    }

    // Asigna los valores a la nueva entitdad, tomados de la vieja entidad (de la persistida)
    private void defaultValues(Patient oldPatient, Patient newPatient){
        newPatient.setUserId(oldPatient.getUserId());
        newPatient.setValidate(oldPatient.getValidate());
        newPatient.setDeleted(oldPatient.getDeleted());
    }

    private void physicallyDeletePatient(String id){
        try{
            patientRepo.deleteById(id);
            log.warning("Se borro fisicamente el paciente con id " + id);

        }catch(IllegalArgumentException e){
            log.warning("No se pudo eliminar el paciente con Id: " + id + ". El paciente seguramente no ha" +
                    " quedado vinculado a un usuario (coleccion Users) con el rol PATIENT. Si es asi, copie el Id del" +
                    " paciente en la clave 'entityId' correspopndiente al rol del usuario, en la coleccion Users. " +
                    "Alternativamente, puede eliminar el documento del paciente e ingresarlo nuevamente. ");
            throw new PatientPhysicallyDeleteException(
                    "No se pudo eliminar el paciente con Id: " + id);
        }
    }

    //Valida las key requeridas para guardar un nuevo Cuidador Referente
    private void saveValidate(Patient patient){
        String msg;
        if (patient.getUserId() == null || patient.getUserId().isBlank()){
            msg = "El Paciente debe estar vinculado a un Usuario (clave 'userId' no puede ser nula ni vacia)";
            log.warning(msg);
            throw new PatientUserIdOmittedException(msg);
        }
    }

    //Devuelve el oridnal del volunteerPersonId especificado, en la lista 'volunteerPeople'
    private int existVolunteer(String volunteerPersonId, List<VolunteerPersonMatchObject> volunteerPeople ) {
        for (int i = 0; i < volunteerPeople.size(); i++) {
            if (volunteerPeople.get(i).getVolunteerPersonId().equals(volunteerPersonId))
                return i;
        }
        return -1;

        /*for (VolunteerPersonMatchObject volunteerPersonMatchObject : volunteerPeople) {
            if (volunteerPersonMatchObject.getVolunteerPersonId().equals(volunteerPersonId)) {
                return volunteerPersonMatchObject.isMatch();
            }
        }
        return false;
        */
    }

}

