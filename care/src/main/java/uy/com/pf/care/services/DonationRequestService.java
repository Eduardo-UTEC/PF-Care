package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.DonationRequestAcceptException;
import uy.com.pf.care.exceptions.DonationRequestNotFoundException;
import uy.com.pf.care.exceptions.DonationRequestSaveException;
import uy.com.pf.care.infra.repos.IDonationRequestRepo;
import uy.com.pf.care.model.documents.DonationRequest;
import uy.com.pf.care.model.enums.RequestStatusEnum;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class DonationRequestService implements IDonationRequestService{

    @Autowired
    private IDonationRequestRepo donationRequestRepo;


    //Tener en cuenta que quien inicia un proceso de solicitud es un administrador web, un paciente o un
    //cuidador (formal o referente). Nunca lo inicia una empresa.
    @Override
    public String save(DonationRequest donationRequest) {
        try{
            this.defaultValues(donationRequest);
            String newDonationRequestlId = donationRequestRepo.save(donationRequest).getDonationRequestId();
            log.info("Solicitud de donación guardada con éxito");
            return newDonationRequestlId;

        }catch(Exception e){
            String msg = "*** ERROR GUARDANDO MATERIAL";
            log.warning(msg + ": " + e.getMessage());
            throw new DonationRequestSaveException(msg);
        }

    }

    @Override
    public Boolean acceptRequest(String donationRequestId, String volunteerCompanyId) {
        try {
            Optional<DonationRequest> found = donationRequestRepo.findById(donationRequestId);
            if (found.isPresent()) {
                found.get().setVolunteerCompanyId(volunteerCompanyId);
                donationRequestRepo.save(found.get());
                log.info("La empresa '" + volunteerCompanyId + "' aceptó la solicitud de donación '" + donationRequestId +
                        "'. Vínculo registrado con éxito");
                return true;
            }
            String msg = "Solicitud de donación con Id " + donationRequestId + " no encontrada";
            log.warning(msg);
            throw new DonationRequestNotFoundException(msg);

        }catch(DonationRequestNotFoundException e){
            throw new DonationRequestNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR GUARDANDO MATERIAL";
            log.warning(msg + ": " + e.getMessage());
            throw new DonationRequestAcceptException(msg);
        }

    }

    @Override
    public Boolean changeRequestStatus(String donationRequestId, RequestStatusEnum requestStatus) {
        return null;
    }

    @Override
    public Boolean setActive(String donationRequestId, Boolean isActive) {
        return null;
    }

    @Override
    public List<DonationRequest> findAll(Boolean includeNotActive, String departmentName, String countryName) {
        return includeNotActive ?
                donationRequestRepo.findByCountryNameAndDepartmentName(countryName, departmentName) :
                donationRequestRepo.findByCountryNameAndDepartmentNameAndActiveTrue(countryName, departmentName);
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
        donationRequest.setStartRequestDate(LocalDate.now());
        donationRequest.setFinishRequestDate(null);
        donationRequest.setVolunteerCompanyId(null);
        donationRequest.setActive(true);
    }


}
