package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.EmergencyServiceSaveException;
import uy.com.pf.care.model.documents.EmergencyService;
import uy.com.pf.care.model.documents.Patient;
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
            return emergencyServiceRepo.findByCountryName(countryName);
        else
            return emergencyServiceRepo.findByCountryNameAndDeletedFalse(countryName);
    }

    @Override
    public Optional<EmergencyService> findId(String id) {
        return emergencyServiceRepo.findById(id);
    }

    @Override
    public List<EmergencyService> findByCity(
            Boolean includeDeleted, String cityName, String departmentName, String countryName) {

        if (includeDeleted)
            return emergencyServiceRepo.findByCityNameAndDepartmentNameAndCountryName(
                    cityName, departmentName, countryName);
        else
            return emergencyServiceRepo.findByCityNameAndDepartmentNameAndCountryNameAndDeletedFalse(
                    cityName, departmentName, countryName);
    }

    @Override
    public List<EmergencyService> findByDepartment(Boolean includeDeleted, String departmentName, String countryName) {
        if (includeDeleted)
            return emergencyServiceRepo.findByDepartmentNameAndCountryName(departmentName, countryName);
        else
            return emergencyServiceRepo.findByDepartmentNameAndCountryNameAndDeletedFalse(departmentName, countryName);
    }

    @Override
    public EmergencyService findByName(
            Boolean includeDeleted, String name, String cityName, String departmentName, String countryName) {

        if (includeDeleted)
            return emergencyServiceRepo.findByNameAndCityNameAndDepartmentNameAndCountryName(
                    name, cityName, departmentName, countryName);
        else
            return emergencyServiceRepo.findByNameAndCityNameAndDepartmentNameAndCountryNameAndDeletedFalse(
                    name,
                    cityName,
                    departmentName,
                    countryName);
    }

    @Override
    public boolean logicalDelete(String id) {
        Optional<EmergencyService> emergencyService = this.findId(id);
        if (emergencyService.isPresent()) {
            emergencyService.get().setDeleted(true);
            this.save(emergencyService.get());
            return true;
        }
        return false;
    }
}
