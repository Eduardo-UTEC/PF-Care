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
import uy.com.pf.care.exceptions.UserSaveException;
import uy.com.pf.care.exceptions.UserUpdateException;
import uy.com.pf.care.model.documents.User;
import uy.com.pf.care.model.objects.LoginObject;
import uy.com.pf.care.services.IUserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Log
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @NotNull @RequestBody User user){
        try{
            return ResponseEntity.ok(userService.save(user));

        }catch (UserSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error guardando usuario");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@Valid @NotNull @RequestBody User newUser){
        try {
            return ResponseEntity.ok(userService.update(newUser));

        }catch(UserUpdateException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("findAll/{countryName}")
    public ResponseEntity<List<User>> findAll(@PathVariable String countryName){
        try{
            return ResponseEntity.ok(userService.findAll(countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando todos los usuarios de " + countryName);
        }
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<Optional<User>> findId(@PathVariable String id) {
        try{
            return ResponseEntity.ok(userService.findId(id));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando usuario con id " + id);
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

    @GetMapping("login")
    public ResponseEntity<User> login(@Valid @NotNull @RequestBody LoginObject loginObject) {
        try{
            return ResponseEntity.ok(userService.login(loginObject));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error realizando login: " +
                    e.getMessage());

        }
    }

    @GetMapping("exist/{userName}")
    public ResponseEntity<Boolean> exist(@PathVariable String userName) {
        try{
            return ResponseEntity.ok(userService.existUserName(userName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error buscando usuario: " +
                    e.getMessage());

        }
    }


    @GetMapping("findUserName/{userName}")
    public ResponseEntity<Optional<User>> findUserName(@PathVariable String userName) {
        try{
            return ResponseEntity.ok(userService.findUserName(userName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando usuario con userName: " + userName);

        }
    }

    @GetMapping("findCity/{cityName}/{departmentName}/{countryName}")
    public ResponseEntity<List<User>> findCity(@PathVariable String cityName,
                                               @PathVariable String departmentName,
                                               @PathVariable String countryName) {
        try{
            return ResponseEntity.ok(userService.findCity(cityName, departmentName,countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando usuarios en ciudad/localidad: " +
                            cityName + " (" + departmentName + ", " + countryName + ")");
        }
    }

    @GetMapping("findDepartment/{departmentName}/{countryName}")
    public ResponseEntity<List<User>> findDepartment(@PathVariable String departmentName,
                                                     @PathVariable String countryName) {
        try{
            return ResponseEntity.ok(userService.findDepartment(departmentName,countryName));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando usuarios en departamento/provincia: " +
                            departmentName + " (" + countryName + ")");
        }
    }

}
