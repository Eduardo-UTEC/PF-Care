package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.RoleNotFoundException;
import uy.com.pf.care.exceptions.RoleSaveException;
import uy.com.pf.care.exceptions.RoleUpdateException;
import uy.com.pf.care.infra.config.ParamConfig;
import uy.com.pf.care.infra.repos.IRoleRepo;
import uy.com.pf.care.model.documents.Role;

import java.time.LocalDateTime;
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

}
