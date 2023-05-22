package uy.com.pf.care.infra.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.FormalCaregiverUpdateException;
import uy.com.pf.care.exceptions.RoleSaveException;
import uy.com.pf.care.exceptions.RoleUpdateException;
import uy.com.pf.care.exceptions.UserSaveException;
import uy.com.pf.care.model.documents.Role;
import uy.com.pf.care.services.IRoleService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/roles")
@Log
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @NotNull @RequestBody Role role){
        try{
            return ResponseEntity.ok(roleService.save(role));

        }catch (RoleSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error guardando rol");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@Valid @NotNull @RequestBody Role newRol){
        try {
            return ResponseEntity.ok(roleService.update(newRol));

        }catch(RoleUpdateException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("findAll/{departmentName}/{countryName}")
    public ResponseEntity<List<Role>> findAll(@PathVariable String countryName, @PathVariable String departmentName){
        try{
            return ResponseEntity.ok(roleService.findAll(countryName, departmentName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando todos roles en: " + countryName + ", " + departmentName);
        }
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<Optional<Role>> findId(@PathVariable String id) {
        try{
            return ResponseEntity.ok(roleService.findId(id));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando rol con id " + id);
        }
    }

}
