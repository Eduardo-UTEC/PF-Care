package uy.com.pf.care.infra.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.EmergencyServiceNotFoundException;
import uy.com.pf.care.exceptions.EmergencyServiceSaveException;
import uy.com.pf.care.exceptions.EmergencyServiceUpdateException;
import uy.com.pf.care.infra.repos.IEmergencyServiceRepo;
import uy.com.pf.care.model.documents.EmergencyService;
import uy.com.pf.care.services.IEmergencyServiceService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/emergency_services")
public class EmergencyServiceController {

    @Autowired
    private IEmergencyServiceService emergencyServiceService;
    @Autowired
    private IEmergencyServiceRepo iEmergencyServiceRepo;

    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @NotNull @RequestBody EmergencyService emergencyService){
        try{
            return ResponseEntity.ok(emergencyServiceService.save(emergencyService));

        }catch (EmergencyServiceSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@Valid @NotNull @RequestBody EmergencyService newEmergencyService){
        try {
            return ResponseEntity.ok(emergencyServiceService.update(newEmergencyService));

        }catch (EmergencyServiceNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (EmergencyServiceUpdateException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("findAll/{includeDeleted}/{countryName}")
    public ResponseEntity<List<EmergencyService>> findAll(
            @PathVariable Boolean includeDeleted,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(emergencyServiceService.findAll(includeDeleted, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando todos los servicios de emergencia de " + countryName);
        }
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<Optional<EmergencyService>> findId( @PathVariable String id) {
        try {
            return ResponseEntity.ok(emergencyServiceService.findId(id));

        }catch (EmergencyServiceNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando servicio de emergencia con id " + id);
        }
    }

    @GetMapping("findByCity/{includeDeleted}/{cityName}/{departmentName}/{countryName}")
    public ResponseEntity<List<EmergencyService>> findByCity(
            @PathVariable Boolean includeDeleted,
            @PathVariable String cityName,
            @PathVariable String departmentName,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(
                    emergencyServiceService.findByCity(includeDeleted, cityName, departmentName, countryName));

        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando servicios de emergencia en ciudad/localidad: " +
                            cityName + " (" + departmentName + ", " + countryName + ")");
        }
    }

    @GetMapping("findByDepartment/{includeDeleted}/{departmentName}/{countryName}")
    public ResponseEntity<List<EmergencyService>> findByDepartment(
            @PathVariable Boolean includeDeleted,
            @PathVariable String departmentName,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(
                    emergencyServiceService.findByDepartment(includeDeleted, departmentName, countryName));

        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando servicios de emergencia en departamento/provincia: " +
                            departmentName + " (" + countryName + ")");
        }
    }

    @GetMapping("findByName/{includeDeleted}/{name}/{cityName}/{departmentName}/{countryName}")
    public ResponseEntity<EmergencyService> findByName(@PathVariable Boolean includeDeleted,
                                                       @PathVariable String name,
                                                       @PathVariable String cityName,
                                                       @PathVariable String departmentName,
                                                       @PathVariable String countryName){
        try{
            return ResponseEntity.ok(
                    emergencyServiceService.findByName(includeDeleted, name, cityName, departmentName, countryName));

        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando el servicio de emergencia " + name + " en ciudad/localidad: " +
                            cityName + " (" + departmentName + ", " + countryName + ")");
        }
    }

    // Devuelve true si la operación fue exitosa
    @PutMapping("setDeletion/{id}/{isDeleted}")
    public ResponseEntity<Boolean> setDeletion(@PathVariable String id, @PathVariable Boolean isDeleted) {
        try {
            return ResponseEntity.ok(emergencyServiceService.setDeletion(id, isDeleted));

        }catch (EmergencyServiceNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo setear el borrado lógico del servicio de emergencia con id " + id);
        }
    }

}
