package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.DonationRequest;
import uy.com.pf.care.model.enums.RequestStatusEnum;

import java.util.List;

public interface IDonationRequestService {
    String save(DonationRequest donationRequest);
    Boolean AcceptRequest(String donationRequestId, String volunteerCompanyId);
    Boolean changeRequestStatus(String donationRequestId, RequestStatusEnum requestStatus);

    Boolean setActive(String donationRequestId, Boolean isActive);
    List<DonationRequest> findAll(Boolean isActive, String departmentName, String countryName);
    DonationRequest findId(String id);
    List<DonationRequest> findIds(List<String> donationsRequestId);
    List<DonationRequest> findByStatus(
            Boolean isActive, RequestStatusEnum requestStatus, String departmentName, String countryName);
}