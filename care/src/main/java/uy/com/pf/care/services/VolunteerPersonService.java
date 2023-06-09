package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.infra.config.ParamConfig;
import uy.com.pf.care.model.documents.VolunteerPerson;
import uy.com.pf.care.model.objects.DayTimeRangeObject;
import uy.com.pf.care.model.objects.NeighborhoodObject;
import uy.com.pf.care.repos.IVolunteerPersonRepo;

import java.time.LocalDateTime;
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
    private ParamConfig paramConfig;

   // private static final Logger log = LoggerFactory.getLogger(CuidadosApplication.class);

    @Override
    public String save(VolunteerPerson volunteerPerson) {
        try{
            String id = volunteerPersonRepo.save(volunteerPerson).getVolunteerPersonId();
            log.info("*** Persona Voluntaria guardada con exito: " + LocalDateTime.now());
            return id;

        }catch(Exception e){
            log.warning("*** ERROR GUARDANDO PERSONA VOLUNTARIA: " + e);
            throw new VolunteerPersonSaveException("*** ERROR GUARDANDO PERSONA VOLUNTARIA");
        }
    }

    @Override
    public Boolean update(VolunteerPerson newVolunteerActivity) {
        try{
            Optional<VolunteerPerson> entityFound = volunteerPersonRepo.findById(newVolunteerActivity.
                    getVolunteerPersonId());
            if (entityFound.isPresent()){
                volunteerPersonRepo.save(newVolunteerActivity);
                log.info("Persona Voluntaria actualizada con exito");
                return true;
            }
            log.info("No se encontro la Persona Voluntaria con id " + newVolunteerActivity.getVolunteerPersonId());
            return false;

        } catch(Exception e){
            log.warning("*** ERROR ACTUALIZANDO PERSONA VOLUNTARIA: " + e);
            throw new VolunteerPersonUpdateException("*** ERROR ACTUALIZANDO PERSONA VOLUNTARIA");
        }
    }

    @Override
    public Optional<VolunteerPerson> findId(String id) {return volunteerPersonRepo.findById(id);}

    @Override
    public Boolean exist(String identificationDocument, String countryName) {
        return volunteerPersonRepo.findByCountryNameAndIdentificationDocument(identificationDocument, countryName).
                isPresent();
    }

    @Override
    public List<VolunteerPerson> findAll(Boolean includeDeleted, String countryName) {
        if (includeDeleted)
            return volunteerPersonRepo.findByCountryName(countryName);
        return volunteerPersonRepo.findByCountryNameAndDeletedFalse(countryName);
    }

    @Override
    public Optional<VolunteerPerson> findMail(String mail) {return volunteerPersonRepo.findByMailIgnoreCase(mail);}

    @Override
    public List<VolunteerPerson> findName(Boolean includeDeleted, String countryName, String name1) {
        if (includeDeleted)
            return volunteerPersonRepo.findByCountryNameAndName1IgnoreCase(countryName, name1);
        return volunteerPersonRepo.findByCountryNameAndName1IgnoreCaseAndDeletedFalse(countryName, name1);
    }

    @Override
    public List<VolunteerPerson> findNameLike(Boolean includeDeleted, String countryName, String name1) {
        if (includeDeleted)
            return volunteerPersonRepo.findByCountryNameAndName1LikeIgnoreCase(countryName, name1);
        return volunteerPersonRepo.findByCountryNameAndName1LikeIgnoreCaseAndDeletedFalse(countryName, name1);
    }

    @Override
    public List<VolunteerPerson> findInterestZones_Neighborhood(
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
                listReturn = this.findAll(includeDeleted, countryName).stream().filter(
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
            log.warning( "Error buscando personas voluntarias por zona de interés en barrio: "
                    + interestNeighborhoodName + " (" + interestCityName+ ", " + interestDepartmentName + ", "
                    + countryName + ")" + ". " + e.getMessage());
            throw new VolunteerPersonFindInterestZones_NeighborhoodException(
                    "Error buscando personas voluntarias por zona de interés en barrio " + interestNeighborhoodName +
                            " (" + interestCityName+ ", " + interestDepartmentName + ", " + countryName + ")");
        }

    }

    @Override
    public List<VolunteerPerson> findInterestZones_City(
            Boolean validateCity,
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
                listReturn = this.findAll(includeDeleted, countryName).stream().filter(
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

                listReturn = this.findAll(includeDeleted, countryName).stream().filter(volunteerPerson ->
                                volunteerPerson.getInterestZones().isEmpty() ||
                                        ! volunteerPerson.getInterestZones().stream().filter(interestZonesObject ->
                                                interestZonesObject.getDepartmentName().equals(interestDepartmentName)
                                        ).toList().isEmpty())
                        .toList();
            }
            return listReturn;

        }catch(Exception e){
            log.warning("Error buscando personas voluntarias por zona de interés en departamento/provincia: " +
                    interestDepartmentName + " (" + countryName + ")" + ". " + e.getMessage());
            throw new FormalCaregiverFindInterestZones_DepartmentException(
                    "Error buscando personas voluntarias por zona de interés en departamento/provincia: " +
                            interestDepartmentName + " (" + countryName + ")");
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
                        true, false, interestCityName, interestDepartmentName, countryName);
            else
                volunteerPersonList = this.findInterestZones_Neighborhood(
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
            log.warning("Error buscando personas voluntarias por rango de dias/horas. " + e.getMessage());
            throw new VolunteerPersonFindDateTimeRangeException(
                    "Error buscando personas voluntarias por rango de dias/horas");
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
}
