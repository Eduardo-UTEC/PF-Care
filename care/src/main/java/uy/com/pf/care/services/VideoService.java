package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.VideoSaveException;
import uy.com.pf.care.exceptions.VideoUpdateException;
import uy.com.pf.care.model.documents.Video;
import uy.com.pf.care.infra.repos.IVideoRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class VideoService implements IVideoService{
    @Autowired
    private IVideoRepo videoRepo;

   // private static final Logger log = LoggerFactory.getLogger(CuidadosApplication.class);

    @Override
    public String save(Video video) {
        try{
            String id = videoRepo.save(video).getVideoId();
            log.info("*** Video guardado con exito: " + LocalDateTime.now());
            return id;

        }catch(Exception e){
            log.warning("*** ERROR GUARDANDO VIDEO: " + e);
            throw new VideoSaveException("*** ERROR GUARDANDO VIDEO");
        }
    }

    @Override
    public Boolean update(Video newVideo) {
        try{
            Optional<Video> entityFound = videoRepo.findById(newVideo.getVideoId());
            if (entityFound.isPresent()){
                videoRepo.save(newVideo);
                log.info("Video actualizado con exito");
                return true;
            }
            log.info("No se encontro el video con id " + newVideo.getVideoId());
            return false;

        } catch(Exception e){
            log.warning("*** ERROR ACTUALIZANDO VIDEO: " + e);
            throw new VideoUpdateException("*** ERROR ACTUALIZANDO VIDEO");
        }
    }

    @Override
    public List<Video> findAll(String countryName, String departmentName) {
        return videoRepo.findByCountryNameAndDepartmentName(countryName, departmentName);
    }

    @Override
    public Optional<Video> findId(String id) {return videoRepo.findById(id);}

}
