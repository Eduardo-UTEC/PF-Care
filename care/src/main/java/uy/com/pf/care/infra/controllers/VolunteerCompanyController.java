package uy.com.pf.care.infra.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.model.documents.VolunteerCompany;
import uy.com.pf.care.model.documents.VolunteerPerson;
import uy.com.pf.care.services.IVolunteerCompanyService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/volunteer_company")
@Log
public class VolunteerCompanyController {

    @Autowired
    private IVolunteerCompanyService volunteerCompanyService;

    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @NotNull @RequestBody VolunteerCompany volunteerCompany){
        try {
            return ResponseEntity.ok(volunteerCompanyService.save(volunteerCompany));

        } catch (DuplicateKeyException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UserUpdateEntityIdInRolesListException | VolunteerCompanySaveException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@Valid @NotNull @RequestBody VolunteerCompany newVolunteerCompany){
        try {
            return ResponseEntity.ok(volunteerCompanyService.update(newVolunteerCompany));

        }catch (VolunteerCompanyNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (VolunteerCompanyDuplicateKeyException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(VolunteerCompanyUpdateException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/addDonations/{volunteerCompanyId}")
    public ResponseEntity<Boolean> addDonationsId(
            @PathVariable String volunteerCompanyId,
            @Valid @NotNull @RequestBody List<String> donationsId){
        try {
            return ResponseEntity.ok(volunteerCompanyService.addDonations(volunteerCompanyId, donationsId));

        }catch (VolunteerCompanyNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(VolunteerCompanyAddDonationsException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/delDonations/{volunteerCompanyId}")
    public ResponseEntity<Boolean> delDonations(
            @PathVariable String volunteerCompanyId,
            @Valid @NotNull @RequestBody List<String> donationsId){
        try {
            return ResponseEntity.ok(volunteerCompanyService.delDonations(volunteerCompanyId, donationsId));

        }catch (VolunteerCompanyNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(VolunteerCompanyDelDonationsException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("setAvailability/{id}/{isAvailable}")
    public ResponseEntity<Boolean> setAvailability(@PathVariable String id, @PathVariable Boolean isAvailable) {
        try{
            return ResponseEntity.ok(volunteerCompanyService.setAvailability(id, isAvailable));

        }catch (VolunteerCompanyNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(VolunteerCompanySetAvailabilityException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // Devuelve true si la operación fue exitosa
    @PutMapping("setValidation/{id}/{isValidated}")
    public ResponseEntity<Boolean> setValidation(@PathVariable String id, @PathVariable Boolean isValidated) {
        try {
            return ResponseEntity.ok(volunteerCompanyService.setValidation(id, isValidated));

        }catch (VolunteerCompanyNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(VolunteerCompanySetValidationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("setDeletion/{id}/{isDeleted}")
    public ResponseEntity<Boolean> setDeletion(@PathVariable String id, @PathVariable Boolean isDeleted) {
        try{
            return ResponseEntity.ok(volunteerCompanyService.setDeletion(id, isDeleted));

        }catch (VolunteerCompanyNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(VolunteerCompanySetDeletionException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<Optional<VolunteerCompany>> findId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(volunteerCompanyService.findId(id));

        }catch(VolunteerCompanyNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    @GetMapping("findAll/{validateOnly}/{includeDeleted}/{countryName}")
    public ResponseEntity<List<VolunteerCompany>> findAll(
            @PathVariable Boolean validateOnly,
            @PathVariable Boolean includeDeleted,
            @PathVariable String countryName){

        try {
            return ResponseEntity.ok(volunteerCompanyService.findAll(validateOnly, includeDeleted, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
