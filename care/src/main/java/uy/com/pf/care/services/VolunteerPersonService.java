package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.infra.config.ParamConfig;
import uy.com.pf.care.model.documents.VolunteerPerson;
import uy.com.pf.care.model.enums.RoleEnum;
import uy.com.pf.care.model.globalFunctions.ForceEnumsToVolunteerPerson;
import uy.com.pf.care.model.globalFunctions.UpdateEntityId;
import uy.com.pf.care.model.objects.DayTimeRangeObject;
import uy.com.pf.care.model.objects.NeighborhoodObject;
import uy.com.pf.care.infra.repos.IVolunteerPersonRepo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class VolunteerPersonService implements IVolunteerPersonService{
    @Autowired
    private IVolunteerPersonRepo volunteerPersonRepo;
    @Autowired
    private UpdateEntityId updateEntityId;
    @Autowired
    private ParamConfig paramConfig;

    @Override
    public String save(VolunteerPerson volunteerPerson) {
        String newVolunteerPersonId = null;
        try {
            this.defaultValues(volunteerPerson);
            ForceEnumsToVolunteerPerson.execute(volunteerPerson);
            newVolunteerPersonId = volunteerPersonRepo.save(volunteerPerson).getVolunteerPersonId();

            ResponseEntity<Boolean> response = updateEntityId.execute(
                    volunteerPerson.getUserId(), RoleEnum.VOLUNTEER_PERSON.getOrdinal(), newVolunteerPersonId);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("*** Persona Voluntaria guardada con exito");
                return newVolunteerPersonId;
            }

            String msg = "Error actualizando entityId en el rol 'Persona Voluntaria', en documento Users";
            log.warning(msg + ": " + response);
            throw new UserUpdateEntityIdInRolesListException(msg);

            //En caso de excepción, si el nuevo Voluntario fue persistido, se elimina, evitando inconsistencia de la bbdd
        } catch (UserUpdateEntityIdInRolesListException e) {
            if (newVolunteerPersonId != null)
                this.physicallyDeleteVolunteerPerson(newVolunteerPersonId);
            throw new UserUpdateEntityIdInRolesListException(e.getMessage());

        } catch (DuplicateKeyException e){
            String msg = "Error guardando Persona Voluntaria (clave duplicada)";
            log.warning(msg + ": " + e.getMessage());
            throw new VolunteerPersonDuplicateKeyException(msg);

        } catch (Exception e) {
            String msg = "*** ERROR GUARDANDO PERSONA VOLUNTARIA";
            log.warning(msg + ": " + e.getMessage());
            throw new VolunteerPersonSaveException(msg);
        }
    }

    @Override
    public Boolean update(VolunteerPerson newVolunteerPerson) {
        try {
            Optional<VolunteerPerson> entityFound = volunteerPersonRepo.findById(newVolunteerPerson.getVolunteerPersonId());
            if (entityFound.isPresent()) {
                this.defaultValues(newVolunteerPerson, entityFound.get());
                ForceEnumsToVolunteerPerson.execute(newVolunteerPerson);
                volunteerPersonRepo.save(newVolunteerPerson);
                log.info("Persona Voluntaria actualizada con exito");
                return true;
            }
            String msg = "No se encontro la Persona Voluntaria con id " + newVolunteerPerson.getVolunteerPersonId();
            log.info(msg);
            throw new VolunteerPersonNotFoundException(msg);

        } catch (VolunteerPersonNotFoundException e) {
            throw new VolunteerPersonNotFoundException(e.getMessage());
        } catch (DuplicateKeyException e){
            throw new VolunteerPersonDuplicateKeyException(e.getMessage());
        } catch(Exception e){
            String msg = "*** ERROR ACTUALIZANDO PERSONA VOLUNTARIA";
            log.warning(msg + ": " + e.getMessage());
            throw new VolunteerPersonUpdateException(msg);
        }
    }

    @Override
    public Optional<VolunteerPerson> findId(String id) {
        Optional<VolunteerPerson> found = volunteerPersonRepo.findById(id);
        if (found.isPresent())
            return found;

        String msg = "No se encontro la Persona Voluntaria con id " + id;
        log.info(msg);
        throw new VolunteerPersonNotFoundException(msg);
    }

    @Override
    public VolunteerPerson findIdentificationNumber(String identificationDocument, String countryName) {
        Optional<VolunteerPerson> found = volunteerPersonRepo.
                findByCountryNameAndIdentificationDocument(identificationDocument, countryName);
        if (found.isPresent())
            return found.get();

        String msg = "No se encontro la Persona Voluntaria con documento: " + identificationDocument;
        log.info(msg);
        throw new VolunteerPersonNotFoundException(msg);
    }

    @Override
    public List<VolunteerPerson> findAll(Boolean withoutValidate, Boolean includeDeleted, String countryName) {
        /*if (includeDeleted)
            return volunteerPersonRepo.findByCountryName(countryName);
        return volunteerPersonRepo.findByCountryNameAndDeletedFalse(countryName);

         */

        if (withoutValidate) {
            if (includeDeleted)
                return volunteerPersonRepo.findByCountryNameAndValidateFalseOrderByName1(countryName);
            return volunteerPersonRepo.findByCountryNameAndValidateFalseAndDeletedFalseOrderByName1(countryName);

        }else {
            if (includeDeleted)
                return volunteerPersonRepo.findByCountryNameAndValidateTrueOrderByName1(countryName);
            return volunteerPersonRepo.findByCountryNameAndValidateTrueAndDeletedFalseOrderByName1(countryName);
        }
    }

    @Override
    public VolunteerPerson findMail(String mail) {
        Optional<VolunteerPerson> found = volunteerPersonRepo.findByMailIgnoreCase(mail);
        if (found.isPresent())
            return found.get();

        String msg = "No se encontro la Persona Voluntaria con mail: " + mail;
        log.info(msg);
        throw new VolunteerPersonNotFoundException(msg);
    }

    @Override
    public List<VolunteerPerson> findName(
            Boolean withoutValidate, Boolean includeDeleted, String countryName, String name1) {
        /*if (includeDeleted)
            return volunteerPersonRepo.findByCountryNameAndName1IgnoreCase(countryName, name1);
        return volunteerPersonRepo.findByCountryNameAndName1IgnoreCaseAndDeletedFalse(countryName, name1);
         */

        if (withoutValidate) {
            if (includeDeleted)
                return volunteerPersonRepo.findByCountryNameAndValidateFalseAndName1IgnoreCase(countryName, name1);
            return volunteerPersonRepo.findByCountryNameAndValidateFalseAndName1IgnoreCaseAndDeletedFalse(
                    countryName, name1);

        }else{
            if (includeDeleted)
                return volunteerPersonRepo.findByCountryNameAndValidateTrueAndName1IgnoreCase(countryName, name1);
            return volunteerPersonRepo.findByCountryNameAndValidateTrueAndName1IgnoreCaseAndDeletedFalse(
                    countryName, name1);
        }
    }

    @Override
    public List<VolunteerPerson> findNameLike(
            Boolean withoutValidate, Boolean includeDeleted, String countryName, String name1) {

        if (withoutValidate) {
            if (includeDeleted)
                return volunteerPersonRepo.findByCountryNameAndValidateFalseAndName1LikeIgnoreCase(countryName, name1);
            return volunteerPersonRepo.findByCountryNameAndValidateFalseAndName1LikeIgnoreCaseAndDeletedFalse(
                    countryName, name1);

        }else{
            if (includeDeleted)
                return volunteerPersonRepo.findByCountryNameAndValidateTrueAndName1LikeIgnoreCase(countryName, name1);
            return volunteerPersonRepo.findByCountryNameAndValidateTrueAndName1LikeIgnoreCaseAndDeletedFalse(
                    countryName, name1);
        }
    }

    @Override
    public List<VolunteerPerson> findInterestZones_Neighborhood(
            Boolean withoutValidate,
            Boolean includeDeleted,
            String interestNeighborhoodName,
            String interestCityName,
            String interestDepartmentName,
            String countryName) {

        try{
            RestTemplate restTemplate = new RestTemplate();
            NeighborhoodObject[] neighborhoods = restTemplate.getForEntity(
                    getUrlNeighborhoods(includeDeleted, interestCityName, interestDepartmentName, countryName),
                    NeighborhoodObject[].class).getBody();

            List<VolunteerPerson> listReturn = new ArrayList<>();

            // Verifico que el barrio exista
            if (neighborhoods != null &&
                    neighborhoods.length > 0 &&
                    Arrays.stream(neighborhoods).anyMatch(neighborhoodObject ->
                            neighborhoodObject.getNeighborhoodName().equals(interestNeighborhoodName))){

                // TODO: Testear cual de los dos filtros previos es el mas eficiente (tomando por ciudad o findAll)
                //listReturn = this.findInterestZones_City(
                //        false, includeDeleted, interestCityName, interestDepartmentName, countryName)
                //        .stream().filter(
                listReturn = this.findAll(withoutValidate, includeDeleted, countryName).stream().filter(
                        volunteerPerson -> volunteerPerson.getInterestZones().isEmpty() ||
                                !volunteerPerson.getInterestZones().stream().filter(interestZonesObject ->
                                        interestZonesObject.getDepartmentName().equals(interestDepartmentName) &&
                                                (interestZonesObject.getCities().isEmpty() ||
                                                        !interestZonesObject.getCities().stream().filter(cityObject ->
                                                                cityObject.getCityName().equals(interestCityName) &&
                                                                        (cityObject.getNeighborhoodNames().isEmpty() ||
                                                                                cityObject.getNeighborhoodNames().
                                                                                        contains(interestNeighborhoodName))
                                                        ).toList().isEmpty())
                                ).toList().isEmpty()
                ).toList();
            }
            return listReturn;

        }catch(Exception e){
            String msg = "Error buscando personas voluntarias por zona de interés en barrio: "
                    + interestNeighborhoodName + " (" + interestCityName+ ", " + interestDepartmentName + ", "
                    + countryName + ")";
            log.warning( msg + ": " + e.getMessage());
            throw new VolunteerPersonFindInterestZones_NeighborhoodException(msg);
        }

    }

    @Override
    public List<VolunteerPerson> findInterestZones_City(
            Boolean validateCity,
            Boolean withoutValidate,
            Boolean includeDeleted,
            String interestCityName,
            String interestDepartmentName,
            String countryName) {

        try{
            List<VolunteerPerson> listReturn = new ArrayList<>();
            RestTemplate restTemplate = new RestTemplate();
            String[] cities = null;

            if (validateCity)
                cities = restTemplate.getForEntity(
                                getUrlCities(includeDeleted, interestDepartmentName, countryName), String[].class)
                        .getBody();

            // Si se desea validar la ciudad, verifico que la ciudad exista
            if (!validateCity ||
                    (cities != null && cities.length > 0 && Arrays.asList(cities).contains(interestCityName))){

                // TODO: Testear cual de los dos filtros previos es el mas eficiente (tomando por departamento o findAll)
                //listReturn = this.findInterestZones_Department(
                //       false, includeDeleted, interestDepartmentName, countryName).stream().filter(
                listReturn = this.findAll(withoutValidate, includeDeleted, countryName).stream().filter(
                        volunteerPerson -> volunteerPerson.getInterestZones().isEmpty() ||
                                !volunteerPerson.getInterestZones().stream().filter(interestZonesObject ->
                                        interestZonesObject.getDepartmentName().equals(interestDepartmentName) &&
                                                (interestZonesObject.getCities().isEmpty() ||
                                                        !interestZonesObject.getCities().stream().filter( cityObject ->
                                                                cityObject.getCityName().equals(interestCityName)
                                                        ).toList().isEmpty())
                                ).toList().isEmpty()
                ).toList();
            }
            return listReturn;

        }catch(Exception e){
            log.warning("Error buscando personas voluntarias por zona de interés en ciudad: " + interestCityName +
                    " (" + interestDepartmentName + ", " + countryName + ")" + ". " + e.getMessage());
            throw new VolunteerPersonFindInterestZones_CityException(
                    "Error buscando personas voluntarias por zona de interés en ciudad " + interestCityName + " (" +
                            interestDepartmentName + ", " + countryName + ")");
        }
    }

    @Override
    public List<VolunteerPerson> findInterestZones_Department(
            Boolean validateInterestDepartment,
            Boolean withoutValidate,
            Boolean includeDeleted,
            String interestDepartmentName,
            String countryName) {

        try{
            RestTemplate restTemplate = new RestTemplate();
            String[] departments = null;
            List<VolunteerPerson> listReturn = new ArrayList<>();

            if (validateInterestDepartment)
                departments = restTemplate.
                        getForEntity(getUrlDepartments(includeDeleted, countryName), String[].class).getBody();

            // Si se desea validar el Departamento de Interes, verifico que el departamento exista
            if (!validateInterestDepartment ||
                    (departments != null &&
                            departments.length > 0 &&
                            Arrays.asList(departments).contains(interestDepartmentName))){

                listReturn = this.findAll(withoutValidate, includeDeleted, countryName)
                        .stream().filter(volunteerPerson ->
                                volunteerPerson.getInterestZones().isEmpty() ||
                                        ! volunteerPerson.getInterestZones().stream().filter(interestZonesObject ->
                                                interestZonesObject.getDepartmentName().equals(interestDepartmentName)
                                        ).toList().isEmpty())
                        .toList();
            }
            return listReturn;

        }catch(Exception e){
            String msg = "Error buscando personas voluntarias por zona de interés en departamento/provincia: " +
                    interestDepartmentName + " (" + countryName + ")" + ". " + e.getMessage();
            log.warning(msg + ": " + e.getMessage());
            throw new FormalCaregiverFindInterestZones_DepartmentException(msg);
        }
    }

    @Override
    public List<VolunteerPerson> findDateTimeRange(
            List<DayTimeRangeObject> dayTimeRange,
            String interestNeighborhoodName,
            String interestCityName,
            String interestDepartmentName,
            String countryName) {

        try{
            List<VolunteerPerson> volunteerPersonList;

            if (interestNeighborhoodName.isEmpty())
                volunteerPersonList = this.findInterestZones_City(
                        true,
                        false,
                        false,
                        interestCityName,
                        interestDepartmentName,
                        countryName);
            else
                volunteerPersonList = this.findInterestZones_Neighborhood(
                        false,
                        false,
                        interestNeighborhoodName,
                        interestCityName,
                        interestDepartmentName,
                        countryName);

            if (dayTimeRange.isEmpty()) // Todos los dias y horarios
                return volunteerPersonList;

            return volunteerPersonList.stream().filter(volunteerPerson -> {
                if (volunteerPerson.getDayTimeRange().isEmpty())
                    return true;
                return volunteerPerson.getDayTimeRange().stream().anyMatch(volunteerPersonRange ->
                        dayTimeRange.stream().anyMatch(searchRange -> {
                            if (volunteerPersonRange.getDay().ordinal() ==  searchRange.getDay().ordinal()){
                                if (volunteerPersonRange.getTimeRange().isEmpty())
                                    return true;
                                return volunteerPersonRange.getTimeRange().stream().anyMatch(VolunteerPersonSubRange ->
                                        searchRange.getTimeRange().stream().anyMatch(searchSubRange ->
                                                (VolunteerPersonSubRange.
                                                        getStartTime().isBefore(searchSubRange.getStartTime()) ||
                                                        VolunteerPersonSubRange.getStartTime().equals(searchSubRange.
                                                                getStartTime()))
                                                        &&
                                                        (VolunteerPersonSubRange.getEndTime().isAfter(searchSubRange.
                                                                getEndTime()) ||
                                                                VolunteerPersonSubRange.getEndTime().
                                                                        equals(searchSubRange.getEndTime()))
                                        ));
                            }
                            return false;
                        }));
            }).toList();

        }catch(Exception e){
            String msg = "Error buscando personas voluntarias por rango de dias/horas";
            log.warning(msg + ": " + e.getMessage());
            throw new VolunteerPersonFindDateTimeRangeException(msg);
        }
    }

    @Override
    public Boolean setAvailability(String id, Boolean isAvailable) {
        try{
            Optional<VolunteerPerson> volunteerPerson = this.findId(id);
            if (volunteerPerson.isPresent() && ! volunteerPerson.get().getDeleted()) {
                volunteerPerson.get().setAvailable(isAvailable);
                volunteerPersonRepo.save(volunteerPerson.get());
                return true;
            }
            return false;

        }catch(Exception e){
            log.warning("No se pudo setear la disponibilidad de la persona voluntaria con id: " + id + ". "
                    + e.getMessage());
            throw new VolunteerPersonSetAvailabilityException(
                    "No se pudo setear la disponibilidad de la persona voluntaria con id: " + id + ". ");
        }
    }

    @Override
    public Boolean setValidation(String id, Boolean isValidated) {
        Optional<VolunteerPerson> volunteerPersonFound = this.findId(id);
        if (volunteerPersonFound.isPresent()) {
            volunteerPersonFound.get().setValidate(isValidated);
            volunteerPersonRepo.save(volunteerPersonFound.get());
            return true;
        }
        return false;
    }

    /*  Devuelve true si la operación fue exitosa.
        1. Esta es una tarea del "administrador del sistema"
        2. La "disponiblidad" del voluntario pasa a false, sin importar si se pasa a borrado o no borrado.
            Ello posibilita que el administrador sea quien recupere a un voluntario, y luego sea este quien se
            disponibilice.
     */
    @Override
    public Boolean setDeletion(String id, Boolean isDeleted) {
        try {
            Optional<VolunteerPerson> volunteerPerson = this.findId(id);
            if (volunteerPerson.isPresent()) {
                volunteerPerson.get().setDeleted(isDeleted);
                volunteerPerson.get().setAvailable(false);
                volunteerPersonRepo.save(volunteerPerson.get());
                return true;
            }
            return false;

        }catch(Exception e){
            log.warning("No se pudo setear el borrado lógico de la persona voluntaria con id: " + id + ". "
                    + e.getMessage());
            throw new VolunteerPersonSetDeletionException(
                    "No se pudo setear el borrado lógico de la persona voluntaria con id " + id);
        }
    }

    private String getUrlNeighborhoods(
            Boolean includeDeleted, String cityName, String departmentName, String countryName) {

        return getStartUrl() +
                //"zones/findNeighborhoods/false/" + // Se incluyen barrios que no estén eliminados
                "zones/findNeighborhoods/" + includeDeleted.toString() + "/" +
                cityName + "/" +
                departmentName + "/" +
                countryName;
    }

    private String getStartUrl(){
        return paramConfig.getProtocol() + "://" + paramConfig.getSocket() + "/";
    }

    private String getUrlCities(Boolean includeDeleted, String departmentName, String countryName){
        return  getStartUrl() +
                //"zones/findCities/false/" + // Se incluyen ciudades que no estén eliminadas
                "zones/findCities/" + includeDeleted.toString() + "/" +
                departmentName + "/" +
                countryName;
    }

    private String getUrlDepartments(Boolean includeDeleted, String countryName){
        return  getStartUrl() +
                //"zones/findDepartments/false/" + // Se incluyen departamentos que no estén eliminados
                "zones/findDepartments/" + includeDeleted.toString() + "/" +
                countryName;
    }

    private void physicallyDeleteVolunteerPerson(String id){
        try{
            volunteerPersonRepo.deleteById(id);
            log.warning("Se borró físicamente el Voluntario con id " + id);

        }catch(IllegalArgumentException e){
            log.warning("No se pudo eliminar el Voluntario con Id: " + id + ". El Voluntario seguramente no ha" +
                    " quedado vinculado a un usuario (coleccion Users) con el rol VOLUNTEER_PERSON. Si es asi, copie " +
                    "el Id del Voluntario en la clave 'entityId' correspopndiente al rol del usuario, en la coleccion" +
                    " Users. Alternativamente, puede eliminar el documento del Voluntario e ingresarlo nuevamente.");
            throw new VolunteerPersonPhysicallyDeleteException("No se pudo eliminar el Voluntario con Id: " + id);
        }
    }

    private void defaultValues(VolunteerPerson volunteerPerson){
        volunteerPerson.setValidate(false);
        volunteerPerson.setDeleted(false);
    }

    private void defaultValues(VolunteerPerson volunteerPerson, VolunteerPerson oldVolunteerPerson){
        volunteerPerson.setAvailable(oldVolunteerPerson.getAvailable());
        volunteerPerson.setValidate(oldVolunteerPerson.getValidate());
        volunteerPerson.setDeleted(oldVolunteerPerson.getDeleted());
    }
}
