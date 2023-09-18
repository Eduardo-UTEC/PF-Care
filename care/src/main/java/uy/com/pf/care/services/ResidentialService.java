package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.ResidentialSaveException;
import uy.com.pf.care.exceptions.ResidentialUpdateException;
import uy.com.pf.care.model.documents.Residential;
import uy.com.pf.care.infra.repos.IResidentialRepo;

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
            this.defaultValues(residential);
            String id = residentialRepo.save(residential).getResidentialId();
            log.info("*** Resiencial guardado con exito: " + LocalDateTime.now());
            return id;

        }catch(Exception e){
            log.warning("*** ERROR GUARDANDO RESIDENCIAL: " + e);
            throw new ResidentialSaveException("*** ERROR GUARDANDO RESIDENCIAL: ");
        }
    }

    @Override
    public Boolean update(Residential newResidential) {
        try{
            Optional<Residential> entityFound = residentialRepo.findById(newResidential.getResidentialId());
            if (entityFound.isPresent()){
                this.defaultValues(entityFound.get(), newResidential);
                residentialRepo.save(newResidential);
                log.info("Residencial actualizado con exito");
                return true;
            }
            log.info("No se encontro el residencial con id " + newResidential.getResidentialId());
            return false;

        }catch(Exception e){
            log.warning("*** ERROR ACTUALIZANDO RESIDENCIAL: " + e);
            throw new ResidentialUpdateException("*** ERROR ACTUALIZANDO RESIDENCIAL: ");
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
            return residentialRepo.findByCountryNameAndDepartmentNameAndCityNameAndNameIgnoreCase(
                    countryName, departmentName, cityName, name);

        return residentialRepo.findByCountryNameAndDepartmentNameAndCityNameAndNameIgnoreCaseAndDeletedFalse(
                countryName, departmentName, cityName, name);
    }

    // Asigna los valores por default a la entitdad
    private void defaultValues(Residential residential){
        residential.setDeleted(false);
    }

    // Asigna los valores a la nueva entitdad, tomados de la vieja entidad (de la persistida)
    private void defaultValues(Residential oldResidential, Residential newResidential){
        newResidential.setDeleted(oldResidential.getDeleted());
    }

}
