package uy.com.pf.care.infra.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.FormalCaregiverUpdateException;
import uy.com.pf.care.exceptions.PatientSaveException;
import uy.com.pf.care.exceptions.PatientUpdateException;
import uy.com.pf.care.model.documents.HealthProvider;
import uy.com.pf.care.model.documents.Patient;
import uy.com.pf.care.model.objects.PatientIdObject;
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
    //public ResponseEntity<PatientIdObject> add(@Valid @NotNull @RequestBody Patient patient){
    public ResponseEntity<String> add(@Valid @NotNull @RequestBody Patient patient){
        try{
            //return ResponseEntity.ok(new PatientIdObject(patientService.save(patient).getPatientId()));
            return ResponseEntity.ok(patientService.save(patient));

        }catch (PatientSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@Valid @NotNull @RequestBody Patient newPatient){
        try {
            return ResponseEntity.ok(patientService.update(newPatient));

        }catch(PatientUpdateException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("findAll/{includeDeleted}/{countryName}")
    public ResponseEntity<List<Patient>> findAll(@PathVariable Boolean includeDeleted, @PathVariable String countryName){
        try{
            return ResponseEntity.ok(patientService.findAll(includeDeleted, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando todos los pacientes de " + countryName);
        }
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<Optional<Patient>> findId(@PathVariable String id) {
        try{
            return ResponseEntity.ok(patientService.findId(id));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando paciente con id " + id);
        }
    }

    @GetMapping("findIdentificationDocument/{document}/{countryName}")
    public ResponseEntity<Optional<Patient>> findIdentificationDocument(
            @PathVariable Integer document,
            @PathVariable String countryName) {
        try{
            return ResponseEntity.ok(patientService.findIdentificationDocument(document, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando paciente con documento de identificación " + document + " (" + countryName + ")");

        }
    }

    @GetMapping(value = {
            "findName1/{name1}/{cityName}/{departmentName}/{countryName}",
            "findName1/{name1}/{neighborhoodName}/{cityName}/{departmentName}/{countryName}"
    })
    public ResponseEntity<List<Patient>> findName1(
            @PathVariable String name1,
            @PathVariable(required = false) String neighborhoodName,
            @PathVariable String cityName,
            @PathVariable String departmentName,
            @PathVariable String countryName) {

        try{
            return ResponseEntity.ok(
                    patientService.findName1(name1, neighborhoodName, cityName, departmentName, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando los pacientes de " + cityName +
                    " cuyo 1er nombre sea " + name1 +
                    " (" + (neighborhoodName != null ? neighborhoodName + ", " : "") +
                    departmentName + ", " +
                    countryName + ")"
            );
        }
    }

    @GetMapping("findCity/{includeDeleted}/{cityName}/{departmentName}/{countryName}")
    public ResponseEntity<List<Patient>> findCity(@PathVariable Boolean includeDeleted,
                                                  @PathVariable String cityName,
                                                  @PathVariable String departmentName,
                                                  @PathVariable String countryName) {
        try{
            return ResponseEntity.ok(patientService.findCity(includeDeleted, cityName, departmentName,countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando pacientes en ciudad/localidad: " +
                            cityName + " (" + departmentName + ", " + countryName + ")");
        }
    }

    @GetMapping("findDepartment/{includeDeleted}/{departmentName}/{countryName}")
    public ResponseEntity<List<Patient>> findDepartment(@PathVariable Boolean includeDeleted,
                                                        @PathVariable String departmentName,
                                                        @PathVariable String countryName) {
        try{
            return ResponseEntity.ok(patientService.findDepartment(includeDeleted, departmentName,countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando pacientes en departamento/provincia: " +
                            departmentName + " (" + countryName + ")");
        }
    }

    @GetMapping("findMail/{mail}")
    public ResponseEntity<Optional<Patient>> findMail(@PathVariable String mail) {
        try{
            return ResponseEntity.ok(patientService.findMail(mail));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando por indice el paciente con mail: " + mail);
        }
    }

    // Devuelve true si la operación fue exitosa
    @PutMapping("setValidate/{id}/{isValidated}")
    public ResponseEntity<Boolean> setValidate(@PathVariable String id, @PathVariable Boolean isValidated) {
        try{
            return ResponseEntity.ok(patientService.setValidate(id, isValidated));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo setear la validación del paciente con id " + id);
        }
    }

    // Devuelve true si la operación fue exitosa
    @PutMapping("setDeletion/{id}/{isDeleted}")
    public ResponseEntity<Boolean> setDeletion(@PathVariable String id, @PathVariable Boolean isDeleted) {
        try{
            return ResponseEntity.ok(patientService.setDeletion(id, isDeleted));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo setear el borrado lógico del paciente con id " + id);
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
