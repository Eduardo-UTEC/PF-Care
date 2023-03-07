package uy.com.pf.care.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.HealthProviderSaveException;
import uy.com.pf.care.exceptions.PatientSaveException;
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
            return new ResponseEntity<>(
                    new HealthProviderIdObject(healthProviderService.save(healthProvider).getHealthProvider_id()),
                    HttpStatus.OK);

        }catch (HealthProviderSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error guardando proveedor de salud");
        }
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<Optional<HealthProvider>> findId(@PathVariable String id) {
        try{
            return new ResponseEntity<>(healthProviderService.findId(id), HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando proveedor de salud con id " + id);
        }
    }

    @GetMapping("findAll/{countryName}")
    public ResponseEntity<List<HealthProvider>> findAll(@PathVariable String countryName ){
        try{
            return new ResponseEntity<>(healthProviderService.findAll(countryName), HttpStatus.OK);

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
            return new ResponseEntity<>(
                    healthProviderService.findByCity(cityName, departmentName, countryName),
                    HttpStatus.OK);

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
            return new ResponseEntity<>(
                    healthProviderService.findByDepartment(departmentName, countryName),
                    HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando proveedores de salud en el departamento/provincia de " +
                            departmentName + " (" + countryName + ")");
        }
    }

}
