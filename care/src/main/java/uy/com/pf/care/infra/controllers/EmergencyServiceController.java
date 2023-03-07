package uy.com.pf.care.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.EmergencyServiceSaveException;
import uy.com.pf.care.model.documents.EmergencyService;
import uy.com.pf.care.model.objects.EmergencyServiceIdObject;
import uy.com.pf.care.services.IEmergencyServiceService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/emergency_services")
public class EmergencyServiceController {

    @Autowired
    private IEmergencyServiceService emergencyServiceService;

    @PostMapping("/add")
    public ResponseEntity<EmergencyServiceIdObject> add(@RequestBody EmergencyService emergencyService){
        try{
            return new ResponseEntity<>(
                    new EmergencyServiceIdObject(emergencyServiceService.save(emergencyService).getEmergencyService_id()),
                    HttpStatus.OK);

        }catch (EmergencyServiceSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error guardando servicio de emergencia");
        }
    }

    @GetMapping("findAll/{countryName}")
    public ResponseEntity<List<EmergencyService>> findAll(@PathVariable String countryName){
        try{
            return new ResponseEntity<>(emergencyServiceService.findAll(countryName), HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando todos los servicios de emergencia de " + countryName);
        }
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<Optional<EmergencyService>> findId( @PathVariable String id) {
        try{
            return new ResponseEntity<>(emergencyServiceService.findId(id), HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando servicio de emergencia con id " + id);
    }
    }

    @GetMapping("findByCity/{cityName}/{departmentName}/{countryName}")
    public ResponseEntity<List<EmergencyService>> findByCity(@PathVariable String cityName,
                                                             @PathVariable String departmentName,
                                                             @PathVariable String countryName){
        try{
            return new ResponseEntity<>(
                    emergencyServiceService.findByCity(cityName, departmentName, countryName),
                    HttpStatus.OK);

        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando servicios de emergencia en ciudad/localidad: " +
                            cityName + " (" + departmentName + ", " + countryName + ")");
        }
    }

    @GetMapping("findByDepartment/{departmentName}/{countryName}")
    public ResponseEntity<List<EmergencyService>> findByDepartment(@PathVariable String departmentName,
                                                                   @PathVariable String countryName){
        try{
            return new ResponseEntity<>(
                    emergencyServiceService.findByDepartment(departmentName, countryName),
                    HttpStatus.OK);

        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando servicios de emergencia en departamento/provincia: " +
                            departmentName + " (" + countryName + ")");
        }
    }

}
