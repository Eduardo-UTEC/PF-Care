package uy.com.pf.care.infra.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.model.documents.Video;
import uy.com.pf.care.model.enums.RoleEnum;
import uy.com.pf.care.model.objects.VideoObject;
import uy.com.pf.care.services.IVideoService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videos")
@Log
public class VideoController {
    @Autowired
    private IVideoService videoService;

    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @NotNull @RequestBody Video video){
        try {
            return ResponseEntity.ok(videoService.save(video));

        }catch (VideoWithoutRoleException | VideoDuplicateKeyException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch (VideoSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@Valid @NotNull @RequestBody Video newVideo){
        try {
            return ResponseEntity.ok(videoService.update(newVideo));

        }catch(VideoNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(VideoUpdateException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/addRoles/{videoId}")
    public ResponseEntity<Boolean> addRoles(
            @PathVariable String videoId,
            @Valid @NotNull @RequestBody List<RoleEnum> ordinalRoles){

        try {
            return ResponseEntity.ok(videoService.addRoles(videoId, ordinalRoles));

        }catch(VideoNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(VideoAddRolesException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    @PutMapping("/changeRole/{videoId}/{oldOrdinalRole}/{newOrdinalRole}")
    public ResponseEntity<Boolean> changeRole(
            @PathVariable String videoId,
            @PathVariable int oldOrdinalRole,
            @PathVariable int newOrdinalRole){

        try {
            return ResponseEntity.ok(videoService.changeRole(
                    videoId,
                    RoleEnum.values()[oldOrdinalRole],
                    RoleEnum.values()[newOrdinalRole]));

        }catch (VideoRoleAlreadyLinkedException | VideoRolNotLinkedException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(VideoNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(VideoAddRolesException | VideoChangeRoleException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    @PutMapping("/delRole/{videoId}")
    public ResponseEntity<Boolean> delRole(
            @PathVariable String videoId,
            @Valid @NotNull @RequestBody List<RoleEnum> ordinalRoles){

        try {
            return ResponseEntity.ok(videoService.delRoles(videoId, ordinalRoles));

        }catch(VideoNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(VideoDelRoleException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @GetMapping("findAll/{departmentName}/{countryName}")
    public ResponseEntity<List<Video>> findAll(@PathVariable String departmentName, @PathVariable String countryName){
        try{
            return ResponseEntity.ok(videoService.findAll(countryName, departmentName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando todos los videos de: " + countryName + ", " + departmentName);
        }
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<Optional<Video>> findId(@PathVariable String id) {
        try{
            return ResponseEntity.ok(videoService.findId(id));

        }catch(VideoNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(VideoFindIdException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("findByRole/{ordinalRole}/{departmentName}/{countryName}")
    public ResponseEntity<List<VideoObject>> findByRole(
            @PathVariable Integer ordinalRole,
            @PathVariable String countryName,
            @PathVariable String departmentName){
        try{
            return ResponseEntity.ok(videoService.findByRole(ordinalRole, countryName, departmentName));

        }catch(VideoFindByRoleException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


}
