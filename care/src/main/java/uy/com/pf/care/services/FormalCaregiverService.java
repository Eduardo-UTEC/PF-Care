package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Log
public class FormalCaregiverService implements IFormalCaregiverService {

    @Autowired
    private IFormalCaregiverRepo formalCaregiverRepo;
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
    public FormalCaregiver findWithIndex_Mail(String mail) {
        return formalCaregiverRepo.findByMail(mail);
    }

    @Override
    public List<FormalCaregiver> findWithIndex_Name(
            Boolean includeDeleted, String name, String countryName) {

        if (includeDeleted)
            return formalCaregiverRepo.findByNameAndCountryNameOrderByInterestZones_DepartmentName(name, countryName);

        return formalCaregiverRepo.findByNameAndCountryNameAndDeletedFalseOrderByInterestZones_DepartmentName(name, countryName);
    }

    @Override
    public List<FormalCaregiver> findByNameLike(Boolean includeDeleted, String name, String countryName) {

        if (includeDeleted)
            return formalCaregiverRepo.findByNameLikeAndCountryNameOrderByInterestZones_DepartmentName(name, countryName);

        return formalCaregiverRepo.findByNameLikeAndCountryNameAndDeletedFalseOrderByInterestZones_DepartmentName(name, countryName);
    }

    @Override
    public List<FormalCaregiver> findByInterestZones_Neighborhood(
            Boolean includeDeleted,
            String interestNeighborhoodName,
            String interestCityName,
            String interestDepartmentName,
            String countryName) {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<NeighborhoodObject[]> neighborhoodsResponse = restTemplate.getForEntity(
                getUrlAllNeighborhoods(interestCityName, interestDepartmentName, countryName),
                NeighborhoodObject[].class);
        NeighborhoodObject[] neighborhoods = neighborhoodsResponse.getBody();

        List<FormalCaregiver> listReturn = new ArrayList<>();

        // Verifico que el barrio de la ciudad y departamento existan y no esté eliminada
        if (neighborhoods != null &&
            neighborhoods.length > 0 &&
            Arrays.stream(neighborhoods).anyMatch(neighborhoodObject ->
                        neighborhoodObject.getNeighborhoodName().equals(interestNeighborhoodName))){

            List<FormalCaregiver> formalCaregiversByCity = this.findByInterestZones_City(
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
    public List<FormalCaregiver> findByInterestZones_City(
            Boolean validateCity,
            Boolean includeDeleted,
            String interestCityName,
            String interestDepartmentName,
            String countryName) {

        List<FormalCaregiver> listReturn = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        String[] cities = null;

        if (validateCity){
            ResponseEntity<String[]> citiesResponse = restTemplate.getForEntity(
                    getUrlAllCities(interestDepartmentName, countryName), String[].class);
            cities = citiesResponse.getBody();
        }

        // Si se desea validar la ciudad, verifico que la ciudad del departamento exista y no esté eliminada
        if (!validateCity ||
                (cities != null && cities.length > 0 && Arrays.asList(cities).contains(interestCityName))){

            List<FormalCaregiver> formalCaregiversByDepartment = this.findByInterestZones_Department(
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
    public List<FormalCaregiver> findByInterestZones_Department(
            Boolean validateInterestDepartment, Boolean includeDeleted, String interestDepartmentName, String countryName) {

        RestTemplate restTemplate = new RestTemplate();
        String[] departments = null;
        List<FormalCaregiver> listReturn = new ArrayList<>();

        if (validateInterestDepartment){
            ResponseEntity<String[]> departmentsResponse = restTemplate.getForEntity(
                    getUrlAllDepartments(countryName), String[].class);
            departments = departmentsResponse.getBody();
        }

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
    public List<FormalCaregiver> findByPriceRange(
            Integer maxPrice,
            String interestNeighborhoodName,
            String interestCityName,
            String interestDepartmentName,
            String countryName) {

        return this.findByInterestZones_Neighborhood(
                false, interestNeighborhoodName, interestCityName, interestDepartmentName, countryName)
                .stream().filter(formalCaregiver -> formalCaregiver.getPriceHour() <= maxPrice).toList();
    }

    @Override
    public List<FormalCaregiver> findByDateTimeRange(
            List<DayTimeRangeObject> dayTimeRange,
            String interestNeighborhoodName, // Si se omite, se asumen todos los barrios.
            String interestCityName,
            String interestDepartmentName,
            String countryName){

        List<FormalCaregiver> formalCaregiversList = new ArrayList<>();

        if (interestNeighborhoodName.isEmpty())
             formalCaregiversList = this.findByInterestZones_City(
                    true, false, interestCityName, interestDepartmentName, countryName);
        else
            formalCaregiversList = this.findByInterestZones_Neighborhood(
                    false, interestNeighborhoodName, interestCityName, interestDepartmentName, countryName);

        if (dayTimeRange.isEmpty()) // Todos los dias y horarios
            return formalCaregiversList;

        return formalCaregiversList.stream().filter(formalCaregiver -> {
            if (formalCaregiver.getDayTimeRange().isEmpty())
                return true;
            return formalCaregiver.getDayTimeRange().stream().anyMatch(formalCaregiverRange -> {
                return dayTimeRange.stream().anyMatch(searchRange -> {
                    if (formalCaregiverRange.getDay().equals(searchRange.getDay())){
                        if (formalCaregiverRange.getTimeRange().isEmpty())
                            return true;
                        return formalCaregiverRange.getTimeRange().stream().anyMatch(formalCaregiverSubRange -> {
                            return searchRange.getTimeRange().stream().anyMatch(searchSubRange ->
                                     formalCaregiverSubRange.getStartTime().isAfter(searchSubRange.getStartTime()) &&
                                     formalCaregiverSubRange.getEndTime().isBefore(searchSubRange.getEndTime())
                                     //formalCaregiverSubRange.getEndTime().toSecondOfDay() > 0
                            );
                        });
                    }
                    return false;
                });
            });
        }).toList();
    };

    private String getStartUrl(){
        return paramConfig.getProtocol() + "://" + paramConfig.getSocket() + "/";
    }

    private String getUrlAllNeighborhoods(String cityName, String departmentName, String countryName) {
        return getStartUrl() +
               "zones/findAllNeighborhoods/false/" + // Se incluyen barrios que no estén eliminados
               cityName + "/" +
               departmentName + "/" +
               countryName;
    }

    private String getUrlAllCities(String departmentName, String countryName){
        return  getStartUrl() +
                "zones/findAllCities/false/" + // Se incluyen ciudades que no estén eliminadas
                departmentName + "/" +
                countryName;
    }

    private String getUrlAllDepartments(String countryName){
        return  getStartUrl() +
               "zones/findAllDepartments/false/" + // Se incluyen departamentos que no estén eliminados
                countryName;
    }

}
