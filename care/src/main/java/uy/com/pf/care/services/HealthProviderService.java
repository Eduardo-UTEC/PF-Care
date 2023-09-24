package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.HealthProviderNotFoundException;
import uy.com.pf.care.exceptions.HealthProviderSaveException;
import uy.com.pf.care.exceptions.HealthProviderUpdateException;
import uy.com.pf.care.infra.repos.IHealthProviderRepo;
import uy.com.pf.care.model.documents.HealthProvider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class HealthProviderService implements IHealthProviderService {

    @Autowired
    private IHealthProviderRepo healthProviderRepo;

    @Override
    public String save(HealthProvider healthProvider) {
        try{
            this.defaultValues(healthProvider);
            String id = healthProviderRepo.save(healthProvider).getHealthProviderId();
            log.info("Proveedor de Salud guardado con exito: " + LocalDateTime.now());
            return id;

        }catch(Exception e){
            String msg = "*** ERROR GUARDANDO PROVEEDOR DE SALUD";
            log.warning(msg + ": " + e.getMessage());
            throw new HealthProviderSaveException(msg);
        }
    }

    @Override
    public Boolean update(HealthProvider newHealthProvider) {
        try {
            Optional<HealthProvider> entityFound = healthProviderRepo.findById(newHealthProvider.getHealthProviderId());
            if (entityFound.isPresent()) {
                this.defaultValues(entityFound.get(), newHealthProvider);
                healthProviderRepo.save(newHealthProvider);
                log.info("Proveedor de salud actualizado con exito");
                return true;
            }
            String msg = "No se encontro el proveedor de salud con id " + newHealthProvider.getHealthProviderId();
            log.info(msg);
            throw new HealthProviderNotFoundException(msg);

        }catch (HealthProviderNotFoundException e){
            throw new HealthProviderNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR ACTUALIZANDO PROVEEDOR DE SALUD";
            log.warning(msg + ": " + e.getMessage());
            throw new HealthProviderUpdateException(msg);
        }

    }

    @Override
    public Optional<HealthProvider> findId(String id) {

        Optional<HealthProvider> found = healthProviderRepo.findById(id);
        if (found.isPresent())
            return found;

        String msg = "No se encontro un proveedor de salud con id " + id;
        log.warning(msg);
        throw new HealthProviderNotFoundException(msg);
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
        return healthProviderRepo.findByCountryNameAndDepartmentNameAndCityNameAndNameAndDeletedFalse(
                    countryName, departmentName, cityName, name);
    }

    @Override
    public List<HealthProvider> findByCity(
            Boolean includeDeleted, String cityName, String departmentName, String countryName) {

        if (includeDeleted)
            return healthProviderRepo.findByCountryNameAndDepartmentNameAndCityName(
                countryName, departmentName, cityName);
        else
            return healthProviderRepo.findByCountryNameAndDepartmentNameAndCityNameAndDeletedFalse(
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
        String msg = "No se encontro un proveedor de salud con id " + id;
        log.warning(msg);
        throw new HealthProviderNotFoundException(msg);
    }

    // Asigna los valores por default a la entitdad
    private void defaultValues(HealthProvider healthProvider){
        healthProvider.setDeleted(false);
    }

    // Asigna los valores a la nueva entitdad, tomados de la vieja entidad (de la persistida)
    private void defaultValues(HealthProvider oldHealthProvider, HealthProvider newHealthProvider){
        newHealthProvider.setDeleted(oldHealthProvider.getDeleted());
    }

}
