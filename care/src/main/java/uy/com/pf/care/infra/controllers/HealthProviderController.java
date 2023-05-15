package uy.com.pf.care.infra.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.HealthProviderSaveException;
import uy.com.pf.care.model.documents.HealthProvider;
import uy.com.pf.care.model.objects.HealthProviderIdObject;
import uy.com.pf.care.services.IHealthProviderService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/health_providers")
public class HealthProviderController {

    @Autowired
    private IHealthProviderService healthProviderService;

    @PostMapping("/add")
    //public ResponseEntity<HealthProviderIdObject> add(@Valid @NotNull @RequestBody HealthProvider healthProvider){
    public ResponseEntity<String> add(@Valid @NotNull @RequestBody HealthProvider healthProvider){
        try{
            /*return ResponseEntity.ok(
                    new HealthProviderIdObject(healthProviderService.save(healthProvider).getHealthProviderId()));
*/
            return ResponseEntity.ok(healthProviderService.save(healthProvider));

        }catch (HealthProviderSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error guardando proveedor de salud");
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
    @PostMapping("setDeletion/{id}/{isDeleted}")
    public ResponseEntity<Boolean> setDeletion(@PathVariable String id, @PathVariable Boolean isDeleted) {
        try{
            return ResponseEntity.ok(healthProviderService.setDeletion(id, isDeleted));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo setear el borrado lógico del proveedor de salud con id " + id);
        }
    }

}
