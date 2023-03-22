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

    @GetMapping("findAllZones/{includeDeleted}/{countryName}")
    public ResponseEntity<List<Zone>> findAllZones(@PathVariable Boolean includeDeleted,
                                                @PathVariable String countryName){
        try{
            return new ResponseEntity<>(zoneService.findAllZones(includeDeleted, countryName), HttpStatus.OK);

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

    @GetMapping("findAllNeighborhoods/{includeDeleted}/{cityName}/{departmentName}/{countryName}")
    public ResponseEntity<List<NeighborhoodObject>> findAllNeighborhood(@PathVariable Boolean includeDeleted,
                                                                        @PathVariable String cityName,
                                                                        @PathVariable String departmentName,
                                                                        @PathVariable String countryName){
        try{
            return new ResponseEntity<>(
                    zoneService.findAllNeighborhoods(includeDeleted, cityName, departmentName, countryName),
                    HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando los barrios de " + cityName + " (" + departmentName + ", " + countryName + ")");
        }
    }

    @GetMapping("findAllCities/{includeDeleted}/{departmentName}/{countryName}")
    public ResponseEntity<List<String>> findAllCities(@PathVariable Boolean includeDeleted,
                                                      @PathVariable String departmentName,
                                                      @PathVariable String countryName){
        try{
            return new ResponseEntity<>(
                    zoneService.findAllCities(includeDeleted, departmentName, countryName),
                    HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando las cuidades/localidades de " + departmentName + " (" + countryName + ")");
        }
    }

    @GetMapping("findAllDepartments/{includeDeleted}/{countryName}")
    public ResponseEntity<List<String>> findAllDepartment(@PathVariable Boolean includeDeleted,
                                                          @PathVariable String countryName){
        try{
            return new ResponseEntity<>(zoneService.findAllDepartments(includeDeleted, countryName), HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando los departamentos/provincias de " + countryName);
        }
    }

    @GetMapping("findAllCountries/{includeDeleted}")
    public ResponseEntity<List<String>> findAllCountries(@PathVariable Boolean includeDeleted){
        try{
            return new ResponseEntity<>(zoneService.findAllCountries(includeDeleted), HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error buscando países");
        }
    }

}
