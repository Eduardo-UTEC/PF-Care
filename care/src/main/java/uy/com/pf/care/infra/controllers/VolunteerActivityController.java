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
import uy.com.pf.care.exceptions.VolunteerActivityDuplicateKeyException;
import uy.com.pf.care.exceptions.VolunteerActivityNotFoundException;
import uy.com.pf.care.exceptions.VolunteerActivitySaveException;
import uy.com.pf.care.exceptions.VolunteerActivityUpdateException;
import uy.com.pf.care.model.documents.VolunteerActivity;
import uy.com.pf.care.model.documents.VolunteerPerson;
import uy.com.pf.care.services.IVolunteerActivityService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/volunteerActivities")
@Log
public class VolunteerActivityController {
    @Autowired
    private IVolunteerActivityService volunteerActivityService;

    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @NotNull @RequestBody VolunteerActivity volunteerActivity){
        try {
            return ResponseEntity.ok(volunteerActivityService.save(volunteerActivity));

        }catch (VolunteerActivityDuplicateKeyException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch (VolunteerActivitySaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@Valid @NotNull @RequestBody VolunteerActivity newVolunteerActivity){
        try {
            return ResponseEntity.ok(volunteerActivityService.update(newVolunteerActivity));

        }catch (VolunteerActivityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (VolunteerActivityDuplicateKeyException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(VolunteerActivityUpdateException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(
            value = "findAll/{includeDeleted}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<VolunteerActivity>> findAll(
            @PathVariable Boolean includeDeleted,
            @PathVariable String countryName){
        try{
            return ResponseEntity.ok(volunteerActivityService.findAll(includeDeleted, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando todos las Actividades de Voluntarios de: " + countryName);
        }
    }

    @GetMapping(
            value = "findDepartment/{includeDeleted}/{departmentName}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<VolunteerActivity>> findDepartment(
            @PathVariable Boolean includeDeleted,
            @PathVariable String departmentName,
            @PathVariable String countryName){
        try{
            return ResponseEntity.ok(volunteerActivityService.findDepartment(
                    includeDeleted, countryName, departmentName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando Actividades de Voluntarios de: " + departmentName + ", " + countryName);
        }
    }

    @PutMapping("setDeletion/{id}/{isDeleted}")
    public ResponseEntity<Boolean> setDeletion(
            @PathVariable String id,
            @PathVariable Boolean isDeleted) {
        try {
            return ResponseEntity.ok(volunteerActivityService.setDeletion(id, isDeleted));

        }catch (VolunteerActivityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo setear el borrado lógico de la Actividad del Voluntario con id " + id);
        }
    }

    @GetMapping(value = "findId/{id}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Optional<VolunteerActivity>> findId(@PathVariable String id) {
        try{
            return ResponseEntity.ok(volunteerActivityService.findId(id));

        }catch (VolunteerActivityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando Actividad de Voluntario con id " + id);
        }
    }

    @PostMapping(value = "findIds", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<VolunteerActivity>> findIds(
            @Valid @NotNull @RequestBody List<String> volunteersActivitiesId) {
        try {
            return ResponseEntity.ok(volunteerActivityService.findIds(volunteersActivitiesId));

        }catch(Exception e) {
            String msg = "Error buscando actividades del voluntario por id";
            log.warning(msg + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping("exist/{name}/{departmentName}/{countryName}")
    public ResponseEntity<Boolean> exist(
            @PathVariable String name,
            @PathVariable String departmentName,
            @PathVariable String countryName) {
        try{
            return ResponseEntity.ok(volunteerActivityService.exist(name, departmentName, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando Actividad de Voluntario de nombre " + name + " en " + departmentName + ", " +
                            countryName);
        }
    }
}
