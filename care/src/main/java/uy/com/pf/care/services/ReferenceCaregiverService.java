package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.infra.config.ParamConfig;
import uy.com.pf.care.infra.repos.IReferenceCaregiverRepo;
import uy.com.pf.care.model.documents.ReferenceCaregiver;
import uy.com.pf.care.model.enums.RelationshipEnum;
import uy.com.pf.care.model.enums.RoleEnum;
import uy.com.pf.care.model.globalFunctions.ForceEnumsToReferenceCaregiver;
import uy.com.pf.care.model.globalFunctions.UpdateEntityId;
//import uy.com.pf.care.model.objects.PatientLinkedReferentObject;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log
public class ReferenceCaregiverService implements IReferenceCaregiverService {

    @Autowired
    private IReferenceCaregiverRepo referenceCaregiverRepo;
    @Autowired
    private UpdateEntityId updateEntityId;
    @Autowired
    private ParamConfig paramConfig;

    @Override
    public String save(ReferenceCaregiver referenceCaregiver) {

        this.saveValidate(referenceCaregiver);

        //Se define aqui para controlar la excepcion, estableciendo si se debe eliminar el ReferenceCaregiver
        String newReferenceCaregiverId = null;

        try {
            ForceEnumsToReferenceCaregiver.execute(referenceCaregiver);

            List<String> patientsNotFound = this.updateReferenceCaregiverOnPatients(referenceCaregiver);
            if (! patientsNotFound.isEmpty()){
                String msg = "Advertencia: hubo Pacientes cuyo Id no se encontró, y no pudieron vincularse al " +
                        "Cuidador Referente. Id de Pacientes no encontrados: " + Arrays.toString(patientsNotFound.toArray());
                log.warning(msg);
            }

            this.defaultValues(referenceCaregiver);
            newReferenceCaregiverId = referenceCaregiverRepo.save(referenceCaregiver).getReferenceCaregiverId();

            ResponseEntity<Boolean> response = updateEntityId.execute(
                    referenceCaregiver.getUserId(), RoleEnum.REFERENCE_CARE.getOrdinal(), newReferenceCaregiverId);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("*** Cuidador Referente guardado con exito");
                return newReferenceCaregiverId;
            }

            String msg = "Error actualizando entityId en el rol Cuidador Referente, en documento Users";
            log.warning(msg);
            throw new UserUpdateEntityIdInRolesListException(msg);

        }catch(ReferenceCaregiverUserIdOmittedException e){
            throw new ReferenceCaregiverUserIdOmittedException(e.getMessage());
        }catch(ReferenceCaregiverPatientsIdOmittedException e){
            throw new ReferenceCaregiverPatientsIdOmittedException(e.getMessage());
        }catch (DuplicateKeyException e){
            String msg = "Error guardando Cuidador Referente (clave duplicada)";
            log.warning(msg + ": " + e.getMessage());
            throw new ReferenceCaregiverDuplicateKeyException(msg);

        //Si el nuevo Cuidador Referente fue persistido, se elimina, evitando inconsistencia de la bbdd
        }catch (UserUpdateEntityIdInRolesListException e) {
            if (newReferenceCaregiverId != null)
                this.physicallyDeleteReferenceCaregiver(newReferenceCaregiverId);
            throw new UserUpdateEntityIdInRolesListException(e.getMessage());
        }catch(Exception e){
            if (newReferenceCaregiverId != null)
                this.physicallyDeleteReferenceCaregiver(newReferenceCaregiverId);

            String msg = "*** ERROR GUARDANDO CUIDADOR REFERENTE: " + e.getMessage();
            log.warning(msg);
            throw new ReferenceCaregiverSaveException(msg);
        }
    }

    @Override
    public Boolean update(ReferenceCaregiver newReferenceCaregiver) {
        try{
            Optional<ReferenceCaregiver> entityFound =
                    referenceCaregiverRepo.findById(newReferenceCaregiver.getReferenceCaregiverId());
            if (entityFound.isPresent()) {
                this.defaultValues(newReferenceCaregiver, entityFound.get());
                ForceEnumsToReferenceCaregiver.execute(newReferenceCaregiver);
                referenceCaregiverRepo.save(newReferenceCaregiver);
                log.info("Cuidador Referente actualizado con exito");
                return true;
            }
            String msg = "No se encontro el Cuidador Referente con id " + newReferenceCaregiver.getReferenceCaregiverId();
            log.info(msg);
            throw new ReferenceCaregiverNotFoundException(msg);

        }catch (ReferenceCaregiverNotFoundException e){
            throw new ReferenceCaregiverNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR ACTUALIZANDO CUIDADOR REFERENTE";
            log.warning(msg +  ": " + e.getMessage());
            throw new ReferenceCaregiverUpdateException(msg);
        }

    }

    @Override
    public Boolean addPatient(String referenceCaregiverId, String patientId) {
        Optional<ReferenceCaregiver> found = referenceCaregiverRepo.findById(referenceCaregiverId);
        if (found.isPresent()){
            if (this.patientLinked(found.get(), patientId)) {
                String msg = "El Paciente ya está vinculado al Cuidador Referente";
                log.warning(msg);
                throw new ReferenceCaregiverChangeRelationshipPatientException(msg);
            }
            //found.get().getPatients().add(new PatientLinkedReferentObject(patientId, relationship));
            found.get().getPatients().add(patientId);
            ForceEnumsToReferenceCaregiver.execute(found.get());
            referenceCaregiverRepo.save(found.get());

            if (this.updateReferenceCaregiverOnPatients(found.get()).isEmpty()) {
                log.warning("Paciente vinculado con exito al Cuidador Referente");
                return true;

            } else
                return false;

        }

        String msg = "No se encontro el Cuidador Referente con id " + referenceCaregiverId;
        log.warning(msg);
        throw new ReferenceCaregiverNotFoundException(msg);
    }

    /*@Override
    public Boolean changeRelationshipPatient(String referenceCaregiverId, String patientId, RelationshipEnum relationship) {
        Optional<ReferenceCaregiver> found = referenceCaregiverRepo.findById(referenceCaregiverId);
         if (found.isPresent()){
             for (PatientLinkedReferentObject object : found.get().getPatients()){
                 if (object.getPatientId().equals(patientId)){
                     object.setRelationship(relationship);
                     ForceEnumsToReferenceCaregiver.execute(found.get());
                     referenceCaregiverRepo.save(found.get());
                     log.warning("La relación del Cuidador Referente con el paciente ha cambiado con exito");
                     return true;
                 }
             }

             String msg = "El Cuidador Referente '" + referenceCaregiverId + "' no está vinculado al paciente '" + patientId +"'";
             log.warning(msg);
             throw new ReferenceCaregiverChangeRelationshipPatientException(msg);
         }

        String msg = "No se encontro el Cuidador Referente con id " + referenceCaregiverId;
        log.warning(msg);
        throw new ReferenceCaregiverNotFoundException(msg);
    }

     */

    @Override
    public Optional<ReferenceCaregiver> findId(String id) {
        return referenceCaregiverRepo.findById(id);
    }

    @Override
    public Optional<ReferenceCaregiver> findMail(String mail) {
        return referenceCaregiverRepo.findByMailIgnoreCase(mail);
    }

    @Override
    public Optional<ReferenceCaregiver> findIdentificationDocument(Integer identificationDocument, String countryName) {
        return referenceCaregiverRepo.findByIdentificationDocumentAndZone_CountryName(identificationDocument, countryName);
    }

    @Override
    public List<ReferenceCaregiver> findName1(
            String name1, String neighborhoodName, String cityName, String departmentName, String countryName) {
        return referenceCaregiverRepo.
                findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameAndZone_NeighborhoodNameAndName1IgnoreCaseOrderByName1(
                        countryName, departmentName, cityName, neighborhoodName, name1);
    }

    @Override
    public List<ReferenceCaregiver> findCity(String cityName, String departmentName, String countryName) {
        return referenceCaregiverRepo.findByZone_CountryNameAndZone_DepartmentNameAndZone_CityNameOrderByName1(
                countryName, departmentName, cityName);
    }

    @Override
    public List<ReferenceCaregiver> findDepartment(String departmentName, String countryName) {
        return referenceCaregiverRepo.findByZone_CountryNameAndZone_DepartmentNameOrderByName1(
                countryName, departmentName);
    }

    @Override
    public List<ReferenceCaregiver> findAll(String countryName) {
        return referenceCaregiverRepo.findByZone_CountryNameOrderByName1(countryName);
    }

    private void physicallyDeleteReferenceCaregiver(String id){
        try{
            referenceCaregiverRepo.deleteById(id);
            log.warning("Se borro fisicamente el Cuidador Referente con id " + id);

        }catch(IllegalArgumentException e){
            log.warning("No se pudo eliminar el Cuidador Referente con Id: " + id + ". El Cuidador Referente " +
                    "seguramente no ha quedado vinculado a un usuario (coleccion Users) con el rol REFERENCE_CARE. " +
                    "Si es asi, copie el Id del Cuidador Referente en la clave 'entityId' correspopndiente al rol del " +
                    "usuario, en la coleccion Users. Alternativamente, puede eliminar el documento del Cuidador Referente " +
                    "e ingresarlo nuevamente. ");
            throw new ReferenceCaregiverPhysicallyDeleteException("No se pudo eliminar el Cuidador Referente con Id: " + id);
        }
    }

    //Valores por defecto para el Add
    private void defaultValues(ReferenceCaregiver referenceCaregiver) {
        referenceCaregiver.setRegistrationDate(LocalDate.now());
    }

    //Valores por defecto para el Update
    private void defaultValues(ReferenceCaregiver newreferenceCaregiver, ReferenceCaregiver oldReferenceCaregiver){
        newreferenceCaregiver.setUserId(oldReferenceCaregiver.getUserId());
        newreferenceCaregiver.setPatients(oldReferenceCaregiver.getPatients());
    }

    //Valida las key requeridas para guardar un nuevo Cuidador Referente
    private void saveValidate(ReferenceCaregiver referenceCaregiver){
        String msg;
        if (referenceCaregiver.getUserId() == null || referenceCaregiver.getUserId().isBlank()){
            msg = "El Cuidador Referente debe estar vinculado a un Usuario (clave 'userId' no puede ser nula ni vacia)";
            log.warning(msg);
            throw new ReferenceCaregiverUserIdOmittedException(msg);
        }
        /*if (referenceCaregiver.getPatients().isEmpty()){
            msg = "El Cuidador Referente debe estar vinculado al menos a un Paciente (clave 'patientsId' no puede ser nula ni vacia)";
            log.warning(msg);
            throw new ReferenceCaregiverPatientsIdOmittedException(msg);
        }*/
    }

    //Actualiza los Pacientes (segun su id), en la key referenceCaregiver, con el Id del Cuidador Referente
    private List<String> updateReferenceCaregiverOnPatients(ReferenceCaregiver referenceCaregiver){

        /*List<String> patientIds = referenceCaregiver.getPatients().stream()
                .map(PatientLinkedReferentObject::getPatientId)
                .collect(Collectors.toList());

         */
        List<String> patientIds = referenceCaregiver.getPatients();

        try {
            RestTemplate restTemplate = new RestTemplate();
            List<String> notFounds = restTemplate.exchange(
                    getStartUrl() + "patients/updateReferenceCaregiver/" + referenceCaregiver.getReferenceCaregiverId(),
                    HttpMethod.PUT,
                    new HttpEntity<>(patientIds),   // Dato pasado por el body
                    new ParameterizedTypeReference<List<String>>(){} // Tipo de respuesta esperado
            ).getBody();

            // Si hubo Pacientes no encontrados, borros sus referencias de la key "patients" del documento ReferenceCaregiver
            assert notFounds != null;
            if (! notFounds.isEmpty()){
                //referenceCaregiver.getPatients().removeIf(patientLinkedReferentObject ->
                //        notFounds.contains(patientLinkedReferentObject.getPatientId()));
                referenceCaregiver.getPatients().removeIf(patientId -> notFounds.contains(patientId));
                referenceCaregiverRepo.save(referenceCaregiver);
            }
            return notFounds;

        } catch (HttpClientErrorException e) {
            // Error HTTP 4xx
            throw new HttpClientErrorException(e.getStatusCode(), e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            // Error HTTP 5xx
            throw new HttpServerErrorException(e.getStatusCode(), e.getResponseBodyAsString());
        } catch (RestClientException e) {
            throw new RestClientException(e.getMessage());
        } catch (Exception e) {
            throw new ReferenceCaregiverUpdateOnPatientsException(e.getMessage());
        }

    }

    private String getStartUrl() {
        if (paramConfig == null)
            throw new IllegalStateException("ParamConfig no está inicializado");
        return paramConfig.getProtocol() + "://" + paramConfig.getSocket() + "/";
    }

    private boolean patientLinked(ReferenceCaregiver referenceCaregiver, String patientId){
        /*for (PatientLinkedReferentObject patient : referenceCaregiver.getPatients()){
            if (patient.getPatientId().equals(patientId))
                return true;
        }

         */
        for (String patientIdStore : referenceCaregiver.getPatients()){
            if (patientIdStore.equals(patientId))
                return true;
        }
        return false;
    }

}
