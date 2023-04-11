package uy.com.pf.care.infra.controllers;

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
    public ResponseEntity<HealthProviderIdObject> add(@RequestBody HealthProvider healthProvider){
        try{
            return ResponseEntity.ok(
                    new HealthProviderIdObject(healthProviderService.save(healthProvider).getHealthProvider_id()));

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

    @GetMapping("findAll/{countryName}")
    public ResponseEntity<List<HealthProvider>> findAll(@PathVariable String countryName ){
        try{
            return ResponseEntity.ok(healthProviderService.findAll(countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando todos los proveedores de salud de " + countryName);
        }
    }

    @GetMapping("findByCity/{cityName}/{departmentName}/{countryName}")
    public ResponseEntity<List<HealthProvider>> findByCity(@PathVariable String cityName,
                                                           @PathVariable String departmentName,
                                                           @PathVariable String countryName){
        try{
            return ResponseEntity.ok(healthProviderService.findByCity(cityName, departmentName, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando proveedores de salud en la ciudad/localidad de" +
                            cityName + " (" + departmentName + ", " + countryName + ")");
        }
    }

    @GetMapping("findByDepartment/{departmentName}/{countryName}")
    public ResponseEntity<List<HealthProvider>> findByDepartment(@PathVariable String departmentName,
                                                                 @PathVariable String countryName){
        try{
            return ResponseEntity.ok(healthProviderService.findByDepartment(departmentName, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando proveedores de salud en el departamento/provincia de " +
                            departmentName + " (" + countryName + ")");
        }
    }

    // Devuelve true si la operación fue exitosa
    @PatchMapping("setDeletion/{id}/{isDeleted}")
    public ResponseEntity<Boolean> setDeletion(@PathVariable String id, @PathVariable Boolean isDeleted) {
        try{
            return ResponseEntity.ok(healthProviderService.setDeletion(id, isDeleted));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo setear el borrado lógico del proveedor de salud con id " + id);
        }
    }

}
