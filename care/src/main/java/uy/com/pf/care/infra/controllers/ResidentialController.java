package uy.com.pf.care.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.ResidentialSaveException;
import uy.com.pf.care.model.documents.Residential;
import uy.com.pf.care.model.objects.ResidentialIdObject;
import uy.com.pf.care.services.IResidentialService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/residential")
public class ResidentialController {

    @Autowired
    private IResidentialService residentialService;

    @PostMapping("/add")
    public ResponseEntity<ResidentialIdObject> add(@RequestBody Residential residential){
        try{
            return new ResponseEntity<>(
                    new ResidentialIdObject(residentialService.save(residential).getResidential_id()),
                    HttpStatus.OK);

        }catch (ResidentialSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error guardando residencial");
        }
    }

    @GetMapping("findAll/{countryName}")
    public ResponseEntity<List<Residential>> findAll(@PathVariable String countryName){
        try{
            return new ResponseEntity<>(residentialService.findByCountry(countryName), HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando todos los residenciales de " + countryName);
        }
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<Optional<Residential>> findId(@PathVariable String id) {
        try{
            return new ResponseEntity<>(residentialService.findId(id), HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando residencial con id " + id);
        }
    }

    @GetMapping("findByDepartment/{departmentName}/{countryName}")
    public ResponseEntity<List<Residential>> findByDepartment(@PathVariable String departmentName,
                                                              @PathVariable String countryName){
        try{
            return new ResponseEntity<>(residentialService.findByDepartment(departmentName, countryName), HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando pacientes en residenciales de " +
                            departmentName + " (" + countryName + ")");
        }
    }

    @GetMapping("findByCity/{cityName}/{departmentName}/{countryName}")
    public ResponseEntity<List<Residential>> findByCity(@PathVariable String cityName,
                                                        @PathVariable String departmentName,
                                                        @PathVariable String countryName){
        try{
            return new ResponseEntity<>(
                    residentialService.findByCity(cityName, departmentName, countryName),
                    HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando residenciales en la ciudad/localidad de " +
                            cityName + " (" + departmentName + ", " + countryName + ")");
        }
    }

}
