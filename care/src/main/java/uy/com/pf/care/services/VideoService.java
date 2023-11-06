package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.infra.repos.IVideoRepo;
import uy.com.pf.care.model.documents.Video;
import uy.com.pf.care.model.enums.RoleEnum;
import uy.com.pf.care.model.objects.VideoObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
    public Boolean addRoles(String videoId, List<RoleEnum> ordinalRoles) {
        if (ordinalRoles.isEmpty()) return false;
        try {
            Optional<Video> found = videoRepo.findById(videoId);
            if (found.isPresent()) {
                AtomicInteger count = new AtomicInteger(0);
                ordinalRoles.forEach(roleEnum -> {
                    if (!found.get().getOrdinalRoles().contains(roleEnum.getOrdinal())) {
                        found.get().getOrdinalRoles().add((roleEnum.getOrdinal()));
                        count.incrementAndGet();
                    }
                });
                if (count.get() > 0) {
                    videoRepo.save(found.get());
                    log.warning( count.get() == ordinalRoles.size() ?
                            "Roles agregados exitosamente al video." :
                            "Roles agregados correctamente (hubo " + (ordinalRoles.size() - count.get()) +
                                    " roles que ya estaban asignados)");
                    return true;
                }else{
                    log.warning("Los roles ya estaban asignados al video.");
                    return false;
                }
            }
            String msg = "No se encontro el video con id " + videoId;
            log.warning(msg);
            throw new VideoNotFoundException((msg));

        }catch (VideoNotFoundException e){
            throw new VideoNotFoundException(e.getMessage());
        }catch (Exception e){
            String msg = "*** ERROR AGREGANDO ROLES AL VIDEO";
            log.warning(msg + ": " +e.getMessage());
            throw new VideoAddRolesException(msg);
        }

    }

    @Override
    public Boolean changeRole(String videoId, RoleEnum oldOrdinalRole, RoleEnum newOrdinalRole) {
        try {
            Optional<Video> found = videoRepo.findById(videoId);
            if (found.isPresent()) {
                if (found.get().getOrdinalRoles().contains(newOrdinalRole.getOrdinal())) {
                    String msg = "El video ya tiene asignado el rol '" + newOrdinalRole.getName() +"'";
                    log.warning(msg);
                    throw new VideoRoleAlreadyLinkedException(msg);
                }
                int index = found.get().getOrdinalRoles().indexOf(oldOrdinalRole.getOrdinal());
                if (index >= 0) {
                    found.get().getOrdinalRoles().set(index, newOrdinalRole.getOrdinal());
                    videoRepo.save(found.get());
                    log.warning("Rol cambiado con exito.");
                    return true;
                }
                String msg = "El rol que se pretende cambiar (" + oldOrdinalRole.getName() + ")" +
                        " no esta asignado al video " + videoId;
                log.warning(msg);
                throw new VideoRolNotLinkedException(msg);
            }
            String msg = "Video no encontrado";
            log.warning(msg);
            throw new VideoNotFoundException(msg);

        }catch(VideoRoleAlreadyLinkedException e){
            throw new VideoRoleAlreadyLinkedException(e.getMessage());
        }catch(VideoRolNotLinkedException e){
            throw new VideoRolNotLinkedException(e.getMessage());
        }catch(VideoNotFoundException e){
            throw new VideoNotFoundException(e.getMessage());
        }catch (Exception e){
            String msg = "*** ERROR CAMBIANDO EL ROL DEL VIDEO";
            log.warning(msg + ": " + e.getMessage());
            throw new VideoChangeRoleException(msg);
        }
    }

    @Override
    public Boolean delRoles(String videoId, List<RoleEnum> ordinalRoles) {
        if (ordinalRoles.isEmpty()) return false;
        try {
            Optional<Video> found = videoRepo.findById(videoId);
            if (found.isPresent()) {
                int sizeRoles = found.get().getOrdinalRoles().size();
                ordinalRoles.forEach(roleEnum -> {
                    found.get().getOrdinalRoles().removeIf(savedRolOrdinal ->
                            Objects.equals(savedRolOrdinal, roleEnum.getOrdinal()));

                });

                int erased = sizeRoles - found.get().getOrdinalRoles().size();
                if (erased == 0){
                    log.warning("Los roles proporcionados no pertenecen al video.");
                    return false;
                }

                videoRepo.save(found.get());
                if (erased == ordinalRoles.size())
                    log.warning("Los roles fueron eliminados del video.");
                else
                    log.warning(erased + " roles eliminaodos del video (" + (ordinalRoles.size() - erased) +
                            " roles no pertenecían al video)");
                return true;
            }
            String msg = "Video no encontrado";
            log.warning(msg);
            throw new VideoNotFoundException(msg);

        } catch (VideoNotFoundException e) {
            throw new VideoNotFoundException(e.getMessage());
        } catch (Exception e) {
            String msg = "*** ERROR BORRANDO ROLES DEL VIDEO";
            log.warning(msg + ": " + e.getMessage());
            throw new VideoDelRoleException(msg);
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

    @Override
    public List<VideoObject> findTitle(String title, Integer ordinalRole, String countryName, String departmentName) {
        try {
            List<VideoObject> videosByRol = this.findByRole(ordinalRole, countryName, departmentName);
            if (videosByRol.isEmpty()) {
                log.info("No se encontraron videos para el " + RoleEnum.values()[ordinalRole].getName());
                return videosByRol;
            }
            String titleUpper = title.toUpperCase();
            return videosByRol.stream()
                    .filter(videoObject -> videoObject.getTitle().toUpperCase().contains(titleUpper))
                    .toList();

        } catch (VideoFindByRoleException e) {
            throw new VideoFindByRoleException(e.getMessage());
        }
    }

    private void notFound(String videoId){
        String msg = "No se encontro el video con id " + videoId;
        log.warning(msg);
        throw new VideoNotFoundException(msg);
    }

    private VideoObject toVideoObject(Video video){
        return new VideoObject(video.getVideoId(), video.getTitle(), video.getDescription(), video.getUrl());
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
