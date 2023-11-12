package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.EmergencyServiceNotFoundException;
import uy.com.pf.care.exceptions.EmergencyServiceSaveException;
import uy.com.pf.care.exceptions.EmergencyServiceUpdateException;
import uy.com.pf.care.infra.repos.IEmergencyServiceRepo;
import uy.com.pf.care.model.documents.EmergencyService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class EmergencyServiceService implements IEmergencyServiceService{

    @Autowired
    private IEmergencyServiceRepo emergencyServiceRepo;

    @Override
    public String save(EmergencyService emergencyService) {
        try{
            this.defaultValues(emergencyService);
            String id = emergencyServiceRepo.save(emergencyService).getEmergencyServiceId();
            log.info("*** Servicio de Emergencia guardado con exito: " + LocalDateTime.now());
            return id;

        }catch(Exception e){
            String msg = "*** ERROR GUARDANDO SERVICIO DE EMERGENCIA";
            log.warning(msg + ": " + e.getMessage());
            throw new EmergencyServiceSaveException(msg);
        }
    }

    @Override
    public Boolean update(EmergencyService newEmergencyService) {
        try {
            Optional<EmergencyService> entityFound = emergencyServiceRepo.
                    findById(newEmergencyService.getEmergencyServiceId());

            if (entityFound.isPresent()) {
                this.defaultValues(entityFound.get(), newEmergencyService);
                emergencyServiceRepo.save(newEmergencyService);
                log.info("Servicio de emergencia actualizado con exito");
                return true;
            }

            String msg = "No se encontro el servicio de emergencia con id: " + newEmergencyService.getEmergencyServiceId();
            log.warning(msg);
            throw new EmergencyServiceNotFoundException(msg);

        }catch (EmergencyServiceNotFoundException e){
            throw new EmergencyServiceNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR ACTUALIZANDO SERVICIO DE EMERGENCIA" ;
            log.warning(msg+ ": " + e.getMessage());
            throw new EmergencyServiceUpdateException(msg);
        }
    }

    @Override
    public List<EmergencyService> findAll(Boolean includeDeleted, String countryName) {
        if (includeDeleted)
            return emergencyServiceRepo.findByCountryName(countryName);

        return emergencyServiceRepo.findByCountryNameAndDeletedFalse(countryName);
    }

    @Override
    public Optional<EmergencyService> findId(String id) {
        Optional<EmergencyService> found = emergencyServiceRepo.findById(id);
        if (found.isPresent())
            return found;

        String msg = "Servicio de emergencia con id '" + id + "' no encontrado";
        log.warning(msg);
        throw new EmergencyServiceNotFoundException(msg);
    }

    @Override
    public List<EmergencyService> findByCity(
            Boolean includeDeleted, String cityName, String departmentName, String countryName) {

        if (includeDeleted)
            return emergencyServiceRepo.findByCountryNameAndDepartmentNameAndCityName(
                    countryName, departmentName, cityName);

        return emergencyServiceRepo.findByCountryNameAndDepartmentNameAndCityNameAndDeletedFalse(
                countryName, departmentName, cityName);
    }

    @Override
    public List<EmergencyService> findByDepartment(Boolean includeDeleted, String departmentName, String countryName) {
        if (includeDeleted)
            return emergencyServiceRepo.findByCountryNameAndDepartmentName(countryName, departmentName);

        return emergencyServiceRepo.findByCountryNameAndDepartmentNameAndDeletedFalse(countryName, departmentName);
    }

    @Override
    public EmergencyService findByName(
            Boolean includeDeleted, String name, String cityName, String departmentName, String countryName) {

        if (includeDeleted)
            return emergencyServiceRepo.findByCountryNameAndDepartmentNameAndCityNameAndNameIgnoreCase(
                    countryName, departmentName, cityName, name);

        return emergencyServiceRepo.findByCountryNameAndDepartmentNameAndCityNameAndNameIgnoreCaseAndDeletedFalse(
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
        String msg = "No se encontro el servicio de emergencia con id: " + id;
        log.warning(msg);
        throw new EmergencyServiceNotFoundException(msg);
    }

    // Asigna los valores por default a la entitdad
    private void defaultValues(EmergencyService emergencyService){
        emergencyService.setRegistrationDate(LocalDate.now());
        emergencyService.setDeleted(false);
    }

    //Asigna los valores a la nueva entitdad, tomados de la vieja entidad (de la persistida)
    //evitando la modificacion accidental
    private void defaultValues(EmergencyService oldEmergencyService, EmergencyService newEmergencyService){
        newEmergencyService.setDeleted(oldEmergencyService.getDeleted());
    }

}
