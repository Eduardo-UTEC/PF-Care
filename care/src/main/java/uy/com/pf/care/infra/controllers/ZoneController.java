package uy.com.pf.care.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.ZoneSaveException;
import uy.com.pf.care.model.documents.Zone;
import uy.com.pf.care.model.objects.NeighborhoodObject;
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
    public ResponseEntity<ZoneIdObject> add(@RequestBody Zone zone){
        try{
            return new ResponseEntity<>(
                    new ZoneIdObject(zoneService.save(zone).getZone_id()),
                    HttpStatus.OK);

        }catch (ZoneSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error guardando zona");
        }
    }

    @GetMapping("findAll/{countryName}")
    public ResponseEntity<List<Zone>> findAll(@PathVariable String countryName){
        try{
            return new ResponseEntity<>(zoneService.findByCountry(countryName), HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando todos las zonas de " + countryName);
        }
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<Optional<Zone>> findId(@PathVariable String id) {
        try{
            return new ResponseEntity<>(zoneService.findId(id), HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando zona con id " + id);
        }
    }

    @GetMapping("findAllNeighborhoods/{cityName}/{departmentName}/{countryName}")
    public ResponseEntity<List<NeighborhoodObject>> findAllNeighborhood(@PathVariable String cityName,
                                                                        @PathVariable String departmentName,
                                                                        @PathVariable String countryName){
        try{
            return new ResponseEntity<>(
                    zoneService.findAllNeighborhoods(cityName, departmentName, countryName),
                    HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando los barrios de " + cityName + " (" + departmentName + ", " + countryName + ")");
        }
    }

    @GetMapping("findAllCities/{departmentName}/{countryName}")
    public ResponseEntity<List<String>> findAllCities(@PathVariable String departmentName,
                                                      @PathVariable String countryName){
        try{
            return new ResponseEntity<>(zoneService.findAllCities(departmentName, countryName), HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando las cuidades/localidades de " + departmentName + " (" + countryName + ")");
        }
    }

    @GetMapping("findAllDepartments/{countryName}")
    public ResponseEntity<List<String>> findAllDepartment(@PathVariable String countryName){
        try{
            return new ResponseEntity<>(zoneService.findAllDepartments(countryName), HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando los departamentos/provincias de " + countryName);
        }
    }

    @GetMapping("findAllCountries")
    public ResponseEntity<List<String>> findAllCountries(){
        try{
            return new ResponseEntity<>(zoneService.findAllCountries(), HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error buscando países");
        }
    }

}
