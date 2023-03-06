package uy.com.pf.care.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uy.com.pf.care.model.documents.FormalCaregiver;
import uy.com.pf.care.services.IFormalCaregiverService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/formal_caregivers")
public class FormalCaregiverController {

    @Autowired
    private IFormalCaregiverService formalCaregiverService;

    @PostMapping("/add")
    public void add( @RequestBody FormalCaregiver formalCaregiver){
        formalCaregiverService.save(formalCaregiver);
    }

    @GetMapping("findAll/{countryName}")
    public List<FormalCaregiver> findAll(@PathVariable String countryName){
        return formalCaregiverService.findAll(countryName);
    }

    @GetMapping("findId/{id}")
    public Optional<FormalCaregiver> findId( @PathVariable String id) {
        return formalCaregiverService.findId(id);
    }

    @GetMapping("findByDepartment/{departmentName}/{countryName}")
    public List<FormalCaregiver> findByDepartment(@PathVariable String departmentName,
                                                  @PathVariable String countryName){
        return formalCaregiverService.findByDepartment(departmentName, countryName);
    }
}
