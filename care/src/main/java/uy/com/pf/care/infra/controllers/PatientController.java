package uy.com.pf.care.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.PatientSaveException;
import uy.com.pf.care.model.documents.Patient;
import uy.com.pf.care.model.objects.PatientIdObject;
import uy.com.pf.care.services.IPatientService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
public class PatientController {
    @Autowired
    private IPatientService patientService;

    @PostMapping("/add")
    public ResponseEntity<PatientIdObject> add(@RequestBody Patient patient){
        try{
            return ResponseEntity.ok(new PatientIdObject(patientService.save(patient).getPatient_id()));

        }catch (PatientSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error guardando paciente");
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
            return ResponseEntity.ok(patientService.findWithIndex_IdentificationDocument(document, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando paciente con documento de identificación " + document + " (" + countryName + ")");

        }
    }

    @GetMapping(value = {
            "findWithIndex_Name1/{name1}/{cityName}/{departmentName}/{countryName}",
            "findWithIndex_Name1/{name1}/{cityName}/{departmentName}/{countryName}/{neighborhoodName}"
    })
    public ResponseEntity<List<Patient>> findWithIndex_Name1(@PathVariable String name1,
                                                             @PathVariable String cityName,
                                                             @PathVariable String departmentName,
                                                             @PathVariable String countryName,
                                                             @PathVariable(required = false) String neighborhoodName) {
        try{
            return ResponseEntity.ok(
                    patientService.findWithIndex_Name1(name1, cityName, departmentName, countryName, neighborhoodName));

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

    @GetMapping("findByCity/{includeDeleted}/{cityName}/{departmentName}/{countryName}")
    public ResponseEntity<List<Patient>> findByCity(@PathVariable Boolean includeDeleted,
                                                    @PathVariable String cityName,
                                                    @PathVariable String departmentName,
                                                    @PathVariable String countryName) {
        try{
            return ResponseEntity.ok(patientService.findByCity(includeDeleted, cityName, departmentName,countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando pacientes en ciudad/localidad: " +
                            cityName + " (" + departmentName + ", " + countryName + ")");
        }
    }

    @GetMapping("findByDepartment/{includeDeleted}/{departmentName}/{countryName}")
    public ResponseEntity<List<Patient>> findByDepartment(@PathVariable Boolean includeDeleted,
                                                          @PathVariable String departmentName,
                                                          @PathVariable String countryName) {
        try{
            return ResponseEntity.ok(patientService.findByDepartment(includeDeleted, departmentName,countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando pacientes en departamento/provincia: " +
                            departmentName + " (" + countryName + ")");
        }
    }

    @GetMapping("findWithIndex_mail/{mail}")
    public ResponseEntity<Optional<Patient>> findWithIndex_Mail(@PathVariable String mail) {
        try{
            return ResponseEntity.ok(patientService.findWithIndex_Mail(mail));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando por indice el paciente con mail: " + mail);
        }
    }

    // Devuelve true si la operación fue exitosa
    @PatchMapping("setDeletion/{id}/{isDeleted}")
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
