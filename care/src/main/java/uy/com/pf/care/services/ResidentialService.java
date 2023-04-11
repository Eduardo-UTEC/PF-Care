package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.ResidentialSaveException;
import uy.com.pf.care.model.documents.HealthProvider;
import uy.com.pf.care.model.documents.Residential;
import uy.com.pf.care.repos.IResidentialRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class ResidentialService implements IResidentialService{

    @Autowired
    private IResidentialRepo residentialRepo;

    //private static final Logger log = LoggerFactory.getLogger(CuidadosApplication.class);

    @Override
    public Residential save(Residential residential) {
        try{
            Residential newResidential = residentialRepo.save(residential);
            log.info("*** Resiencial guardado con exito: " + LocalDateTime.now());
            return newResidential;

        }catch(Exception e){
            log.warning("*** ERROR GUARDANDO RESIDENCIAL: " + e);
            throw new ResidentialSaveException(residential);
        }
    }

    @Override
    public Boolean setDeletion(String id, Boolean isDeleted) {
        Optional<Residential> residential = this.findId(id);
        if (residential.isPresent()) {
            residential.get().setDeleted(isDeleted);
            this.save(residential.get());
            return true;
        }
        return false;
    }

    @Override
    public Optional<Residential> findId(String id) {
        return residentialRepo.findById(id);
    }

    @Override
    public List<Residential> findByCountry(String countryName) {
        return residentialRepo.findByCountryName(countryName);
    }

    @Override
    public List<Residential> findByDepartment(String departmentName, String countryName) {
        return residentialRepo.findByDepartmentNameAndCountryName(departmentName, countryName);
    }

    @Override
    public List<Residential> findByCity(String cityName, String departmentName, String countryName) {
        return residentialRepo.findByCityNameAndDepartmentNameAndCountryName(cityName, departmentName, countryName);
    }

}
