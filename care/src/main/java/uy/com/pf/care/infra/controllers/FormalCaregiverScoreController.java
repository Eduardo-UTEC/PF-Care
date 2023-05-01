package uy.com.pf.care.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.FormalCaregiverSaveException;
import uy.com.pf.care.exceptions.FormalCaregiverScoreDuplicateKeyException;
import uy.com.pf.care.exceptions.FormalCaregiverScoreNotFoundException;
import uy.com.pf.care.exceptions.FormalCaregiverScoreSaveException;
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
    public ResponseEntity<String> add(@RequestBody FormalCaregiverScore formalCaregiverScore){
        try{
            return ResponseEntity.ok(formalCaregiverScoreService.save(formalCaregiverScore));

        }catch (FormalCaregiverScoreDuplicateKeyException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());

        }catch (FormalCaregiverScoreSaveException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("findAll/{formalCaregiverId}")
    public ResponseEntity<List<FormalCaregiverScore>> findAll(@PathVariable String formalCaregiverId){
        try{
            return ResponseEntity.ok(formalCaregiverScoreService.findAll(formalCaregiverId));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando puntajes del cuidador formal con id " + formalCaregiverId);
        }
    }

    @GetMapping("findId/{id}")
    public ResponseEntity<Optional<FormalCaregiverScore>> findId(@PathVariable String id) {
        try{
            return ResponseEntity.ok(formalCaregiverScoreService.findId(id));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error buscando score con id " + id);
        }
    }

    //  Devuelve true si la operación fue exitosa.
    @GetMapping("findScore/{formalCaregiverId}/{patientId}")
    public ResponseEntity<FormalCaregiverScore> findScore(
            @PathVariable String formalCaregiverId,
            @PathVariable String patientId) {

        try{
            return ResponseEntity.ok(formalCaregiverScoreService.findScore(formalCaregiverId, patientId));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando score del cuidador formal con id " + formalCaregiverId +
                    "del paciente con id " + patientId);
        }
    }

    //  Devuelve true si la operación fue exitosa.
    @PostMapping("/updateScore")
    public ResponseEntity<Boolean> updateScore(@RequestBody FormalCaregiverScore formalCaregiverScore ) {

        try {
            return ResponseEntity.ok(formalCaregiverScoreService.updateScore(formalCaregiverScore));

        }catch (FormalCaregiverScoreNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
