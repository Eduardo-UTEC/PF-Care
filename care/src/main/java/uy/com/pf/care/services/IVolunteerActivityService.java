package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.VolunteerActivity;

import java.util.List;
import java.util.Optional;

public interface IVolunteerActivityService {
    String save(VolunteerActivity volunteerActivity);
    String update(VolunteerActivity newVolunteerActivity);
    Optional<VolunteerActivity> findId(String id);
    List<VolunteerActivity> findIds(List<String> volunteersActivitiesId);
    Boolean exist(String name, String departmentName, String countryName);
    List<VolunteerActivity> findAll(Boolean includeDeleted, String countryName);
    List<VolunteerActivity> findDepartment(Boolean includeDeleted, String countryName, String departmentName);
    Boolean setDeletion(String id, Boolean isDeleted);

}
