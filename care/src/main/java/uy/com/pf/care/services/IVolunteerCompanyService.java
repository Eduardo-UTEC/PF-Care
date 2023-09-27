package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.VolunteerCompany;

import java.util.List;
import java.util.Optional;

public interface IVolunteerCompanyService {
    String save(VolunteerCompany volunteerCompany);
    Boolean update(VolunteerCompany newVolunteerCompany);
    Optional<VolunteerCompany> findId(String id);
    List<VolunteerCompany> findAll(Boolean validateOnly, Boolean includeDeleted, String countryName);
    Boolean addDonations(String volunteerCompanyId, List<String> donationsId);
    Boolean delDonations(String volunteerCompanyId, List<String> donationsId);
    Boolean setAvailability(String id, Boolean isAvailable);
    Boolean setValidation(String id, Boolean isValidated);
    Boolean setDeletion(String id, Boolean isDeleted);
}
