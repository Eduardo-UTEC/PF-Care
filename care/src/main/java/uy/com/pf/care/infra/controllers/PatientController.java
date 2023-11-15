package uy.com.pf.care.infra.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.model.documents.Patient;
import uy.com.pf.care.model.dtos.MostDemandedServicesVolunteerDTO;
import uy.com.pf.care.model.dtos.StatisticPatientWithOthersDTO;
import uy.com.pf.care.services.IPatientService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
@Log
public class PatientController {
    @Autowired
    private IPatientService patientService;

    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @NotNull @RequestBody Patient patient){
        try {
            return ResponseEntity.ok(patientService.save(patient));

        }catch (UserUpdateEntityIdInRolesListException | PatientSaveException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@Valid @NotNull @RequestBody Patient newPatient){
        try {
            return ResponseEntity.ok(patientService.update(newPatient));

        }catch (PatientNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(PatientUpdateException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    //Devuelve un lista con los pacientes cuya Id no existe, con lo cual no pudieron actualizarse.
    @PutMapping(
            value = "/updateReferenceCaregiver/{referenceCaregiverId}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<String>> updateReferenceCaregiver(
            @Valid @NotNull @RequestBody List<String> patientsId,
            @PathVariable String referenceCaregiverId) {

        try {
            return ResponseEntity.ok(patientService.updateReferenceCaregiverOnPatients(patientsId, referenceCaregiverId));

        }catch(Exception e){
            String msg = "*** ERROR ACTUALIZANDO ID'S DE CUIDADORES REFERENTES EN COLECCION PATIENTS";
            log.warning(msg + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(
            value = "findAll/{withoutValidate}/{includeDeleted}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<Patient>> findAll(
            @PathVariable Boolean withoutValidate,
            @PathVariable Boolean includeDeleted,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(patientService.findAll(withoutValidate, includeDeleted, countryName));

        }catch(Exception e) {
            String msg = "Error buscando todos los pacientes de " + countryName;
            log.warning(msg + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(value = "findId/{id}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Optional<Patient>> findId(@PathVariable String id) {
        try{
            return ResponseEntity.ok(patientService.findId(id));

        }catch(Exception e) {
            String msg = "Error buscando paciente con id " + id;
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @PostMapping(value = "findIds", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<Patient>> findIds(
            @Valid @NotNull @RequestBody List<String> patientsId) {
        try {
            return ResponseEntity.ok(patientService.findIds(patientsId));

        }catch(Exception e) {
            String msg = "Error buscando pacientes por id";
            log.warning(msg + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }


    ///Devuelve true si el paciente esta validado y no esta borrado
    @GetMapping(value = "isValidated_notDeleted/{id}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Boolean> isValidated_notDeleted(@PathVariable String id) {
        try{
            Optional<Patient> found = patientService.findId(id);
            if (found.isPresent())
                return ResponseEntity.ok(found.get().getValidate() && ! found.get().getDeleted());
            return ResponseEntity.ok(false);

        }catch(Exception e) {
            String msg = "Error buscando paciente con id " + id;
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(
            value = "findIdentificationDocument/{document}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Optional<Patient>> findIdentificationDocument(
            @PathVariable Integer document,
            @PathVariable String countryName) {
        try{
            return ResponseEntity.ok(patientService.findIdentificationDocument(document, countryName));

        }catch(Exception e) {
            String msg = "Error buscando paciente con documento de identificación " + document + " (" + countryName + ")";
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(
            value = "existIdentificationDocument/{document}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Boolean> existIdentificationDocument(
            @PathVariable Integer document,
            @PathVariable String countryName) {
        try{
            return ResponseEntity.ok(patientService.findIdentificationDocument(document, countryName).isPresent());

        }catch(Exception e) {
            String msg = "Error buscando paciente con documento de identificación " + document + " (" + countryName + ")";
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }


    @GetMapping(value = {
            "findName1/{name1}/{withoutValidate}/{includeDeleted}/{cityName}/{departmentName}/{countryName}",
            "findName1/{name1}/{withoutValidate}/{includeDeleted}/{neighborhoodName}/{cityName}/{departmentName}/{countryName}"
    }, produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<Patient>> findName1(
            @PathVariable String name1,
            @PathVariable Boolean withoutValidate,
            @PathVariable Boolean includeDeleted,
            @PathVariable(required = false) String neighborhoodName,
            @PathVariable String cityName,
            @PathVariable String departmentName,
            @PathVariable String countryName) {

        try{
            return ResponseEntity.ok(
                    patientService.findName1(
                            name1,
                            withoutValidate,
                            includeDeleted,
                            neighborhoodName,
                            cityName,
                            departmentName,
                            countryName
                    )
            );

        }catch(Exception e) {
            String msg =
                    "Error buscando los pacientes de " + cityName +
                    " cuyo 1er nombre sea " + name1 +
                    " (" + (neighborhoodName != null ? neighborhoodName + ", " : "") +
                    departmentName + ", " +
                    countryName + ")";
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(
            value = "findCity/{withoutValidate}/{includeDeleted}/{cityName}/{departmentName}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<Patient>> findCity(
            @PathVariable Boolean withoutValidate,
            @PathVariable Boolean includeDeleted,
            @PathVariable String cityName,
            @PathVariable String departmentName,
            @PathVariable String countryName) {

        try{
            return ResponseEntity.ok(patientService.findCity(
                    withoutValidate, includeDeleted, cityName, departmentName,countryName));

        }catch(Exception e) {
            String msg = "Error buscando pacientes en ciudad/localidad: " +
                    cityName + " (" + departmentName + ", " + countryName + ")";
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(
            value = "findDepartment/{withoutValidate}/{includeDeleted}/{departmentName}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<Patient>> findDepartment(
            @PathVariable Boolean withoutValidate,
            @PathVariable Boolean includeDeleted,
            @PathVariable String departmentName,
            @PathVariable String countryName) {

        try{
            return ResponseEntity.ok(patientService.findDepartment(
                    withoutValidate, includeDeleted, departmentName,countryName));

        }catch(Exception e) {
            String msg = "Error buscando pacientes en departamento/provincia: " +
                    departmentName + " (" + countryName + ")";
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(value = "findMail/{mail}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Optional<Patient>> findMail(@PathVariable String mail) {
        try{
            return ResponseEntity.ok(patientService.findMail(mail));

        }catch(Exception e) {
            String msg = "Error buscando por indice el paciente con mail: " + mail;
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(value = "existMail/{mail}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Boolean> existMail(@PathVariable String mail) {
        try{
            return ResponseEntity.ok(patientService.findMail(mail).isPresent());

        }catch(Exception e) {
            String msg = "Error buscando por indice el paciente con mail: " + mail;
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    // Devuelve true si la operación fue exitosa
    @PutMapping("setValidation/{id}/{isValidated}")
    public ResponseEntity<Boolean> setValidation(@PathVariable String id, @PathVariable Boolean isValidated) {
        try {
            return ResponseEntity.ok(patientService.setValidation(id, isValidated));

        }catch (PatientNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e) {
            String msg = "Error seteando validación del paciente con id " + id;
            log.warning(msg + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    // Devuelve true si la operación fue exitosa
    @PutMapping("setDeletion/{id}/{isDeleted}")
    public ResponseEntity<Boolean> setDeletion(@PathVariable String id, @PathVariable Boolean isDeleted) {
        try{
            return ResponseEntity.ok(patientService.setDeletion(id, isDeleted));

        }catch (PatientNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e) {
            String msg = "Error seteando borrado lógico de paciente con id " + id;
            log.warning(msg + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    //Envía o reenvía una solicitud de contacto a una persona voluntaria.
    //El proceso de matcheo comienza en este punto.
    @GetMapping(
            value = "sendRequestVolunteerPerson/{patientId}/{volunteerPersonId}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Boolean> sendRequestVolunteerPerson(
            @PathVariable String patientId, @PathVariable String volunteerPersonId) {
        try {
            return ResponseEntity.ok(patientService.sendRequestVolunteerPerson(patientId, volunteerPersonId));

        } catch (SendRequestVolunteerPersonException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch(SetMatchException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    //Este servicio se expone para ser llamado desde el voluntario (no debe llamarse desde un cliente).
    //El voluntario llama a este servicio desde: /vounteer_people/setMatchPatient
    @PutMapping("setMatchVolunteerPerson/{patientId}/{volunteerPersonId}/{isMatch}")
    public ResponseEntity<Boolean> setMatchVolunteerPerson (
            @PathVariable String patientId, @PathVariable String volunteerPersonId, @PathVariable Boolean isMatch) {
        try {
            return ResponseEntity.ok(patientService.setMatchVolunteerPerson(patientId, volunteerPersonId, isMatch));

        }catch (PatientNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(PatientSetMatchException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(
            value = "getMonthlyRequestStatsForLastSixMonths/" +
                    "{withoutValidate}/{includeDeleted}/{departmentName}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<StatisticPatientWithOthersDTO>> getMonthlyRequestStatsForLastSixMonths(
            @PathVariable Boolean withoutValidate,
            @PathVariable Boolean includeDeleted,
            @PathVariable String departmentName,
            @PathVariable String countryName) {

        try {
            return ResponseEntity.ok(patientService.getMonthlyRequestStatsForLastSixMonths(
                    withoutValidate, includeDeleted, departmentName, countryName));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(
            value = "getMostDemandedVolunteerActivities/" +
                    "{withoutValidate}/{includeDeleted}/{departmentName}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<MostDemandedServicesVolunteerDTO>> getMostDemandedVolunteerActivities(
            @PathVariable Boolean withoutValidate,
            @PathVariable Boolean includeDeleted,
            @PathVariable String departmentName,
            @PathVariable String countryName) {

        try {
            return ResponseEntity.ok(patientService.getMostDemandedVolunteerActivities(
                    withoutValidate, includeDeleted, departmentName, countryName));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    /*@GetMapping("findName1Like/{name1}/{cityName}/{departmentName}/{countryName}")
    public ResponseEntity<List<Patient>> findName1Like( @PathVariable String name1,
                                                        @PathVariable String cityName,
                                                        @PathVariable String departmentName,
                                                        @PathVariable String countryName) {
        try{
            return ResponseEntity.ok(
                    patientService.findName1Like(name1, cityName, departmentName,countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando pacientes de " + cityName + " cuyo 1er nombre contenga " +
                            name1 + " (" + departmentName + ", " + countryName + ")");
        }
    }

    @GetMapping("findSurname1Like/{surname1}/{cityName}/{departmentName}/{countryName}")
    public ResponseEntity<List<Patient>> findSurname1Like( @PathVariable String surname1,
                                                           @PathVariable String cityName,
                                                           @PathVariable String departmentName,
                                                           @PathVariable String countryName) {
        try{
            return ResponseEntity.ok(
                    patientService.findSurname1Like(surname1, cityName, departmentName,countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando pacientes de " + cityName + " cuyo 1er apellido contenga " +
                            surname1 + " (" + departmentName + ", " + countryName + ")");
        }
    }

    @GetMapping("findName1Surname1Likes/{name1}/{surname1}/{cityName}/{departmentName}/{countryName}")
        public ResponseEntity<List<Patient>> findName1Surname1Likes(@PathVariable String name1,
                                                                    @PathVariable String surname1,
                                                                    @PathVariable String cityName,
                                                                    @PathVariable String departmentName,
                                                                    @PathVariable String countryName) {
        try{
            return ResponseEntity.ok(
                    patientService.findName1Surname1Likes(name1, surname1, cityName, departmentName,countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando pacientes de " + cityName + " cuyo 1er nombre y 1er apellido contengan " +
                            name1 + " " + surname1 +
                            " (" + departmentName + ", " + countryName + ")");
        }
    }*/

}
