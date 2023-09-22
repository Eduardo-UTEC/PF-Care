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
import uy.com.pf.care.model.documents.ReferenceCaregiver;
import uy.com.pf.care.services.IReferenceCaregiverService;

@RestController
@RequestMapping("/reference_caregiver")
@Log
public class ReferenceCaregiverController {

    @Autowired
    private IReferenceCaregiverService referenceCaregiverService;

    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @NotNull @RequestBody ReferenceCaregiver referenceCaregiver){
        try {
            return ResponseEntity.ok(referenceCaregiverService.save(referenceCaregiver));

        }catch(ReferenceCaregiverUserIdOmittedException | ReferenceCaregiverPatientsIdOmittedException e){
            throw new ResponseStatusException((HttpStatus.BAD_REQUEST), e.getMessage());
        }catch (ReferenceCaregiverDuplicateKeyException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch (UserUpdateEntityIdInRolesListException | ReferenceCaregiverSaveException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@Valid @NotNull @RequestBody ReferenceCaregiver referenceCaregiver){
        try {
            return ResponseEntity.ok(referenceCaregiverService.update(referenceCaregiver));

        }catch (ReferenceCaregiverNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(ReferenceCaregiverUpdateException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
