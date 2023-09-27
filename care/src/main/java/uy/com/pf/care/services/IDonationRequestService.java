package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.DonationRequest;
import uy.com.pf.care.model.enums.RequestStatusEnum;

import java.util.List;

public interface IDonationRequestService {
    String save(DonationRequest donationRequest);
    Boolean acceptRequest(String donationRequestId, String volunteerCompanyId);
    Boolean changeRequestStatus(String donationRequestId, RequestStatusEnum requestStatus);
    Boolean setActive(String donationRequestId, Boolean isActive);
    List<DonationRequest> findAll(Boolean includeNotActive, String departmentName, String countryName);
    DonationRequest findId(String donationRequestId);
    List<DonationRequest> findIds(List<String> donationsRequestId);
    List<DonationRequest> findByStatus(
            Boolean activeOnly, RequestStatusEnum ordinalRequestStatus, String departmentName, String countryName);
}
