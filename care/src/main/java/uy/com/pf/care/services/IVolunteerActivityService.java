package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.Residential;
import uy.com.pf.care.model.documents.Video;
import uy.com.pf.care.model.documents.VolunteerActivity;

import java.util.List;
import java.util.Optional;

public interface IVolunteerActivityService {
    String save(VolunteerActivity volunteerActivity);
    Boolean update(VolunteerActivity newVolunteerActivity);
    Optional<VolunteerActivity> findId(String id);
    List<VolunteerActivity> findAll(Boolean includeDeleted, String countryName);
    List<VolunteerActivity> findDepartment(Boolean includeDeleted, String countryName, String departmentName);
    Boolean setDeletion(String id, Boolean isDeleted);

}
