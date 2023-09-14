package uy.com.pf.care.infra.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.model.documents.VolunteerPerson;
import uy.com.pf.care.model.objects.DayTimeRangeObject;
import uy.com.pf.care.services.IVolunteerPersonService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/volunteer_people")
@Log
public class VolunteerPersonController {

    @Autowired
    private IVolunteerPersonService volunteerPersonService;

    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @NotNull @RequestBody VolunteerPerson volunteerPerson){
        try {
            return ResponseEntity.ok(volunteerPersonService.save(volunteerPerson));

        }catch(VolunteerPersonSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@Valid @NotNull @RequestBody VolunteerPerson newVolunteerPerson){
        try {
            return ResponseEntity.ok(volunteerPersonService.update(newVolunteerPerson));

        }catch(VolunteerPersonUpdateException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("findAll/{includeDeleted}/{countryName}")
    public ResponseEntity<List<VolunteerPerson>> findAll(
            @PathVariable Boolean includeDeleted,
            @PathVariable String countryName){

        try {
            return ResponseEntity.ok(volunteerPersonService.findAll(includeDeleted, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<Optional<VolunteerPerson>> findId(@PathVariable String id) {
        try{
            return ResponseEntity.ok(volunteerPersonService.findId(id));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("findMail/{mail}")
    public ResponseEntity<Optional<VolunteerPerson>> findMail(@PathVariable String mail) {
        try{
            return ResponseEntity.ok(volunteerPersonService.findMail(mail));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("findName/{includeDeleted}/{name}/{countryName}")
    public ResponseEntity<List<VolunteerPerson>> findName(
            @PathVariable Boolean includeDeleted,
            @PathVariable String name,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(volunteerPersonService.findName(includeDeleted, countryName, name));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("findNameLike/{includeDeleted}/{name}/{countryName}")
    public ResponseEntity<List<VolunteerPerson>> findNameLike(
            @PathVariable Boolean includeDeleted,
            @PathVariable String name,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(volunteerPersonService.findNameLike(includeDeleted, countryName, name));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    //  Devuelve true si la operación fue exitosa.
    @PutMapping("setAvailability/{id}/{isAvailable}")
    public ResponseEntity<Boolean> setAvailability(@PathVariable String id, @PathVariable Boolean isAvailable) {
        try{
            return ResponseEntity.ok(volunteerPersonService.setAvailability(id, isAvailable));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // Devuelve true si la operación fue exitosa
    @PutMapping("setValidation/{id}/{isValidated}")
    public ResponseEntity<Boolean> setValidate(@PathVariable String id, @PathVariable Boolean isValidated) {
        try{
            return ResponseEntity.ok(volunteerPersonService.setValidation(id, isValidated));

        }catch(VolunteerPersonSetValidationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // Devuelve true si la operación fue exitosa
    @PutMapping("setDeletion/{id}/{isDeleted}")
    public ResponseEntity<Boolean> setDeletion(@PathVariable String id, @PathVariable Boolean isDeleted) {
        try{
            return ResponseEntity.ok(volunteerPersonService.setDeletion(id, isDeleted));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(
            "findInterestZones_Neighborhood/" +
                    "{includeDeleted}/" +
                    "{interestNeighborhoodName}/" +
                    "{interestCityName}/" +
                    "{interestDepartmentName}/" +
                    "{countryName}")
    public ResponseEntity<List<VolunteerPerson>> findInterestZones_Neighborhood(
            @PathVariable Boolean includeDeleted,
            @PathVariable String interestNeighborhoodName,
            @PathVariable String interestCityName,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(volunteerPersonService.findInterestZones_Neighborhood(
                    includeDeleted,
                    interestNeighborhoodName,
                    interestCityName,
                    interestDepartmentName,
                    countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    @GetMapping("findInterestZones_City/{includeDeleted}/{interestCityName}/{interestDepartmentName}/{countryName}")
    public ResponseEntity<List<VolunteerPerson>> findInterestZones_City(
            @PathVariable Boolean includeDeleted,
            @PathVariable String interestCityName,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName){
        try{
            return ResponseEntity.ok(volunteerPersonService.findInterestZones_City(
                    true, includeDeleted, interestCityName, interestDepartmentName, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    @GetMapping("findInterestZones_Department/{includeDeleted}/{interestDepartmentName}/{countryName}")
    public ResponseEntity<List<VolunteerPerson>> findInterestZones_Department(
            @PathVariable Boolean includeDeleted,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(volunteerPersonService.findInterestZones_Department(
                    true, includeDeleted, interestDepartmentName, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(
            "findDateTimeRange/" +
                    "{interestNeighborhoodName}/" +
                    "{interestCityName}/" +
                    "{interestDepartmentName}/" +
                    "{countryName}")
    public ResponseEntity<List<VolunteerPerson>> findDateTimeRange(
            @PathVariable String interestNeighborhoodName,
            @PathVariable String interestCityName,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName,
            @Valid @NotNull @RequestBody List<DayTimeRangeObject> dayTimeRange){

        try{
            return ResponseEntity.ok(volunteerPersonService.findDateTimeRange(
                    dayTimeRange, interestNeighborhoodName, interestCityName, interestDepartmentName, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
