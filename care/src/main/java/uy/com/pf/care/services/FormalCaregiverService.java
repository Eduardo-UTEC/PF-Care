package uy.com.pf.care.services;

import com.mongodb.client.result.UpdateResult;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.infra.config.ParamConfig;
import uy.com.pf.care.model.documents.FormalCaregiver;
import uy.com.pf.care.model.enums.RoleEnum;
import uy.com.pf.care.model.globalFunctions.UpdateEntityId;
import uy.com.pf.care.model.objects.DayTimeRangeObject;
import uy.com.pf.care.model.objects.NeighborhoodObject;
import uy.com.pf.care.infra.repos.IFormalCaregiverRepo;

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
    @Autowired
    UpdateEntityId updateEntityId;

    //Se define aqui para controlar la excepcion, estableciendo si se debe eliminar el Cuidador Formal
    String newFormalCaregiverId = null;

    @Override
    public String save(FormalCaregiver formalCaregiver) {
        try {
            this.defaultValues(formalCaregiver);
            newFormalCaregiverId = formalCaregiverRepo.save(formalCaregiver).getFormalCaregiverId();
            //log.info("Cuidador Formal guardado con exito");
            //return newFormalCaregiverId;

            ResponseEntity<Boolean> response = updateEntityId.execute(
                    formalCaregiver.getUserId(), RoleEnum.FORMAL_CARE.getOrdinal(), newFormalCaregiverId);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Cuidador Formal guardado con exito");
                return newFormalCaregiverId;
            }

            String msg = "Error actualizando entityId en el rol Cuidador Formal, en documento Users";
            log.warning(msg);
            throw new UserUpdateEntityIdInRolesListException(msg);

            //En caso de excepción, si el nuevo cuidador formal fue persistido, se elimina, evitando
            // inconsistencia de la bbdd
        } catch (UserUpdateEntityIdInRolesListException e) {
            if (newFormalCaregiverId != null)
                this.physicallyDeleteFormalCaregiver(newFormalCaregiverId);
            throw new UserUpdateEntityIdInRolesListException(e.getMessage());
        } catch (Exception e) {
            if (newFormalCaregiverId != null)
                this.physicallyDeleteFormalCaregiver(newFormalCaregiverId);
            String msg = "*** ERROR GUARDANDO PACIENTE";
            log.warning(msg + ": " + e.getMessage());
            throw new PatientSaveException(msg);
        }
    }

    @Override
    public Boolean update(FormalCaregiver newFormalCaregiver) {
        try {
            Optional<FormalCaregiver> entityFound = formalCaregiverRepo.findById(newFormalCaregiver.getFormalCaregiverId());
            if (entityFound.isPresent()) {
                this.defaultValues(entityFound.get(), newFormalCaregiver);
                formalCaregiverRepo.save(newFormalCaregiver);
                log.info("Cuidador formal actualizado con exito");
                return true;
            }

            String msg = "No se encontro el cuidador formal con id " + newFormalCaregiver.getFormalCaregiverId();
            log.info(msg);
            throw new FormalCaregiverNotFoundException(msg);

        } catch (FormalCaregiverNotFoundException e) {
            throw new FormalCaregiverNotFoundException(e.getMessage());
        } catch (Exception e) {
            String msg = "Error actualizando Cuidador Formal";
            log.warning(msg + ": " + e.getMessage());
            throw new FormalCaregiverUpdateException(msg);
        }
    }

    /*  Devuelve true si la operación fue exitosa.
        1. La disponibilidad de un cuidador formal la establece el mismo.
        2. Si el cuidador formal esta eliminado, no es posible estabelcer su disponibilidad.
     */
    @Override
    public Boolean setAvailability(String id, Boolean isAvailable) {
        try {
            Optional<FormalCaregiver> formalCaregiver = this.findId(id);
            if (formalCaregiver.isPresent() && !formalCaregiver.get().getDeleted()) {
                formalCaregiver.get().setAvailable(isAvailable);
                formalCaregiverRepo.save(formalCaregiver.get());
                return true;
            }
            String msg = "No se encontro un cuidador formal con id " + id;
            log.warning(msg);
            throw new FormalCaregiverNotFoundException(msg);

        } catch (FormalCaregiverNotFoundException e) {
            throw new FormalCaregiverNotFoundException(e.getMessage());
        } catch (Exception e) {
            String msg = "No se pudo setear la disponibilidad del cuidador formal con id: " + id;
            log.warning(msg + ": " + e.getMessage());
            throw new FormalCaregiverSetAvailabilityException(msg);
        }
    }

    @Override
    public Boolean setValidation(String id, Boolean isValidated) {
        try {
            Optional<FormalCaregiver> formalCaregiverFound = this.findId(id);
            if (formalCaregiverFound.isPresent()) {
                formalCaregiverFound.get().setValidate(isValidated);
                formalCaregiverRepo.save(formalCaregiverFound.get());
                return true;
            }
            String msg = "No se encontro un cuidador formal con id " + id;
            log.warning(msg);
            throw new FormalCaregiverNotFoundException(msg);

        }catch (FormalCaregiverNotFoundException e) {
            throw new FormalCaregiverNotFoundException(e.getMessage());
        }catch (Exception e) {
            String msg = "*** ERROR VALIDANDO CUIDADOR FORMAL CON ID " + id;
            log.warning(msg + ": " + e.getMessage());
            throw new FormalCaregiverSetValidationException(msg);
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
            String msg = "No se encontro un cuidador formal con id " + id;
            log.warning(msg);
            throw new FormalCaregiverNotFoundException(msg);

        }catch (FormalCaregiverNotFoundException e) {
            throw new FormalCaregiverNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "No se pudo setear el borrado lógico del cuidador formal con id: " + id;
            log.warning( msg + ": " + e.getMessage());
            throw new FormalCaregiverSetDeletionException(msg);
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
            String msg = "No se encontro un cuidador formal con id " + formalCaregiverId;
            log.warning(msg);
            throw new FormalCaregiverNotFoundException(msg);

        }catch (FormalCaregiverNotFoundException e) {
            throw new FormalCaregiverNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "No se pudo actualizar votos del cuidador formal con id: " + formalCaregiverId;
            log.warning(msg + ": " + e.getMessage());
            throw new FormalCaregiverUpdateVotesException(msg);
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
    public List<FormalCaregiver> findAll(Boolean withoutValidate, Boolean includeDeleted, String countryName) {
        try{
            if (withoutValidate) {
                if (includeDeleted)
                    return formalCaregiverRepo.findByCountryNameAndValidateFalse(countryName);

                return formalCaregiverRepo.findByCountryNameAndValidateFalseAndDeletedFalse(countryName);
            }
            // solo los validados
            if (includeDeleted)
                return formalCaregiverRepo.findByCountryNameAndValidateTrue(countryName);

            return formalCaregiverRepo.findByCountryNameAndValidateTrueAndDeletedFalse(countryName);

        }catch (Exception e){
            String msg = "Error buscando todos los cuidadores formales de " + countryName;
            log.warning(msg + ": " + e.getMessage());
            throw new FormalCaregiverFindAllException(msg);
        }
    }

    @Override
    public Optional<FormalCaregiver> findId(String id) {
        try{
            Optional<FormalCaregiver> found = formalCaregiverRepo.findById(id);
            if (found.isPresent())
                return found;

            String msg = "No se encontro un cuidador formal con id " + id;
            log.warning(msg);
            throw new FormalCaregiverNotFoundException(msg);

        }catch (FormalCaregiverNotFoundException e) {
            throw new FormalCaregiverNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "Error buscando cuidador formal con id: " + id;
            log.warning(msg + ": " + e.getMessage());
            throw new FormalCaregiverFindIdException(msg);
        }
    }

    @Override
    public FormalCaregiver findMail(String mail) {
        try{
            FormalCaregiver found = formalCaregiverRepo.findByMailIgnoreCase(mail);
            if (found != null)
                return found;

            String msg = "No se encontro un cuidador formal con email " + mail;
            log.warning(msg);
            throw new FormalCaregiverNotFoundException(msg);

        }catch (FormalCaregiverNotFoundException e) {
            throw new FormalCaregiverNotFoundException(e.getMessage());
        }catch(Exception e){
            log.warning("Error buscando cuidador formal con mail: " + mail + ". " + e.getMessage());
            throw new FormalCaregiverFindMailException("Error buscando cuidador formal con mail: " + mail);
        }
    }

    @Override
    public List<FormalCaregiver> findName(Boolean withoutValidate, Boolean includeDeleted, String countryName, String name) {
        try{
            if (withoutValidate) {
                if (includeDeleted)
                    return formalCaregiverRepo.findByCountryNameAndValidateFalseAndNameIgnoreCase(countryName, name);

                return formalCaregiverRepo.findByCountryNameAndNameIgnoreCaseAndValidateFalseAndDeletedFalse(
                        countryName, name);
            }
            // solo los validados
            if (includeDeleted)
                return formalCaregiverRepo.findByCountryNameAndValidateTrueAndNameIgnoreCase(countryName, name);

            return formalCaregiverRepo.findByCountryNameAndNameIgnoreCaseAndValidateTrueAndDeletedFalse(
                    countryName, name);

        }catch(Exception e){
            String msg = "Error buscando cuidador formal: " + name + " (" + countryName + ")";
            log.warning(msg + ": " + e.getMessage());
            throw new FormalCaregiverFindNameException(msg);
        }
    }

    @Override
    public List<FormalCaregiver> findNameLike(Boolean witoutValidate, Boolean includeDeleted, String countryName, String name) {
        try{
            if (witoutValidate) {
                if (includeDeleted)
                    return formalCaregiverRepo.findByCountryNameAndNameLikeIgnoreCaseAndValidateFalse(countryName, name);

                return formalCaregiverRepo.findByCountryNameAndNameLikeIgnoreCaseAndValidateFalseAndDeletedFalse(
                        countryName, name);
            }
            //solo los validados
            if (includeDeleted)
                return formalCaregiverRepo.findByCountryNameAndNameLikeIgnoreCaseAndValidateTrue(countryName, name);

            return formalCaregiverRepo.findByCountryNameAndNameLikeIgnoreCaseAndValidateTrueAndDeletedFalse(
                    countryName, name);

        }catch(Exception e){
            String msg = "Error buscando cuidadores formales de nombre: " + name + " (" + countryName + ")";
            log.warning(msg + ": " + e.getMessage());
            throw new FormalCaregiverFindNameLikeException(msg);
        }
    }

    @Override
    public List<FormalCaregiver> findInterestZones_Neighborhood(
            Boolean withoutValidate,
            Boolean includeDeleted,
            String interestNeighborhoodName,
            String interestCityName,
            String interestDepartmentName,
            String countryName) {

        try{
            RestTemplate restTemplate = new RestTemplate();
            NeighborhoodObject[] neighborhoods = restTemplate.getForEntity(
                    getUrlNeighborhoods(withoutValidate, includeDeleted, interestCityName, interestDepartmentName, countryName),
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
                listReturn = this.findAll(withoutValidate, includeDeleted, countryName).stream().filter(
                        formalCaregiver -> formalCaregiver.getInterestZones().isEmpty() ||
                                !formalCaregiver.getInterestZones().stream().filter(interestZonesObject ->
                                        interestZonesObject.getDepartmentName().equals(interestDepartmentName) &&
                                                (interestZonesObject.getCities().isEmpty() ||
                                                        !interestZonesObject.getCities().stream().filter(cityObject ->
                                                                cityObject.getCityName().equals(interestCityName) &&
                                                                        (cityObject.getNeighborhoodNames().isEmpty() ||
                                                                                cityObject.getNeighborhoodNames().
                                                                                        contains(interestNeighborhoodName))
                                                                                //cityObject.getNeighborhoodNames().stream()
                                                                                //        .anyMatch(name ->
                                                                                //                name.equalsIgnoreCase(interestNeighborhoodName))                                                                        )
                                                        ).toList().isEmpty())
                                ).toList().isEmpty()
                ).toList();
            }
            return listReturn;

        }catch(Exception e){
            String msg = "Error buscando cuidadores formales por zona de interés en barrio: "
                    + interestNeighborhoodName + " (" + interestCityName+ ", " + interestDepartmentName + ", "
                    + countryName + ")";
            log.warning( msg + ": " + e.getMessage());
            throw new FormalCaregiverFindInterestZones_NeighborhoodException(msg);
        }
    }

    @Override
    public List<FormalCaregiver> findInterestZones_City(
            Boolean validateCity,
            Boolean withoutValidate,
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
                        getUrlCities(withoutValidate, includeDeleted, interestDepartmentName, countryName), String[].class)
                        .getBody();

            // Si se desea validar la ciudad, verifico que la ciudad exista
            if (!validateCity ||
                    (cities != null && cities.length > 0 && Arrays.asList(cities).contains(interestCityName))){

                // TODO: Testear cual de los dos filtros previos es el mas eficiente (tomando por departamento o findAll)
                //listReturn = this.findInterestZones_Department(
                //       false, includeDeleted, interestDepartmentName, countryName).stream().filter(
                listReturn = this.findAll(withoutValidate, includeDeleted, countryName).stream().filter(
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
            String msg = "Error buscando cuidadores formales por zona de interés en ciudad: " + interestCityName +
                    " (" + interestDepartmentName + ", " + countryName + ")";
            log.warning(msg + ": " + e.getMessage());
            throw new FormalCaregiverFindInterestZones_CityException(msg);
        }
    }

    @Override
    public List<FormalCaregiver> findInterestZones_Department(
            Boolean validateInterestDepartment,
            Boolean withoutValidate,
            Boolean includeDeleted,
            String interestDepartmentName,
            String countryName) {

        try{
            RestTemplate restTemplate = new RestTemplate();
            String[] departments = null;
            List<FormalCaregiver> listReturn = new ArrayList<>();

            if (validateInterestDepartment)
                departments = restTemplate.
                        getForEntity(getUrlDepartments(withoutValidate, includeDeleted, countryName), String[].class)
                        .getBody();

            // Si se desea validar el Departamento de Interes, verifico que el departamento exista
            if (!validateInterestDepartment ||
                    (departments != null &&
                            departments.length > 0 &&
                            Arrays.asList(departments).contains(interestDepartmentName))){

                listReturn = this.findAll(withoutValidate, includeDeleted, countryName).stream().filter(formalCaregiver ->
                                formalCaregiver.getInterestZones().isEmpty() ||
                                        ! formalCaregiver.getInterestZones().stream().filter(interestZonesObject ->
                                                interestZonesObject.getDepartmentName().equals(interestDepartmentName)
                                        ).toList().isEmpty())
                        .toList();
            }
            return listReturn;

        }catch(Exception e){
            String msg = "Error buscando cuidadores formales por zona de interés en departamento/provincia: " +
                    interestDepartmentName + " (" + countryName + ")";
            log.warning(msg + ": " + e.getMessage());
            throw new FormalCaregiverFindInterestZones_DepartmentException(msg);
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
                            false,
                            interestNeighborhoodName,
                            interestCityName,
                            interestDepartmentName,
                            countryName)
                    .stream().filter(formalCaregiver -> formalCaregiver.getPriceHour() <= maxPrice).toList();

        }catch(Exception e){
            String msg = "Error buscando cuidadores formales por rango de precios";
            log.warning( msg + ": " + e.getMessage());
            throw new FormalCaregiverFindPriceRangeException(msg);
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
                        true,
                        false,
                        false,
                        interestCityName,
                        interestDepartmentName,
                        countryName
                );
            else
                formalCaregiversList = this.findInterestZones_Neighborhood(
                        false,
                        false,
                        interestNeighborhoodName,
                        interestCityName,
                        interestDepartmentName,
                        countryName
                );

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
            String msg = "Error buscando cuidadores formales por rango de dias/horas";
            log.warning(msg + ": " + e.getMessage());
            throw new FormalCaregiverFindDateTimeRangeException(msg);
        }
    }

    private String getStartUrl(){
        return paramConfig.getProtocol() + "://" + paramConfig.getSocket() + "/";
    }

    private String getUrlNeighborhoods(
            Boolean withoutValidate, Boolean includeDeleted, String cityName, String departmentName, String countryName) {

        return getStartUrl() +
                "zones/findNeighborhoods/" +
                withoutValidate.toString() + "/" +
                includeDeleted.toString() + "/" +
                cityName + "/" +
                departmentName + "/" +
                countryName;
    }

    //"zones/findCities/false/" + // Se incluyen ciudades que no estén eliminadas
    private String getUrlCities(Boolean withoutValidate, Boolean includeDeleted, String departmentName, String countryName){
        return  getStartUrl() +
                "zones/findCities/" +
                withoutValidate.toString() + "/" +
                includeDeleted.toString() + "/" +
                departmentName + "/" +
                countryName;
    }

    private String getUrlDepartments(Boolean withoutValidate, Boolean includeDeleted, String countryName){
        return  getStartUrl() +
                "zones/findDepartments/" +
                withoutValidate.toString() + "/" +
                includeDeleted.toString() + "/" +
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
        formalCaregiver.setAvailable(false);
        formalCaregiver.setDeleted(false);
    }

    // Asigna los valores a la nueva entitdad, tomados de la vieja entidad (de la persistida)
    private void defaultValues(FormalCaregiver oldFormalCaregiver, FormalCaregiver newFormalCaregiver){
        newFormalCaregiver.setVotes(oldFormalCaregiver.getVotes());
        newFormalCaregiver.setAvailable(oldFormalCaregiver.getAvailable());
        newFormalCaregiver.setDeleted(oldFormalCaregiver.getDeleted());
    }

    private void physicallyDeleteFormalCaregiver(String id){
        try{
            formalCaregiverRepo.deleteById(id);
            log.warning("Se borro fisicamente el cuidador formal con id " + id);

        }catch(IllegalArgumentException e){
            log.warning("No se pudo eliminar el cuidador formal con Id: " + id + ". El cuidador formal " +
                    "seguramente no ha quedado vinculado a un usuario (coleccion Users) con el rol FORMAL_CARE. " +
                    "Si es asi, copie el Id del cuidador formal en la clave 'entityId' correspopndiente al rol del " +
                    "usuario, en la coleccion Users. Alternativamente, puede eliminar el documento del cuidador formal " +
                    "e ingresarlo nuevamente.");
            throw new FormalCaregiverPhysicallyDeleteException("No se pudo eliminar el cuidador formal con Id: " + id);
        }
    }


}
