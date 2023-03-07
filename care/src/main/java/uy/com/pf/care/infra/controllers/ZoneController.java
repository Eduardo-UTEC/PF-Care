package uy.com.pf.care.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.PatientSaveException;
import uy.com.pf.care.exceptions.ZoneSaveException;
import uy.com.pf.care.model.documents.Zone;
import uy.com.pf.care.model.objects.NeighborhoodObject;
import uy.com.pf.care.model.objects.PatientIdObject;
import uy.com.pf.care.model.objects.ZoneIdObject;
import uy.com.pf.care.services.IZoneService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/zones")
public class ZoneController {

    @Autowired
    private IZoneService zoneService;

    @PostMapping("/add")
    public ZoneIdObject add(@RequestBody uy.com.pf.care.model.documents.Zone zone){
        try{
            return new ZoneIdObject(zoneService.save(zone).getZone_id());

        }catch (ZoneSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error guardando zona");
        }
    }

    @GetMapping("findAll/{countryName}")
    public List<Zone> findAll(@PathVariable String countryName){
        try{
            return zoneService.findByCountry(countryName);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando todos las zonas de " + countryName);
        }
    }

    @GetMapping("findId/{id}")
    public Optional<uy.com.pf.care.model.documents.Zone> findId(@PathVariable String id) {
        try{
            return zoneService.findId(id);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando zona con id " + id);
        }
    }

    @GetMapping("findAllNeighborhoods/{cityName}/{departmentName}/{countryName}")
    public List<NeighborhoodObject> findAllNeighborhood(@PathVariable String cityName,
                                            @PathVariable String departmentName,
                                            @PathVariable String countryName){
        try{
            return zoneService.findAllNeighborhoods(cityName, departmentName, countryName);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando los barrios de " + cityName + " (" + departmentName + ", " + countryName + ")");
        }
    }

    @GetMapping("findAllCities/{departmentName}/{countryName}")
    public List<String> findAllCities(@PathVariable String departmentName, @PathVariable String countryName){
        try{
            return zoneService.findAllCities(departmentName, countryName);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando las cuidades/localidades de " + departmentName + " (" + countryName + ")");
        }

    }

    @GetMapping("findAllDepartments/{countryName}")
    public List<String> findAllDepartment(@PathVariable String countryName){
        try{
            return zoneService.findAllDepartments(countryName);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando los departamentos/provincias de " + countryName);
        }
    }

    @GetMapping("findAllCountries")
    public List<String> findAllCountries(){
        try{
            return zoneService.findAllCountries();

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error buscando países");
        }

    }

}
