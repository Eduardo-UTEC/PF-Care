package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.EmergencyServiceSaveException;
import uy.com.pf.care.model.documents.EmergencyService;
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
    public EmergencyService save(EmergencyService emergencyService) {
        try{
            EmergencyService newEmeregencyService = emergencyServiceRepo.save(emergencyService);
            log.info("*** Servicio de Emergencia guardado con exito: " + LocalDateTime.now());
            return newEmeregencyService;

        }catch(Exception e){
            log.warning("*** ERROR GUARDANDO SERVICIO DE EMERGENCIA: " + e);
            throw new EmergencyServiceSaveException(emergencyService);
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
            this.save(emergencyService.get());
            return true;
        }
        return false;
    }
}
