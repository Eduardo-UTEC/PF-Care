package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.infra.repos.IMaterialRepo;
import uy.com.pf.care.model.documents.Material;

import java.util.List;
import java.util.Optional;

@Service
@Log
public class MaterialService implements IMaterialService{

    @Autowired
    private IMaterialRepo materialRepo;

    @Override
    public String save(Material material) {
        try{
            this.defaultValues(material);
            String newMaterialId = materialRepo.save(material).getMaterialId();
            log.info("Material guardado con exito");
            return newMaterialId;

        }catch(Exception e){
            String msg = "*** ERROR GUARDANDO MATERIAL";
            log.warning(msg + ": " + e.getMessage());
            throw new MaterialSaveException(msg);
        }
    }

    @Override
    public Boolean update(Material newMaterial) {
        try {
            Optional<Material> entityFound = materialRepo.findById(newMaterial.getMaterialId());
            if (entityFound.isPresent()) {
                this.defaultValues(newMaterial, entityFound.get());
                materialRepo.save(newMaterial);
                log.info("Material actualizado con exito");
                return true;
            }
            String msg = "No se encontro el material con id: " + newMaterial.getMaterialId();
            log.warning(msg);
            throw new MaterialNotFoundException(msg);

        }catch (MaterialNotFoundException e){
            throw new MaterialNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR ACTUALIZANDO MATERIAL" ;
            log.warning(msg+ ": " + e.getMessage());
            throw new MaterialUpdateException(msg);
        }
    }

    @Override
    public List<Material> findAll(Boolean includeDeleted, String countryName) {
        if (includeDeleted)
            return materialRepo.findByCountryName(countryName);
        return materialRepo.findByCountryNameAndDeletedFalse(countryName);
    }

    @Override
    public Optional<Material> findId(String id) {
        Optional<Material> found = materialRepo.findById(id);
        if (found.isPresent())
            return found;

        String msg = "Material con id '" + id + "' no encontrado";
        log.warning(msg);
        throw new MaterialNotFoundException(msg);
    }

    @Override
    public List<Material> findIds(List<String> materialsId) {
        return materialRepo.findAllById(materialsId);
    }

    @Override
    public List<Material> findName(Boolean includeDeleted, Boolean exactMatch, String name, String countryName) {
        if (includeDeleted) {
            return exactMatch ?
                    materialRepo.findByCountryNameAndNameIgnoreCase(countryName, name) :
                    materialRepo.findByCountryNameAndNameIgnoreCaseLike(countryName, name);
        }
        return exactMatch ?
                materialRepo.findByCountryNameAndNameIgnoreCaseAndDeletedFalse(countryName, name) :
                materialRepo.findByCountryNameAndNameIgnoreCaseLikeAndDeletedFalse(countryName,name);
    }

    @Override
    public Boolean setDeletion(String id, Boolean isDeleted) {
        Optional<Material> found = this.findId(id);
        if (found.isPresent()) {
            found.get().setDeleted(isDeleted);
            materialRepo.save(found.get());
            return true;
        }
        String msg = "No se encontro el material con id: " + id;
        log.warning(msg);
        throw new MaterialNotFoundException(msg);
    }

    private void defaultValues(Material material){
        material.setDeleted(false);
    }

    private void defaultValues(Material newMaterial, Material oldMaterial){
        newMaterial.setDeleted(oldMaterial.getDeleted());
    }
}
