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
import uy.com.pf.care.infra.repos.IVolunteerPersonRepo;
import uy.com.pf.care.model.documents.Patient;
import uy.com.pf.care.model.documents.VolunteerActivity;
import uy.com.pf.care.model.documents.VolunteerPerson;
import uy.com.pf.care.model.enums.RoleEnum;
import uy.com.pf.care.model.globalFunctions.ForceEnumsToVolunteerPerson;
import uy.com.pf.care.model.globalFunctions.MostDemandedServicesVolunteerRequestStats;
import uy.com.pf.care.model.globalFunctions.SetMatch;
import uy.com.pf.care.model.globalFunctions.UpdateEntityId;
import uy.com.pf.care.model.objects.DayTimeRangeObject;
import uy.com.pf.care.model.objects.NeighborhoodObject;
import uy.com.pf.care.model.objects.VolunteerPersonMatchObject;

import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log
public class VolunteerPersonService implements IVolunteerPersonService {
    @Autowired
    private IVolunteerPersonRepo volunteerPersonRepo;
    @Autowired
    private UpdateEntityId updateEntityId;
    @Autowired
    private SetMatch setMatch;
    @Autowired
    private ParamConfig paramConfig;
    @Autowired
    private VolunteerActivityService volunteerActivity;

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
            if (newVolunteerPersonId != null)
                this.physicallyDeleteVolunteerPerson(newVolunteerPersonId);
            String msg = "*** ERROR GUARDANDO PERSONA VOLUNTARIA";
            log.warning(msg + ": " + e.getMessage());
            throw new VolunteerPersonSaveException(msg);
        }
    }

    @Override
    public String update(VolunteerPerson newVolunteerPerson) {
        try {
            Optional<VolunteerPerson> entityFound = volunteerPersonRepo.findById(newVolunteerPerson.getVolunteerPersonId());
            if (entityFound.isPresent()) {
                this.defaultValues(newVolunteerPerson, entityFound.get());
                ForceEnumsToVolunteerPerson.execute(newVolunteerPerson);
                String id = volunteerPersonRepo.save(newVolunteerPerson).getVolunteerPersonId();
                log.info("Persona Voluntaria actualizada con exito");
                return id;
            }
            this.notFound(newVolunteerPerson.getVolunteerPersonId());
            return null;

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
    public Boolean addVolunteerActivitiesId(String volunteerPersonId, List<String> volunteerActivitiesId) {
        if (volunteerActivitiesId.isEmpty()) return false;
        try {
            Optional<VolunteerPerson> found = volunteerPersonRepo.findById(volunteerPersonId);
            if (found.isPresent()) {
                AtomicInteger count = new AtomicInteger(0);
                volunteerActivitiesId.forEach(activityId -> {
                    if (!found.get().getVolunteerActivitiesId().contains(activityId)) {
                        found.get().getVolunteerActivitiesId().add((activityId));
                        count.incrementAndGet();
                    }
                });

                if (count.get() > 0) {
                    volunteerPersonRepo.save(found.get());
                    log.warning(count.get() == volunteerActivitiesId.size() ?
                            "Actividades agregadas exitosamente al Voluntario." :
                            "Actividades agregadas correctamente (hubo " + (volunteerActivitiesId.size() - count.get()) +
                                    " actividades que ya estaban asignadas)");
                    return true;
                } else {
                    log.warning("Las actividades ya estaban asignadas al Voluntario.");
                    return false;
                }
            }
            this.notFound(volunteerPersonId);
            return null;

        }catch (VolunteerPersonNotFoundException e){
            throw new VolunteerPersonNotFoundException(e.getMessage());
        }catch (Exception e){
            String msg = "*** ERROR AGREGANDO ROLES AL VIDEO";
            log.warning(msg + ": " +e.getMessage());
            throw new VolunteerPersonAddVolunteerActivitiesIdException(msg);
        }

    }

    @Override
    public Boolean changeVolunteerActivityId(
            String volunteerPersonId, String oldVolunteerActivityId, String newVolunteerActivityId) {
        try {
            Optional<VolunteerPerson> found = volunteerPersonRepo.findById(volunteerPersonId);
            if (found.isPresent()) {
                if (found.get().getVolunteerActivitiesId().contains(newVolunteerActivityId)) {
                    String msg = "El Voluntario ya tiene asignada la actividad '" + newVolunteerActivityId +"'";
                    log.warning(msg);
                    throw new VolunteerPersonActivityAlreadyLinkedException(msg);
                }
                int index = found.get().getVolunteerActivitiesId().indexOf(oldVolunteerActivityId);
                if (index >= 0) {
                    found.get().getVolunteerActivitiesId().set(index, newVolunteerActivityId);
                    volunteerPersonRepo.save(found.get());
                    log.warning("Actividad cambiada con exito.");
                    return true;
                }
                String msg = "La actividad que se pretende cambiar (" + oldVolunteerActivityId + ")" +
                        " no esta asignada al Voluntario " + newVolunteerActivityId;
                log.warning(msg);
                throw new VolunteerPersonActivityNotLinkedException(msg);
            }
            this.notFound(volunteerPersonId);
            return null;

        }catch(VolunteerPersonActivityAlreadyLinkedException e){
            throw new VolunteerPersonActivityAlreadyLinkedException(e.getMessage());
        }catch(VolunteerPersonActivityNotLinkedException e){
            throw new VolunteerPersonActivityNotLinkedException(e.getMessage());
        }catch(VolunteerPersonNotFoundException e){
            throw new VolunteerPersonNotFoundException(e.getMessage());
        }catch (Exception e){
            String msg = "*** ERROR CAMBIANDO ACTIVIDAD DEL VOLUNTARIO";
            log.warning(msg + ": " + e.getMessage());
            throw new VolunteerPersonChangeActivityException(msg);
        }
    }

    @Override
    public Boolean delVolunteerActivitiesId(String volunteerPersonId, List<String> volunteerActivitiesId) {
        if (volunteerActivitiesId.isEmpty()) return false;
        try {
            Optional<VolunteerPerson> found = volunteerPersonRepo.findById(volunteerPersonId);
            if (found.isPresent()) {
                int sizeVolunteerActivitiesId = found.get().getVolunteerActivitiesId().size();
                volunteerActivitiesId.forEach(activityId -> {
                    found.get().getVolunteerActivitiesId().removeIf(savedActivityOrdinal ->
                            Objects.equals(savedActivityOrdinal, activityId));
                });
                int erased = sizeVolunteerActivitiesId - found.get().getVolunteerActivitiesId().size();
                if (erased == 0){
                    log.warning("Las actividades proporcionadas no pertenecen al Voluntario.");
                    return false;
                }
                volunteerPersonRepo.save(found.get());
                if (erased == volunteerActivitiesId.size())
                    log.warning("Las actividades fueron eliminadas del Voluntario.");
                else
                    log.warning(erased + " actividades eliminadas del Voluntario (" +
                            (volunteerActivitiesId.size() - erased) + " actividades no pertenecían al Voluntario)");
                return true;
            }
            this.notFound(volunteerPersonId);
            return null;

        } catch (VolunteerPersonNotFoundException e) {
            throw new VolunteerPersonNotFoundException(e.getMessage());
        } catch (Exception e) {
            String msg = "*** ERROR BORRANDO ACTIVIDADES DEL VOLUNTARIO";
            log.warning(msg + ": " + e.getMessage());
            throw new VolunteerPersonDelActivitiesException(msg);
        }
    }

    @Override
    public Boolean setAvailability(String id, Boolean isAvailable) {
        try {
            Optional<VolunteerPerson> volunteerPerson = this.findId(id);
            if (volunteerPerson.isPresent()) {
                if (volunteerPerson.get().getDeleted()) {
                    log.warning("La Persona Voluntaria está eliminada");
                    return false;
                }
                volunteerPerson.get().setAvailable(isAvailable);
                volunteerPersonRepo.save(volunteerPerson.get());
                log.info("Se estableció la disponiblidad de la persona voluntaria con id " + id + " como " +
                        (isAvailable ? "DISPONIBLE" : "NO DISPONIBLE"));
                return true;
            }
            this.notFound(id);
            return false;

        }catch (VolunteerPersonNotFoundException e){
            throw new VolunteerPersonNotFoundException(e.getMessage());
        }catch(Exception e){
            log.warning("No se pudo setear la disponibilidad de la persona voluntaria con id: " + id + ". "
                    + e.getMessage());
            throw new VolunteerPersonSetAvailabilityException(
                    "No se pudo setear la disponibilidad de la persona voluntaria con id: " + id + ". ");
        }
    }

    @Override
    public Boolean setValidation(String id, Boolean isValidated) {
        try {
            Optional<VolunteerPerson> volunteerPersonFound = this.findId(id);
            if (volunteerPersonFound.isPresent()) {
                volunteerPersonFound.get().setValidate(isValidated);
                volunteerPersonRepo.save(volunteerPersonFound.get());
                log.info("Se validó la persona voluntaria con id " + id + " como " +
                        (isValidated ? "VALIDADA" : "NO VALIDADA"));
                return true;
            }
            this.notFound(id);
            return false;

        }catch(VolunteerPersonNotFoundException e){
            throw new VolunteerPersonNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR VALIDANDO PERSONA VOLUNTARIA";
            log.warning(msg + ": " + e.getMessage());
            throw new VolunteerPersonSetValidationException(msg);
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
                log.info("Se " + (isDeleted ? "eliminó" : "restauró") + " la persona voluntaria con id " + id);
                return true;
            }
            this.notFound(id);
            return false;

        }catch (VolunteerPersonNotFoundException e){
            throw new VolunteerPersonNotFoundException(e.getMessage());
        }catch(Exception e){
            log.warning("No se pudo setear el borrado lógico de la persona voluntaria con id: " + id + ". "
                    + e.getMessage());
            throw new VolunteerPersonSetDeletionException(
                    "No se pudo setear el borrado lógico de la persona voluntaria con id " + id);
        }
    }

    @Override
    public Boolean receivePatientRequest (String volunteerPersonId, String patientId) {
        try {
            Optional<VolunteerPerson> volunteerPerson = this.findId(volunteerPersonId);
            if (volunteerPerson.isPresent()) {

                if (volunteerPerson.get().getMatchPatientsId().contains(patientId)) {
                    String msg = "El paciente ya estaba matcheado.";
                    log.info(msg);
                    throw new VolunteerPersonPatientAlreadyMatchException(msg);
                }
                if (volunteerPerson.get().getMatchRequestPatientsId().contains(patientId)) {
                    String msg = "El paciente ya había enviado una solicitud.";
                    log.info(msg);
                    throw new VolunteerPersonPatientAlreadyRequestException(msg);
                }
                volunteerPerson.get().getMatchRequestPatientsId().add(patientId);
                volunteerPersonRepo.save(volunteerPerson.get());
                log.info("Se agregó con éxito la solicitud de contacto del paciente con la persona voluntaria");
                return true;

            } else {
                String msg = "Persona voluntaria con id " + volunteerPersonId + " no encontrada.";
                log.info(msg);
                throw new VolunteerPersonNotFoundException(msg);
            }

        } catch (VolunteerPersonPatientAlreadyMatchException e) {
            throw new VolunteerPersonPatientAlreadyMatchException(e.getMessage());
        } catch (VolunteerPersonPatientAlreadyRequestException e) {
            throw new VolunteerPersonPatientAlreadyRequestException(e.getMessage());
        } catch (VolunteerPersonNotFoundException e) {
            throw new VolunteerPersonNotFoundException(e.getMessage());
        } catch (Exception e) {
            String msg = "Error seteando solicitud de contacto del paciente con la persona voluntaria";
            log.warning(msg + ": " + e.getMessage());
            throw new VolunteerPersonRecievePatientRequestException(msg);
        }
    }


    @Override
    public Boolean setMatchPatient(String volunteerPersonId, String patientId, Boolean isMatch) {
        try {
            Optional<VolunteerPerson> volunteerPerson = this.findId(volunteerPersonId);
            if (volunteerPerson.isPresent()) {

                //Esta matcheado?
                if (volunteerPerson.get().getMatchPatientsId().contains(patientId)) {
                    if (isMatch) {
                        String msg = "El paciente ya estaba matcheado.";
                        log.info(msg);
                        throw new VolunteerPersonPatientAlreadyMatchException(msg);

                        //Deja de estar matcheado
                    } else {

                        //Cancelo el match en "paciente" y actualizo la fecha de confirmacion, indicando así que
                        //es un "rechazo de solicitud"
                        ResponseEntity<Boolean> response =
                                setMatch.execute(patientId, volunteerPersonId, RoleEnum.VOLUNTEER_PERSON, false);

                        if (response.getStatusCode() == HttpStatus.OK) {
                            //Quito el match en "voluntario"
                            volunteerPerson.get().getMatchPatientsId().remove(patientId);
                            volunteerPersonRepo.save(volunteerPerson.get());
                            log.info("Match cancelado con éxito del lado del voluntario");
                            return true;

                        } else
                            return false;
                    }
                }

                //Ya hay una solicitud de matcheo?
                if (volunteerPerson.get().getMatchRequestPatientsId().contains(patientId)) {
                    if (! isMatch) {

                        //Cancelo el match en "paciente" y actualizo la fecha de confirmacion, indicando así que
                        //es un "rechazo de solicitud"
                        ResponseEntity<Boolean> response =
                                setMatch.execute(patientId, volunteerPersonId, RoleEnum.VOLUNTEER_PERSON, false);

                        if (response.getStatusCode() == HttpStatus.OK) {
                            //Quita la solicitud de matcheo en Voluntario
                            volunteerPerson.get().getMatchRequestPatientsId().remove(patientId);
                            volunteerPersonRepo.save(volunteerPerson.get());
                            log.info("Solicitud de matcheo quitada con éxito del lado del voluntario");
                            return true;

                        } else {
                            return false;
                        }
                    }

                } else {
                    String msg = "No hay una solicitud registrada del paciente " + patientId +
                            " con el voluntario " + volunteerPersonId;
                    log.info(msg);
                    throw new VolunteerPersonPatientNotRequestException(msg);
                }

                //Confirma el match del lado del paciente
                ResponseEntity<Boolean> response =
                        setMatch.execute(patientId, volunteerPersonId, RoleEnum.VOLUNTEER_PERSON, true);

                if (response.getStatusCode() == HttpStatus.OK) {
                    //Confirma el match del lado del voluntario
                    volunteerPerson.get().getMatchPatientsId().add(patientId);
                    volunteerPerson.get().getMatchRequestPatientsId().remove(patientId);
                    volunteerPersonRepo.save(volunteerPerson.get());
                    log.info("Match realizado con éxito del lado de la persona voluntaria");
                    return true;
                } else
                    return false;

            } else {
                String msg = "Persona voluntaria con id " + volunteerPersonId + " no encontrada.";
                log.info(msg);
                throw new VolunteerPersonNotFoundException(msg);
            }

        } catch (VolunteerPersonPatientAlreadyMatchException e) {
            throw new VolunteerPersonPatientAlreadyMatchException(e.getMessage());
        } catch (VolunteerPersonPatientNotRequestException e) {
            throw new VolunteerPersonPatientNotRequestException(e.getMessage());
        } catch (VolunteerPersonNotFoundException e) {
            throw new VolunteerPersonNotFoundException(e.getMessage());
        } catch (Exception e) {
            String msg = "Error seteando solicitud de contacto del paciente con la persona voluntaria";
            log.warning(msg + ": " + e.getMessage());
            throw new VolunteerPersonRecievePatientRequestException(msg);
        }
    }

    @Override
    public Optional<VolunteerPerson> findId(String id) {
        Optional<VolunteerPerson> found = volunteerPersonRepo.findById(id);
        if (found.isPresent())
            return found;

        this.notFound(id);
        return Optional.empty();
    }

    @Override
    public List<VolunteerPerson> findIds(List<String> volunteersPersonId) {
        return volunteerPersonRepo.findAllById(volunteersPersonId);
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
    public Optional<VolunteerPerson> findTelephone(String countryName, String telephone) {
        try{
            Optional<VolunteerPerson> found = volunteerPersonRepo.findByCountryNameAndTelephone(countryName, telephone);
            if (found.isPresent())
                return found;

            String msg = "No se encontro un Voluntario con teléfono " + telephone;
            log.warning(msg);
            throw new VolunteerPersonNotFoundException(msg);

        }catch (VolunteerPersonNotFoundException e) {
            throw new VolunteerPersonNotFoundException(e.getMessage());
        }catch(Exception e){
            log.warning("Error buscando voluntario con teléfono: " + telephone + ". " + e.getMessage());
            throw new VolunteerPersonFindTelephoneException("Error buscando voluntario con teléfono: " + telephone);
        }
    }

    @Override
    public List<VolunteerPerson> findAll(Boolean withoutValidate, Boolean includeDeleted, String countryName) {

        if (withoutValidate) {
            if (includeDeleted)
                return volunteerPersonRepo.findByCountryNameAndValidateFalseAndAvailableTrueOrderByName1(countryName);
            return volunteerPersonRepo.findByCountryNameAndValidateFalseAndDeletedFalseAndAvailableTrueOrderByName1(countryName);

        }else {
            if (includeDeleted)
                return volunteerPersonRepo.findByCountryNameAndValidateTrueAndAvailableTrueOrderByName1(countryName);
            return volunteerPersonRepo.findByCountryNameAndValidateTrueAndDeletedFalseAndAvailableTrueOrderByName1(countryName);
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

    //Devuelve las 4 actividades mas demandadas, ordenadas descendente
    @Override
    public Map<String, MostDemandedServicesVolunteerRequestStats> getMostDemandedVolunteerActivities(
            List<Patient> patients, long monthsAgo, long limit) {

        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(monthsAgo);

        Map<String, MostDemandedServicesVolunteerRequestStats> result = patients.stream()
                .flatMap(patient -> patient.getVolunteerPeople().stream())
                .filter(volunteerPersonMatchObject ->
                        volunteerPersonMatchObject.getShippingDate().isAfter(sixMonthsAgo))
                .flatMap(volunteerPersonMatchObject ->
                        this.findId(volunteerPersonMatchObject.getVolunteerPersonId()).stream())
                .flatMap(volunteerPerson -> volunteerActivity.findIds(volunteerPerson.getVolunteerActivitiesId()).stream()
                        .map(VolunteerActivity::getName)
                )
                .collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(e -> 1)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> new MostDemandedServicesVolunteerRequestStats(entry.getKey(), entry.getValue(), 0),
                        (e1, e2) -> e1, LinkedHashMap::new));

        // Calcular porcentajes
        int totalDemands = result.values().stream()
                .mapToInt(MostDemandedServicesVolunteerRequestStats::getDemandCount).sum();
        result.values().forEach(stats -> {
            int demandCount = stats.getDemandCount();
            double percentage = (double) demandCount / totalDemands * 100;
            stats.setDemandPercentage((int) Math.round(percentage));
        });

        return result;

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
                return volunteerPersonRepo.findByCountryNameAndValidateFalseAndAvailableTrueAndName1IgnoreCase(countryName, name1);
            return volunteerPersonRepo.findByCountryNameAndValidateFalseAndName1IgnoreCaseAndDeletedFalseAndAvailableTrue(
                    countryName, name1);

        }else{
            if (includeDeleted)
                return volunteerPersonRepo.findByCountryNameAndValidateTrueAndAvailableTrueAndName1IgnoreCase(countryName, name1);
            return volunteerPersonRepo.findByCountryNameAndValidateTrueAndName1IgnoreCaseAndDeletedFalseAndAvailableTrue(
                    countryName, name1);
        }
    }

    @Override
    public List<VolunteerPerson> findNameLike(
            Boolean withoutValidate, Boolean includeDeleted, String countryName, String name1) {

        if (withoutValidate) {
            if (includeDeleted)
                return volunteerPersonRepo.findByCountryNameAndValidateFalseAndAvailableTrueAndName1LikeIgnoreCase(
                        countryName, name1);
            return volunteerPersonRepo.findByCountryNameAndValidateFalseAndName1LikeIgnoreCaseAndDeletedFalseAndAvailableTrue(
                    countryName, name1);

        }else{
            if (includeDeleted)
                return volunteerPersonRepo.findByCountryNameAndValidateTrueAndAvailableTrueAndName1LikeIgnoreCase(
                        countryName, name1);
            return volunteerPersonRepo.findByCountryNameAndValidateTrueAndName1LikeIgnoreCaseAndDeletedFalseAndAvailableTrue(
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
            String countryName,
            List<String> excludedVolunteerIds) {

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
                        volunteerPerson -> {
                            if (excludedVolunteerIds != null && excludedVolunteerIds.contains(volunteerPerson.getVolunteerPersonId()))
                                return false;
                            else {
                                return volunteerPerson.getInterestZones().isEmpty() ||
                                        !volunteerPerson.getInterestZones().stream().filter(interestZonesObject ->
                                                interestZonesObject.getDepartmentName().equals(interestDepartmentName) &&
                                                        (interestZonesObject.getCities().isEmpty() ||
                                                                !interestZonesObject.getCities().stream().filter(cityObject ->
                                                                        cityObject.getCityName().equals(interestCityName) &&
                                                                                (cityObject.getNeighborhoodNames().isEmpty() ||
                                                                                        cityObject.getNeighborhoodNames().
                                                                                                contains(interestNeighborhoodName))
                                                                ).toList().isEmpty())
                                        ).toList().isEmpty();
                            }
                        }
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
            String countryName,
            List<String> excludedVolunteerIds) {

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
                        countryName,
                        excludedVolunteerIds);

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
        volunteerPerson.setRegistrationDate(LocalDate.now());
        volunteerPerson.setValidate(false);
        volunteerPerson.setDeleted(false);
        volunteerPerson.setMatchPatientsId(new ArrayList<>());
        volunteerPerson.setMatchRequestPatientsId(new ArrayList<>());
    }

    private void defaultValues(VolunteerPerson volunteerPerson, VolunteerPerson oldVolunteerPerson){
        volunteerPerson.setUserId(oldVolunteerPerson.getUserId());
        volunteerPerson.setRegistrationDate(oldVolunteerPerson.getRegistrationDate());
        volunteerPerson.setMatchPatientsId(oldVolunteerPerson.getMatchPatientsId());
        volunteerPerson.setMatchRequestPatientsId(oldVolunteerPerson.getMatchRequestPatientsId());
        volunteerPerson.setValidate(oldVolunteerPerson.getValidate());
        volunteerPerson.setDeleted(oldVolunteerPerson.getDeleted());
    }

    private void notFound(String volunteerPersonId){
        String msg = "No se encontro la Persona Voluntaria con id " + volunteerPersonId;
        log.info(msg);
        throw new VolunteerPersonNotFoundException(msg);
    }

}
