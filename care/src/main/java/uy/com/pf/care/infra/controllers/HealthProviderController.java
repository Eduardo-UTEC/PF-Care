package uy.com.pf.care.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.HealthProviderSaveException;
import uy.com.pf.care.exceptions.PatientSaveException;
import uy.com.pf.care.model.documents.HealthProvider;
import uy.com.pf.care.services.IHealthProviderService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/health_providers")
public class HealthProviderController {

    @Autowired
    private IHealthProviderService healthProviderService;

    @PostMapping("/add")
    public void add( @RequestBody HealthProvider healthProvider){
        try{
            healthProviderService.save(healthProvider);
        }catch (HealthProviderSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error guardando proveedor de salud");
        }
    }

    @GetMapping("findId/{id}")
    public Optional<HealthProvider> findId( @PathVariable String id) {
        try{
            return healthProviderService.findId(id);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando proveedor de salud con id " + id);
        }
    }

    @GetMapping("findAll/{countryName}")
    public List<HealthProvider> findAll(@PathVariable String countryName ){
        try{
            return healthProviderService.findAll(countryName);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando todos los proveedores de salud de " + countryName);
        }
    }

    @GetMapping("findByCity/{cityName}/{departmentName}/{countryName}")
    public List<HealthProvider> findByCity(@PathVariable String cityName,
                                           @PathVariable String departmentName,
                                           @PathVariable String countryName){
        try{
            return healthProviderService.findByCity(cityName, departmentName, countryName);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando proveedores de salud en la ciudad/localidad de" +
                            cityName + " (" + departmentName + ", " + countryName + ")");
        }
    }

    @GetMapping("findByDepartment/{departmentName}/{countryName}")
    public List<HealthProvider> findByDepartment(@PathVariable String departmentName,
                                                 @PathVariable String countryName){
        try{
            return healthProviderService.findByDepartment(departmentName, countryName);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando proveedores de salud en el departamento/provincia de " +
                            departmentName + " (" + countryName + ")");
        }
    }

}
