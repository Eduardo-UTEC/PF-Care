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
            return new ResponseEntity<>(
                    new PatientIdObject(patientService.save(patient).getPatient_id()),
                    HttpStatus.OK);

        }catch (PatientSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error guardando paciente");
        }
    }

    @GetMapping("findAll/{countryName}")
    public ResponseEntity<List<Patient>> findAll(@PathVariable String countryName){
        try{
            return new ResponseEntity<>(patientService.findAll(countryName), HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando todos los pacientes de " + countryName);
        }
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<Optional<Patient>> findId( @PathVariable String id) {
        try{
            return new ResponseEntity<>(patientService.findId(id), HttpStatus.OK)
                    ;
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando paciente con id " + id);
        }
    }

    @GetMapping("findIdentificationDocument/{document}/{countryName}")
    public ResponseEntity<Optional<Patient>> findIdentificationDocument( @PathVariable Integer document,
                                                                         @PathVariable String countryName) {
        try{
            return new ResponseEntity<>(patientService.findIdentificationDocument(document, countryName), HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando paciente con documento de identificación " + document + " (" + countryName + ")");
        }
    }

    @GetMapping("findName1Like/{name1}/{cityName}/{departmentName}/{countryName}")
    public ResponseEntity<List<Patient>> findName1Like( @PathVariable String name1,
                                                        @PathVariable String cityName,
                                                        @PathVariable String departmentName,
                                                        @PathVariable String countryName) {
        try{
            return new ResponseEntity<>(
                    patientService.findName1Like(name1, cityName, departmentName,countryName),
                    HttpStatus.OK);

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
            return new ResponseEntity<>(
                    patientService.findSurname1Like(surname1, cityName, departmentName,countryName),
                    HttpStatus.OK);

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
            return new ResponseEntity<>(
                    patientService.findName1Surname1Likes(name1, surname1, cityName, departmentName,countryName),
                    HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando pacientes de " + cityName + " cuyo 1er nombre y 1er apellido contengan " +
                            name1 + " " + surname1 +
                            " (" + departmentName + ", " + countryName + ")");
        }
    }

    @GetMapping("findByCity/{cityName}/{departmentName}/{countryName}")
    public ResponseEntity<List<Patient>> findByCity(@PathVariable String cityName,
                                                    @PathVariable String departmentName,
                                                    @PathVariable String countryName) {
        try{
            return new ResponseEntity<>(
                    patientService.findByCity(cityName, departmentName,countryName),
                    HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando pacientes en ciudad/localidad: " +
                            cityName + " (" + departmentName + ", " + countryName + ")");
        }
    }

    @GetMapping("findByDepartment/{departmentName}/{countryName}")
    public ResponseEntity<List<Patient>> findByDepartment(@PathVariable String departmentName,
                                                          @PathVariable String countryName) {
        try{
            return new ResponseEntity<>(
                    patientService.findByDepartment(departmentName,countryName),
                    HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando pacientes en departamento/provincia: " +
                            departmentName + " (" + countryName + ")");
        }
    }

    // Devuelve true si la operación fue exitosa
    @PatchMapping("logicalDelete/{id}")
    public ResponseEntity<Boolean> logicalDelete(@PathVariable String id) {
        try{
            return new ResponseEntity<>(patientService.logicalDelete(id), HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo realizar el borrado lógico del paciente con id " + id);
        }
    }

    /*@GetMapping("findName1/{name1}/{cityName}/{departmentName}/{countryName}")
    public List<Patient> findName1( @PathVariable String name1,
                                    @PathVariable String cityName,
                                    @PathVariable String departmentName,
                                    @PathVariable String countryName) {
        return patientService.findName1(name1, cityName, departmentName,countryName);
    }*/

    /*@GetMapping("findSurname1/{surname1}/{cityName}/{departmentName}/{countryName}")
    public List<Patient> findSurname1( @PathVariable String surname1,
                                    @PathVariable String cityName,
                                    @PathVariable String departmentName,
                                    @PathVariable String countryName) {
        return patientService.findSurname1(surname1, cityName, departmentName,countryName);
    }*/

    /*@GetMapping("findName1Surname1/{name1}/{surname1}/{cityName}/{departmentName}/{countryName}")
    public List<Patient> findName1Surname1( @PathVariable String name1,
                                    @PathVariable String surname1,
                                    @PathVariable String cityName,
                                    @PathVariable String departmentName,
                                    @PathVariable String countryName) {
        return patientService.findName1Surname1(name1, surname1, cityName, departmentName,countryName);
    }*/

}
