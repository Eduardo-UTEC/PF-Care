package uy.com.pf.care.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uy.com.pf.care.model.documents.Zone;
import uy.com.pf.care.model.objects.NeighborhoodObject;
import uy.com.pf.care.services.IZoneService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/zones")
public class ZoneController {

    @Autowired
    private IZoneService zoneService;

    @PostMapping("/add")
    public void add( @RequestBody uy.com.pf.care.model.documents.Zone zone){
        zoneService.save(zone);
    }

    @GetMapping("findAll/{countryName}")
    public List<Zone> findAll(@PathVariable String countryName){
        return zoneService.findByCountry(countryName);
    }

    @GetMapping("findId/{id}")
    public Optional<uy.com.pf.care.model.documents.Zone> findId(@PathVariable String id) {
        return zoneService.findId(id);
    }

    @GetMapping("findAllNeighborhoods/{cityName}/{departmentName}/{countryName}")
    public List<NeighborhoodObject> findAllNeighborhood(@PathVariable String cityName,
                                            @PathVariable String departmentName,
                                            @PathVariable String countryName){
        return zoneService.findAllNeighborhoods(cityName, departmentName, countryName);
    }

    @GetMapping("findAllCities/{departmentName}/{countryName}")
    public List<String> findAllCities(@PathVariable String departmentName, @PathVariable String countryName){
        return zoneService.findAllCities(departmentName, countryName);
    }

    @GetMapping("findAllDepartments/{countryName}")
    public List<String> findAllDepartment(@PathVariable String countryName){
        return zoneService.findAllDepartments(countryName);
    }

    @GetMapping("findAllCountries")
    public List<String> findAllCountries(){
        return zoneService.findAllCountries();
    }

}
