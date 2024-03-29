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
import uy.com.pf.care.model.documents.ReferenceCaregiver;
import uy.com.pf.care.model.enums.RelationshipEnum;
import uy.com.pf.care.services.IReferenceCaregiverService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reference_caregiver")
@Log
public class ReferenceCaregiverController {

    @Autowired
    private IReferenceCaregiverService referenceCaregiverService;

    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @NotNull @RequestBody ReferenceCaregiver referenceCaregiver){
        try {
            return ResponseEntity.ok(referenceCaregiverService.save(referenceCaregiver));

        }catch(ReferenceCaregiverUserIdOmittedException | ReferenceCaregiverPatientsIdOmittedException e){
            throw new ResponseStatusException((HttpStatus.BAD_REQUEST), e.getMessage());
        }catch (ReferenceCaregiverDuplicateKeyException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch (UserUpdateEntityIdInRolesListException | ReferenceCaregiverSaveException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@Valid @NotNull @RequestBody ReferenceCaregiver referenceCaregiver){
        try {
            return ResponseEntity.ok(referenceCaregiverService.update(referenceCaregiver));

        }catch (ReferenceCaregiverNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(ReferenceCaregiverUpdateException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /*@PutMapping(
            value = "/changeRelationshipPatient/{referenceCaregiverId}/{patientId}/{ordinalRelationship}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Boolean> changeRelationshipPatient(
            @PathVariable String referenceCaregiverId,
            @PathVariable String patientId,
            @PathVariable int ordinalRelationship) {
        try {
            return ResponseEntity.ok(referenceCaregiverService.changeRelationshipPatient(
                    referenceCaregiverId, patientId, RelationshipEnum.values()[ordinalRelationship]));

        }catch (ReferenceCaregiverNotFoundException | ReferenceCaregiverChangeRelationshipPatientException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR CAMBIANDO RELACION DEL CUIDADOR REFERENTE CON UN PACIENTE: " + e.getMessage();
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

     */

    @PutMapping(
            value = "/addPatient/{referenceCaregiverId}/{patientId}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Boolean> addPatient(
            @PathVariable String referenceCaregiverId,
            @PathVariable String patientId) {

        try {
            return ResponseEntity.ok(referenceCaregiverService.addPatient(referenceCaregiverId, patientId));

        }catch (ReferenceCaregiverNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (ReferenceCaregiverChangeRelationshipPatientException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR VINCULANDO NUEVO PACIENTE AL CUIDADOR REFERENTE: " + e.getMessage();
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(value = "findId/{id}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Optional<ReferenceCaregiver>> findId(@PathVariable String id) {
        try{
            return ResponseEntity.ok(referenceCaregiverService.findId(id));

        }catch(Exception e) {
            String msg = "Error buscando Cuidador Referente con id " + id;
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(
            value = "findIdentificationDocument/{document}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Optional<ReferenceCaregiver>> findIdentificationDocument(
            @PathVariable Integer document,
            @PathVariable String countryName) {
        try{
            return ResponseEntity.ok(referenceCaregiverService.findIdentificationDocument(document, countryName));

        }catch(Exception e) {
            String msg = "Error buscando Cuidador Referente con documento de identificación " +
                    document + " (" + countryName + "): " + e.getMessage();
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(value = "findMail/{mail}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Optional<ReferenceCaregiver>> findMail(@PathVariable String mail) {
        try{
            return ResponseEntity.ok(referenceCaregiverService.findMail(mail));

        }catch(Exception e) {
            String msg = "Error buscando cuidador referente con mail: " + mail;
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(value = "existMail/{mail}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Boolean> existMail(@PathVariable String mail) {
        try{
            return ResponseEntity.ok(referenceCaregiverService.findMail(mail).isPresent());

        }catch(Exception e) {
            String msg = "Error buscando cuidador referente con mail: " + mail;
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    //true si tiene por lo menos un paciente asignado
    @GetMapping(value = "hasPatientAssigned/{referenceCareId}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Boolean> hasPatientAssigned(@PathVariable String referenceCareId) {
        try{
            Optional<ReferenceCaregiver> found = referenceCaregiverService.findId(referenceCareId);
            if (found.isPresent())
                return ResponseEntity.ok(! found.get().getPatients().isEmpty());
            return ResponseEntity.ok(false);

        }catch(Exception e) {
            String msg = "Error buscando cuidador referente con mail: " + referenceCareId;
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(
            value = "findName1/{name1}/{neighborhoodName}/{cityName}/{departmentName}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<ReferenceCaregiver>> findName1(
            @PathVariable String name1,
            @PathVariable String neighborhoodName,
            @PathVariable String cityName,
            @PathVariable String departmentName,
            @PathVariable String countryName) {

        try {
            return ResponseEntity.ok(referenceCaregiverService.findName1(
                    name1, neighborhoodName, cityName, departmentName, countryName));

        } catch (Exception e) {
            String msg =
                    "Error buscando Cuidadores Referentes de " + cityName +
                            " cuyo 1er nombre sea " + name1 + "( " + neighborhoodName + ", " + departmentName + ", " +
                            countryName + ")";
            log.warning(msg + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(
            value = "findCity/{cityName}/{departmentName}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<ReferenceCaregiver>> findCity(
            @PathVariable String cityName,
            @PathVariable String departmentName,
            @PathVariable String countryName) {

        try {
            return ResponseEntity.ok(referenceCaregiverService.findCity(cityName, departmentName, countryName));

        } catch (Exception e) {
            String msg =
                    "Error buscando Cuidadores Referentes de " + cityName + ", " + departmentName + ", " + countryName +
                            ": " + e.getMessage();
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(
            value = "findDepartment/{departmentName}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<ReferenceCaregiver>> findDepartment(
            @PathVariable String departmentName,
            @PathVariable String countryName) {

        try {
            return ResponseEntity.ok(referenceCaregiverService.findDepartment(departmentName, countryName));

        } catch (Exception e) {
            String msg =
                    "Error buscando Cuidadores Referentes de " + departmentName + ", " + countryName + ": " + e.getMessage();
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(value = "findAll/{countryName}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<ReferenceCaregiver>> findAll(@PathVariable String countryName) {

        try {
            return ResponseEntity.ok(referenceCaregiverService.findAll(countryName));

        } catch (Exception e) {
            String msg = "Error buscando Cuidadores Referentes de + " + countryName + ": " + e.getMessage();
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }



}
