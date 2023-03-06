package uy.com.pf.care.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uy.com.pf.care.model.documents.EmergencyService;
import uy.com.pf.care.services.IEmergencyServiceService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/emergency_services")
public class EmergencyServiceController {

    @Autowired
    private IEmergencyServiceService emergencyServiceService;

    @PostMapping("/add")
    public void add( @RequestBody EmergencyService emergencyService){
        emergencyServiceService.save(emergencyService);
    }

    @GetMapping("findAll/{countryName}")
    public List<EmergencyService> findAll(@PathVariable String countryName){
        return emergencyServiceService.findAll(countryName);
    }

    @GetMapping("findId/{id}")
    public Optional<EmergencyService> findId( @PathVariable String id) {
        return emergencyServiceService.findId(id);
    }

    @GetMapping("findByCity/{cityName}/{departmentName}/{countryName}")
    public List<EmergencyService> findByCity(@PathVariable String cityName,
                                             @PathVariable String departmentName,
                                             @PathVariable String countryName){
        return emergencyServiceService.findByCity(cityName, departmentName, countryName);
    }

    @GetMapping("findByDepartment/{departmentName}/{countryName}")
    public List<EmergencyService> findByDepartment(@PathVariable String departmentName,
                                                   @PathVariable String countryName){
        return emergencyServiceService.findByDepartment(departmentName, countryName);
    }
}
