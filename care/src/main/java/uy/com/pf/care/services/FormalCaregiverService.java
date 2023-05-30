package uy.com.pf.care.services;

import com.mongodb.client.result.UpdateResult;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uy.com.pf.care.exceptions.*;
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
    public String save(FormalCaregiver formalCaregiver) {
        //this.validateVote(formalCaregiver.getVotes());
        try{
            this.defaultValues(formalCaregiver);
            String id = formalCaregiverRepo.save(formalCaregiver).getFormalCaregiverId();
            log.info("Cuidador Formal guardado con exito: " + LocalDateTime.now());
            return id;

        }catch(Exception e){
            log.warning("Error guardando Cuidador Formal: " + e.getMessage());
            throw new FormalCaregiverSaveException("Error guardando Cuidador Formal");
        }
    }

    @Override
    public Boolean update(FormalCaregiver newFormalCaregiver) {
        try{
            Optional<FormalCaregiver> entityFound = formalCaregiverRepo.findById(newFormalCaregiver.getFormalCaregiverId());
            if (entityFound.isPresent()){
                this.defaultValues(entityFound.get(), newFormalCaregiver);
                formalCaregiverRepo.save(newFormalCaregiver);
                log.info("Cuidador formal actualizado con exito");
                return true;
            }
            log.info("No se encontro el cuidador formal con id " + newFormalCaregiver.getFormalCaregiverId());
            return false;

        }catch(Exception e){
            log.warning("Error actualizando Cuidador Formal: " + e.getMessage());
            throw new FormalCaregiverUpdateException("Error actualizando Cuidador Formal");
        }

    }

    /*  Devuelve true si la operación fue exitosa.
        1. La disponibilidad de un cuidador formal la establece el mismo.
        2. Si el cuidador formal esta eliminado, no es posible estabelcer su disponibilidad.
     */
    @Override
    public Boolean setAvailability(String id, Boolean isAvailable) {
        try{
            Optional<FormalCaregiver> formalCaregiver = this.findId(id);
            if (formalCaregiver.isPresent() && ! formalCaregiver.get().getDeleted()) {
                formalCaregiver.get().setAvailable(isAvailable);
                formalCaregiverRepo.save(formalCaregiver.get());
                return true;
            }
            return false;

        }catch(Exception e){
            log.warning("No se pudo setear la disponibilidad del cuidador formal con id: " + id + ". "
                    + e.getMessage());
            throw new FormalCaregiverSetAvailabilityException("No se pudo setear la disponibilidad del cuidador formal con id: "
                    + id + ". ");
        }
    }

    /*  Devuelve true si la operación fue exitosa.
        1. Esta es una tarea del "administrador del sistema"
        2. La "disponiblidad" del cuidador formal pasa a false, sin importar si se pasa a borrado o no borrado.
            Ello posibilita que el administrador sea quien recupere a un cuidador, y luego sea este quien se
            disponibilice.
     */
    @Override
    public Boolean setDeletion(String id, Boolean isDeleted) {
        try{
            Optional<FormalCaregiver> formalCaregiver = this.findId(id);
            if (formalCaregiver.isPresent()) {
                formalCaregiver.get().setDeleted(isDeleted);
                formalCaregiver.get().setAvailable(false);
                formalCaregiverRepo.save(formalCaregiver.get());
                return true;
            }
            return false;

        }catch(Exception e){
            log.warning("No se pudo setear el borrado lógico del cuidador formal con id: " + id + ". "
                    + e.getMessage());
            throw new FormalCaregiverSetDeletionException(
                    "No se pudo setear el borrado lógico del cuidador formal con id " + id);
        }
    }

    @Override
    public Boolean updateVotes(String formalCaregiverId, int previousScore, int currentScore) {
        try{
            Query query = new Query(Criteria.where("_id").is(formalCaregiverId));
            FormalCaregiver formalCaregiver = mongoTemplate.findOne(query, FormalCaregiver.class);

            if (formalCaregiver != null) {
                Update update = new Update().set("votes",
                        this.obtainNewVotes(formalCaregiver.getVotes(), previousScore, currentScore));
                UpdateResult updateResult = mongoTemplate.updateFirst(query, update, FormalCaregiver.class);
                return updateResult.wasAcknowledged();
            }
            return false;

        }catch(Exception e){
            log.warning("No se pudo actualizar votos del cuidador formal con id: " + formalCaregiverId + ". "
                    + e.getMessage());
            throw new FormalCaregiverUpdateVotesException("No se pudo actualizar votos del cuidador formal con id: "
                    + formalCaregiverId + ". " + e.getMessage());
        }
    }

    /*  Si previousScore = -1, implica que no hay un puntaje previo asignado.
       Si previousScore = [1..5], implica hay un puntaje previo en el ordinal previousScore - 1 y debe restarse.
       currentScore: es el puntaje que debe sumarse en el ordinal currentScore - 1.
    */
    private int[] obtainNewVotes(int[] votes, int previousScore, int currentScore){
        if (previousScore != -1)
            votes[previousScore-1] = votes[previousScore-1] - 1;
        votes[currentScore-1] = votes[currentScore-1] + 1;
        return votes;
    }

    @Override
    public List<FormalCaregiver> findAll(Boolean includeDeleted, String countryName) {
        try{
            if (includeDeleted)
                return formalCaregiverRepo.findByCountryName(countryName);

            return formalCaregiverRepo.findByCountryNameAndDeletedFalse(countryName);

        }catch (Exception e){
            log.warning("Error buscando todos los cuidadores formales de " + countryName + ". " + e.getMessage());
            throw new FormalCaregiverFindAllException("Error buscando todos los cuidadores formales de " + countryName);
        }
    }

    @Override
    public Optional<FormalCaregiver> findId(String id) {
        try{
            return formalCaregiverRepo.findById(id);

        }catch(Exception e){
            log.warning("Error buscando cuidador formal con id: " + id + ". " + e.getMessage());
            throw new FormalCaregiverFindIdException("Error buscando cuidador formal con id: " + id);
        }
    }

    @Override
    public FormalCaregiver findMail(String mail) {
        try{
            return formalCaregiverRepo.findByMail(mail);

        }catch(Exception e){
            log.warning("Error buscando cuidador formal con mail: " + mail + ". " + e.getMessage());
            throw new FormalCaregiverFindMailException("Error buscando cuidador formal con mail: " + mail);
        }
    }

    @Override
    public List<FormalCaregiver> findName(Boolean includeDeleted, String countryName, String name) {

        try{
            if (includeDeleted)
                return formalCaregiverRepo.findByCountryNameAndName(countryName, name);

            return formalCaregiverRepo.findByCountryNameAndNameAndDeletedFalse(
                    countryName, name);

        }catch(Exception e){
            log.warning("Error buscando cuidador formal: " + name + " (" + countryName + ")" + ". "
                    + e.getMessage());
            throw new FormalCaregiverFindNameException("Error buscando cuidador formal: " + name
                    + " (" + countryName + ")");
        }
    }

    @Override
    public List<FormalCaregiver> findNameLike(Boolean includeDeleted, String countryName, String name) {
        try{
            if (includeDeleted)
                return formalCaregiverRepo.findByCountryNameAndNameLike(
                        countryName, name);

            return formalCaregiverRepo.findByCountryNameAndNameLikeAndDeletedFalse(
                    countryName, name);

        }catch(Exception e){
            log.warning("Error buscando cuidadores formales de nombre: " + name + " (" + countryName + ")" + ". "
                    + e.getMessage());
            throw new FormalCaregiverFindNameLikeException("Error buscando cuidadores formales de nombre: " + name
                    + " (" + countryName + ")");
        }
    }

    @Override
    public List<FormalCaregiver> findInterestZones_Neighborhood(
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

            List<FormalCaregiver> listReturn = new ArrayList<>();

            // Verifico que el barrio exista
            if (neighborhoods != null &&
                    neighborhoods.length > 0 &&
                    Arrays.stream(neighborhoods).anyMatch(neighborhoodObject ->
                            neighborhoodObject.getNeighborhoodName().equals(interestNeighborhoodName))){

                //TODO: Testear cual de los dos filtros previos es el mas eficiente (tomando por ciudad o findAll)
                //listReturn = this.findInterestZones_City(
                //        false, includeDeleted, interestCityName, interestDepartmentName, countryName)
                //        .stream().filter(
                listReturn = this.findAll(includeDeleted, countryName).stream().filter(
                        formalCaregiver -> formalCaregiver.getInterestZones().isEmpty() ||
                                !formalCaregiver.getInterestZones().stream().filter(interestZonesObject ->
                                        interestZonesObject.getDepartmentName().equals(interestDepartmentName) &&
                                                (interestZonesObject.getCities().isEmpty() ||
                                                        !interestZonesObject.getCities().stream().filter(cityObject ->
                                                                cityObject.getCityName().equals(interestCityName) &&
                                                                        (cityObject.getNeighborhoodNames().isEmpty() ||
                                                                                cityObject.getNeighborhoodNames().contains(
                                                                                        interestNeighborhoodName))
                                                        ).toList().isEmpty())
                                ).toList().isEmpty()
                ).toList();
            }
            return listReturn;

        }catch(Exception e){
            log.warning( "Error buscando cuidadores formales por zona de interés en barrio: "
                    + interestNeighborhoodName + " (" + interestCityName+ ", " + interestDepartmentName + ", "
                    + countryName + ")" + ". " + e.getMessage());
            throw new FormalCaregiverFindInterestZones_NeighborhoodException(
                    "Error buscando cuidadores formales por zona de interés en barrio " + interestNeighborhoodName +
                    " (" + interestCityName+ ", " + interestDepartmentName + ", " + countryName + ")");
        }
    }

    @Override
    public List<FormalCaregiver> findInterestZones_City(
            Boolean validateCity,
            Boolean includeDeleted,
            String interestCityName,
            String interestDepartmentName,
            String countryName) {

        try{
            List<FormalCaregiver> listReturn = new ArrayList<>();
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
                        formalCaregiver -> formalCaregiver.getInterestZones().isEmpty() ||
                                !formalCaregiver.getInterestZones().stream().filter(interestZonesObject ->
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
            log.warning("Error buscando cuidadores formales por zona de interés en ciudad: " + interestCityName +
                    " (" + interestDepartmentName + ", " + countryName + ")" + ". " + e.getMessage());
            throw new FormalCaregiverFindInterestZones_CityException(
                    "Error buscando cuidadores formales por zona de interés en ciudad " + interestCityName + " (" +
                            interestDepartmentName + ", " + countryName + ")");
        }
    }

    @Override
    public List<FormalCaregiver> findInterestZones_Department(
            Boolean validateInterestDepartment,
            Boolean includeDeleted,
            String interestDepartmentName,
            String countryName) {

        try{
            RestTemplate restTemplate = new RestTemplate();
            String[] departments = null;
            List<FormalCaregiver> listReturn = new ArrayList<>();

            if (validateInterestDepartment)
                departments = restTemplate.
                        getForEntity(getUrlDepartments(includeDeleted, countryName), String[].class).getBody();

            // Si se desea validar el Departamento de Interes, verifico que el departamento exista
            if (!validateInterestDepartment ||
                    (departments != null &&
                            departments.length > 0 &&
                            Arrays.asList(departments).contains(interestDepartmentName))){

                listReturn = this.findAll(includeDeleted, countryName).stream().filter(formalCaregiver ->
                                formalCaregiver.getInterestZones().isEmpty() ||
                                        ! formalCaregiver.getInterestZones().stream().filter(interestZonesObject ->
                                                interestZonesObject.getDepartmentName().equals(interestDepartmentName)
                                        ).toList().isEmpty())
                        .toList();
            }
            return listReturn;

        }catch(Exception e){
            log.warning("Error buscando cuidadores formales por zona de interés en departamento/provincia: " +
                    interestDepartmentName + " (" + countryName + ")" + ". " + e.getMessage());
            throw new FormalCaregiverFindInterestZones_DepartmentException(
                    "Error buscando cuidadores formales por zona de interés en departamento/provincia: " +
                            interestDepartmentName + " (" + countryName + ")");
        }
    }

    @Override
    public List<FormalCaregiver> findPriceRange(
            Integer maxPrice,
            String interestNeighborhoodName,
            String interestCityName,
            String interestDepartmentName,
            String countryName) {

        try {
            return this.findInterestZones_Neighborhood(
                            false,
                            interestNeighborhoodName,
                            interestCityName,
                            interestDepartmentName,
                            countryName)
                    .stream().filter(formalCaregiver -> formalCaregiver.getPriceHour() <= maxPrice).toList();

        }catch(Exception e){
            log.warning("Error buscando cuidadores formales por rango de precios. " + e.getMessage());
            throw new FormalCaregiverFindPriceRangeException(
                    "Error buscando cuidadores formales por rango de precios");
        }
    }

    @Override
    public List<FormalCaregiver> findDateTimeRange(
            List<DayTimeRangeObject> dayTimeRange,
            String interestNeighborhoodName, // Si se omite, se asumen todos los barrios.
            String interestCityName,
            String interestDepartmentName,
            String countryName){

        try{
            List<FormalCaregiver> formalCaregiversList;

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
                return formalCaregiver.getDayTimeRange().stream().anyMatch(formalCaregiverRange ->
                        dayTimeRange.stream().anyMatch(searchRange -> {
                            if (formalCaregiverRange.getDay().ordinal() ==  searchRange.getDay().ordinal()){
                                if (formalCaregiverRange.getTimeRange().isEmpty())
                                    return true;

                                return formalCaregiverRange.getTimeRange().stream().anyMatch(formalCaregiverSubRange ->
                                        searchRange.getTimeRange().stream().anyMatch(searchSubRange ->
                                                (formalCaregiverSubRange.getStartTime().isBefore(searchSubRange.getStartTime()) ||
                                                        formalCaregiverSubRange.getStartTime().equals(searchSubRange.getStartTime()))
                                                        &&
                                                        (formalCaregiverSubRange.getEndTime().isAfter(searchSubRange.getEndTime()) ||
                                                                formalCaregiverSubRange.getEndTime().equals(searchSubRange.getEndTime()))
                                        ));
                            }
                            return false;
                        }));
            }).toList();

        }catch(Exception e){
            log.warning("Error buscando cuidadores formales por rango de dias/horas. " + e.getMessage());
            throw new FormalCaregiverFindDateTimeRangeException(
                    "Error buscando cuidadores formales por rango de dias/horas");
        }
    }

    private String getStartUrl(){
        return paramConfig.getProtocol() + "://" + paramConfig.getSocket() + "/";
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

    // Valida que 'votes' no tenga votos negativos
    /*private void validateVote(int[] votes){
        for (int vote : votes) {
            if (vote < 0)
                throw new FormalCaregiverValidateVoteException(
                        "FormalCaregiverService: la clave 'votes' debe contener dígitos enteros positivos o 0");
        }
    }*/

    // Asigna los valores por default a la entitdad
    private void defaultValues(FormalCaregiver formalCaregiver){
        formalCaregiver.setVotes(new int[] {0,0,0,0,0});
        formalCaregiver.setAvailable(true);
        formalCaregiver.setDeleted(false);
    }

    // Asigna los valores a la nueva entitdad, tomados de la vieja entidad (de la persistida)
    private void defaultValues(FormalCaregiver oldFormalCaregiver, FormalCaregiver newFormalCaregiver){
        newFormalCaregiver.setVotes(oldFormalCaregiver.getVotes());
        newFormalCaregiver.setAvailable(oldFormalCaregiver.getAvailable());
        newFormalCaregiver.setDeleted(oldFormalCaregiver.getDeleted());
    }


}
