package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.infra.config.ParamConfig;
import uy.com.pf.care.model.documents.Role;
import uy.com.pf.care.infra.repos.IRoleRepo;
import uy.com.pf.care.model.documents.Video;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class RoleService implements IRoleService{
    @Autowired
    private IRoleRepo roleRepo;
    @Autowired
    private ParamConfig paramConfig;

    @Override
    public String save(Role role) {
        try{
            String id = roleRepo.save(role).getRoleId();
            log.info("*** Rol guardado con exito: " + LocalDateTime.now());
            return id;

        }catch(Exception e){
            String msg = "*** ERROR GUARDANDO ROL";
            log.warning(msg + ": " + e.getMessage());
            throw new RoleSaveException(msg);
        }
    }

    @Override
    public Boolean update(Role newRole) {
        try {
            Optional<Role> entityFound = roleRepo.findById(newRole.getRoleId());
            if (entityFound.isPresent()) {
                roleRepo.save(newRole);
                log.info("Rol actualizado con exito");
                return true;
            }
            this.notFound(newRole.getRoleId());
            return false;

        }catch(RoleNotFoundException e){
            throw new RoleNotFoundException(e.getMessage());
        } catch(Exception e){
            String msg = "*** ERROR ACTUALIZANDO ROL";
            log.warning(msg + ": " + e.getMessage());
            throw new RoleUpdateException(msg);
        }

    }

    @Override
    public List<Role> findAll(String countryName, String departmentName) {
        return roleRepo.findByCountryNameAndDepartmentNameOrderByDepartmentName(countryName, departmentName);
    }

    /*
    @Override
    public Boolean addVideos(String roleId, List<String> videosId) {
        try {
            Optional<Role> roleFound = roleRepo.findById(roleId);
            if (roleFound.isPresent()) {
                videosId.forEach(videoId -> {
                    List<String> videos = roleFound.get().getVideosId();
                    if (videos.contains(videoId)) {
                        log.warning("El video '" + videoId + "' ya existe");
                    }else{
                        videos.add(videoId);
                        log.warning("Video " + videoId + " agregado");
                    }
                });
                this.update(roleFound.get());
                return true;
            }
            this.notFound(roleId);
            return false;

        }catch(RoleNotFoundException e){
            throw new RoleNotFoundException(e.getMessage());
        }catch(RoleUpdateException e){
            throw new RoleUpdateException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR AGREGANDO VIDEOS AL ROL";
            log.warning(msg + ": " + e.getMessage());
            throw new RoleAddVideoException(msg);
        }
    }
    */

    /*
    @Override
    public Boolean delVideo(String roleId, String videoId) {
        try {
            Optional<Role> roleFound = roleRepo.findById(roleId);
            if (roleFound.isPresent()) {
                if (! roleFound.get().getVideosId().contains(videoId)) {
                    String msg = "El video '" + videoId + "' no está registrado en el rol '" + roleId + "'";
                    log.warning(msg);
                    throw new RoleVideoNotRegisteredException(msg);
                }
                roleFound.get().getVideosId().remove(videoId);
                this.update(roleFound.get());
                return true;
            }
            this.notFound(roleId);
            return false;

        }catch(RoleNotFoundException e){
            throw new RoleNotFoundException(e.getMessage());
        }catch(RoleVideoNotRegisteredException e){
                throw new RoleVideoNotRegisteredException(e.getMessage());
        }catch(RoleUpdateException e){
            throw new RoleUpdateException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR BORRANDO VIDEO DEL ROL";
            log.warning(msg + ": " + e.getMessage());
            throw new RoleDelVideoException(msg);
        }

    }
    */

    /*
    @Override
    public List<Video> findVideos(String roleId) {
        try {
            Optional<Role> roleFound = roleRepo.findById(roleId);
            if (roleFound.isPresent()) {
                List<Video> videosReturn = new ArrayList<Video>();
                for (String videoId : roleFound.get().getVideosId()){
                    Video video = this.findVideo(videoId);
                    if (video != null)
                        videosReturn.add(video);
                }
                return videosReturn;
            }
            this.notFound(roleId);
            return null;

        } catch (RoleNotFoundException e) {
            throw new RoleNotFoundException(e.getMessage());
        } catch (Exception e) {
            String msg = "*** ERROR BUSCANDO VIDEOS DEL ROL " + roleId;
            log.warning(msg + ": " + e.getMessage());
            throw new RoleFindVideosException(msg);
        }
    }
    */

    @Override
    public Optional<Role> findId(String roleId) {
        Optional<Role> found = roleRepo.findById(roleId);
        if (found.isPresent())
            return found;

        this.notFound(roleId);
        return Optional.empty();
    }

    private void notFound(String roleId) {
        String msg = "No se encontro el rol con id " + roleId;
        log.info(msg);
        throw new RoleNotFoundException(msg);
    }

    /*
    private Video findVideo(String videoId){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Video> response = restTemplate.getForEntity(
                getStartUrl() + "videos/findId/" + videoId,
                Video.class);
        if (response.getStatusCode() == HttpStatus.OK)
            return response.getBody();

        return null;
    }
    */

    //private String getStartUrl(){
    //    return paramConfig.getProtocol() + "://" + paramConfig.getSocket() + "/";
    //}

}
