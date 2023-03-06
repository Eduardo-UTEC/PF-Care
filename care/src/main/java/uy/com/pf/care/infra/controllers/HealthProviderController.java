package uy.com.pf.care.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
        healthProviderService.save(healthProvider);
    }

    @GetMapping("findId/{id}")
    public Optional<HealthProvider> findId( @PathVariable String id) {
        return healthProviderService.findId(id);
    }

    @GetMapping("findAll/{countryName}")
    public List<HealthProvider> findAll(@PathVariable String countryName ){
        return healthProviderService.findAll(countryName);
    }

    @GetMapping("findByCity/{cityName}/{departmentName}/{countryName}")
    public List<HealthProvider> findByCity(@PathVariable String cityName,
                                           @PathVariable String departmentName,
                                           @PathVariable String countryName){
        return healthProviderService.findByCity(cityName, departmentName, countryName);
    }

    @GetMapping("findByDepartment/{departmentName}/{countryName}")
    public List<HealthProvider> findByDepartment(@PathVariable String departmentName,
                                                 @PathVariable String countryName){
        return healthProviderService.findByDepartment(departmentName, countryName);
    }

}
