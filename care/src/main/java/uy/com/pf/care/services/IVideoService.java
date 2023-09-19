package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.Video;
import uy.com.pf.care.model.objects.VideoObject;

import java.util.List;
import java.util.Optional;

public interface IVideoService {
    String save(Video video);
    Boolean update(Video newVideo);
    Optional<Video> findId(String id);
    List<Video> findAll(String countryName, String departmentName);
    List<VideoObject> findByRole(Integer ordinalRole, String countryName, String departmentName);

}
