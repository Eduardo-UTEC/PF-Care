package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.model.documents.Patient;
import uy.com.pf.care.model.documents.Video;
import uy.com.pf.care.infra.repos.IVideoRepo;
import uy.com.pf.care.model.objects.VideoObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Log
public class VideoService implements IVideoService{
    @Autowired
    private IVideoRepo videoRepo;

    @Override
    public String save(Video video) {

        this.saveValidate(video);

        try {
            String id = videoRepo.save(video).getVideoId();
            log.info("*** Video guardado con exito");
            return id;

        }catch(DuplicateKeyException e){
            String msg = "Error guardando nuevo video (clave duplicada)";
            log.warning(msg + ": " + e.getMessage());
            throw new VideoDuplicateKeyException(msg);

        }catch(Exception e){
            String msg = "*** ERROR GUARDANDO VIDEO";
            log.warning(msg + ": " + e.getMessage());
            throw new VideoSaveException(msg);
        }
    }

    @Override
    public Boolean update(Video newVideo) {
        try {
            Optional<Video> entityFound = videoRepo.findById(newVideo.getVideoId());
            if (entityFound.isPresent()) {
                this.defaultValues(newVideo, entityFound.get());
                videoRepo.save(newVideo);
                log.info("Video actualizado con exito");
                return true;
            }
            this.notFound(newVideo.getVideoId());
            return false;

        }catch (VideoNotFoundException e){
            throw new VideoNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR ACTUALIZANDO VIDEO";
            log.warning(msg + ": " + e.getMessage());
            throw new VideoUpdateException(msg);
        }
    }

    @Override
    public List<Video> findAll(String countryName, String departmentName) {
        return videoRepo.findByCountryNameAndDepartmentName(countryName, departmentName);
    }

    @Override
    public List<VideoObject> findByRole(Integer ordinalRole, String countryName, String departmentName) {
        try {
            List<VideoObject> videosListReturn = new ArrayList<>();
            for (Video video : this.findAll(countryName, departmentName)) {
                for (Integer role : video.getOrdinalRoles()) {
                    if (Objects.equals(role, ordinalRole)) {
                        videosListReturn.add(this.toVideoObject(video));
                        break;
                    }
                }
            }
            return videosListReturn;

        }catch(Exception e){
            String msg = "*** ERROR BUSCANDO VIDEOS POR ROL";
            log.warning(msg + ": " + e.getMessage());
            throw new VideoFindByRoleException(msg);
        }
    }

    @Override
    public Optional<Video> findId(String id) {
        try {
            Optional<Video> found = videoRepo.findById(id);
            if (found.isPresent())
                return found;

            this.notFound(id);
            return Optional.empty();

        }catch(VideoNotFoundException e){
            throw new VideoNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR BUSCANDO VIDEO POR ID";
            log.warning(msg + ": " + e.getMessage());
            throw new VideoFindIdException(msg);
        }
    }

    private void notFound(String videoId){
        String msg = "No se encontro el video con id " + videoId;
        log.warning(msg);
        throw new VideoNotFoundException(msg);
    }

    private VideoObject toVideoObject(Video video){
        return new VideoObject(video.getVideoId(), video.getDescription(), video.getUrl());
    }

    private void saveValidate(Video video){
        if (video.getOrdinalRoles().isEmpty()){
            String msg = "El video debe tener un rol asignado";
            log.warning(msg);
            throw new VideoWithoutRoleException(msg);
        }

    }

    // Asigna los valores a la nueva entitdad, tomados de la vieja entidad (de la persistida)
    private void defaultValues(Video newVideo, Video oldVideo){
        newVideo.setOrdinalRoles(oldVideo.getOrdinalRoles());
    }

}
