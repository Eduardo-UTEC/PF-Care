package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.EmergencyServiceSaveException;
import uy.com.pf.care.exceptions.EmergencyServiceUpdateException;
import uy.com.pf.care.model.documents.EmergencyService;
import uy.com.pf.care.model.documents.FormalCaregiver;
import uy.com.pf.care.repos.IEmergencyServiceRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class EmergencyServiceService implements IEmergencyServiceService{

    @Autowired
    private IEmergencyServiceRepo emergencyServiceRepo;

    //private static final Logger log = LoggerFactory.getLogger(CuidadosApplication.class);

    @Override
    public String save(EmergencyService emergencyService) {
        try{
            this.defaultValues(emergencyService);
            String id = emergencyServiceRepo.save(emergencyService).getEmergencyServiceId();
            log.info("*** Servicio de Emergencia guardado con exito: " + LocalDateTime.now());
            return id;

        }catch(Exception e){
            log.warning("*** ERROR GUARDANDO SERVICIO DE EMERGENCIA: " + e);
            throw new EmergencyServiceSaveException("*** ERROR GUARDANDO SERVICIO DE EMERGENCIA");
        }
    }

    @Override
    public Boolean update(EmergencyService newEmergencyService) {
        try{
            Optional<EmergencyService> entityFound = emergencyServiceRepo.
                    findById(newEmergencyService.getEmergencyServiceId());

            if (entityFound.isPresent()){
                this.defaultValues(entityFound.get(), newEmergencyService);
                emergencyServiceRepo.save(newEmergencyService);
                log.info("Servicio de emergencia actualizado con exito");
                return true;
            }
            log.info("No se encontro el servicio de emergencia con id " + newEmergencyService.getEmergencyServiceId());
            return false;

        }catch(Exception e){
            log.warning("*** ERROR ACTUALIZANDO SERVICIO DE EMERGENCIA: " + e);
            throw new EmergencyServiceUpdateException("*** ERROR ACTUALIZANDO SERVICIO DE EMERGENCIA");
        }

    }

    @Override
    public List<EmergencyService> findAll(Boolean includeDeleted, String countryName) {
        if (includeDeleted)
            return emergencyServiceRepo.findByCountryNameOrderByName(countryName);
        else
            return emergencyServiceRepo.findByCountryNameAndDeletedFalseOrderByName(countryName);
    }

    @Override
    public Optional<EmergencyService> findId(String id) {
        return emergencyServiceRepo.findById(id);
    }

    @Override
    public List<EmergencyService> findByCity(
            Boolean includeDeleted, String cityName, String departmentName, String countryName) {

        if (includeDeleted)
            return emergencyServiceRepo.findByCountryNameAndDepartmentNameAndCityNameOrderByName(
                    countryName, departmentName, cityName);
        else
            return emergencyServiceRepo.findByCountryNameAndDepartmentNameAndCityNameAndDeletedFalseOrderByName(
                    countryName, departmentName, cityName);
    }

    @Override
    public List<EmergencyService> findByDepartment(Boolean includeDeleted, String departmentName, String countryName) {
        if (includeDeleted)
            return emergencyServiceRepo.findByCountryNameAndDepartmentNameOrderByName(countryName, departmentName);
        else
            return emergencyServiceRepo.findByCountryNameAndDepartmentNameAndDeletedFalseOrderByName(countryName, departmentName);
    }

    @Override
    public EmergencyService findByName(
            Boolean includeDeleted, String name, String cityName, String departmentName, String countryName) {

        if (includeDeleted)
            return emergencyServiceRepo.findByCountryNameAndDepartmentNameAndCityNameAndName(
                    countryName, departmentName, cityName, name);
        else
            return emergencyServiceRepo.findByCountryNameAndDepartmentNameAndCityNameAndNameAndDeletedFalse(
                    countryName, departmentName, cityName, name);
    }

    @Override
    public boolean setDeletion(String id, Boolean isDeleted) {
        Optional<EmergencyService> emergencyService = this.findId(id);
        if (emergencyService.isPresent()) {
            emergencyService.get().setDeleted(isDeleted);
            emergencyServiceRepo.save(emergencyService.get());
            return true;
        }
        return false;
    }

    // Asigna los valores por default a la entitdad
    private void defaultValues(EmergencyService emergencyService){
        emergencyService.setDeleted(false);
    }

    // Asigna los valores a la nueva entitdad, tomados de la vieja entidad (de la persistida)
    private void defaultValues(EmergencyService oldEmergencyService, EmergencyService newEmergencyService){
        newEmergencyService.setDeleted(oldEmergencyService.getDeleted());
    }

}
