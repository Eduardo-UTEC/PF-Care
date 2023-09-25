package uy.com.pf.care.infra.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.model.documents.Material;
import uy.com.pf.care.services.IMaterialService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/materials")
@Log
public class MaterialController {

    @Autowired
    private IMaterialService materialService;

    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @NotNull @RequestBody Material material){
        try {
            return ResponseEntity.ok(materialService.save(material));

        }catch (MaterialSaveException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@Valid @NotNull @RequestBody Material newMaterial){
        try {
            return ResponseEntity.ok(materialService.update(newMaterial));

        }catch (MaterialNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(MaterialUpdateException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<Optional<Material>> findId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(materialService.findId(id));

        }catch (MaterialNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e) {
            String msg = "Error buscando material con id " + id;
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @PostMapping("findIds")
    public ResponseEntity<List<Material>> findIds(
            @Valid @NotNull @RequestBody List<String> materialsId) {
        try {
            return ResponseEntity.ok(materialService.findIds(materialsId));

        }catch(Exception e) {
            String msg = "Error buscando materiales por id";
            log.warning(msg + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping("findAll/{includeDeleted}/{countryName}")
    public ResponseEntity<List<Material>> findAll(
            @PathVariable Boolean includeDeleted,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(materialService.findAll(includeDeleted, countryName));

        }catch(Exception e) {
            String msg = "Error buscando todos los materiales de " + countryName;
            log.warning(msg + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    //Boolean includeDeleted, Boolean exactMatch, String name, String countryName
    @GetMapping("findName/{includeDeleted}/{exactMatch}/{name}/{countryName}")
    public ResponseEntity<List<Material>> findName(
            @PathVariable Boolean includeDeleted,
            @PathVariable Boolean exactMatch,
            @PathVariable String name,
            @PathVariable String countryName){

        try{
            return ResponseEntity.ok(materialService.findName(includeDeleted, exactMatch, name, countryName));

        }catch(Exception e) {
            String msg = "Error buscando materiales de nombre '" + name + "' de " + countryName;
            log.warning(msg + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    // Devuelve true si la operación fue exitosa
    @PutMapping("setDeletion/{id}/{isDeleted}")
    public ResponseEntity<Boolean> setDeletion(@PathVariable String id, @PathVariable Boolean isDeleted) {
        try{
            return ResponseEntity.ok(materialService.setDeletion(id, isDeleted));

        }catch (MaterialNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e) {
            String msg = "Error seteando borrado lógico del material con id " + id;
            log.warning(msg + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

}
