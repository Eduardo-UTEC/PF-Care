package uy.com.pf.care.infra.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.exceptions.DonationRequestSaveException;
import uy.com.pf.care.model.documents.DonationRequest;
import uy.com.pf.care.services.IDonationRequestService;

@Controller
@RequestMapping("/donation_request")
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
}
