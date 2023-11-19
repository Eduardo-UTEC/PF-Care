package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.Video;
import uy.com.pf.care.model.enums.RoleEnum;
import uy.com.pf.care.model.objects.VideoObject;

import java.util.List;
import java.util.Optional;

public interface IVideoService {
    String save(Video video);
    String update(Video newVideo);
    Boolean addRoles(String videoId, List<RoleEnum> ordinalRoles);
    Boolean changeRole(String videoId, RoleEnum oldOrdinalRole, RoleEnum newOrdinalRole);
    Boolean delRoles(String videoId, List<RoleEnum> ordinalRoles);
    Optional<Video> findId(String id);
    List<VideoObject> findTitle(String title, Integer ordinalRole, String countryName, String departmentName);
    List<Video> findAll(String countryName, String departmentName);
    List<VideoObject> findByRole(Integer ordinalRole, String countryName, String departmentName);

}
