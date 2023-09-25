package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.MaterialSaveException;
import uy.com.pf.care.infra.repos.IDonationRequestRepo;
import uy.com.pf.care.model.documents.DonationRequest;
import uy.com.pf.care.model.enums.RequestStatusEnum;

import java.time.LocalDate;
import java.util.List;

@Service
@Log
public class DonationRequestService implements IDonationRequestService{

    @Autowired
    private IDonationRequestRepo donationRequestRepo;


    @Override
    public String save(DonationRequest donationRequest) {
        try{
            this.defaultValues(material);
            String newMaterialId = materialRepo.save(material).getMaterialId();
            log.info("Material guardado con exito");
            return newMaterialId;

        }catch(Exception e){
            String msg = "*** ERROR GUARDANDO MATERIAL";
            log.warning(msg + ": " + e.getMessage());
            throw new MaterialSaveException(msg);
        }

    }

    @Override
    public Boolean update(DonationRequest newDonationRequest) {
        return null;
    }

    @Override
    public List<DonationRequest> findAll(Boolean isActive, String departmentName, String countryName) {
        return null;
    }

    @Override
    public DonationRequest findId(String id) {
        return null;
    }

    @Override
    public List<DonationRequest> findIds(List<String> donationsRequestId) {
        return null;
    }

    @Override
    public List<DonationRequest> findByStatus(Boolean isActive, RequestStatusEnum requestStatus, String departmentName, String countryName) {
        return null;
    }

    private void defaultValues(DonationRequest donationRequest){
        donationRequest.setRequestStatus(RequestStatusEnum.STARTED);
        donationRequest.setRequestDate(LocalDate.now());
        donationRequest.setFinishedDate(null);
        donationRequest.setActive(true);
    }

    private void defaultValues(DonationRequest newDonationRequest, DonationRequest oldDonationRequest){
        newDonationRequest.setRequestStatus(oldDonationRequest.getRequestStatus());
        newDonationRequest.setRequestDate(oldDonationRequest.getRequestDate());
        newDonationRequest.setFinishedDate(oldDonationRequest.getFinishedDate());
        newDonationRequest.setActive(oldDonationRequest.getActive());
    }
}
