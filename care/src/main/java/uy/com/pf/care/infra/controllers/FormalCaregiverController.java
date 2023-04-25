package uy.com.pf.care.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.FormalCaregiverSaveException;
import uy.com.pf.care.model.documents.FormalCaregiver;
import uy.com.pf.care.model.objects.DayTimeRangeObject;
import uy.com.pf.care.model.objects.FormalCaregiverIdObject;
import uy.com.pf.care.services.IFormalCaregiverService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/formal_caregivers")
public class FormalCaregiverController {

    @Autowired
    private IFormalCaregiverService formalCaregiverService;

    @PostMapping("/add")
    public ResponseEntity<FormalCaregiverIdObject> add(@RequestBody FormalCaregiver formalCaregiver){
        try{
            return ResponseEntity.ok(
                    new FormalCaregiverIdObject(formalCaregiverService.save(formalCaregiver).getFormalCaregiverId()));

        }catch (FormalCaregiverSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error guardando cuidador formal");
        }
    }

    @GetMapping("findAll/{includeDeleted}/{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findAll(
            @PathVariable Boolean includeDeleted,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(formalCaregiverService.findAll(includeDeleted, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando todos los cuidadores formales de " + countryName);
        }
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<Optional<FormalCaregiver>> findId(@PathVariable String id) {
        try{
            return ResponseEntity.ok(formalCaregiverService.findId(id));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidador formal con id " + id);
        }
    }

    @GetMapping("findMail/{mail}")
    public ResponseEntity<FormalCaregiver> findByMail(@PathVariable String mail) {
        try{
            return ResponseEntity.ok(formalCaregiverService.findMail(mail));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando por mail del cuidador formal (" + mail + ")");
        }
    }

    @GetMapping("findName/{includeDeleted}/{name}/{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findName(
            @PathVariable Boolean includeDeleted,
            @PathVariable String name,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(formalCaregiverService.findName(includeDeleted, countryName, name));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando el cuidador formal " + name + " (" + countryName + ")");
        }
    }

    @GetMapping("findNameLike/{includeDeleted}/{name}/{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findNameLike(
            @PathVariable Boolean includeDeleted,
            @PathVariable String name,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(formalCaregiverService.findNameLike(includeDeleted, countryName, name));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidadores formales de nombre " + name + " (" + countryName + ")");
        }
    }

    //  Devuelve true si la operación fue exitosa.
    @PostMapping("setAvailability/{id}/{isAvailable}")
    public ResponseEntity<Boolean> setAvailability(@PathVariable String id, @PathVariable Boolean isAvailable) {
        try{
            return ResponseEntity.ok(formalCaregiverService.setAvailability(id, isAvailable));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo setear la disponibilidad del cuidador formal con id " + id);
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

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo actualizar el scoreData del cuidador formal con id " + formalCaregiverId);
        }
    }


    // Devuelve true si la operación fue exitosa
    @PostMapping("setDeletion/{id}/{isDeleted}")
    public ResponseEntity<Boolean> setDeletion(@PathVariable String id, @PathVariable Boolean isDeleted) {
        try{
            return ResponseEntity.ok(formalCaregiverService.setDeletion(id, isDeleted));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo setear el borrado lógico del cuidador formal con id " + id);
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

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidadores formales por zona de interés en barrio " + interestNeighborhoodName +
                            " (" + interestCityName+ ", " + interestDepartmentName + ", " + countryName + ")");
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

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidadores formales por zona de interés en ciudad " + interestCityName + " (" +
                            interestDepartmentName + ", " + countryName + ")");
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

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidadores formales por zona de interés en departamento/provincia de " +
                            interestDepartmentName + " (" + countryName + ")");
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

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidadores formales por rango de precios");
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
            @RequestBody List<DayTimeRangeObject> dayTimeRange){

        try{
            return ResponseEntity.ok(formalCaregiverService.findDateTimeRange(
                    dayTimeRange, interestNeighborhoodName, interestCityName, interestDepartmentName, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidadores formales por rango de dias/horas");
        }
    }




}
