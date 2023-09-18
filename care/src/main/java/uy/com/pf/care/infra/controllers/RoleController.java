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
import uy.com.pf.care.model.documents.Role;
import uy.com.pf.care.model.documents.Video;
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
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
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
            String msg = "Error buscando todos los roles de: " + countryName + ", " + departmentName;
            log.warning(msg + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    @GetMapping("findId/{roleId}")
    public ResponseEntity<Optional<Role>> findId(@PathVariable String roleId) {
        try {
            return ResponseEntity.ok(roleService.findId(roleId));

        }catch(RoleNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e) {
            String msg = "Error buscando rol con Id " + roleId;
            log.warning(msg);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
        }
    }

    /*
    @PutMapping("/addVideos/{roleId}")
    public ResponseEntity<Boolean> addVideos(
            @PathVariable String roleId,
            @Valid @NotNull @RequestBody List<String> videosId){
        try {
            return ResponseEntity.ok(roleService.addVideos(roleId, videosId));

        }catch(RoleNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(RoleUpdateVideosIdException | RoleAddVideoException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    */

    /*
    @PutMapping("/delVideo/{roleId}/{videoId}")
    public ResponseEntity<Boolean> delVideo(@PathVariable String roleId, @PathVariable String videoId) {
        try {
            return ResponseEntity.ok(roleService.delVideo(roleId, videoId));

        }catch(RoleNotFoundException | RoleVideoNotRegisteredException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(RoleUpdateVideosIdException | RoleDelVideoException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    */

    /*
    @GetMapping("findVideos/{roleId}")
    public ResponseEntity<List<Video>> findVideos(@PathVariable String roleId){
        try {
            return ResponseEntity.ok(roleService.findVideos(roleId));

        }catch(RoleNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(RoleFindVideosException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    */

}
