package uy.com.pf.care.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uy.com.pf.care.model.documents.Residential;
import uy.com.pf.care.services.IResidentialService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/residential")
public class ResidentialController {

    @Autowired
    private IResidentialService residentialService;

    @PostMapping("/add")
    public void add( @RequestBody Residential residential){
        residentialService.save(residential);
    }

    @GetMapping("findAll/{countryName}")
    public List<Residential> findAll(@PathVariable String countryName){
        return residentialService.findByCountry(countryName);
    }

    @GetMapping("findId/{id}")
    public Optional<Residential> findId(@PathVariable String id) {
        return residentialService.findId(id);
    }

    @GetMapping("findByDepartment/{departmentName}/{countryName}")
    public List<Residential> findByDepartment(@PathVariable String departmentName, @PathVariable String countryName){
        return residentialService.findByDepartment(departmentName, countryName);
    }
    @GetMapping("findByCity/{cityName}/{departmentName}/{countryName}")
    public List<Residential> findByCity(@PathVariable String cityName,
                                        @PathVariable String departmentName,
                                        @PathVariable String countryName){
        return residentialService.findByCity(cityName, departmentName, countryName);
    }

}
