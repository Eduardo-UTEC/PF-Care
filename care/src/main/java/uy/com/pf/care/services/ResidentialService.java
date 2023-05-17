package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.ResidentialSaveException;
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
    public String save(Residential residential) {
        try{
            Residential newResidential = residentialRepo.save(residential);
            log.info("*** Resiencial guardado con exito: " + LocalDateTime.now());
            return newResidential.getResidentialId();

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
            residentialRepo.save(residential.get());
            return true;
        }
        return false;
    }

    @Override
    public Optional<Residential> findId(String id) {
        return residentialRepo.findById(id);
    }

    @Override
    public List<Residential> findCountry(Boolean includeDeleted, String countryName) {
        if(includeDeleted)
            return residentialRepo.findByCountryName(countryName);

        return residentialRepo.findByCountryNameAndDeletedFalse(countryName);
    }

    @Override
    public List<Residential> findDepartment(Boolean includeDeleted, String countryName, String departmentName) {
        if(includeDeleted)
            return residentialRepo.findByCountryNameAndDepartmentName(countryName, departmentName);

        return residentialRepo.findByCountryNameAndDepartmentNameAndDeletedFalse(countryName, departmentName);
    }

    @Override
    public List<Residential> findCity(
            Boolean includeDeleted, String countryName, String departmentName, String cityName) {

        if(includeDeleted)
            return residentialRepo.findByCountryNameAndDepartmentNameAndCityName(countryName, departmentName, cityName);

        return residentialRepo.findByCountryNameAndDepartmentNameAndCityNameAndDeletedFalse(
                countryName, departmentName, cityName);
    }

    @Override
    public List<Residential> findName(
            Boolean includeDeleted, String countryName, String departmentName, String cityName, String name) {

        if(includeDeleted)
            return residentialRepo.findByCountryNameAndDepartmentNameAndCityNameAndName(
                    countryName, departmentName, cityName, name);

        return residentialRepo.findByCountryNameAndDepartmentNameAndCityNameAndNameAndDeletedFalse(
                countryName, departmentName, cityName, name);
    }

}
