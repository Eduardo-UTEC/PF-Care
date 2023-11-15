package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.infra.repos.IPatientRepo;
import uy.com.pf.care.model.documents.Patient;
import uy.com.pf.care.model.dtos.MostDemandedServicesVolunteerDTO;
import uy.com.pf.care.model.dtos.StatisticPatientWithOthersDTO;
import uy.com.pf.care.model.enums.RoleEnum;
import uy.com.pf.care.model.globalFunctions.*;
import uy.com.pf.care.model.objects.VolunteerPersonMatchObject;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Math.round;

@Service
@Log
public class PatientService implements IPatientService{

    @Autowired
    private IPatientRepo patientRepo;
    @Autowired
    private UpdateEntityId updateEntityId;
    @Autowired
    private SetMatch setMatch;
    @Autowired
    private VolunteerPersonService volunteerPersonService;
    @Autowired
    private VolunteerActivityService volunteerActivityService;

    @Override
    public String save(Patient patient) {

        //Valido keys necesarias para el Save del nuevo paciente
        this.saveValidate(patient);

        //Se define aqui para controlar la excepcion, estableciendo si se debe eliminar el Paciente
        String newPatientId = null;

        try{
            this.defaultValues(patient);
            ForceEnumsToPatient.execute(patient);
            newPatientId = patientRepo.save(patient).getPatientId();

            ResponseEntity<Boolean> response = updateEntityId.execute(
                    patient.getUserId(), RoleEnum.PATIENT.getOrdinal(), newPatientId);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("*** Paciente guardado con exito");
                return newPatientId;
            }

            String msg = "Error actualizando entityId en el rol Paciente, en documento Users";
            log.warning(msg);
            throw new UserUpdateEntityIdInRolesListException(msg);

        //En caso de excepción, si el nuevo paciente fue persistido, se elimina, evitando inconsistencia de la bbdd
        }catch (UserUpdateEntityIdInRolesListException e){
            if (newPatientId != null)
                this.physicallyDeletePatient(newPatientId);
            throw new UserUpdateEntityIdInRolesListException(e.getMessage());
        }catch(Exception e){
            if (newPatientId != null)
                this.physicallyDeletePatient(newPatientId);

            String msg = "*** ERROR GUARDANDO PACIENTE: " + e.getMessage();
            log.warning(msg);
            throw new PatientSaveException(msg);
        }
    }

    @Override
    public Boolean update(Patient newPatient) {
        try {
            Optional<Patient> entityFound = patientRepo.findById(newPatient.getPatientId());
            if (entityFound.isPresent()) {
                this.defaultValues(entityFound.get(), newPatient);
                ForceEnumsToPatient.execute(newPatient);
                patientRepo.save(newPatient);
                log.info("Paciente actualizado con exito");
                return true;
            }
            String msg = "No se encontro el paciente con id " + newPatient.getPatientId();
            log.info(msg);
            throw new PatientNotFoundException(msg);

        }catch (PatientNotFoundException e){
            throw new PatientNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR ACTUALIZANDO PACIENTE";
            log.warning(msg +  ": " + e.getMessage());
            throw new PatientUpdateException(msg);
        }
    }

    //Devuelve un lista con los pacientes cuya Id no existe, con lo cual no pudieron actualizarse.
    @Override
    public List<String> updateReferenceCaregiverOnPatients(List<String> patientsId, String referenceCaregiverId) {
        List<String> ret = new ArrayList<>();
        for (String patientId : patientsId){
            Optional<Patient> patient = patientRepo.findById(patientId);
            if (patient.isPresent()) {
                patient.get().setReferenceCaregiverId(referenceCaregiverId);
                patientRepo.save(patient.get());
            }else
                ret.add(patientId);
        }
        return ret;
    }

    @Override
    public Boolean sendRequestVolunteerPerson(String patientId, String volunteerPersonId) {
        Optional<Patient> patient = patientRepo.findById(patientId);
        int ordinalVolunteer;

        try {
            if (patient.isPresent()) {
                //Envía solicitud de contacto al voluntario
                ResponseEntity<Boolean> response =
                        setMatch.execute(patientId, volunteerPersonId, RoleEnum.PATIENT, null);

                if (response.getStatusCode() == HttpStatus.OK) {
                    ordinalVolunteer = ordinalVolunteer(volunteerPersonId, patient.get().getVolunteerPeople());
                    if (ordinalVolunteer == -1) {
                        patient.get().getVolunteerPeople().add(
                                new VolunteerPersonMatchObject(
                                        volunteerPersonId, false, LocalDate.now(), null));
                        patientRepo.save(patient.get());
                        log.info("Solicitud de contacto del paciente " + patientId + " enviada con éxito al " +
                                "voluntario " + volunteerPersonId);

                    } else {
                        //Inicializa confirmationDate en null
                        patient.get().getVolunteerPeople().get(ordinalVolunteer).setConfirmationDate(null);
                        patientRepo.save(patient.get());
                        log.info("Se reenvió con éxito la solicitud de contacto del paciente " + patientId +
                                " al voluntario " + volunteerPersonId);
                    }
                    return true;

                } else
                    return false;

            } else {
                String msg = "No se encontró el paciente con id " + patientId;
                log.info(msg);
                return false;
            }

        } catch (SendRequestVolunteerPersonException e) {
            throw new SendRequestVolunteerPersonException(e.getMessage());
        } catch (SetMatchException e) {
            throw new SetMatchException(e.getMessage());
        }
    }

    @Override
    public Boolean setMatchVolunteerPerson(String patientId, String volunteerPersonId, Boolean isMatch) {
        int ordinalVolunteer;
        try {
            Optional<Patient> patient = this.findId(patientId);
            if (patient.isPresent()) {
                ordinalVolunteer = ordinalVolunteer(volunteerPersonId, patient.get().getVolunteerPeople());
                if ( ordinalVolunteer != -1) {
                    //Actualiza match y fecha de confirmacion.
                    //Si match=false y hay un confirmationDate, implica que el voluntario rechazó la solicitud
                    //de contacto. Si match=false y confirmationDate es null, el voluntario aún no aceptó ni rechazó.
                    patient.get().getVolunteerPeople().get(ordinalVolunteer).setMatch(isMatch);
                    patient.get().getVolunteerPeople().get(ordinalVolunteer).setConfirmationDate(LocalDate.now());
                    patientRepo.save(patient.get());
                    log.info("Match " + (isMatch ? "realizado" : "cancelado") + " con éxito del lado del paciente");
                    return true;
                }
                String msg = "El paciente " + patientId +  " no tiene una solicitud de contacto con la persona " +
                        "voluntaria " + volunteerPersonId;
                log.warning(msg);
                return false;

            } else {
                String msg = "No se encontro el paciente con id " + patientId;
                log.warning(msg);
                throw new PatientNotFoundException(msg);
            }

        } catch (PatientNotFoundException e) {
            throw new PatientNotFoundException(e.getMessage());
        } catch (Exception e) {
            String msg = "Error seteando el match de la persona voluntaria en el paciente";
            log.warning(msg + ": " + e.getMessage());
            throw new PatientSetMatchException(e.getMessage());
        }

    }

    @Override
    public Boolean setValidation(String id, Boolean isValidated) {
        Optional<Patient> patient = this.findId(id);
        if (patient.isPresent()) {
            patient.get().setValidate(isValidated);
            patientRepo.save(patient.get());
            return true;
        }
        String msg = "No se encontro el paciente con id " + id;
        log.warning(msg);
        throw new PatientNotFoundException(msg);
    }

    @Override
    public Boolean setDeletion(String id, Boolean isDeleted) {
        Optional<Patient> patient = this.findId(id);
        if (patient.isPresent()) {
            patient.get().setDeleted(isDeleted);
            patientRepo.save(patient.get());
            return true;
        }
        String msg = "No se encontro el paciente con id " + id;
        log.warning(msg);
        throw new PatientNotFoundException(msg);
    }

    @Override
    public List<Patient> findAll(Boolean withoutValidate, Boolean includeDeleted, String countryName) {

        if (withoutValidate) {
            if (includeDeleted)
                return patientRepo.findByZone_CountryNameAndValidateFalseOrderByName1(countryName);

            return patientRepo.findByZone_CountryNameAndValidateFalseAndDeletedFalseOrderByName1(countryName);

        } else {
            //solo los validados
            if (includeDeleted)
                return patientRepo.findByZone_CountryNameAndValidateTrueOrderByName1(countryName);

            return patientRepo.findByZone_CountryNameAndValidateTrueAndDeletedFalseOrderByName1(countryName);
        }
    }

    @Override
    public Optional<Patient> findId(String id) {
        return patientRepo.findById(id);
    }

    @Override
    public List<Patient> findIds(List<String> patientsId) {
        return patientRepo.findAllById(patientsId);
    }

    @Override
    public Optional<Patient> findIdentificationDocument(Integer identificationDocument, String countryName) {
        return patientRepo.findByIdentificationDocumentAndZone_CountryName(identificationDocument, countryName);
    }

    @Override
    public Optional<Patient> findMail(String mail) {
        return patientRepo.findByMailIgnoreCase(mail);
    }

    @Override
    public List<Patient> findName1(
            String name1,
            Boolean withoutValidate,
            Boolean includeDeleted,
            String neighborhoodName,
            String cityName,
            String departmentName,
            String countryName) {

        if (withoutValidate) {
            if (includeDeleted)
                if (neighborhoodName == null)
                    return patientRepo.
                            findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndName1IgnoreCaseAndValidateFalseOrderByName1(
                                    countryName, departmentName, cityName, name1);
                else
                    return patientRepo.
                            findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndZone_NeighborhoodNameAndName1IgnoreCaseAndValidateFalse(
                                    countryName, departmentName, cityName, neighborhoodName, name1);
            else
                if (neighborhoodName == null)
                    return patientRepo.
                            findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndName1IgnoreCaseAndValidateFalseAndDeletedFalse(
                                countryName, departmentName, cityName, name1);
                else
                    return patientRepo.
                            findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndZone_NeighborhoodNameAndName1IgnoreCaseAndValidateFalseAndDeletedFalse(
                                countryName, departmentName, cityName, neighborhoodName, name1);
        }

        // solo los validados
        if (includeDeleted) {
            if (neighborhoodName == null)
                return patientRepo.
                        findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndName1IgnoreCaseAndValidateTrueOrderByName1(
                                countryName, departmentName, cityName, name1);
        }
        else if (neighborhoodName == null)
                return patientRepo.
                    findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndName1IgnoreCaseAndValidateTrueAndDeletedFalseOrderByName1(
                            countryName, departmentName, cityName, name1);

        return patientRepo.
                findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndZone_NeighborhoodNameAndName1IgnoreCaseAndValidateTrueAndDeletedFalse(
                        countryName, departmentName, cityName, neighborhoodName, name1);
    }

    @Override
    public List<Patient> findCity(
            Boolean withoutValidate, Boolean includeDeleted, String cityName, String departmentName, String countryName) {

        if (withoutValidate) {
            if (includeDeleted)
                return patientRepo.findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndValidateFalseOrderByName1(
                        countryName, departmentName, cityName);

            return patientRepo.findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndValidateFalseAndDeletedFalseOrderByName1(
                    countryName, departmentName, cityName);

        } else {
            //solo validados
            if (includeDeleted)
                return patientRepo.findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndValidateTrueOrderByName1(
                        countryName, departmentName, cityName);

            return patientRepo.findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndValidateTrueAndDeletedFalseOrderByName1(
                    countryName, departmentName, cityName);
        }
    }

    @Override
    public List<Patient> findDepartment(
            Boolean withoutValidate, Boolean includeDeleted, String departmentName, String countryName) {

        if (withoutValidate) {
            if (includeDeleted)
                return patientRepo.findByZone_CountryNameAndZone_DepartmentNameAndValidateFalseOrderByName1(
                        countryName, departmentName);

            return patientRepo.findByZone_CountryNameAndZone_DepartmentNameAndValidateFalseAndDeletedFalseOrderByName1(
                    countryName, departmentName);

        } else {
            //solo validados
            if (includeDeleted)
                return patientRepo.findByZone_CountryNameAndZone_DepartmentNameAndValidateTrueOrderByName1(
                        countryName, departmentName);

            return patientRepo.findByZone_CountryNameAndZone_DepartmentNameAndValidateTrueAndDeletedFalseOrderByName1(
                    countryName, departmentName);
        }

    }

/*

    @Override
    public Optional<Patient> findIdentificationDocument(Integer identificationDocument, String countryName) {
        return patientRepo.findByIdentificationDocumentAndZone_CountryNameAndDeletedFalse(
                identificationDocument, countryName);
    }

    @Override
    public List<Patient> findName1Like(String name1, String cityName, String departmentName, String countryName) {
        return patientRepo.findByName1LikeAndZone_CityNameAndZone_DepartmentNameAndZone_CountryNameAndDeletedFalse(
                name1, cityName, departmentName, countryName);
    }


    @Override
    public List<Patient> findSurname1Like(String surname1, String cityName, String departmentName, String countryName) {
        return patientRepo.findBySurname1LikeAndZone_CityNameAndZone_DepartmentNameAndZone_CountryNameAndDeletedFalse(
                surname1, cityName, departmentName, countryName
        );
    }

    @Override
    public List<Patient> findName1Surname1Likes(String name1, String surname1, String cityName, String departmentName, String countryName) {
        return patientRepo.findByName1LikeAndSurname1LikeAndAddress_CityNameAndAddress_DepartmentNameAndAddress_CountryName(
                name1, surname1, cityName, departmentName, countryName
        );
        return patientRepo.findByName1LikeAndSurname1LikeAndZone_CityNameAndZone_DepartmentNameAndZone_CountryNameAndDeletedFalse(
                name1, surname1, cityName, departmentName, countryName
        );
    }
*/

    // Asigna los valores por default a la entidad
    private void defaultValues(Patient patient){
        patient.setRegistrationDate(LocalDate.now());
        patient.setValidate(false);
        patient.setDeleted(false);
    }

    // Asigna los valores a la nueva entitdad, tomados de la vieja entidad (de la persistida)
    private void defaultValues(Patient oldPatient, Patient newPatient){
        newPatient.setUserId(oldPatient.getUserId());
        newPatient.setValidate(oldPatient.getValidate());
        newPatient.setDeleted(oldPatient.getDeleted());
    }

    private void physicallyDeletePatient(String id){
        try{
            patientRepo.deleteById(id);
            log.warning("Se borro fisicamente el paciente con id " + id);

        }catch(IllegalArgumentException e){
            log.warning("No se pudo eliminar el paciente con Id: " + id + ". El paciente seguramente no ha" +
                    " quedado vinculado a un usuario (coleccion Users) con el rol PATIENT. Si es asi, copie el Id del" +
                    " paciente en la clave 'entityId' correspopndiente al rol del usuario, en la coleccion Users. " +
                    "Alternativamente, puede eliminar el documento del paciente e ingresarlo nuevamente. ");
            throw new PatientPhysicallyDeleteException(
                    "No se pudo eliminar el paciente con Id: " + id);
        }
    }

    private void saveValidate(Patient patient){
        String msg;
        if (patient.getUserId() == null || patient.getUserId().isBlank()){
            msg = "El Paciente debe estar vinculado a un Usuario (clave 'userId' no puede ser nula ni vacia)";
            log.warning(msg);
            throw new PatientUserIdOmittedException(msg);
        }
    }

    //Devuelve el ordinal del volunteerPersonId especificado, en la lista 'volunteerPeople'
    private int ordinalVolunteer(String volunteerPersonId, List<VolunteerPersonMatchObject> volunteerPeople ) {
        for (int i = 0; i < volunteerPeople.size(); i++) {
            if (volunteerPeople.get(i).getVolunteerPersonId().equals(volunteerPersonId))
                return i;
        }
        return -1;
    }

    public List<StatisticPatientWithOthersDTO> getMonthlyRequestStatsForLastSixMonths(
            Boolean withoutValidate, Boolean includeDeleted, String departmentName, String countryName) {

        Map<Month, MonthlyRequestStats> statsMap = new HashMap<>();

        for (Patient patient : this.findDepartment(withoutValidate, includeDeleted, departmentName, countryName)) {

            // Obtener las solicitudes de voluntarios enviadas en los últimos 6 meses
            List<VolunteerPersonMatchObject> recentRequests = patient.getVolunteerPeople().stream()
                    .filter(request -> request.getShippingDate() != null &&
                            request.getShippingDate().isAfter(LocalDate.now().minusMonths(6)))
                    .toList();

            // Calcular estadísticas mensuales
            //statsMap = new HashMap<>();
            for (VolunteerPersonMatchObject request : recentRequests) {
                Month month = request.getShippingDate().getMonth();
                MonthlyRequestStats stats = statsMap.getOrDefault(month, new MonthlyRequestStats());

                stats.setMonth(month.getValue());
                stats.incrementTotalRequests();
                if (request.getConfirmationDate() != null) {
                    if (request.isMatch())
                        stats.incrementMatchedRequests();
                    else
                        stats.incrementUnmatchedRequests();
                } else {
                    stats.incrementPendingRequests();
                }

                //Actualiza estaddisticas del mes
                statsMap.put(month, stats);
            }

            // Calcular porcentajes
            for (MonthlyRequestStats stats : statsMap.values()) {
                stats.calculatePercentages();
            }

        }

        //Determinar total de solicitudes de contacto en el periodo
        int generalTotalRequests = 0;
        for (MonthlyRequestStats stats : statsMap.values())
            generalTotalRequests += (int) stats.getTotalRequests();

        //Setear porcentaje del total en cada mes
        for (MonthlyRequestStats stats : statsMap.values())
            stats.setTotalRequestsPercentage(round(stats.getTotalRequests() / generalTotalRequests * 100));

        //Devuelve lista ordenada por mes
        List<StatisticPatientWithOthersDTO> resultList =
                new ArrayList<>(statsPatientVolunteerToDTOList(statsMap.values()));
        resultList.sort(Comparator.comparingInt(StatisticPatientWithOthersDTO::getMonth));
        return resultList;
    }

    private List<StatisticPatientWithOthersDTO> statsPatientVolunteerToDTOList(Collection<MonthlyRequestStats> stats) {
        return stats.stream()
                .map(this::mapPatientVolunteerToDTO)
                .toList();
    }

    private StatisticPatientWithOthersDTO mapPatientVolunteerToDTO(MonthlyRequestStats stats) {
        StatisticPatientWithOthersDTO dto = new StatisticPatientWithOthersDTO();
        dto.setMonth(stats.getMonth());
        dto.setTotalRequests((int) stats.getTotalRequests());
        dto.setTotalRequestsPercentage(stats.getTotalRequestsPercentage());
        dto.setMatchedRequests((int) stats.getMatchedRequests());
        dto.setUnmatchedRequests((int) stats.getUnmatchedRequests());
        dto.setPendingRequests((int) stats.getPendingRequests());
        dto.setMatchPercentage(stats.getMatchRequestsPercentage());
        dto.setUnmatchPercentage(stats.getUnmatchRequestsPercentage());
        dto.setPendingPercentage(stats.getPendingRequestsPercentage());
        return dto;
    }

    public List<MostDemandedServicesVolunteerDTO> getMostDemandedVolunteerActivities(
            Boolean withoutValidate, Boolean includeDeleted, String departmentName, String countryName) {

        List<Patient> patients = findDepartment(withoutValidate, includeDeleted, departmentName, countryName);

        // Obtener servicios de voluntarios más demandados
        Map<String, MostDemandedServicesVolunteerRequestStats> mostDemandedVolunteerServices = volunteerPersonService.
                getMostDemandedVolunteerActivities(patients, 6, 4);

        return mostDemandedVolunteerServicesToDTOList(mostDemandedVolunteerServices);
    }

    private List<MostDemandedServicesVolunteerDTO> mostDemandedVolunteerServicesToDTOList(
            Map<String, MostDemandedServicesVolunteerRequestStats> mostDemandedVolunteerServices) {

        List<MostDemandedServicesVolunteerDTO> dtoList = new ArrayList<>();

        mostDemandedVolunteerServices.forEach((serviceName, stats) -> {
            MostDemandedServicesVolunteerDTO dto = new MostDemandedServicesVolunteerDTO();
            dto.setVolunteerServiceName(serviceName);
            dto.setDemandCount(stats.getDemandCount());
            dto.setDemandPercentage(stats.getDemandPercentage());
            dtoList.add(dto);
        });

        return dtoList;
    }



}

