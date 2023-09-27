package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.infra.config.ParamConfig;
import uy.com.pf.care.infra.repos.VolunteerCompanyRepo;
import uy.com.pf.care.model.documents.VolunteerCompany;
import uy.com.pf.care.model.documents.VolunteerPerson;
import uy.com.pf.care.model.enums.RoleEnum;
import uy.com.pf.care.model.globalFunctions.ForceEnumsToVolunteerCompany;
import uy.com.pf.care.model.globalFunctions.ForceEnumsToVolunteerPerson;
import uy.com.pf.care.model.globalFunctions.UpdateEntityId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Log
public class VolunteerCompanyService implements IVolunteerCompanyService{

    @Autowired
    private VolunteerCompanyRepo volunteerCompanyRepo;
    @Autowired
    private UpdateEntityId updateEntityId;
    @Autowired
    private ParamConfig paramConfig;


    @Override
    public String save(VolunteerCompany volunteerCompany) {
        String newVolunteerCompanyId = null;
        try {
            this.defaultValues(volunteerCompany);
            ForceEnumsToVolunteerCompany.execute(volunteerCompany);
            newVolunteerCompanyId = volunteerCompanyRepo.save(volunteerCompany).getVolunteerCompanyId();

            ResponseEntity<Boolean> response = updateEntityId.execute(
                    volunteerCompany.getUserId(), RoleEnum.VOLUNTEER_COMPANY.getOrdinal(), newVolunteerCompanyId);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("*** Empresa Voluntaria guardada con éxito");
                return newVolunteerCompanyId;
            }

            String msg = "Error actualizando entityId en el rol 'Empresa Voluntaria', en documento Users";
            log.warning(msg + ": " + response);
            throw new UserUpdateEntityIdInRolesListException(msg);

            //En caso de excepción, si la nueva Empresa Voluntaria fue persistida, se elimina, evitando inconsistencia de la bbdd
        } catch (UserUpdateEntityIdInRolesListException e) {
            if (newVolunteerCompanyId != null)
                this.physicallyDeleteVolunteerCompany(newVolunteerCompanyId);
            throw new UserUpdateEntityIdInRolesListException(e.getMessage());

        } catch (DuplicateKeyException e){
            String msg = "Error guardando Empresa Voluntaria (clave duplicada)";
            log.warning(msg + ": " + e.getMessage());
            throw new VolunteerCompanyDuplicateKeyException(msg);

        } catch (Exception e) {
            if (newVolunteerCompanyId != null)
                this.physicallyDeleteVolunteerCompany(newVolunteerCompanyId);
            String msg = "*** ERROR GUARDANDO EMPRESA VOLUNTARIA";
            log.warning(msg + ": " + e.getMessage());
            throw new VolunteerCompanySaveException(msg);
        }
    }

    @Override
    public Boolean update(VolunteerCompany newVolunteerCompany) {
        try {
            Optional<VolunteerCompany> entityFound = volunteerCompanyRepo.findById(newVolunteerCompany.getVolunteerCompanyId());
            if (entityFound.isPresent()) {
                this.defaultValues(newVolunteerCompany, entityFound.get());
                ForceEnumsToVolunteerCompany.execute(newVolunteerCompany);
                volunteerCompanyRepo.save(newVolunteerCompany);
                log.info("Empresa Voluntaria actualizada con éxito");
                return true;
            }
            this.notFound(newVolunteerCompany.getVolunteerCompanyId());
            return null;

        } catch (VolunteerCompanyNotFoundException e) {
            throw new VolunteerCompanyNotFoundException(e.getMessage());
        } catch (DuplicateKeyException e){
            throw new VolunteerCompanyDuplicateKeyException(e.getMessage());
        } catch(Exception e){
            String msg = "*** ERROR ACTUALIZANDO EMPRESA VOLUNTARIA";
            log.warning(msg + ": " + e.getMessage());
            throw new VolunteerCompanyUpdateException(msg);
        }
    }

    @Override
    public Optional<VolunteerCompany> findId(String id) {
        Optional<VolunteerCompany> found = volunteerCompanyRepo.findById(id);
        if (found.isPresent())
            return found;

        this.notFound(id);
        return Optional.empty();
    }

    @Override
    public List<VolunteerCompany> findAll(Boolean validateOnly, Boolean includeDeleted, String countryName) {
        if (validateOnly)
            return includeDeleted ?
                    volunteerCompanyRepo.findByCountryNameAndValidateTrue(countryName) :
                    volunteerCompanyRepo.findByCountryNameAndValidateTrueAndDeletedFalse(countryName);

        return includeDeleted ?
                volunteerCompanyRepo.findByCountryName(countryName) :
                volunteerCompanyRepo.findByCountryNameAndDeletedFalse(countryName);
    }

    @Override
    public Boolean addDonations(String volunteerCompanyId, List<String> donationsId) {
        if (volunteerCompanyId.isEmpty() || donationsId.isEmpty()) return false;
        try {
            Optional<VolunteerCompany> found = volunteerCompanyRepo.findById(volunteerCompanyId);
            if (found.isPresent()) {
                AtomicInteger count = new AtomicInteger(0);
                donationsId.forEach(donationId -> {
                    if (!found.get().getDonationsRequestId().contains(donationId)) {
                        found.get().getDonationsRequestId().add((donationId));
                        count.incrementAndGet();
                    }
                });

                if (count.get() > 0) {
                    volunteerCompanyRepo.save(found.get());
                    log.info(count.get() == donationsId.size() ?
                            "Donaciones vinculadas exitosamente a la Empresa Voluntaria." :
                            "Donaciones vinculadas correctamente (hubo " + (donationsId.size() - count.get()) +
                                    " donaciones que ya estaban asignadas)");
                    return true;
                } else {
                    log.warning("Las donaciones ya estaban asignadas a la Empresa Voluntaria.");
                    return false;
                }
            }
            this.notFound(volunteerCompanyId);
            return null;

        }catch (VolunteerCompanyNotFoundException e){
            throw new VolunteerCompanyNotFoundException(e.getMessage());
        }catch (Exception e){
            String msg = "*** ERROR VINCULANDO DONACIONES CON EMPRESA VOLUNTARIA";
            log.warning(msg + ": " +e.getMessage());
            throw new VolunteerCompanyAddDonationsException(msg);
        }

    }

    @Override
    public Boolean delDonations(String volunteerCompanyId, List<String> donationsId) {
        if (donationsId.isEmpty()) return false;
        try {
            Optional<VolunteerCompany> found = volunteerCompanyRepo.findById(volunteerCompanyId);
            if (found.isPresent()) {
                int sizeDonationsRequestId = found.get().getDonationsRequestId().size();
                donationsId.forEach(donationId -> {
                    found.get().getDonationsRequestId().removeIf(savedDonationyOrdinal ->
                            Objects.equals(savedDonationyOrdinal, donationId));
                });
                int erased = sizeDonationsRequestId - found.get().getDonationsRequestId().size();
                if (erased == 0){
                    log.warning("Las donaciones proporcionadas no pertenecen a la Empresa Voluntaria.");
                    return false;
                }
                volunteerCompanyRepo.save(found.get());
                if (erased == donationsId.size())
                    log.warning("Las donaciones fueron eliminadas de la Empresa Voluntaria.");
                else
                    log.warning(erased + " donaciones eliminadas de la Empresa Voluntaria (" +
                            (donationsId.size() - erased) + " donaciones no pertenecían a la empresa)");
                return true;
            }
            this.notFound(volunteerCompanyId);
            return null;

        } catch (VolunteerCompanyNotFoundException e) {
            throw new VolunteerCompanyNotFoundException(e.getMessage());
        } catch (Exception e) {
            String msg = "*** ERROR BORRANDO DONACIONES DE LA EMPRESA VOLUNTARIA";
            log.warning(msg + ": " + e.getMessage());
            throw new VolunteerCompanyDelDonationsException(msg);
        }
    }

    @Override
    public Boolean setAvailability(String id, Boolean isAvailable) {
        try {
            Optional<VolunteerCompany> volunteerCompany = this.findId(id);
            if (volunteerCompany.isPresent()) {
                if (volunteerCompany.get().getDeleted()) {
                    log.warning("La Empresa Voluntaria está eliminada");
                    return false;
                }
                volunteerCompany.get().setAvailable(isAvailable);
                volunteerCompanyRepo.save(volunteerCompany.get());
                log.info("Se estableció la disponiblidad de la empresa voluntaria con id " + id + " como " +
                        (isAvailable ? "DISPONIBLE" : "NO DISPONIBLE"));
                return true;
            }
            this.notFound(id);
            return null;

        }catch (VolunteerCompanyNotFoundException e){
            throw new VolunteerCompanyNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "No se pudo setear la disponibilidad de la Empresa Voluntaria con id " + id ;
            log.warning(msg + ": " + e.getMessage());
            throw new VolunteerCompanySetAvailabilityException(msg);
        }
    }

    @Override
    public Boolean setValidation(String id, Boolean isValidated) {
        try {
            Optional<VolunteerCompany> found = this.findId(id);
            if (found.isPresent()) {
                found.get().setValidate(isValidated);
                volunteerCompanyRepo.save(found.get());
                log.info("Se validó la empresa voluntaria con id " + id + " como " +
                        (isValidated ? "VALIDADA" : "NO VALIDADA"));
                return true;
            }
            this.notFound(id);
            return null;

        }catch(VolunteerCompanyNotFoundException e){
            throw new VolunteerCompanyNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR VALIDANDO EMPRESA VOLUNTARIA";
            log.warning(msg + ": " + e.getMessage());
            throw new VolunteerCompanySetValidationException(msg);
        }
    }

    @Override
    public Boolean setDeletion(String id, Boolean isDeleted) {
        try {
            Optional<VolunteerCompany> volunteerCompany = this.findId(id);
            if (volunteerCompany.isPresent()) {
                volunteerCompany.get().setDeleted(isDeleted);
                volunteerCompany.get().setAvailable(false);
                volunteerCompanyRepo.save(volunteerCompany.get());
                log.info("Se " + (isDeleted ? "eliminó" : "restauró") + " la empresa voluntaria con id " + id);
                return true;
            }
            this.notFound(id);
            return false;

        }catch (VolunteerCompanyNotFoundException e){
            throw new VolunteerCompanyNotFoundException(e.getMessage());
        }catch(Exception e){
            log.warning("No se pudo setear el borrado lógico de la Empresa Voluntaria con id: " + id + ". "
                    + e.getMessage());
            throw new VolunteerCompanySetDeletionException(
                    "No se pudo setear el borrado lógico de la Empresa Voluntaria con id " + id);
        }
    }

    private void defaultValues(VolunteerCompany volunteerCompany){
        volunteerCompany.setDonationsRequestId(new ArrayList<>());
        volunteerCompany.setValidate(false);
        volunteerCompany.setDeleted(false);
    }

    private void defaultValues(VolunteerCompany newVolunteerCompany, VolunteerCompany oldVolunteerCompany){
        newVolunteerCompany.setUserId(oldVolunteerCompany.getUserId());
        newVolunteerCompany.setDonationsRequestId(oldVolunteerCompany.getDonationsRequestId());
        newVolunteerCompany.setAvailable(oldVolunteerCompany.getAvailable());
        newVolunteerCompany.setValidate(oldVolunteerCompany.getValidate());
        newVolunteerCompany.setDeleted(oldVolunteerCompany.getDeleted());
    }


    private void physicallyDeleteVolunteerCompany(String id){
        try{
            volunteerCompanyRepo.deleteById(id);
            log.warning("Se borró físicamente la Empresa Voluntaria con id " + id);

        }catch(IllegalArgumentException e){
            log.warning("No se pudo eliminar la Empresa Voluntaria con Id: " + id + ". La Empresa Voluntaria " +
                    "seguramente no ha quedado vinculada a un usuario (coleccion Users) con el rol VOLUNTEER_COMPANY. " +
                    "Si es asi, copie el Id de la Empresa Voluntaria en la clave 'entityId' correspopndiente al rol del" +
                    " usuario, en la coleccion Users. Alternativamente, puede eliminar el documento de la Empresa " +
                    "Voluntaria e ingresarlo nuevamente.");
            throw new VolunteerCompanyPhysicallyDeleteException("No se pudo eliminar la Empresa Voluntaria con Id: " + id);
        }
    }

    private void notFound(String volunteerCompanyId){
        String msg = "No se encontro la Empresa Voluntaria con id " + volunteerCompanyId;
        log.warning(msg);
        throw new VolunteerCompanyNotFoundException(msg);

    }

}
