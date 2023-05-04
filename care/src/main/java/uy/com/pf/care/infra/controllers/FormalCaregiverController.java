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
import uy.com.pf.care.model.documents.FormalCaregiver;
import uy.com.pf.care.model.objects.DayTimeRangeObject;
import uy.com.pf.care.model.objects.FormalCaregiverIdObject;
import uy.com.pf.care.services.IFormalCaregiverService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/formal_caregivers")
@Log
public class FormalCaregiverController {

    @Autowired
    private IFormalCaregiverService formalCaregiverService;

    @PostMapping("/add")
    public ResponseEntity<FormalCaregiverIdObject> add(@Valid @NotNull @RequestBody FormalCaregiver formalCaregiver){
        try {
            return ResponseEntity.ok(
                    new FormalCaregiverIdObject(formalCaregiverService.save(formalCaregiver).getFormalCaregiverId()));

        }catch(FormalCaregiverValidateVoteException | FormalCaregiverSaveException e){
            log.info(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("findAll/{includeDeleted}/{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findAll(
            @PathVariable Boolean includeDeleted,
            @PathVariable String countryName){

        try {
            return ResponseEntity.ok(formalCaregiverService.findAll(includeDeleted, countryName));

        }catch(FormalCaregiverFindAllException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<Optional<FormalCaregiver>> findId(@PathVariable String id) {
        try{
            return ResponseEntity.ok(formalCaregiverService.findId(id));

        }catch(FormalCaregiverFindIdException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("findMail/{mail}")
    public ResponseEntity<FormalCaregiver> findByMail(@PathVariable String mail) {
        try{
            return ResponseEntity.ok(formalCaregiverService.findMail(mail));

        }catch(FormalCaregiverFindMailException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("findName/{includeDeleted}/{name}/{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findName(
            @PathVariable Boolean includeDeleted,
            @PathVariable String name,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(formalCaregiverService.findName(includeDeleted, countryName, name));

        }catch(FormalCaregiverFindNameException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("findNameLike/{includeDeleted}/{name}/{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findNameLike(
            @PathVariable Boolean includeDeleted,
            @PathVariable String name,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(formalCaregiverService.findNameLike(includeDeleted, countryName, name));

        }catch(FormalCaregiverFindNameLikeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    //  Devuelve true si la operación fue exitosa.
    @PostMapping("setAvailability/{id}/{isAvailable}")
    public ResponseEntity<Boolean> setAvailability(@PathVariable String id, @PathVariable Boolean isAvailable) {
        try{
            return ResponseEntity.ok(formalCaregiverService.setAvailability(id, isAvailable));

        }catch(FormalCaregiverSetAvailabilityException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    //  Devuelve true si la operación fue exitosa.
    @PostMapping("updateVotes/{formalCaregiverId}/{previousScore}/{currentScore}")
    public ResponseEntity<Boolean> updateVotes(
            @PathVariable String formalCaregiverId,
            @PathVariable Integer previousScore,
            @PathVariable Integer currentScore) {

        try{
            return ResponseEntity.ok(formalCaregiverService.updateVotes(
                    formalCaregiverId, previousScore, currentScore));

        }catch(FormalCaregiverUpdateVotesException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // Devuelve true si la operación fue exitosa
    @PostMapping("setDeletion/{id}/{isDeleted}")
    public ResponseEntity<Boolean> setDeletion(@PathVariable String id, @PathVariable Boolean isDeleted) {
        try{
            return ResponseEntity.ok(formalCaregiverService.setDeletion(id, isDeleted));

        }catch(FormalCaregiverSetDeletionException e) {
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
    public ResponseEntity<List<FormalCaregiver>> findInterestZones_Neighborhood(
            @PathVariable Boolean includeDeleted,
            @PathVariable String interestNeighborhoodName,
            @PathVariable String interestCityName,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(formalCaregiverService.findInterestZones_Neighborhood(
                    includeDeleted,
                    interestNeighborhoodName,
                    interestCityName,
                    interestDepartmentName,
                    countryName));

        }catch(FormalCaregiverFindInterestZones_NeighborhoodException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    @GetMapping("findInterestZones_City/{includeDeleted}/{interestCityName}/{interestDepartmentName}/{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findInterestZones_City(
            @PathVariable Boolean includeDeleted,
            @PathVariable String interestCityName,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName){
        try{
            return ResponseEntity.ok(formalCaregiverService.findInterestZones_City(
                    true, includeDeleted, interestCityName, interestDepartmentName, countryName));

        }catch(FormalCaregiverFindInterestZones_CityException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    @GetMapping("findInterestZones_Department/{includeDeleted}/{interestDepartmentName}/{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findInterestZones_Department(
            @PathVariable Boolean includeDeleted,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(formalCaregiverService.findInterestZones_Department(
                    true, includeDeleted, interestDepartmentName, countryName));

        }catch(FormalCaregiverFindInterestZones_DepartmentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(
            "findPriceRange/" +
                    "{maxPrice}/" +
                    "{interestNeighborhoodName}/" +
                    "{interestCityName}/" +
                    "{interestDepartmentName}/" +
                    "{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findPriceRange(
            @PathVariable Integer maxPrice,
            @PathVariable String interestNeighborhoodName,
            @PathVariable String interestCityName,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(formalCaregiverService.findPriceRange(
                    maxPrice, interestNeighborhoodName, interestCityName, interestDepartmentName, countryName));

        }catch(FormalCaregiverFindPriceRangeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(
            "findDateTimeRange/" +
                    "{interestNeighborhoodName}/" +
                    "{interestCityName}/" +
                    "{interestDepartmentName}/" +
                    "{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findDateTimeRange(
            @PathVariable String interestNeighborhoodName,
            @PathVariable String interestCityName,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName,
            @Valid @RequestBody List<DayTimeRangeObject> dayTimeRange){

        try{
            return ResponseEntity.ok(formalCaregiverService.findDateTimeRange(
                    dayTimeRange, interestNeighborhoodName, interestCityName, interestDepartmentName, countryName));

        }catch(FormalCaregiverFindDateTimeRangeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
