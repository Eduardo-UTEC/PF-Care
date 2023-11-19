package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.ResidentialNotFoundException;
import uy.com.pf.care.exceptions.ResidentialSaveException;
import uy.com.pf.care.exceptions.ResidentialSetDeletionException;
import uy.com.pf.care.exceptions.ResidentialUpdateException;
import uy.com.pf.care.infra.repos.IResidentialRepo;
import uy.com.pf.care.model.documents.Residential;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class ResidentialService implements IResidentialService{

    @Autowired
    private IResidentialRepo residentialRepo;

    @Override
    public String save(Residential residential) {
        try{
            this.defaultValues(residential);
            String id = residentialRepo.save(residential).getResidentialId();
            log.info("*** Resiencial guardado con exito: " + LocalDateTime.now());
            return id;

        }catch(Exception e){
            String msg = "*** ERROR GUARDANDO RESIDENCIAL";
            log.warning(msg + ": " + e.getMessage());
            throw new ResidentialSaveException(msg);
        }
    }

    @Override
    public String update(Residential newResidential) {
        try {
            Optional<Residential> entityFound = residentialRepo.findById(newResidential.getResidentialId());
            if (entityFound.isPresent()) {
                this.defaultValues(entityFound.get(), newResidential);
                String id = residentialRepo.save(newResidential).getResidentialId();
                log.info("Residencial actualizado con exito");
                return id;
            }
            this.notFound(newResidential.getResidentialId());
            return null;

        }catch(ResidentialNotFoundException e){
            throw new ResidentialNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR ACTUALIZANDO RESIDENCIAL";
            log.warning(msg + ": " + e.getMessage());
            throw new ResidentialUpdateException(msg);
        }

    }

    @Override
    public Boolean setDeletion(String id, Boolean isDeleted) {
        try {
            Optional<Residential> residential = this.findId(id);
            if (residential.isPresent()) {
                residential.get().setDeleted(isDeleted);
                residentialRepo.save(residential.get());
                return true;
            }
            this.notFound(id);
            return false;

        }catch(ResidentialNotFoundException e){
            throw new ResidentialNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "No se pudo setear el borrado lógico del residencial con id " + id;
            log.warning(msg + ": " + e.getMessage());
            throw new ResidentialSetDeletionException(msg);
        }
    }

    @Override
    public Optional<Residential> findId(String id) {
        Optional<Residential> found = residentialRepo.findById(id);
        if (found.isPresent())
            return found;

        this.notFound(id);
        return Optional.empty();
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
        residential.setRegistrationDate(LocalDate.now());
        residential.setDeleted(false);
    }

    // Asigna los valores a la nueva entitdad, tomados de la vieja entidad (de la persistida)
    private void defaultValues(Residential oldResidential, Residential newResidential){
        newResidential.setDeleted(oldResidential.getDeleted());
    }

    private void notFound(String residentialId) {
        String msg = "No se encontro el residencial con id " + residentialId;
        log.info(msg);
        throw new ResidentialNotFoundException(msg);
    }
}
