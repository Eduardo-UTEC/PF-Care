package uy.com.pf.care.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.FormalCaregiverSaveException;
import uy.com.pf.care.exceptions.PatientSaveException;
import uy.com.pf.care.model.documents.FormalCaregiver;
import uy.com.pf.care.model.objects.FormalCaregiverIdObject;
import uy.com.pf.care.services.IFormalCaregiverService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/formal_caregivers")
public class FormalCaregiverController {

    @Autowired
    private IFormalCaregiverService formalCaregiverService;

    @PostMapping("/add")
    public FormalCaregiverIdObject add(@RequestBody FormalCaregiver formalCaregiver){
        try{
            return new FormalCaregiverIdObject(formalCaregiverService.save(formalCaregiver).getFormalCaregiver_id());
        }catch (FormalCaregiverSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error guardando cuidador formal");
    }

}

    @GetMapping("findAll/{countryName}")
    public List<FormalCaregiver> findAll(@PathVariable String countryName){
        try{
            return formalCaregiverService.findAll(countryName);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando todos los cuidadores formales de " + countryName);
        }
    }

    @GetMapping("findId/{id}")
    public Optional<FormalCaregiver> findId( @PathVariable String id) {
        try{
            return formalCaregiverService.findId(id);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidador formal con id " + id);
        }
    }

    @GetMapping("findByDepartment/{departmentName}/{countryName}")
    public List<FormalCaregiver> findByDepartment(@PathVariable String departmentName,
                                                  @PathVariable String countryName){
        try{
            return formalCaregiverService.findByDepartment(departmentName, countryName);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidadores formales en departamento/provincia de " +
                            departmentName + " (" + countryName + ")");
        }
    }
}
