package uy.com.pf.care.infra.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.model.documents.User;
import uy.com.pf.care.model.enums.RoleEnum;
import uy.com.pf.care.model.objects.LoginObjectAuthenticate;
import uy.com.pf.care.services.IUserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Log
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @NotNull @RequestBody User user){
        try {
            return ResponseEntity.ok(userService.save(user));

        } catch (UserDuplicateKeyException e){
          throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UserSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/updateEntityId/{userId}/{roleOrdinal}/{entityId}")
    public ResponseEntity<Boolean> updateEntityId(
            @PathVariable String userId,
            @PathVariable int roleOrdinal,
            @PathVariable String entityId) {

            try {
                return ResponseEntity.ok(userService.updateEntityIdInRolesList(
                        userId, RoleEnum.values()[roleOrdinal], entityId));

            }catch(UserAlreadyLinkedException e){
                throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
            }catch(UserNotFoundException | UserRoleNotFoundException e){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }catch (UserUpdateEntityIdInRolesListException e){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
    }

    //@PutMapping("addNewRol/{userId}/{roleId}/{roleOrdinal}")
    @PutMapping("addNewRol/{userId}/{roleOrdinal}")
    public ResponseEntity<Boolean> addNewRol(
            @PathVariable String userId,
            //@PathVariable String roleId,
            @PathVariable int roleOrdinal) {

        try{
            //return ResponseEntity.ok(userService.addNewRol(
            //        userId, roleId, RoleEnum.values()[roleOrdinal]));
            return ResponseEntity.ok(userService.addNewRol(userId, RoleEnum.values()[roleOrdinal]));


        }catch (UserAlreadyDefinedRolException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(UserNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (UserUpdateEntityIdInRolesListException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@Valid @NotNull @RequestBody User newUser){
        try {
            return ResponseEntity.ok(userService.update(newUser));

        }catch (UserNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(UserUpdateException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @GetMapping(value = "findAll/{countryName}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<User>> findAll(@PathVariable String countryName){
        try{
            return ResponseEntity.ok(userService.findAll(countryName));

        }catch(UserFindAllException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "findId/{id}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<User> findId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.findId(id));

        }catch (UserNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(UserFindIdException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /*@GetMapping("findIdentificationDocument/{document}/{countryName}")
    public ResponseEntity<Optional<User>> findIdentificationDocument(
            @PathVariable Integer document,
            @PathVariable String countryName) {
        try{
            return ResponseEntity.ok(userService.findIdentificationDocument(document, countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando usuario con documento de identificación " + document + " (" + countryName + ")");

        }
    }*/

    @PostMapping(value = "login", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<User> login(@Valid @NotNull @RequestBody LoginObjectAuthenticate loginObjectAuthenticate) {
        try{
            return ResponseEntity.ok(userService.login(loginObjectAuthenticate));

        }catch(UserInvalidLoginException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }catch(UserLoginException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /*@GetMapping("exist/{userName}")
    public ResponseEntity<Boolean> exist(@PathVariable String userName) {
        try{
            return ResponseEntity.ok(userService.existUserName(userName));

        }catch(UserExistUserNameException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }*/

    @GetMapping("exist/{identificationDocument}/{countryName}")
    public ResponseEntity<Boolean> exist(@PathVariable Integer identificationDocument, String countryName) {
        try{
            return ResponseEntity.ok(userService.findIdentificationDocument(identificationDocument, countryName)
                    .isPresent());

        }catch(UserExistDocumentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }



    /*@GetMapping(value = "findUserName/{userName}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<User> findUserName(@PathVariable String userName) {
        try{
            return ResponseEntity.ok(userService.findUserName(userName));

        }catch(UserNameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(UserFindUserNameException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

     */

    @GetMapping(
            value = "findCity/{cityName}/{departmentName}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<User>> findCity(@PathVariable String cityName,
                                               @PathVariable String departmentName,
                                               @PathVariable String countryName) {
        try{
            return ResponseEntity.ok(userService.findCity(cityName, departmentName,countryName));

        }catch(UserFindCityException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(
            value = "findDepartment/{departmentName}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<User>> findDepartment(@PathVariable String departmentName,
                                                     @PathVariable String countryName) {
        try{
            return ResponseEntity.ok(userService.findDepartment(departmentName,countryName));

        }catch(UserFindDepartmentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
