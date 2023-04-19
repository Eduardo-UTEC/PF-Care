package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uy.com.pf.care.exceptions.FormalCaregiverSaveException;
import uy.com.pf.care.infra.config.ParamConfig;
import uy.com.pf.care.model.documents.FormalCaregiver;
import uy.com.pf.care.model.objects.DayTimeRangeObject;
import uy.com.pf.care.model.objects.NeighborhoodObject;
import uy.com.pf.care.repos.IFormalCaregiverRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class FormalCaregiverService implements IFormalCaregiverService {

    @Autowired
    private IFormalCaregiverRepo formalCaregiverRepo;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    private ParamConfig paramConfig;

    //private static final Logger log = LoggerFactory.getLogger(CuidadosApplication.class);

    @Override
    public FormalCaregiver save(FormalCaregiver formalCaregiver) {
        try{
            FormalCaregiver newformalCaregiver = formalCaregiverRepo.save(formalCaregiver);
            log.info("*** Cuidador Formal guardado con exito: " + LocalDateTime.now());
            return newformalCaregiver;

        }catch(Exception e){
            log.warning("*** ERROR GUARDANDO CUIDADOR FORMAL: " + e);
            throw new FormalCaregiverSaveException(formalCaregiver);
        }
    }

    /*  Devuelve true si la operación fue exitosa.
        1. La disponibilidad de un cuidador formal la establece el mismo.
        2. Si el cuidador formal esta eliminado, no es posible estabelcer su disponibilidad.
     */
    @Override
    public Boolean setAvailability(String id, Boolean isAvailable) {
        Optional<FormalCaregiver> formalCaregiver = this.findId(id);
        if (formalCaregiver.isPresent() && ! formalCaregiver.get().getDeleted()) {
            formalCaregiver.get().setAvailable(isAvailable);
            this.save(formalCaregiver.get());
            return true;
        }
        return false;

    }

    /*  Devuelve true si la operación fue exitosa.
        1. Esta es una tarea del "administrador del sistema"
        2. La "disponiblidad" del cuidador formal pasa a false, sin importar si se pasa a borrado o no borrado.
            Ello posibilita que el administrador sea quien recupere a un cuidador, y luego sea este quien se
            disponibilice.
     */
    @Override
    public Boolean setDeletion(String id, Boolean isDeleted) {
        Optional<FormalCaregiver> formalCaregiver = this.findId(id);
        if (formalCaregiver.isPresent()) {
            formalCaregiver.get().setDeleted(isDeleted);
            formalCaregiver.get().setAvailable(false);
            this.save(formalCaregiver.get());
            return true;
        }
        return false;
    }

    @Override
    public List<FormalCaregiver> findAll(Boolean includeDeleted, String countryName) {
        if (includeDeleted)
            return formalCaregiverRepo.findByCountryNameOrderByInterestZones_DepartmentName(countryName);

        return formalCaregiverRepo.findByCountryNameAndDeletedFalseOrderByInterestZones_DepartmentName(countryName);
    }

    @Override
    public Optional<FormalCaregiver> findId(String id) {
        return formalCaregiverRepo.findById(id);
    }

    @Override
    public FormalCaregiver findMail(String mail) {
        return formalCaregiverRepo.findByMail(mail);
    }

    @Override
    public List<FormalCaregiver> findName(Boolean includeDeleted, String countryName, String name) {

        if (includeDeleted)
            return formalCaregiverRepo.findByCountryNameAndNameOrderByInterestZones_DepartmentName(countryName, name);

        return formalCaregiverRepo.findByCountryNameAndNameAndDeletedFalseOrderByInterestZones_DepartmentName(
                countryName, name);
    }

    @Override
    public List<FormalCaregiver> findNameLike(Boolean includeDeleted, String countryName, String name) {

        if (includeDeleted)
            return formalCaregiverRepo.findByCountryNameAndNameLikeOrderByInterestZones_DepartmentName(
                    countryName, name);

        return formalCaregiverRepo.findByCountryNameAndNameLikeAndDeletedFalseOrderByInterestZones_DepartmentName(
                countryName, name);
    }

    @Override
    public List<FormalCaregiver> findInterestZones_Neighborhood(
            Boolean includeDeleted,
            String interestNeighborhoodName,
            String interestCityName,
            String interestDepartmentName,
            String countryName) {

        RestTemplate restTemplate = new RestTemplate();
        NeighborhoodObject[] neighborhoods = restTemplate.getForEntity(
                getUrlNeighborhoods(interestCityName, interestDepartmentName, countryName),
                NeighborhoodObject[].class).getBody();

        List<FormalCaregiver> listReturn = new ArrayList<>();

        // Verifico que el barrio de la ciudad y departamento existan y no esté eliminada
        if (neighborhoods != null &&
            neighborhoods.length > 0 &&
            Arrays.stream(neighborhoods).anyMatch(neighborhoodObject ->
                        neighborhoodObject.getNeighborhoodName().equals(interestNeighborhoodName))){

            List<FormalCaregiver> formalCaregiversByCity = this.findInterestZones_City(
                    false, includeDeleted, interestCityName, interestDepartmentName, countryName);

            if (!formalCaregiversByCity.isEmpty())
                listReturn = formalCaregiversByCity.stream().filter(formalCaregiver ->
                        formalCaregiver.getInterestZones().isEmpty() ||
                        !formalCaregiver.getInterestZones().stream().filter(interestZonesObject ->
                                interestZonesObject.getCities().isEmpty() ||
                                !interestZonesObject.getCities().stream().filter(cityObject -> {
                                    if (cityObject.getCityName().equals(interestCityName)){
                                        return cityObject.getNeighborhoodNames().isEmpty() ||
                                                cityObject.getNeighborhoodNames().contains(interestNeighborhoodName);
                                    }
                                    return false;
                                }).toList().isEmpty()
                        ).toList().isEmpty()
                ).toList();
        }
        return listReturn;
    }

    @Override
    public List<FormalCaregiver> findInterestZones_City(
            Boolean validateCity,
            Boolean includeDeleted,
            String interestCityName,
            String interestDepartmentName,
            String countryName) {

        List<FormalCaregiver> listReturn = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        String[] cities = null;

        if (validateCity)
            /*ResponseEntity<String[]> citiesResponse = restTemplate.getForEntity(
                    getUrlCities(interestDepartmentName, countryName), String[].class);
            cities = citiesResponse.getBody();*/
            cities = restTemplate.getForEntity(getUrlCities(interestDepartmentName, countryName), String[].class)
                    .getBody();

        // Si se desea validar la ciudad, verifico que la ciudad del departamento exista y no esté eliminada
        if (!validateCity ||
                (cities != null && cities.length > 0 && Arrays.asList(cities).contains(interestCityName))){

            List<FormalCaregiver> formalCaregiversByDepartment = this.findInterestZones_Department(
                    false, includeDeleted, interestDepartmentName, countryName);

            if (!formalCaregiversByDepartment.isEmpty())
                listReturn = formalCaregiversByDepartment.stream().filter(formalCaregiver ->
                        formalCaregiver.getInterestZones().isEmpty() ||
                        !formalCaregiver.getInterestZones().stream().filter(interestZonesObject ->
                                interestZonesObject.getCities().isEmpty() ||
                                !interestZonesObject.getCities().stream().filter(cityObject ->
                                        cityObject.getCityName().equals(interestCityName)
                                ).toList().isEmpty()
                        ).toList().isEmpty()
                ).toList();
        }
        return listReturn;
    }

    @Override
    public List<FormalCaregiver> findInterestZones_Department(
            Boolean validateInterestDepartment, Boolean includeDeleted, String interestDepartmentName, String countryName) {

        RestTemplate restTemplate = new RestTemplate();
        String[] departments = null;
        List<FormalCaregiver> listReturn = new ArrayList<>();

        if (validateInterestDepartment)
            departments = restTemplate.getForEntity(getUrlDepartments(countryName), String[].class).getBody();

        // Si se desea validar el Departamento de Interes, verifico que el departamento exista y no esté eliminado
        if (!validateInterestDepartment ||
            (departments != null &&
                    departments.length > 0 &&
                    Arrays.asList(departments).contains(interestDepartmentName))){

            listReturn = this.findAll(includeDeleted, countryName).stream().filter(formalCaregiver -> {
                boolean accept = formalCaregiver.getInterestZones().isEmpty();
                if (!accept)
                    accept = ! formalCaregiver.getInterestZones().stream().filter(interestZonesObject ->
                            interestZonesObject.getDepartmentName().equals(interestDepartmentName)
                    ).toList().isEmpty();
                return accept;
            }).toList();
        }
        return listReturn;
    }

    @Override
    public List<FormalCaregiver> findPriceRange(
            Integer maxPrice,
            String interestNeighborhoodName,
            String interestCityName,
            String interestDepartmentName,
            String countryName) {

        return this.findInterestZones_Neighborhood(
                false, interestNeighborhoodName, interestCityName, interestDepartmentName, countryName)
                .stream().filter(formalCaregiver -> formalCaregiver.getPriceHour() <= maxPrice).toList();
    }

    @Override
    public List<FormalCaregiver> findDateTimeRange(
            List<DayTimeRangeObject> dayTimeRange,
            String interestNeighborhoodName, // Si se omite, se asumen todos los barrios.
            String interestCityName,
            String interestDepartmentName,
            String countryName){

        List<FormalCaregiver> formalCaregiversList = new ArrayList<>();

        if (interestNeighborhoodName.isEmpty())
             formalCaregiversList = this.findInterestZones_City(
                    true, false, interestCityName, interestDepartmentName, countryName);
        else
            formalCaregiversList = this.findInterestZones_Neighborhood(
                    false, interestNeighborhoodName, interestCityName, interestDepartmentName, countryName);

        if (dayTimeRange.isEmpty()) // Todos los dias y horarios
            return formalCaregiversList;

        return formalCaregiversList.stream().filter(formalCaregiver -> {
            if (formalCaregiver.getDayTimeRange().isEmpty())
                return true;
            return formalCaregiver.getDayTimeRange().stream().anyMatch(formalCaregiverRange -> {
                return dayTimeRange.stream().anyMatch(searchRange -> {
                    if (formalCaregiverRange.getDay().ordinal() ==  searchRange.getDay().ordinal()){
                        if (formalCaregiverRange.getTimeRange().isEmpty())
                            return true;
                        return formalCaregiverRange.getTimeRange().stream().anyMatch(formalCaregiverSubRange -> {
                            return searchRange.getTimeRange().stream().anyMatch(searchSubRange ->
                                (formalCaregiverSubRange.getStartTime().compareTo(searchSubRange.getStartTime()) < 0 ||
                                formalCaregiverSubRange.getStartTime().compareTo(searchSubRange.getStartTime()) == 0)
                                &&
                                (formalCaregiverSubRange.getEndTime().compareTo(searchSubRange.getEndTime()) > 0 ||
                                formalCaregiverSubRange.getEndTime().compareTo(searchSubRange.getEndTime()) == 0)
                            );
                        });
                    }
                    return false;
                });
            });
        }).toList();
    }

    private String getStartUrl(){
        return paramConfig.getProtocol() + "://" + paramConfig.getSocket() + "/";
    }

    private String getUrlNeighborhoods(String cityName, String departmentName, String countryName) {
        return getStartUrl() +
               "zones/findNeighborhoods/false/" + // Se incluyen barrios que no estén eliminados
               cityName + "/" +
               departmentName + "/" +
               countryName;
    }

    private String getUrlCities(String departmentName, String countryName){
        return  getStartUrl() +
                "zones/findCities/false/" + // Se incluyen ciudades que no estén eliminadas
                departmentName + "/" +
                countryName;
    }

    private String getUrlDepartments(String countryName){
        return  getStartUrl() +
               "zones/findDepartments/false/" + // Se incluyen departamentos que no estén eliminados
                countryName;
    }

}
