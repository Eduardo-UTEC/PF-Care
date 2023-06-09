package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.VolunteerActivitySaveException;
import uy.com.pf.care.exceptions.VolunteerActivityUpdateException;
import uy.com.pf.care.model.documents.VolunteerActivity;
import uy.com.pf.care.repos.IVolunteerActivityRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class VolunteerActivityService implements IVolunteerActivityService{
    @Autowired
    private IVolunteerActivityRepo volunteerActivityRepo;

   // private static final Logger log = LoggerFactory.getLogger(CuidadosApplication.class);

    @Override
    public String save(VolunteerActivity volunteerActivity) {
        try{
            String id = volunteerActivityRepo.save(volunteerActivity).getVolunteerActivityId();
            log.info("*** Actividad del Voluntario guardada con exito: " + LocalDateTime.now());
            return id;

        }catch(Exception e){
            log.warning("*** ERROR GUARDANDO ACTIVIDAD DEL VOLUNTARIO: " + e);
            throw new VolunteerActivitySaveException("*** ERROR GUARDANDO ACTIVIDAD DEL VOLUNTARIO");
        }
    }

    @Override
    public Boolean update(VolunteerActivity newVolunteerActivity) {
        try{
            Optional<VolunteerActivity> entityFound = volunteerActivityRepo.findById(newVolunteerActivity.
                    getVolunteerActivityId());
            if (entityFound.isPresent()){
                volunteerActivityRepo.save(newVolunteerActivity);
                log.info("Actividad del Voluntario actualizada con exito");
                return true;
            }
            log.info("No se encontro la Actividad del Voluntario con id " + newVolunteerActivity.
                    getVolunteerActivityId());
            return false;

        } catch(Exception e){
            log.warning("*** ERROR ACTUALIZANDO ACTIVIDAD DEL VOLUNTARIO: " + e);
            throw new VolunteerActivityUpdateException("*** ERROR ACTUALIZANDO ACTIVIDAD DEL VOLUNTARIO");
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
        return false;
    }

    @Override
    public Optional<VolunteerActivity> findId(String id) {return volunteerActivityRepo.findById(id);}

    @Override
    public Boolean exist(String name, String departmentName, String countryName) {
        return volunteerActivityRepo.findByCountryNameAndDepartmentNameAndNameIgnoreCase(
                countryName, departmentName, name).isPresent();
    }

}
