package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.HealthProviderSaveException;
import uy.com.pf.care.model.documents.FormalCaregiver;
import uy.com.pf.care.model.documents.HealthProvider;
import uy.com.pf.care.repos.IHealthProviderRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class HealthProviderService implements IHealthProviderService {

    @Autowired
    private IHealthProviderRepo healthProviderRepo;

    //private static final Logger log = LoggerFactory.getLogger(CuidadosApplication.class);

    @Override
    public String save(HealthProvider healthProvider) {
        try{
            healthProvider.setDeleted(false); // Seteo propiedad por defecto
            HealthProvider newHealthProvider = healthProviderRepo.save(healthProvider);
            log.info("*** Proveedor de Salud guardado con exito: " + LocalDateTime.now());
            return newHealthProvider.getHealthProviderId();

        }catch(Exception e){
            log.warning("*** ERROR GUARDANDO PROVEEDOR DE SALUD: " + e);
            throw new HealthProviderSaveException(healthProvider);
        }
    }

    @Override
    public Boolean update(HealthProvider newHealtProvider) {
        Optional<HealthProvider> entityFound = healthProviderRepo.findById(newHealtProvider.getHealthProviderId());
        if (entityFound.isPresent()){
            // Tomo valor de la propiedad persistida. Esta propiedad solo se puede alterar mediante setDeleted.
            newHealtProvider.setDeleted(entityFound.get().getDeleted());
            healthProviderRepo.save(newHealtProvider);
            log.info("Proveedor de salud actualizado con exito");
            return true;
        }
        log.info("No se encontro el proveedor de salud con id " + newHealtProvider.getHealthProviderId());
        return false;
    }

    @Override
    public Optional<HealthProvider> findId(String id) {
        return healthProviderRepo.findById(id);
    }

    @Override
    public List<HealthProvider> findAll(Boolean includeDeleted, String countryName) {
        if (includeDeleted)
            return healthProviderRepo.findByCountryNameOrderByName(countryName);
        else
            return healthProviderRepo.findByCountryNameAndDeletedFalseOrderByName(countryName);
    }

    @Override
    public HealthProvider findByName(String cityName, String departmentName, String countryName, String name) {
        return healthProviderRepo.findByCountryNameAndDepartmentNameAndCityNameAndNameAndDeletedFalseOrderByName(
                    countryName, departmentName, cityName, name);
    }

    @Override
    public List<HealthProvider> findByCity(
            Boolean includeDeleted, String cityName, String departmentName, String countryName) {

        if (includeDeleted)
            return healthProviderRepo.findByCountryNameAndDepartmentNameAndCityNameOrderByName(
                countryName, departmentName, cityName);
        else
            return healthProviderRepo.findByCountryNameAndDepartmentNameAndCityNameAndDeletedFalseOrderByName(
                    countryName, departmentName, cityName);
    }

    @Override
    public List<HealthProvider> findByDepartment(Boolean includeDeleted, String departmentName, String countryName) {
        if (includeDeleted)
            return healthProviderRepo.findByCountryNameAndDepartmentNameOrderByName(countryName, departmentName);
        else
            return healthProviderRepo.findByCountryNameAndDepartmentNameAndDeletedFalseOrderByName(
                    countryName, departmentName);
    }

    @Override
    public Boolean setDeletion(String id, Boolean isDeleted) {
        Optional<HealthProvider> healthProvider = this.findId(id);
        if (healthProvider.isPresent()) {
            healthProvider.get().setDeleted(isDeleted);
            healthProviderRepo.save(healthProvider.get());
            return true;
        }
        return false;
    }

}
