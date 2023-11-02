package uy.com.pf.care.infra.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.model.documents.VolunteerPerson;
import uy.com.pf.care.model.objects.ParamsDayTimeRangeAndExcludedIdsObject;
import uy.com.pf.care.services.IVolunteerPersonService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/volunteer_people")
@Log
public class VolunteerPersonController {

    @Autowired
    private IVolunteerPersonService volunteerPersonService;

    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @NotNull @RequestBody VolunteerPerson volunteerPerson){
        try {
            return ResponseEntity.ok(volunteerPersonService.save(volunteerPerson));

        } catch (DuplicateKeyException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UserUpdateEntityIdInRolesListException | VolunteerPersonSaveException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@Valid @NotNull @RequestBody VolunteerPerson newVolunteerPerson){
        try {
            return ResponseEntity.ok(volunteerPersonService.update(newVolunteerPerson));

        }catch (VolunteerPersonNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (VolunteerActivityDuplicateKeyException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(VolunteerPersonUpdateException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/receivePatientRequest/{volunteerPersonId}/{patientId}")
    public ResponseEntity<Boolean> receivePatientRequests(
            @PathVariable String volunteerPersonId, @PathVariable String patientId){
        try {
            return ResponseEntity.ok(volunteerPersonService.receivePatientRequest(volunteerPersonId, patientId));

        }catch (VolunteerPersonPatientAlreadyMatchException | VolunteerPersonPatientAlreadyRequestException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch (VolunteerPersonNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(VolunteerPersonRecievePatientRequestException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    ///El paciente envía una solicitud de contacto. El voluntario la acepta o rechaza en este servicio.
    @PutMapping("/setMatchPatient/{volunteerPersonId}/{patientId}/{isMatch}")
    public ResponseEntity<Boolean> setMatchPatient(
            @PathVariable String volunteerPersonId, @PathVariable String patientId, @PathVariable Boolean isMatch) {
        try {
            return ResponseEntity.ok(volunteerPersonService.setMatchPatient(volunteerPersonId, patientId, isMatch));

        }catch (VolunteerPersonPatientAlreadyMatchException | VolunteerPersonPatientNotRequestException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch (VolunteerPersonNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(VolunteerPersonRecievePatientRequestException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/addActivities/{volunteerPersonId}")
    public ResponseEntity<Boolean> addVolunteerActivitiesId(
            @PathVariable String volunteerPersonId,
            @Valid @NotNull @RequestBody List<String> volunteerActivitiesId){
        try {
            return ResponseEntity.ok(volunteerPersonService.addVolunteerActivitiesId(
                    volunteerPersonId, volunteerActivitiesId));

        }catch (VolunteerPersonNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(VolunteerPersonAddVolunteerActivitiesIdException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/changeActivity/{volunteerPersonId}/{oldVolunteerActivityId}/{newVolunteerActivityId}")
    public ResponseEntity<Boolean> changeActivity(
            @PathVariable String volunteerPersonId,
            @PathVariable String oldVolunteerActivityId,
            @PathVariable String newVolunteerActivityId) {

        try {
            return ResponseEntity.ok(volunteerPersonService.changeVolunteerActivityId(
                    volunteerPersonId, oldVolunteerActivityId, newVolunteerActivityId));

        }catch (VolunteerPersonActivityAlreadyLinkedException | VolunteerPersonActivityNotLinkedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch (VolunteerPersonNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(VolunteerPersonChangeActivityException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/delActivities/{volunteerPersonId}")
    public ResponseEntity<Boolean> delActivitiesId(
            @PathVariable String volunteerPersonId,
            @Valid @NotNull @RequestBody List<String> volunteerActivitiesId){
        try {
            return ResponseEntity.ok(volunteerPersonService.delVolunteerActivitiesId(
                    volunteerPersonId, volunteerActivitiesId));

        }catch (VolunteerPersonNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(VolunteerPersonDelActivitiesException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(
            value = "findAll/{withoutValidate}/{includeDeleted}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<VolunteerPerson>> findAll(
            @PathVariable Boolean withoutValidate,
            @PathVariable Boolean includeDeleted,
            @PathVariable String countryName){

        try {
            return ResponseEntity.ok(volunteerPersonService.findAll(withoutValidate, includeDeleted, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "findId/{id}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Optional<VolunteerPerson>> findId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(volunteerPersonService.findId(id));

        }catch(VolunteerPersonNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "findIds", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<VolunteerPerson>> findIds(
            @Valid @NotNull @RequestBody List<String> volunteersPersonId) {
        try {
            return ResponseEntity.ok(volunteerPersonService.findIds(volunteersPersonId));

        }catch(Exception e) {
            String msg = "Error buscando personas voluntarias por id";
            log.warning(msg + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping(
            value = "findIdentificationNumber/{identificationNumber}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<VolunteerPerson> findIdentificationNumber(
            @PathVariable String identificationNumber,
            @PathVariable String countryName) {
        try {
            return ResponseEntity.ok(volunteerPersonService.findIdentificationNumber(identificationNumber, countryName));

        }catch(VolunteerPersonNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "findTelephone/{telephone}/{countryName}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public Optional<VolunteerPerson> findTelephone(
            @PathVariable String telephone, @PathVariable String countryName) {
        try {
            Optional<VolunteerPerson> found = volunteerPersonService.findTelephone(countryName, telephone);
            if (found.isPresent())
                return found;

            String msg = "No se encontro un voluntario con teléfono " + telephone;
            log.warning(msg);
            throw new VolunteerPersonNotFoundException(msg);

        } catch (VolunteerPersonNotFoundException e) {
            throw new VolunteerPersonNotFoundException(e.getMessage());
        } catch(Exception e) {
            log.warning("Error buscando voluntario con teléfono: " + telephone + ". " + e.getMessage());
            throw new VolunteerPersonFindTelephoneException("Error buscando cuidador formal con teléfono: " + telephone);
        }
    }

    @GetMapping(value = "existTelephone/{telephone}/{countryName}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Boolean> existTelephone(
            @PathVariable String telephone, @PathVariable String countryName) {
        try{
            return ResponseEntity.ok(volunteerPersonService.findTelephone(countryName, telephone).isPresent());

        } catch (VolunteerPersonNotFoundException e) {
            throw new VolunteerPersonNotFoundException(e.getMessage());
        } catch(Exception e) {
            log.warning("Error buscando voluntario con teléfono: " + telephone + ". " + e.getMessage());
            throw new VolunteerPersonFindTelephoneException("Error buscando cuidador formal con teléfono: " + telephone);
        }
    }

    @GetMapping(value = "findMail/{mail}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<VolunteerPerson> findMail(@PathVariable String mail) {
        try{
            return ResponseEntity.ok(volunteerPersonService.findMail(mail));

        }catch(VolunteerPersonNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "existMail/{mail}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Boolean> existMail(@PathVariable String mail) {
        try{
            return ResponseEntity.ok(volunteerPersonService.findMail(mail) != null);

        }catch(VolunteerPersonNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(
            value = "findName/{name}/{withoutValidate}/{includeDeleted}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<VolunteerPerson>> findName(
            @PathVariable String name,
            @PathVariable Boolean withoutValidate,
            @PathVariable Boolean includeDeleted,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(volunteerPersonService.findName(
                    withoutValidate, includeDeleted, countryName, name));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(
            value = "findNameLike/{withoutValidate}/{includeDeleted}/{name}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<VolunteerPerson>> findNameLike(
            @PathVariable Boolean withoutValidate,
            @PathVariable Boolean includeDeleted,
            @PathVariable String name,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(volunteerPersonService.findNameLike(
                    withoutValidate, includeDeleted, countryName, name));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    //  Devuelve true si la operación fue exitosa.
    @PutMapping("setAvailability/{id}/{isAvailable}")
    public ResponseEntity<Boolean> setAvailability(@PathVariable String id, @PathVariable Boolean isAvailable) {
        try {
            return ResponseEntity.ok(volunteerPersonService.setAvailability(id, isAvailable));

        }catch(VolunteerPersonNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(VolunteerPersonSetAvailabilityException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // Devuelve true si la operación fue exitosa
    @PutMapping("setValidation/{id}/{isValidated}")
    public ResponseEntity<Boolean> setValidation(@PathVariable String id, @PathVariable Boolean isValidated) {
        try {
            return ResponseEntity.ok(volunteerPersonService.setValidation(id, isValidated));

        }catch (VolunteerPersonNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(VolunteerPersonSetValidationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // Devuelve true si la operación fue exitosa
    @PutMapping("setDeletion/{id}/{isDeleted}")
    public ResponseEntity<Boolean> setDeletion(@PathVariable String id, @PathVariable Boolean isDeleted) {
        try{
            return ResponseEntity.ok(volunteerPersonService.setDeletion(id, isDeleted));

        }catch(VolunteerPersonNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(VolunteerPersonSetDeletionException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(
            value = "findInterestZones_Neighborhood/" +
                    "{withoutValidate}/" +
                    "{includeDeleted}/" +
                    "{interestNeighborhoodName}/" +
                    "{interestCityName}/" +
                    "{interestDepartmentName}/" +
                    "{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<VolunteerPerson>> findInterestZones_Neighborhood(
            @PathVariable Boolean withoutValidate,
            @PathVariable Boolean includeDeleted,
            @PathVariable String interestNeighborhoodName,
            @PathVariable String interestCityName,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName,
            @RequestBody(required = false) List<String> excludedVolunteerIds) {

        try{
            return ResponseEntity.ok(volunteerPersonService.findInterestZones_Neighborhood(
                    withoutValidate,
                    includeDeleted,
                    interestNeighborhoodName,
                    interestCityName,
                    interestDepartmentName,
                    countryName,
                    excludedVolunteerIds
                    ));

        }catch(VolunteerPersonFindInterestZones_NeighborhoodException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    @GetMapping(value = "findInterestZones_City/" +
                "{withoutValidate}/" +
                "{includeDeleted}/" +
                "{interestCityName}/" +
                "{interestDepartmentName}/" +
                "{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<VolunteerPerson>> findInterestZones_City(
            @PathVariable Boolean withoutValidate,
            @PathVariable Boolean includeDeleted,
            @PathVariable String interestCityName,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(volunteerPersonService.findInterestZones_City(
                    true,
                    withoutValidate,
                    includeDeleted,
                    interestCityName,
                    interestDepartmentName,
                    countryName));

        }catch(VolunteerPersonFindInterestZones_CityException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    @GetMapping(
            value = "findInterestZones_Department/" +
                    "{withoutValidate}/" +
                    "{includeDeleted}/" +
                    "{interestDepartmentName}/" +
                    "{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<VolunteerPerson>> findInterestZones_Department(
            @PathVariable Boolean withoutValidate,
            @PathVariable Boolean includeDeleted,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(volunteerPersonService.findInterestZones_Department(
                    true, withoutValidate, includeDeleted, interestDepartmentName, countryName));

        }catch(FormalCaregiverFindInterestZones_DepartmentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(
            value = "findDateTimeRange/" +
                    "{interestNeighborhoodName}/" +
                    "{interestCityName}/" +
                    "{interestDepartmentName}/" +
                    "{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<VolunteerPerson>> findDateTimeRange(
            @PathVariable String interestNeighborhoodName,
            @PathVariable String interestCityName,
            @PathVariable String interestDepartmentName,
            @PathVariable String countryName,
            //@Valid @NotNull @RequestBody List<DayTimeRangeObject> dayTimeRange,
            //@RequestBody(required = false) List<String> excludedVolunteerIds){
            @RequestBody ParamsDayTimeRangeAndExcludedIdsObject lists) { //Se agrega objeto para aceptar ambas listas en el body

        try{
            return ResponseEntity.ok(volunteerPersonService.findDateTimeRange(
                    lists.getDayTimeRange(),
                    interestNeighborhoodName,
                    interestCityName,
                    interestDepartmentName,
                    countryName,
                    lists.getExcludedVolunteerIds()));

            /*return ResponseEntity.ok(volunteerPersonService.findDateTimeRange(
                    dayTimeRange,
                    interestNeighborhoodName,
                    interestCityName,
                    interestDepartmentName,
                    countryName,
                    excludedVolunteerIds));

             */

        }catch(VolunteerPersonFindDateTimeRangeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    ///Devuelve true si el voluntario esta validado y no esta borrado
    @GetMapping(value = "isValidated_notDeleted/{id}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Boolean> isValidated(@PathVariable String id) {
        try{
            Optional<VolunteerPerson> found = volunteerPersonService.findId(id);
            if (found.isPresent())
                return ResponseEntity.ok(found.get().getValidate() && ! found.get().getDeleted());
            return ResponseEntity.ok(false);

        }catch(Exception e) {
            String msg = "Error buscando persona voluntaria con id " + id;
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

}
