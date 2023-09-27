package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.infra.repos.IDonationRequestRepo;
import uy.com.pf.care.model.documents.DonationRequest;
import uy.com.pf.care.model.documents.FormalCaregiver;
import uy.com.pf.care.model.enums.RequestStatusEnum;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class DonationRequestService implements IDonationRequestService{

    @Autowired
    private IDonationRequestRepo donationRequestRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    //Tener en cuenta que quien inicia un proceso de solicitud es un administrador web, un paciente o un
    //cuidador (formal o referente). Nunca lo inicia una empresa.
    @Override
    public String save(DonationRequest donationRequest) {

        if (donationRequest.getMaterialId().isEmpty()){
            String msg = "Debe especificar el material en la solicitud de donación.";
            log.info(msg);
            throw new DonationRequestMaterialsEmptyException(msg);
        }
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
                if (found.get().getVolunteerCompanyId() != null){
                    String msg = "Ya existe una empresa vinculada a la solicitud de donación";
                    log.info(msg);
                    throw new DonationRequestCompanyExistException(msg);
                }
                found.get().setVolunteerCompanyId(volunteerCompanyId);
                donationRequestRepo.save(found.get());
                log.info("La empresa '" + volunteerCompanyId + "' aceptó la solicitud de donación '" + donationRequestId +
                        "'. Vínculo registrado con éxito");
                return true;
            }
            this.notFound(donationRequestId);
            return null;

        }catch(DonationRequestNotFoundException e) {
            throw new DonationRequestNotFoundException(e.getMessage());
        }catch (DonationRequestCompanyExistException e){
            throw new DonationRequestCompanyExistException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR ACEPTANDO SOLICITUD DE DONACION";
            log.warning(msg + ": " + e.getMessage());
            throw new DonationRequestAcceptException(msg);
        }

    }

    @Override
    public Boolean changeRequestStatus(String donationRequestId, RequestStatusEnum requestStatus) {
        try {
            Optional<DonationRequest> found = donationRequestRepo.findById(donationRequestId);
            if (found.isPresent()) {
                found.get().setRequestStatus(requestStatus);
                donationRequestRepo.save(found.get());
                log.info("Se cambió el estado de la donación a '" + requestStatus.getName() + "'");
                return true;
            }
            this.notFound(donationRequestId);
            return null;

        }catch(DonationRequestNotFoundException e) {
            throw new DonationRequestNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR CAMBIANDO ESTADO DE LA SOLICITUD DE DONACION";
            log.warning(msg + ": " + e.getMessage());
            throw new DonationRequestChangeStatusException(msg);
        }

    }

    @Override
    public Boolean setActive(String donationRequestId, Boolean isActive) {
        try {
            Optional<DonationRequest> found = donationRequestRepo.findById(donationRequestId);
            if (found.isPresent()) {
                if (found.get().getActive() == isActive){
                    log.info("La Solicitud de Donación ya estaba '" + (isActive ? "Activa'" : "Inactiva'"));
                    return false;
                }
                found.get().setActive(isActive);
                donationRequestRepo.save(found.get());
                log.info("La Solicitud de Donación pasó a '" + (isActive ? "Activa'" : "Inactiva'"));
                return true;
            }
            this.notFound(donationRequestId);
            return null;

        }catch(DonationRequestNotFoundException e) {
            throw new DonationRequestNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR ESTABLECIENDO COMO ACTIVA/INACTIVA LA SOLICITUD DE DONACION";
            log.warning(msg + ": " + e.getMessage());
            throw new DonationRequestSetActiveException(msg);
        }
    }

    @Override
    public List<DonationRequest> findAll(Boolean activeOnly, String departmentName, String countryName) {
        return activeOnly ?
                donationRequestRepo.findByCountryNameAndDepartmentNameAndActiveTrue(countryName, departmentName):
                donationRequestRepo.findByCountryNameAndDepartmentName(countryName, departmentName);
    }

    @Override
    public DonationRequest findId(String donationRequestId) {
        Optional<DonationRequest> found = donationRequestRepo.findById(donationRequestId);
        if (found.isPresent())
            return found.get();

        this.notFound(donationRequestId);
        return null;
    }

    @Override
    public List<DonationRequest> findIds(List<String> donationsRequestId) {
            return donationRequestRepo.findAllById(donationsRequestId);
    }

    @Override
    public List<DonationRequest> findByStatus(
            Boolean activeOnly, RequestStatusEnum ordinalRequestStatus, String departmentName, String countryName) {

        Query query = new Query(
                Criteria.where("requestStatus").is(RequestStatusEnum.values()[ordinalRequestStatus.getOrdinal()]).
                        and("departmentName").is(departmentName).
                        and("countryName").is(countryName)
        );
        if (activeOnly)
            query.addCriteria(Criteria.where("active").is(true));

        return mongoTemplate.find(query, DonationRequest.class);
    }

    private void defaultValues(DonationRequest donationRequest){
        donationRequest.setRequestStatus(RequestStatusEnum.STARTED);
        donationRequest.setStartRequestDate(LocalDate.now());
        donationRequest.setFinishRequestDate(null);
        donationRequest.setVolunteerCompanyId(null);
        donationRequest.setActive(true);
    }

    private void notFound(String donationRequestId){
        String msg = "Solicitud de donación con Id " + donationRequestId + " no encontrada";
        log.warning(msg);
        throw new DonationRequestNotFoundException(msg);
    }


}
