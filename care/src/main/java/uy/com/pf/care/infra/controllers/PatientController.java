package uy.com.pf.care.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public PatientIdObject add(@RequestBody Patient patient){
        try{
            return new PatientIdObject(patientService.save(patient).getPatient_id());
        }catch (PatientSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error guardando paciente");
        }
    }

    @GetMapping("findAll/{countryName}")
    public List<Patient> findAll(@PathVariable String countryName){
        try{
            return patientService.findAll(countryName);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error buscando pacientes");
        }
    }

    @GetMapping("findId/{id}")
    public Optional<Patient> findId( @PathVariable String id) {
        try{
            return patientService.findId(id);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error buscando paciente");
        }
    }

    @GetMapping("findIdentificationDocument/{document}/{countryName}")
    public Optional<Patient> findIdentificationDocument( @PathVariable Integer document, @PathVariable String countryName) {
        try{
            return  patientService.findIdentificationDocument(document, countryName);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error buscando paciente");
        }
    }

    @GetMapping("findName1Like/{name1}/{cityName}/{departmentName}/{countryName}")
    public List<Patient> findName1Like( @PathVariable String name1,
                                        @PathVariable String cityName,
                                        @PathVariable String departmentName,
                                        @PathVariable String countryName) {
        try{
            return patientService.findName1Like(name1, cityName, departmentName,countryName);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error buscando pacientes");
        }
    }

    @GetMapping("findSurname1Like/{surname1}/{cityName}/{departmentName}/{countryName}")
    public List<Patient> findSurname1Like( @PathVariable String surname1,
                                           @PathVariable String cityName,
                                           @PathVariable String departmentName,
                                           @PathVariable String countryName) {
        try{
            return patientService.findSurname1Like(surname1, cityName, departmentName,countryName);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error buscando pacientes");
        }
    }

    @GetMapping("findName1Surname1Likes/{name1}/{surname1}/{cityName}/{departmentName}/{countryName}")
        public List<Patient> findName1Surname1Likes(@PathVariable String name1,
                                                    @PathVariable String surname1,
                                                    @PathVariable String cityName,
                                                    @PathVariable String departmentName,
                                                    @PathVariable String countryName) {
        try{
            return patientService.findName1Surname1Likes(name1, surname1, cityName, departmentName,countryName);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error buscando pacientes");
        }
    }

    @GetMapping("findByCity/{cityName}/{departmentName}/{countryName}")
    public List<Patient> findByCity(@PathVariable String cityName,
                                        @PathVariable String departmentName,
                                        @PathVariable String countryName) {
        try{
            return patientService.findByCity(cityName, departmentName,countryName);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error buscando pacientes");
        }
    }

    @GetMapping("findByDepartment/{departmentName}/{countryName}")
    public List<Patient> findByDepartment(@PathVariable String departmentName,
                                        @PathVariable String countryName) {
        try{
            return patientService.findByDepartment(departmentName,countryName);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error buscando pacientes");
        }
    }

    @PatchMapping("logicalDelete/{id}")
    public boolean logicalDelete(@PathVariable String id) {
        try{
            return patientService.logicalDelete(id);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo realizar el borrado lógico del paciente");
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
