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
import uy.com.pf.care.exceptions.ResidentialNotFoundException;
import uy.com.pf.care.exceptions.ResidentialSaveException;
import uy.com.pf.care.exceptions.ResidentialSetDeletionException;
import uy.com.pf.care.exceptions.ResidentialUpdateException;
import uy.com.pf.care.model.documents.Residential;
import uy.com.pf.care.services.IResidentialService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/residential")
@Log
public class ResidentialController {

    @Autowired
    private IResidentialService residentialService;

    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @NotNull @RequestBody Residential residential){
        try{
            return ResponseEntity.ok(residentialService.save(residential));

        }catch (ResidentialSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@Valid @NotNull @RequestBody Residential newResidential){
        try {
            return ResponseEntity.ok(residentialService.update(newResidential));

        }catch(ResidentialNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(ResidentialUpdateException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(
            value = "findAll/{includeDeleted}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<Residential>> findAll(
            @PathVariable Boolean includeDeleted,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(residentialService.findCountry(includeDeleted, countryName));

        }catch(Exception e) {
            String msg = "Error buscando todos los residenciales de " + countryName;
            log.warning(msg + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(value = "findId/{id}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Optional<Residential>> findId(@PathVariable String id) {
        try{
            return ResponseEntity.ok(residentialService.findId(id));

        }catch(ResidentialNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e) {
            String msg = "Error buscando residencial con id " + id;
            log.warning(msg + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(
            value = "findDepartment/{includeDeleted}/{departmentName}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<Residential>> findDepartment(
            @PathVariable Boolean includeDeleted,
            @PathVariable String departmentName,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(residentialService.findDepartment(includeDeleted, countryName, departmentName));

        }catch(Exception e) {
            String msg = "Error buscando pacientes en residenciales de " +  departmentName + " (" + countryName + ")";
            log.warning(msg + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(
            value = "findCity/{includeDeleted}/{cityName}/{departmentName}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<Residential>> findCity(
            @PathVariable Boolean includeDeleted,
            @PathVariable String cityName,
            @PathVariable String departmentName,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(residentialService.findCity(includeDeleted, countryName, departmentName, cityName));

        }catch(Exception e) {
            String msg = "Error buscando residenciales en la ciudad/localidad de " +
                    cityName + " (" + departmentName + ", " + countryName + ")";
            log.warning(msg + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(
            value = "findName/{includeDeleted}/{name}/{cityName}/{departmentName}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<Residential>> findName(
            @PathVariable Boolean includeDeleted,
            @PathVariable String name,
            @PathVariable String cityName,
            @PathVariable String departmentName,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(residentialService.findName(
                    includeDeleted, countryName, departmentName, cityName, name));

        }catch(Exception e) {
            String msg = "Error buscando residenciales en la ciudad/localidad de " +
                    cityName + " (" + departmentName + ", " + countryName + ")";
            log.warning(msg + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg );
        }
    }

    // Devuelve true si la operación fue exitosa
    @PutMapping("setDeletion/{id}/{isDeleted}")
    public ResponseEntity<Boolean> setDeletion(@PathVariable String id, @PathVariable Boolean isDeleted) {
        try {
            return ResponseEntity.ok(residentialService.setDeletion(id, isDeleted));

        }catch(ResidentialNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(ResidentialSetDeletionException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
