package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.*;
import uy.com.pf.care.model.documents.User;
import uy.com.pf.care.model.enums.RoleEnum;
import uy.com.pf.care.model.objects.LoginObjectAuthenticate;
import uy.com.pf.care.model.objects.RoleObject;
import uy.com.pf.care.model.objects.UserObject;
import uy.com.pf.care.repos.IUserRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class UserService implements IUserService{
    @Autowired
    private IUserRepo userRepo;

   // private static final Logger log = LoggerFactory.getLogger(CuidadosApplication.class);

    @Override
    public String save(User newUser) {
        try{
            String id = userRepo.save(newUser).getUserId();
            log.info("*** Usuario guardado con exito: " + LocalDateTime.now());
            return id;

        }catch(Exception e){
            String msg = "*** ERROR GUARDANDO USUARIO";
            log.warning( msg +": " + e.getMessage());
            throw new UserSaveException(msg);
        }
    }

    @Override
    public Boolean update(User user) {
        try {
            Optional<User> oldUser = userRepo.findById(user.getUserId());
            if (oldUser.isPresent()) {
                userRepo.save(user);
                log.info("Usuario actualizado con exito");
                return true;
            }
            String msg = "No se encontro el usuario con id " + user.getUserId();
            log.info(msg);
            throw new UserNotFoundException(msg);

        }catch (UserNotFoundException e){
            throw new UserNotFoundException(e.getMessage());
        }catch (Exception e){
            String msg = "*** ERROR ACTUALIZANDO USUARIO";
            log.warning(msg + ": " + e.getMessage());
            throw new UserUpdateException(msg);
        }
    }

    @Override
    public Boolean addNewRol(String userId, String roleId, RoleEnum rol) {
        try {
            Optional<User> user = userRepo.findById(userId);
            if (user.isPresent()) {
                if (this.roleExist(user.get(), rol)) {
                    String msg = "El usuario ya tiene el rol '" + rol.getName() + "'";
                    log.info(msg);
                    throw new UserAlreadyDefinedRolException(msg);
                }
                //Agrego el nuevo rol.
                //Nota: entityId=null porque aún no se agregó el document corrrespondiente al rol.
                //Ej: Si 'rol' es PATIENT, cuando se agregue el paciente al document "Patients", se obtiene su id
                //y se actualiza 'entityId'
                user.get().getRoles().add( new UserObject(null, new RoleObject(roleId, rol)));
                this.update(user.get());
                return true;
            }

            String msg = "No se encontro el usuario con id: " + userId;
            log.info(msg);
            throw new UserNotFoundException(msg);

        }catch (UserAlreadyDefinedRolException e){
            throw new UserAlreadyDefinedRolException(e.getMessage());
        }catch (UserNotFoundException e){
            throw new UserNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR ACTUALIZANDO EL ENTITYID DEL USUARIO " + userId;
            log.info(msg + ": " + e.getMessage());
            throw new UserUpdateEntityIdInRolesListException(msg);
        }
    }

    @Override
    public Boolean updateEntityIdInRolesList(String userId, RoleEnum roleOrdinal, String entityId) {

        try {
            Optional<User> user = userRepo.findById(userId);
            if (user.isPresent()) {
                boolean roleExist = false;
                for (UserObject userObject : user.get().getRoles()) {
                    if (userObject.getRole().getRol().getOrdinal() == roleOrdinal.getOrdinal()) {
                        userObject.setEntityId(entityId);
                        roleExist = true;
                        break;
                    }
                }
                if (roleExist) {
                    this.update(user.get());
                    return true;
                }
                String msg = "El usuario " + userId + " no tiene el rol '" + roleOrdinal.getName();
                log.info(msg);
                throw new UserRoleNotFoundException(msg);
            }
            String msg = "No se encontro el usuario con id: " + userId;
            log.info(msg);
            throw new UserNotFoundException(msg);

        }catch (UserRoleNotFoundException e){
            throw new UserRoleNotFoundException(e.getMessage());
        }catch (UserNotFoundException e){
            throw new UserNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR ACTUALIZANDO EL ENTITYID DEL USUARIO " + userId;
            log.info(msg + ": " + e.getMessage());
            throw new UserUpdateEntityIdInRolesListException(msg);
        }
    }

    @Override
    public List<User> findAll(String countryName) {
        try {
            return userRepo.findByResidenceZone_CountryName(countryName);

        }catch(Exception e){
            String msg = "*** ERROR BUSCANDO USUARIOS DEL PAIS '" + countryName + "'";
            log.warning(msg + ": " + e.getMessage());
            throw new UserFindAllException(msg);
        }
    }

    @Override
    public User findId(String id) {
        try {
            Optional<User> user = userRepo.findById(id);
            if (user.isPresent())
                return user.get();

            String msg = "Usuario con Id '" + id + "' no encontrado";
            log.warning(msg);
            throw new UserNotFoundException(msg);

        }catch (UserNotFoundException e){
            throw new UserNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR BUSCANDO USUARIO CON ID: " + e.getMessage();
            log.warning(msg);
            throw new UserFindIdException(msg);
        }
    }

    @Override
    public User login(LoginObjectAuthenticate loginObjectAuthenticate) {
//        User found = userRepo.findByUserName(loginObject.getUserName());
//        return (found.getPass().equals(loginObject.getPass()) ? found : null);
        try {
            User userFound = userRepo.findByUserName(loginObjectAuthenticate.getUserName());
            if (userFound != null && userFound.getPass().equals(loginObjectAuthenticate.getPass()))
                return userFound;

            String msg = "Credenciales inválidas";
            log.warning(msg);
            throw new UserInvalidLoginException(msg);

        }catch (UserInvalidLoginException e){
            throw new UserInvalidLoginException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR EN LOGIN";
            log.warning(msg + ": " + e.getMessage());
            throw new UserLoginException(msg);
        }
    }

    @Override
    public Boolean existUserName(String userName) {
        try {
            return userRepo.findByUserName(userName) != null;

        }catch(Exception e){
            String msg = "ERROR BUSCANDO USUARIO CON USERNAME '" + userName + "'";
            log.warning(msg + ": " + e.getMessage());
            throw new UserExistUserNameException(msg);
        }
    }

    /* @Override
     public Optional<User> findIdentificationDocument(Integer identificationDocument, String countryName) {
         return Optional.ofNullable(
                 userRepo.findByIdentificationDocumentAndZone_CountryName(identificationDocument, countryName));
     }
 */
    @Override
    public User findUserName(String userName) {
        try {
            //return Optional.ofNullable(userRepo.findByUserName(userName));
            Optional<User> ret = Optional.ofNullable(userRepo.findByUserName(userName));
            if (ret.isPresent())
                return ret.get();

            String msg = "USUARIO NO ENCONTRADO: '" + userName + "'";
            log.warning(msg);
            throw new UserNameNotFoundException(msg);

        }catch (UserNameNotFoundException e){
            throw new UserNameNotFoundException(e.getMessage());
        }catch(Exception e){
            String msg = "*** ERROR BUSCANDO USUARIO POR USERNAME '" + userName + "'";
            log.warning(msg + ": " + e.getMessage());
            throw new UserFindUserNameException(msg);
        }
    }

    @Override
    public List<User> findCity(String cityName, String departmentName, String countryName) {
        try{
            return userRepo.findByResidenceZone_CountryNameAndResidenceZone_DepartmentNameAndResidenceZone_CityName(
                    countryName, departmentName, cityName);

        }catch(Exception e){
            String msg = "*** ERROR BUSCANDO USUSARIOS POR CIUDAD (" +
                    cityName + ", " + departmentName + ", " +  countryName + ")";
            log.warning(msg + ": " + e.getMessage());
            throw new UserFindCityException(msg);
        }
    }

    @Override
    public List<User> findDepartment(String departmentName, String countryName) {
        try{
            return userRepo.findByResidenceZone_CountryNameAndResidenceZone_DepartmentName(countryName, departmentName);

        }catch(Exception e){
            String msg = "*** ERROR BUSCANDO USUSARIOS POR DEPARTAMENTO (" +
                    departmentName + ", " +  countryName + ")";
            log.warning(msg + ": " + e.getMessage());
            throw new UserFindDepartmentException(msg);
        }
    }

    private boolean roleExist(User user, RoleEnum rol){
        boolean found = false;
        for (UserObject userObject : user.getRoles()){
            if (userObject.getRole().getRol().getOrdinal() == rol.getOrdinal()){
                found = true;
                break;
            }
        }
        return found;
    }

}
