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
import uy.com.pf.care.model.documents.DonationRequest;
import uy.com.pf.care.model.enums.RequestStatusEnum;
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

        }catch(DonationRequestMaterialsEmptyException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch (DonationRequestSaveException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/accept_request/{donationRequestId}/{volunteerCompanyId}")
    public ResponseEntity<Boolean> acceptRequest(
            @PathVariable String donationRequestId,
            @PathVariable String volunteerCompanyId){
        try {
            return ResponseEntity.ok(donationRequestService.acceptRequest(donationRequestId, volunteerCompanyId));

        }catch (DonationRequestNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (DonationRequestCompanyExistException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch (DonationRequestAcceptException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/change_request_status/{donationRequestId}/{ordinalRequestStatus}")
    public ResponseEntity<Boolean> changeRequestStatus(
            @PathVariable String donationRequestId,
            @PathVariable Integer ordinalRequestStatus){
        try {
            return ResponseEntity.ok(donationRequestService.changeRequestStatus(
                    donationRequestId, RequestStatusEnum.values()[ordinalRequestStatus]));

        }catch (DonationRequestNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (DonationRequestChangeStatusException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(
            value = "/findAll/{activeOnly}/{departmentName}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<DonationRequest>> findAll(
            @PathVariable Boolean activeOnly,
            @PathVariable String departmentName,
            @PathVariable String countryName){
        try {
            return ResponseEntity.ok(donationRequestService.findAll(activeOnly, departmentName, countryName));

        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/findId/{donationRequestId}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<DonationRequest> findId(@PathVariable String donationRequestId){
        try {
            return ResponseEntity.ok(donationRequestService.findId(donationRequestId));

        }catch(DonationRequestNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/findIds", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<DonationRequest>> findIds(@Valid @NotNull @RequestBody List<String> donationsRequestId){
        try {
            return ResponseEntity.ok(donationRequestService.findIds(donationsRequestId));

        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(
            value = "/findByStatus/{activeOnly}/{ordinalRequestStatus}/{departmentName}/{countryName}",
            produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<List<DonationRequest>> findByStatus(
            @PathVariable Boolean activeOnly,
            @PathVariable Integer ordinalRequestStatus,
            @PathVariable String departmentName,
            @PathVariable String countryName){
        try {
            return ResponseEntity.ok(donationRequestService.findByStatus(
                    activeOnly, RequestStatusEnum.values()[ordinalRequestStatus], departmentName, countryName));

        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/setActive/{donationRequestId}/{isActive}")
    public ResponseEntity<Boolean> setActive(
            @PathVariable String donationRequestId,
            @PathVariable Boolean isActive){
        try {
            return ResponseEntity.ok(donationRequestService.setActive(donationRequestId, isActive));

        }catch (DonationRequestNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (DonationRequestSetActiveException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


}
