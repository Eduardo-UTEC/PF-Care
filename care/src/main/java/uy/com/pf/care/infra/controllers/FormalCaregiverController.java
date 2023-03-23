package uy.com.pf.care.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.FormalCaregiverSaveException;
import uy.com.pf.care.model.documents.FormalCaregiver;
import uy.com.pf.care.model.objects.FormalCaregiverIdObject;
import uy.com.pf.care.repos.IFormalCaregiverRepo;
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

    @GetMapping("findAll/{includeDeleted}/{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findAll(
            @PathVariable Boolean includeDeleted,
            @PathVariable String countryName){

        try{
            return new ResponseEntity<>(formalCaregiverService.findAll(includeDeleted, countryName), HttpStatus.OK);

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

    @GetMapping("findByMail/{mail}")
    public ResponseEntity<FormalCaregiver> findByMail(@PathVariable String mail) {
        try{
            return new ResponseEntity<>(formalCaregiverService.findWithIndex_Mail(mail), HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando por mail del cuidador formal (" + mail + ")");
        }
    }

    @GetMapping("findByName/{includeDeleted}/{name}/{telephone}/{departmentName}/{countryName}")
    public ResponseEntity<FormalCaregiver> findByName(
            @PathVariable Boolean includeDeleted,
            @PathVariable String name,
            @PathVariable String telephone,
            @PathVariable String departmentName,
            @PathVariable String countryName){
        try{
            return new ResponseEntity<>(
                    formalCaregiverService.findByName(includeDeleted, name, telephone, departmentName, countryName),
                    HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando el cuidador formal " + name + ", con telefono " + telephone +
                            " en departamento/provincia de " + departmentName + " (" + countryName + ")");
        }
    }

    @GetMapping("findByNameLike/{includeDeleted}/{name}/{departmentName}/{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findByNameLike(
            @PathVariable Boolean includeDeleted,
            @PathVariable String name,
            @PathVariable String departmentName,
            @PathVariable String countryName){
        try{
            return new ResponseEntity<>(
                    formalCaregiverService.findByNameLike(includeDeleted, name, departmentName, countryName),
                    HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidadores formales de nombre " + name + ", en departamento/provincia de " +
                            departmentName + " (" + countryName + ")");
        }
    }

    @GetMapping("findByAvailable/{departmentName}/{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findByAvailable(
            @PathVariable String departmentName,
            @PathVariable String countryName){
        try{
            return new ResponseEntity<>(
                    formalCaregiverService.findByAvailable(departmentName, countryName),
                    HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidadores formales disponibles en " + departmentName + " (" + countryName + ")");
        }
    }

    @GetMapping("findByDepartment/{includeDeleted}/{departmentName}/{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findByDepartment(
            @PathVariable Boolean includeDeleted,
            @PathVariable String departmentName,
            @PathVariable String countryName){
        try{
            return new ResponseEntity<>(
                    formalCaregiverService.findByDepartment(includeDeleted, departmentName, countryName),
                    HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidadores formales en departamento/provincia de " +
                            departmentName + " (" + countryName + ")");
        }
    }

    //  Devuelve true si la operación fue exitosa.
    @PatchMapping("setAvailability/{id}/{isAvailable}")
    public ResponseEntity<Boolean> setAvailability(@PathVariable String id, @PathVariable Boolean isAvailable) {
        try{
            return new ResponseEntity<>(formalCaregiverService.setAvailability(id, isAvailable), HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo setear la disponibilidad del cuidador formal con id " + id);
        }
    }

    // Devuelve true si la operación fue exitosa
    @PatchMapping("setDeletion/{id}/{isDeleted}")
    public ResponseEntity<Boolean> setDeletion(@PathVariable String id, @PathVariable Boolean isDeleted) {
        try{
            return new ResponseEntity<>(formalCaregiverService.setDeletion(id, isDeleted), HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo setear el borrado lógico del cuidador formal con id " + id);
        }
    }

    @GetMapping(
            "findByInterestZones_Neighborhood/" +
            "{includeDeleted}/" +
            "{interestNeighborhoodName}/" +
            "{interestCityName}/" +
            "{interestDepartmentName}/" +
            "{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findByInterestZones_Neighborhood(
            @PathVariable Boolean includeDeleted,
            @PathVariable String interestNeighborhoodName,
            @PathVariable String interestCityName,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName){
        try{
            return new ResponseEntity<>(
                    formalCaregiverService.findByInterestZones_Neighborhood(
                            includeDeleted,
                            interestNeighborhoodName,
                            interestCityName,
                            interestDepartmentName,
                            countryName),
                    HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidadores formales por zona de interés en barrio " + interestNeighborhoodName +
                            " (" + interestCityName+ ", " + interestDepartmentName + ", " + countryName + ")");
        }
    }
    @GetMapping("findByInterestZones_City/{includeDeleted}/{interestCityName}/{interestDepartmentName}/{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findByInterestZones_City(
            @PathVariable Boolean includeDeleted,
            @PathVariable String interestCityName,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName){
        try{
            return new ResponseEntity<>(
                    formalCaregiverService.findByInterestZones_City(
                            true, includeDeleted, interestCityName, interestDepartmentName, countryName),
                    HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidadores formales por zona de interés en ciudad " + interestCityName + " (" +
                            interestDepartmentName + ", " + countryName + ")");
        }
    }
    @GetMapping("findByInterestZones_Department/{includeDeleted}/{interestDepartmentName}/{countryName}")
    public ResponseEntity<List<FormalCaregiver>> findByInterestZones_Department(
            @PathVariable Boolean includeDeleted,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName){
        try{
            return new ResponseEntity<>(
                    formalCaregiverService.findByInterestZones_Department(
                            true, includeDeleted, interestDepartmentName, countryName),
                    HttpStatus.OK);

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando cuidadores formales por zona de interés en departamento/provincia de " +
                            interestDepartmentName + " (" + countryName + ")");
        }
    }

}
