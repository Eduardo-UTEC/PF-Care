package uy.com.pf.care.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public EmergencyServiceIdObject add(@RequestBody EmergencyService emergencyService){
        try{
            return new EmergencyServiceIdObject(emergencyServiceService.save(emergencyService).getEmergencyService_id());
        }catch (EmergencyServiceSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error guardando servicio de emergencia");
        }
    }

    @GetMapping("findAll/{countryName}")
    public List<EmergencyService> findAll(@PathVariable String countryName){
        try{
            return emergencyServiceService.findAll(countryName);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando todos los servicios de emergencia de " + countryName);
        }
    }

    @GetMapping("findId/{id}")
    public Optional<EmergencyService> findId( @PathVariable String id) {
        try{
            return emergencyServiceService.findId(id);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando servicio de emergencia con id " + id);
    }
    }

    @GetMapping("findByCity/{cityName}/{departmentName}/{countryName}")
    public List<EmergencyService> findByCity(@PathVariable String cityName,
                                             @PathVariable String departmentName,
                                             @PathVariable String countryName){
        try{
            return emergencyServiceService.findByCity(cityName, departmentName, countryName);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando servicios de emergencia en ciudad/localidad: " +
                            cityName + " (" + departmentName + ", " + countryName + ")");
        }
    }

    @GetMapping("findByDepartment/{departmentName}/{countryName}")
    public List<EmergencyService> findByDepartment(@PathVariable String departmentName,
                                                   @PathVariable String countryName){
        try{
            return emergencyServiceService.findByDepartment(departmentName, countryName);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando servicios de emergencia en departamento/provincia: " +
                            departmentName + " (" + countryName + ")");
        }
    }
}
