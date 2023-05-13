package uy.com.pf.care.infra.controllers;

import jakarta.validation.Valid;
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
    public ResponseEntity<ZoneIdObject> add(@Valid @RequestBody Zone zone){
        try{
            return ResponseEntity.ok(new ZoneIdObject(zoneService.save(zone).getZoneId()));

        }catch (ZoneSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error guardando zona");
        }
    }

    @GetMapping("findAll/{includeDeleted}/{countryName}")
    public ResponseEntity<List<Zone>> findAll(@PathVariable Boolean includeDeleted, @PathVariable String countryName){
        try{
            return ResponseEntity.ok(zoneService.findAll(includeDeleted, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando todos las zonas de " + countryName);
        }
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<Optional<Zone>> findId(@PathVariable String id) {
        try{
            return ResponseEntity.ok(zoneService.findId(id));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando zona con id " + id);
        }
    }

    @GetMapping("findNeighborhoods/{includeDeleted}/{cityName}/{departmentName}/{countryName}")
    public ResponseEntity<List<NeighborhoodObject>> findNeighborhood(@PathVariable Boolean includeDeleted,
                                                                     @PathVariable String cityName,
                                                                     @PathVariable String departmentName,
                                                                     @PathVariable String countryName){
        try{
            return ResponseEntity.ok(
                    zoneService.findNeighborhoods(includeDeleted, cityName, departmentName, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando los barrios de " + cityName + " (" + departmentName + ", " + countryName + ")");
        }
    }

    @GetMapping("findCities/{includeDeleted}/{departmentName}/{countryName}")
    public ResponseEntity<List<String>> findCities(@PathVariable Boolean includeDeleted,
                                                   @PathVariable String departmentName,
                                                   @PathVariable String countryName){
        try{
            return ResponseEntity.ok(zoneService.findCities(includeDeleted, departmentName, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando las cuidades/localidades de " + departmentName + " (" + countryName + ")");
        }
    }

    @GetMapping("findDepartments/{includeDeleted}/{countryName}")
    public ResponseEntity<List<String>> findDepartment(@PathVariable Boolean includeDeleted,
                                                          @PathVariable String countryName){
        try{
            return ResponseEntity.ok(zoneService.findDepartments(includeDeleted, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando los departamentos/provincias de " + countryName);
        }
    }

    @GetMapping("findCountries/{includeDeleted}")
    public ResponseEntity<List<String>> findCountries(@PathVariable Boolean includeDeleted){
        try{
            return ResponseEntity.ok(zoneService.findCountries(includeDeleted));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error buscando países");
        }
    }

    // Devuelve true si la operación fue exitosa
    @PostMapping("setDeletion/{id}/{isDeleted}")
    public ResponseEntity<Boolean> setDeletion(@PathVariable String id, @PathVariable Boolean isDeleted) {
        try{
            return ResponseEntity.ok(zoneService.setDeletion(id, isDeleted));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo setear el borrado lógico de la zona con id " + id);
        }
    }

}
