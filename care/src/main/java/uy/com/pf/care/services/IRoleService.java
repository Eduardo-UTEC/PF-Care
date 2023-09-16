package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.Role;
import uy.com.pf.care.model.documents.Role;
import uy.com.pf.care.model.documents.Video;

import java.util.List;
import java.util.Optional;

public interface IRoleService {
    String save(Role role);
    Boolean update(Role newRole);
    Optional<Role> findId(String id);
    List<Role> findAll(String countryName, String departmentName);
    Boolean updateVideos(List<String> videosId);
}
