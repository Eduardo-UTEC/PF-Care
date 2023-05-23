package uy.com.pf.care.infra.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.FormalCaregiverUpdateException;
import uy.com.pf.care.exceptions.HealthProviderSaveException;
import uy.com.pf.care.model.documents.HealthProvider;
import uy.com.pf.care.services.IHealthProviderService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/health_providers")
@Log
public class HealthProviderController {

    @Autowired
    private IHealthProviderService healthProviderService;

    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @NotNull @RequestBody HealthProvider healthProvider){
        try{
            return ResponseEntity.ok(healthProviderService.save(healthProvider));

        }catch (HealthProviderSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@Valid @NotNull @RequestBody HealthProvider newHealthProvider){
        try {
            return ResponseEntity.ok(healthProviderService.update(newHealthProvider));

        }catch(FormalCaregiverUpdateException e){
            log.info(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<Optional<HealthProvider>> findId(@PathVariable String id) {
        try{
            return ResponseEntity.ok(healthProviderService.findId(id));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando proveedor de salud con id " + id);
        }
    }

    @GetMapping("findAll/{includeDeleted}/{countryName}")
    public ResponseEntity<List<HealthProvider>> findAll(
            @PathVariable Boolean includeDeleted,
            @PathVariable String countryName ){

        try{
            return ResponseEntity.ok(healthProviderService.findAll(includeDeleted, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando todos los proveedores de salud de " + countryName);
        }
    }

    @GetMapping("findByName/{name}/{cityName}/{departmentName}/{countryName}")
    public ResponseEntity<HealthProvider> findByName(
            @PathVariable String name,
            @PathVariable String cityName,
            @PathVariable String departmentName,
            @PathVariable String countryName ){

        try{
            return ResponseEntity.ok(healthProviderService.findByName(cityName, departmentName, countryName, name));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando proveedor de salud de nombre: " + name);
        }
    }

    @GetMapping("findByCity/{includeDeleted}/{cityName}/{departmentName}/{countryName}")
    public ResponseEntity<List<HealthProvider>> findByCity(
            @PathVariable Boolean includeDeleted,
            @PathVariable String cityName,
            @PathVariable String departmentName,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(healthProviderService.findByCity(
                    includeDeleted, cityName, departmentName, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando proveedores de salud en la ciudad/localidad de" +
                            cityName + " (" + departmentName + ", " + countryName + ")");
        }
    }

    @GetMapping("findByDepartment/{includeDeleted}/{departmentName}/{countryName}")
    public ResponseEntity<List<HealthProvider>> findByDepartment(
            @PathVariable Boolean includeDeleted,
            @PathVariable String departmentName,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(healthProviderService.findByDepartment(includeDeleted, departmentName, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando proveedores de salud en el departamento/provincia de " +
                            departmentName + " (" + countryName + ")");
        }
    }

    // Devuelve true si la operación fue exitosa
    @PutMapping("setDeletion/{id}/{isDeleted}")
    public ResponseEntity<Boolean> setDeletion(@PathVariable String id, @PathVariable Boolean isDeleted) {
        try{
            return ResponseEntity.ok(healthProviderService.setDeletion(id, isDeleted));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo setear el borrado lógico del proveedor de salud con id " + id);
        }
    }

}
