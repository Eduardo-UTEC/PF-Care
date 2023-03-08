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
    public List<EmergencyService> findAll(String countryName) {
        return emergencyServiceRepo.findByCountryNameAndDeletedFalse(countryName);
    }

    @Override
    public Optional<EmergencyService> findId(String id) {
        return emergencyServiceRepo.findById(id);
    }

    @Override
    public List<EmergencyService> findByCity(String cityName, String departmentName, String countryName) {
        return emergencyServiceRepo.findByCityNameAndDepartmentNameAndCountryNameAndDeletedFalse(cityName, departmentName, countryName);
    }

    @Override
    public List<EmergencyService> findByDepartment(String departmentName, String countryName) {
        return emergencyServiceRepo.findByDepartmentNameAndCountryNameAndDeletedFalse(departmentName, countryName);
    }

    @Override
    public EmergencyService findByName(String name, String cityName, String departmentName, String countryName) {
        return emergencyServiceRepo.findByNameAndCityNameAndDepartmentNameAndCountryNameAndDeletedFalse(
                name,
                cityName,
                departmentName,
                countryName);
    }
}
