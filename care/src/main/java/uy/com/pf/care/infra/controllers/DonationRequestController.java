package uy.com.pf.care.infra.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.DonationRequestAcceptException;
import uy.com.pf.care.exceptions.DonationRequestNotFoundException;
import uy.com.pf.care.exceptions.DonationRequestSaveException;
import uy.com.pf.care.model.documents.DonationRequest;
import uy.com.pf.care.services.IDonationRequestService;

import java.util.List;

@RestController
@RequestMapping("/donation_request")
@Log
public class DonationRequestController {

    @Autowired
    private IDonationRequestService donationRequestService;

    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @NotNull @RequestBody DonationRequest donationRequest){
        try {
            return ResponseEntity.ok(donationRequestService.save(donationRequest));

        }catch (DonationRequestSaveException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/accept_request")
    public ResponseEntity<Boolean> acceptRequest(
            @PathVariable String donationRequestId,
            @PathVariable String volunteerCompanyId){
        try {
            return ResponseEntity.ok(donationRequestService.acceptRequest(donationRequestId, volunteerCompanyId));

        }catch (DonationRequestNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (DonationRequestAcceptException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/findAll/{includeNotActive}/{departmentName}/{countryName}")
    public ResponseEntity<List<DonationRequest>> findAll(
            @PathVariable Boolean includeNotActive,
            @PathVariable String departmentName,
            @PathVariable String countryName){
        try {
            return ResponseEntity.ok(donationRequestService.findAll(includeNotActive, departmentName, countryName));

        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
