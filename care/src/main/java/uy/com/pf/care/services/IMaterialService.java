package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.Material;

import java.util.List;
import java.util.Optional;

public interface IMaterialService {
    String save(Material material);
    Boolean update(Material newMaterial);
    Optional<Material> findId(String id);
    List<Material> findIds(List<String> materialsId);
    List<Material> findAll(Boolean includeDeleted, String countryName);
    List<Material> findName(Boolean includeDeleted, Boolean exactMatch, String name, String countryName);
    Boolean setDeletion(String id, Boolean isDeleted);
}
