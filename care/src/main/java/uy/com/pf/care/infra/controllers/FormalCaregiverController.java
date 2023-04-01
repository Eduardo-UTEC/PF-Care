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
                    new FormalCaregiverIdObject(formalCaregiverService.save(formalCaregiver).getFormalCaregiver_id()));

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

    @GetMapping("findByMail/{mail}")
    public ResponseEntity<FormalCaregiver> findByMail(@PathVariable String mail) {
        try{
            return ResponseEntity.ok(formalCaregiverService.findWithIndex_Mail(mail));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando por mail del cuidador formal (" + mail + ")");
        }
    }

    @GetMapping("findByName/{includeDeleted}/{name}/{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findByName(
            @PathVariable Boolean includeDeleted,
            @PathVariable String name,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(formalCaregiverService.findWithIndex_Name(includeDeleted, name, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando el cuidador formal " + name + " (" + countryName + ")");
        }
    }

    @GetMapping("findByNameLike/{includeDeleted}/{name}/{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findByNameLike(
            @PathVariable Boolean includeDeleted,
            @PathVariable String name,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(formalCaregiverService.findByNameLike(includeDeleted, name, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidadores formales de nombre " + name + " (" + countryName + ")");
        }
    }

    //  Devuelve true si la operación fue exitosa.
    @PatchMapping("setAvailability/{id}/{isAvailable}")
    public ResponseEntity<Boolean> setAvailability(@PathVariable String id, @PathVariable Boolean isAvailable) {
        try{
            return ResponseEntity.ok(formalCaregiverService.setAvailability(id, isAvailable));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo setear la disponibilidad del cuidador formal con id " + id);
        }
    }

    // Devuelve true si la operación fue exitosa
    @PatchMapping("setDeletion/{id}/{isDeleted}")
    public ResponseEntity<Boolean> setDeletion(@PathVariable String id, @PathVariable Boolean isDeleted) {
        try{
            return ResponseEntity.ok(formalCaregiverService.setDeletion(id, isDeleted));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo setear el borrado lógico del cuidador formal con id " + id);
        }
    }

    @GetMapping(
            "findByInterestZones_Neighborhood/" +
            "{includeDeleted}/" +
            "{interestNeighborhoodName}/" +
            "{interestCityName}/" +
            "{interestDepartmentName}/" +
            "{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findByInterestZones_Neighborhood(
            @PathVariable Boolean includeDeleted,
            @PathVariable String interestNeighborhoodName,
            @PathVariable String interestCityName,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(formalCaregiverService.findByInterestZones_Neighborhood(
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
    @GetMapping("findByInterestZones_City/{includeDeleted}/{interestCityName}/{interestDepartmentName}/{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findByInterestZones_City(
            @PathVariable Boolean includeDeleted,
            @PathVariable String interestCityName,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName){
        try{
            return ResponseEntity.ok(formalCaregiverService.findByInterestZones_City(
                    true, includeDeleted, interestCityName, interestDepartmentName, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidadores formales por zona de interés en ciudad " + interestCityName + " (" +
                            interestDepartmentName + ", " + countryName + ")");
        }
    }
    @GetMapping("findByInterestZones_Department/{includeDeleted}/{interestDepartmentName}/{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findByInterestZones_Department(
            @PathVariable Boolean includeDeleted,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(formalCaregiverService.findByInterestZones_Department(
                    true, includeDeleted, interestDepartmentName, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidadores formales por zona de interés en departamento/provincia de " +
                            interestDepartmentName + " (" + countryName + ")");
        }
    }

    @GetMapping(
            "findByPriceRange/" +
                    "{maxPrice}/" +
                    "{interestNeighborhoodName}/" +
                    "{interestCityName}/" +
                    "{interestDepartmentName}/" +
                    "{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findByPriceRange(
            @PathVariable Integer maxPrice,
            @PathVariable String interestNeighborhoodName,
            @PathVariable String interestCityName,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(formalCaregiverService.findByPriceRange(
                    maxPrice, interestNeighborhoodName, interestCityName, interestDepartmentName, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidadores formales por rango de precios");
        }
    }

    @GetMapping(
            "findByDateTimeRange/" +
                    "{dayTimeRange}/" +
                    "{interestNeighborhoodName}/" +
                    "{interestCityName}/" +
                    "{interestDepartmentName}/" +
                    "{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findByDateTimeRange(
            @PathVariable List<DayTimeRangeObject> dayTimeRange, //TODO: ver como pasar el argmuento en el body
            @PathVariable String interestNeighborhoodName,
            @PathVariable String interestCityName,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(formalCaregiverService.findByDateTimeRange(
                    dayTimeRange, interestNeighborhoodName, interestCityName, interestDepartmentName, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidadores formales por rango de dias/horas");
        }
    }


}
