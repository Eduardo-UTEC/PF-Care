package uy.com.pf.care.infra.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.model.documents.FormalCaregiverScore;
import uy.com.pf.care.services.IFormalCaregiverScoreService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/formalCaregiversScores")
public class FormalCaregiverScoreController {

    @Autowired
    private IFormalCaregiverScoreService formalCaregiverScoreService;

    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @NotNull @RequestBody FormalCaregiverScore formalCaregiverScore){
        try{
            return ResponseEntity.ok(formalCaregiverScoreService.save(formalCaregiverScore));

        }catch (FormalCaregiverScoreDuplicateKeyException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch (FormalCaregiverScorePhysicallyDeleteException |
                FormalCaregiverScoreSaveException |
                FormalCaregiverScoreUpdateVotesException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "findAll/{formalCaregiverId}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<FormalCaregiverScore>> findAll(@PathVariable String formalCaregiverId){
        try{
            return ResponseEntity.ok(formalCaregiverScoreService.findAll(formalCaregiverId));

        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error buscando puntajes del cuidador formal con id " + formalCaregiverId);
        }
    }

    @GetMapping(value = "findId/{id}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Optional<FormalCaregiverScore>> findId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(formalCaregiverScoreService.findId(id));

        }catch (FormalCaregiverScoreNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error buscando score con id " + id);
        }
    }

    @GetMapping(
            value = "findScore/{formalCaregiverId}/{patientId}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
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
    @PutMapping("/updateScore")
    public ResponseEntity<Boolean> updateScore(@Valid @NotNull @RequestBody FormalCaregiverScore formalCaregiverScore ){

        try {
            return ResponseEntity.ok(formalCaregiverScoreService.updateScore(formalCaregiverScore));

        }catch(FormalCaregiverScoreNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(FormalCaregiverScoreUpdateVotesException | FormalCaregiverScoreUpdateScoreException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
