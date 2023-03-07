package uy.com.pf.care.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<FormalCaregiverIdObject> add(@RequestBody FormalCaregiver formalCaregiver){
        try{
            return new ResponseEntity<>(
                    new FormalCaregiverIdObject(formalCaregiverService.save(formalCaregiver).getFormalCaregiver_id()),
                    HttpStatus.OK);

        }catch (FormalCaregiverSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error guardando cuidador formal");
        }
    }

    @GetMapping("findAll/{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findAll(@PathVariable String countryName){
        try{
            return new ResponseEntity<>(formalCaregiverService.findAll(countryName), HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando todos los cuidadores formales de " + countryName);
        }
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<Optional<FormalCaregiver>> findId(@PathVariable String id) {
        try{
            return new ResponseEntity<>(formalCaregiverService.findId(id), HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidador formal con id " + id);
        }
    }

    @GetMapping("findByDepartment/{departmentName}/{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findByDepartment(@PathVariable String departmentName,
                                                                  @PathVariable String countryName){
        try{
            return new ResponseEntity<>(
                    formalCaregiverService.findByDepartment(departmentName, countryName),
                    HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidadores formales en departamento/provincia de " +
                            departmentName + " (" + countryName + ")");
        }
    }

}
