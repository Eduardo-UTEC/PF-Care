package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.RoleDuplicateKeyException;
import uy.com.pf.care.exceptions.RoleNotFoundException;
import uy.com.pf.care.exceptions.RoleSaveException;
import uy.com.pf.care.exceptions.RoleUpdateException;
import uy.com.pf.care.infra.config.ParamConfig;
import uy.com.pf.care.infra.repos.IRoleRepo;
import uy.com.pf.care.model.documents.Role;
import uy.com.pf.care.model.enums.RoleEnum;

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
            this.toRoleEnum(role);
            String id = roleRepo.save(role).getRoleId();
            log.info("*** Rol guardado con exito: " + LocalDateTime.now());
            return id;

        }catch(DuplicateKeyException e){
            String msg = "Error guardando nuevo rol (clave duplicada)";
            log.warning(msg + ": " + e.getMessage());
            throw new RoleDuplicateKeyException(msg);
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

    //Fuerzo la asignación del un tipo RoleEnum válido para la key 'roleName', evitando nulos en la base de datos
    private void toRoleEnum(Role role){
        switch (role.getRoleName()){
            case WEB_ADMIN -> role.setRoleName(RoleEnum.WEB_ADMIN);
            case PATIENT -> role.setRoleName(RoleEnum.PATIENT);
            case REFERRING_CARE -> role.setRoleName(RoleEnum.REFERRING_CARE);
            case FORMAL_CARE -> role.setRoleName(RoleEnum.FORMAL_CARE);
            case VOLUNTEER_PERSON -> role.setRoleName(RoleEnum.VOLUNTEER_PERSON);
            case VOLUNTEER_COMPANY -> role.setRoleName(RoleEnum.VOLUNTEER_COMPANY);
        }
    }
}
