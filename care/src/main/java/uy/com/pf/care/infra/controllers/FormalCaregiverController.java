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
import uy.com.pf.care.model.documents.FormalCaregiver;
import uy.com.pf.care.model.objects.DayTimeRangeObject;
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
    public ResponseEntity<String> add(@Valid @NotNull @RequestBody FormalCaregiver formalCaregiver){
        try {
            return ResponseEntity.ok(formalCaregiverService.save(formalCaregiver));

        }catch(FormalCaregiverValidateVoteException | FormalCaregiverSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@Valid @NotNull @RequestBody FormalCaregiver newFormalCaregiver){
        try {
            return ResponseEntity.ok(formalCaregiverService.update(newFormalCaregiver));

        }catch(FormalCaregiverUpdateException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(
            value = "findAll/{withoutValidate}/{includeDeleted}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<FormalCaregiver>> findAll(
            @PathVariable Boolean withoutValidate,
            @PathVariable Boolean includeDeleted,
            @PathVariable String countryName){

        try {
            return ResponseEntity.ok(formalCaregiverService.findAll(withoutValidate, includeDeleted, countryName));

        }catch(FormalCaregiverFindAllException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "findId/{id}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Optional<FormalCaregiver>> findId(@PathVariable String id) {
        try{
            return ResponseEntity.ok(formalCaregiverService.findId(id));

        }catch (FormalCaregiverNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(FormalCaregiverFindIdException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "findMail/{mail}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<FormalCaregiver> findMail(@PathVariable String mail) {
        try{
            return ResponseEntity.ok(formalCaregiverService.findMail(mail).get());

        }catch (FormalCaregiverNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(FormalCaregiverFindMailException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "existMail/{mail}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Boolean> existMail(@PathVariable String mail) {
        try{
            return ResponseEntity.ok(formalCaregiverService.findMail(mail).isPresent());

        }catch(Exception e) {
            String msg = "Error buscando Cuidador Formal con mail: " + mail;
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(value = "findTelephone/{telephone}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<FormalCaregiver> findTelephone(@PathVariable String telephone) {
        try{
            return ResponseEntity.ok(formalCaregiverService.findTelephone(telephone).get());

        }catch (FormalCaregiverNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(FormalCaregiverFindMailException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "existTelephone/{telephone}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Boolean> existTelephone(@PathVariable String telephone) {
        try{
            return ResponseEntity.ok(formalCaregiverService.findTelephone(telephone).isPresent());

        }catch(Exception e) {
            String msg = "Error buscando Cuidador Formal con telefono: " + telephone;
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(
            value = "findName/{withoutValidate}/{includeDeleted}/{name}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<FormalCaregiver>> findName(
            @PathVariable Boolean withoutValidate,
            @PathVariable Boolean includeDeleted,
            @PathVariable String name,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(formalCaregiverService.findName(withoutValidate, includeDeleted, countryName, name));

        }catch(FormalCaregiverFindNameException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(
            value = "findNameLike/{withoutValidate}/{includeDeleted}/{name}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<FormalCaregiver>> findNameLike(
            @PathVariable Boolean withoutValidate,
            @PathVariable Boolean includeDeleted,
            @PathVariable String name,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(formalCaregiverService.findNameLike(withoutValidate, includeDeleted, countryName, name));

        }catch(FormalCaregiverFindNameLikeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    //  Devuelve true si la operación fue exitosa.
    @PutMapping("setAvailability/{id}/{isAvailable}")
    public ResponseEntity<Boolean> setAvailability(@PathVariable String id, @PathVariable Boolean isAvailable) {
        try {
            return ResponseEntity.ok(formalCaregiverService.setAvailability(id, isAvailable));

        }catch (FormalCaregiverNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(FormalCaregiverSetAvailabilityException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // *** NOTA: este servicio es solo para uso interno de FormalCaregiverScore.
    // A saber: si se usa 'manualmente', si bien actualiza correctamente el array 'votes' del Cuidador Formal,
    // no actualiza la coleccion FormalCaregiverScores, con lo cual los votos y el promedio quedan descordinados.
    //
    // Devuelve true si la operación fue exitosa.
    @PutMapping("updateVotes/{formalCaregiverId}/{previousScore}/{currentScore}")
    public ResponseEntity<Boolean> updateVotes(
            @PathVariable String formalCaregiverId,
            @PathVariable Integer previousScore,
            @PathVariable Integer currentScore) {

        try{
            return ResponseEntity.ok(formalCaregiverService.updateVotes(
                    formalCaregiverId, previousScore, currentScore));

        }catch(FormalCaregiverNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(FormalCaregiverUpdateVotesException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // Devuelve true si la operación fue exitosa
    @PutMapping("setValidation/{id}/{isValidated}")
    public ResponseEntity<Boolean> setValidate(@PathVariable String id, @PathVariable Boolean isValidated) {
        try {
            return ResponseEntity.ok(formalCaregiverService.setValidation(id, isValidated));

        }catch(FormalCaregiverNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(FormalCaregiverSetValidationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // Devuelve true si la operación fue exitosa
    @PutMapping("setDeletion/{id}/{isDeleted}")
    public ResponseEntity<Boolean> setDeletion(@PathVariable String id, @PathVariable Boolean isDeleted) {
        try{
            return ResponseEntity.ok(formalCaregiverService.setDeletion(id, isDeleted));

        }catch(FormalCaregiverNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(FormalCaregiverSetDeletionException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    ///Devuelve true si el cuidador esta validado y no esta borrado
    @GetMapping(value = "isValidated_notDeleted/{id}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Boolean> isValidated_notDeleted(@PathVariable String id) {
        try{
            Optional<FormalCaregiver> found = formalCaregiverService.findId(id);
            if (found.isPresent())
                return ResponseEntity.ok(found.get().getValidate() && ! found.get().getDeleted());
            return ResponseEntity.ok(false);

        }catch(Exception e) {
            String msg = "Error buscando cuidador formal con id " + id;
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(
            value = "findInterestZones_Neighborhood/" +
            "{withoutValidate}/" +
            "{includeDeleted}/" +
            "{interestNeighborhoodName}/" +
            "{interestCityName}/" +
            "{interestDepartmentName}/" +
            "{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<FormalCaregiver>> findInterestZones_Neighborhood(
            @PathVariable Boolean withoutValidate,
            @PathVariable Boolean includeDeleted,
            @PathVariable String interestNeighborhoodName,
            @PathVariable String interestCityName,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(formalCaregiverService.findInterestZones_Neighborhood(
                    withoutValidate,
                    includeDeleted,
                    interestNeighborhoodName,
                    interestCityName,
                    interestDepartmentName,
                    countryName));

        }catch(FormalCaregiverFindInterestZones_NeighborhoodException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    @GetMapping(
            value = "findInterestZones_City/" +
                    "{withoutValidate}/" +
                    "{includeDeleted}/" +
                    "{interestCityName}/" +
                    "{interestDepartmentName}/" +
                    "{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<FormalCaregiver>> findInterestZones_City(
            @PathVariable Boolean withoutValidate,
            @PathVariable Boolean includeDeleted,
            @PathVariable String interestCityName,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName){
        try{
            return ResponseEntity.ok(formalCaregiverService.findInterestZones_City(
                    true, withoutValidate, includeDeleted, interestCityName, interestDepartmentName, countryName));

        }catch(FormalCaregiverFindInterestZones_CityException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    @GetMapping(
            value = "findInterestZones_Department/" +
                    "{withoutValidate}/" +
                    "{includeDeleted}/" +
                    "{interestDepartmentName}/" +
                    "{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<FormalCaregiver>> findInterestZones_Department(
            @PathVariable Boolean withoutValidate,
            @PathVariable Boolean includeDeleted,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(formalCaregiverService.findInterestZones_Department(
                     true, withoutValidate, includeDeleted, interestDepartmentName, countryName));

        }catch(FormalCaregiverFindInterestZones_DepartmentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(
            value = "findPriceRange/" +
                    "{maxPrice}/" +
                    "{interestNeighborhoodName}/" +
                    "{interestCityName}/" +
                    "{interestDepartmentName}/" +
                    "{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
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
            value = "findDateTimeRange/" +
                    "{interestNeighborhoodName}/" +
                    "{interestCityName}/" +
                    "{interestDepartmentName}/" +
                    "{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<FormalCaregiver>> findDateTimeRange(
            @PathVariable String interestNeighborhoodName,
            @PathVariable String interestCityName,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName,
            @Valid @NotNull @RequestBody List<DayTimeRangeObject> dayTimeRange){

        try{
            return ResponseEntity.ok(formalCaregiverService.findDateTimeRange(
                    dayTimeRange, interestNeighborhoodName, interestCityName, interestDepartmentName, countryName));

        }catch(FormalCaregiverFindDateTimeRangeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
