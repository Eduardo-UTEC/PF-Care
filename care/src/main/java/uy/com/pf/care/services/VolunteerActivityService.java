package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.VolunteerActivityDuplicateKeyException;
import uy.com.pf.care.exceptions.VolunteerActivityNotFoundException;
import uy.com.pf.care.exceptions.VolunteerActivitySaveException;
import uy.com.pf.care.exceptions.VolunteerActivityUpdateException;
import uy.com.pf.care.infra.repos.IVolunteerActivityRepo;
import uy.com.pf.care.model.documents.VolunteerActivity;

import java.util.List;
import java.util.Optional;

@Service
@Log
public class VolunteerActivityService implements IVolunteerActivityService{
    @Autowired
    private IVolunteerActivityRepo volunteerActivityRepo;

    @Override
    public String save(VolunteerActivity volunteerActivity) {
        try {
            this.defaultValues(volunteerActivity);
            String id = volunteerActivityRepo.save(volunteerActivity).getVolunteerActivityId();
            log.info("Actividad del Voluntario guardada con exito");
            return id;

        }catch (DuplicateKeyException e){
            String msg = "Error guardando Actividad del Voluntario (clave duplicada)";
            log.warning(msg + ": " + e.getMessage());
            throw new VolunteerActivityDuplicateKeyException(msg);
        }catch(Exception e){
            String msg = "*** ERROR GUARDANDO ACTIVIDAD DEL VOLUNTARIO";
            log.warning(msg + ": " + e.getMessage());
            throw new VolunteerActivitySaveException(msg);
        }
    }

    @Override
    public Boolean update(VolunteerActivity newVolunteerActivity) {
        try {
            Optional<VolunteerActivity> entityFound =
                    volunteerActivityRepo.findById(newVolunteerActivity.getVolunteerActivityId());
            if (entityFound.isPresent()) {
                this.defaultValues(newVolunteerActivity, entityFound.get());
                volunteerActivityRepo.save(newVolunteerActivity);
                log.info("Actividad del Voluntario actualizada con exito");
                return true;
            }
            String msg = "No se encontro la Actividad del Voluntario con id " + newVolunteerActivity.getVolunteerActivityId();
            log.info(msg);
            throw new VolunteerActivityNotFoundException(msg);

        }catch (VolunteerActivityNotFoundException e){
            throw new VolunteerActivityNotFoundException(e.getMessage());
        }catch (DuplicateKeyException e){
            String msg = "Error actualizando Actividad del Voluntario (clave duplicada)";
            log.warning(msg + ": " + e.getMessage());
            throw new VolunteerActivityDuplicateKeyException(msg);
        } catch(Exception e){
            String msg = "*** ERROR ACTUALIZANDO ACTIVIDAD DEL VOLUNTARIO";
            log.warning(msg + ": " + e.getMessage());
            throw new VolunteerActivityUpdateException(msg);
        }
    }

    @Override
    public List<VolunteerActivity> findAll(Boolean includeDeleted, String countryName) {
        if (includeDeleted)
            return volunteerActivityRepo.findByCountryNameOrderByName(countryName);
        return volunteerActivityRepo.findByCountryNameAndDeletedFalseOrderByName(countryName);
    }

    @Override
    public List<VolunteerActivity> findDepartment(Boolean includeDeleted, String countryName, String departmentName) {
        if (includeDeleted)
            return volunteerActivityRepo.findByCountryNameAndDepartmentNameOrderByName(countryName, departmentName);
        return volunteerActivityRepo.
                findByCountryNameAndDepartmentNameAndDeletedFalseOrderByName(countryName, departmentName);
    }

    @Override
    public Boolean setDeletion(String id, Boolean isDeleted) {
        Optional<VolunteerActivity> volunteerActivity = this.findId(id);
        if (volunteerActivity.isPresent()) {
            volunteerActivity.get().setDeleted(isDeleted);
            volunteerActivityRepo.save(volunteerActivity.get());
            return true;
        }
        String msg = "Actividad del voluntario no encontrada";
        log.warning(msg);
        throw new VolunteerActivityNotFoundException(msg);
    }

    @Override
    public Optional<VolunteerActivity> findId(String id) {
        Optional<VolunteerActivity> found = volunteerActivityRepo.findById(id);
        if (found.isPresent())
            return found;

        String msg = "Actividad del voluntario no encontrada";
        log.warning(msg);
        throw new VolunteerActivityNotFoundException(msg);
    }

    @Override
    public List<VolunteerActivity> findIds(List<String> volunteersActivitiesId) {
        return volunteerActivityRepo.findAllById(volunteersActivitiesId);
    }

    @Override
    public Boolean exist(String name, String departmentName, String countryName) {
        return volunteerActivityRepo.findByCountryNameAndDepartmentNameAndNameIgnoreCase(
                countryName, departmentName, name).isPresent();
    }

    private void defaultValues(VolunteerActivity volunteerActivity){
        volunteerActivity.setDeleted(false);
    }

    private void defaultValues(VolunteerActivity volunteerActivity, VolunteerActivity oldVolunteerActivity){
        volunteerActivity.setDeleted(oldVolunteerActivity.getDeleted());
    }
}
