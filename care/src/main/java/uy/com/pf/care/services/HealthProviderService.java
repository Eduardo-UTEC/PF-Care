package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.HealthProviderSaveException;
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
    public HealthProvider save(HealthProvider healthProvider) {
        try{
            HealthProvider newHealthProvider = healthProviderRepo.save(healthProvider);
            log.info("*** Proveedor de Salud guardado con exito: " + LocalDateTime.now());
            return newHealthProvider;

        }catch(Exception e){
            log.warning("*** ERROR GUARDANDO PROVEEDOR DE SALUD: " + e);
            throw new HealthProviderSaveException(healthProvider);
        }
    }

    @Override
    public Optional<HealthProvider> findId(String id) {
        return healthProviderRepo.findById(id);
    }

    @Override
    public List<HealthProvider> findAll(String countryName) {
        return healthProviderRepo.findByCountryName(countryName);
    }

    @Override
    public List<HealthProvider> findByCity(String cityName, String departmentName, String countryName) {
        return healthProviderRepo.findByCityNameAndDepartmentNameAndCountryName(cityName, departmentName, countryName);
    }

    @Override
    public List<HealthProvider> findByDepartment(String departmentName, String countryName) {
        return healthProviderRepo.findByDepartmentNameAndCountryName(departmentName, countryName);
    }

}
