package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uy.com.pf.care.exceptions.FormalCaregiverSaveException;
import uy.com.pf.care.infra.config.ParamConfig;
import uy.com.pf.care.model.documents.FormalCaregiver;
import uy.com.pf.care.model.objects.NeighborhoodObject;
import uy.com.pf.care.repos.IFormalCaregiverRepo;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

import static uy.com.pf.care.infra.tools.Strings.containEqual;

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
            return formalCaregiverRepo.findByCountryName(countryName);
        else
            return formalCaregiverRepo.findByCountryNameAndDeletedFalse(countryName);
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
    public FormalCaregiver findByName(
            Boolean includeDeleted, String name, String telephone, String departmentName, String countryName) {

        if (includeDeleted)
            return formalCaregiverRepo.findByNameAndTelephoneAndDepartmentNameAndCountryName(
                    name, telephone, departmentName, countryName);
        else
            return formalCaregiverRepo.findByNameAndTelephoneAndDepartmentNameAndCountryNameAndDeletedFalse(
                    name, telephone, departmentName, countryName);
    }

    @Override
    public List<FormalCaregiver> findByNameLike(
            Boolean includeDeleted, String name, String departmentName, String countryName) {

        if (includeDeleted)
            return formalCaregiverRepo.findByNameLikeAndDepartmentNameAndCountryName(name, departmentName,countryName);
        else
            return formalCaregiverRepo.findByNameLikeAndDepartmentNameAndCountryNameAndDeletedFalse(
                    name, departmentName, countryName);
    }

    @Override
    public List<FormalCaregiver> findByDepartment(Boolean includeDeleted, String departmentName, String countryName) {
        if (includeDeleted)
            return formalCaregiverRepo.findByDepartmentNameAndCountryName(departmentName, countryName);
        else
            return formalCaregiverRepo.findByDepartmentNameAndCountryNameAndDeletedFalse(departmentName, countryName);
    }

    @Override
    public List<FormalCaregiver> findByAvailable(String departmentName, String countryName) {
        return formalCaregiverRepo.findByAvailableTrueAndDepartmentNameAndCountryNameAndDeletedFalse(
                departmentName, countryName);
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
                                        ! interestZonesObject.getCities().stream().filter(cityObject ->
                                                cityObject.getNeighborhoodNames().contains(interestNeighborhoodName)
                                        ).toList().isEmpty()
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
        String cities = null;

        if (validateCity)
            cities = restTemplate.getForEntity(
                    getUrlAllCities(interestDepartmentName, countryName), String.class).getBody();

        // Si se desea validar la ciudad, verifico que la ciudad del departamento exista y no esté eliminada
        if (!validateCity ||
            (cities != null && !cities.equals("[]") && containEqual(cities, interestCityName))) {

            List<FormalCaregiver> formalCaregiversByDepartment = this.findByInterestZones_Department(
                    false, includeDeleted, interestDepartmentName, countryName);

            if (!formalCaregiversByDepartment.isEmpty())
                listReturn = formalCaregiversByDepartment.stream().filter(formalCaregiver ->
                        formalCaregiver.getInterestZones().isEmpty() ||
                        !formalCaregiver.getInterestZones().stream().filter(interestZonesObject ->
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
        //String departments = null;
        String[] departments = null;
        List<FormalCaregiver> listReturn = new ArrayList<>();

        if (validateInterestDepartment){
            //departments = restTemplate.getForEntity(getUrlAllDepartments(countryName), String.class).getBody();
            ResponseEntity<String[]> departmentsResponse = restTemplate.getForEntity(
                    getUrlAllDepartments(countryName), String[].class);
            departments = departmentsResponse.getBody();
        }

        // Si se desea validar el Departamento de Interes, verifico que el departamento exista y no esté eliminado
        if (!validateInterestDepartment ||
            //(departments != null && !departments.equals("[]") && containEqual(departments, interestDepartmentName))){
            (departments != null &&
                departments.length > 0 &&
                    Arrays.asList(departments).contains(interestDepartmentName))){

            //TODO: posible cuello de botella en findAll.
            // Carga todos los cuidadores formales del pais para luego hacer el filtro.
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
