package uy.com.pf.care.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.FormalCaregiverSaveException;
import uy.com.pf.care.model.documents.FormalCaregiverScore;
import uy.com.pf.care.model.objects.VoteObject;
import uy.com.pf.care.services.IFormalCaregiverScoreService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/formalCaregiversScores")
public class FormalCaregiverScoreController {

    @Autowired
    private IFormalCaregiverScoreService formalCaregiverScoreService;

    @PostMapping("/add")
    public ResponseEntity<FormalCaregiverScore> add(@RequestBody FormalCaregiverScore formalCaregiverScore){
        try{
            return ResponseEntity.ok(formalCaregiverScoreService.save(formalCaregiverScore));

        }catch (FormalCaregiverSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error guardando rating del cuidador formal");
        }
    }

    @GetMapping("findAll/{formalCaregiverId}")
    public ResponseEntity<List<FormalCaregiverScore>> findAll(@PathVariable String formalCaregiverId){
        try{
            return ResponseEntity.ok(formalCaregiverScoreService.findAll(formalCaregiverId));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando ratings del cuidador formal con id " + formalCaregiverId);
        }
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<Optional<FormalCaregiverScore>> findId(@PathVariable String id) {
        try{
            return ResponseEntity.ok(formalCaregiverScoreService.findId(id));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error buscando rating con id " + id);
        }
    }

    //  Devuelve true si la operación fue exitosa.
    @PostMapping("updateScore/{formalCaregiverId}/{patientId}")
    public ResponseEntity<Boolean> updateScore(
            @PathVariable String formalCaregiverId,
            @PathVariable String patientId,
            @RequestBody VoteObject rating) {

        try{
            return ResponseEntity.ok(formalCaregiverScoreService.updateScore(formalCaregiverId, patientId, rating));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo actualizar el rating del cuidador formal con id " + formalCaregiverId +
                    " del paciente con id " + patientId);
        }
    }

    //  Devuelve true si la operación fue exitosa.
    @GetMapping("find_rating_of_patient/{formalCaregiverId}/{patientId}")
    public ResponseEntity<FormalCaregiverScore> findRatingOfPatient(
            @PathVariable String formalCaregiverId,
            @PathVariable String patientId) {

        try{
            return ResponseEntity.ok(formalCaregiverScoreService.findRatingOfPatient(formalCaregiverId, patientId));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando rating del cuidador formal con id " + formalCaregiverId +
                    "del paciente con id " + patientId);
        }
    }

}
