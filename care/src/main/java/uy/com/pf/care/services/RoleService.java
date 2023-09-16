package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.RoleSaveException;
import uy.com.pf.care.exceptions.RoleUpdateException;
import uy.com.pf.care.model.documents.Role;
import uy.com.pf.care.repos.IRoleRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class RoleService implements IRoleService{
    @Autowired
    private IRoleRepo roleRepo;

   // private static final Logger log = LoggerFactory.getLogger(CuidadosApplication.class);

    @Override
    public String save(Role role) {
        try{
            String id = roleRepo.save(role).getRoleId();
            log.info("*** Rol guardado con exito: " + LocalDateTime.now());
            return id;

        }catch(Exception e){
            log.warning("*** ERROR GUARDANDO ROL: " + e);
            throw new RoleSaveException("*** ERROR GUARDANDO ROL");
        }
    }

    @Override
    public Boolean update(Role newRole) {
        try{
            Optional<Role> entityFound = roleRepo.findById(newRole.getRoleId());
            if (entityFound.isPresent()){
                roleRepo.save(newRole);
                log.info("Rol actualizado con exito");
                return true;
            }
            log.info("No se encontro el rol con id " + newRole.getRoleId());
            return false;

        } catch(Exception e){
            log.warning("*** ERROR ACTUALIZANDO ROL: " + e);
            throw new RoleUpdateException("*** ERROR ACTUALIZANDO ROL");
        }

    }

    @Override
    public List<Role> findAll(String countryName, String departmentName) {
        return roleRepo.findByCountryNameAndDepartmentNameOrderByDepartmentName(countryName, departmentName);
    }

    @Override
    public Boolean updateVideos(List<String> videosId) {
        //TODO: implementar...
        return null;
    }

    @Override
    public Optional<Role> findId(String id) {return roleRepo.findById(id);}

}
