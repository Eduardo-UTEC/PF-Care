package uy.com.pf.care.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uy.com.pf.care.exceptions.UserSaveException;
import uy.com.pf.care.exceptions.UserUpdateException;
import uy.com.pf.care.model.documents.Patient;
import uy.com.pf.care.model.documents.User;
import uy.com.pf.care.model.objects.LoginObject;
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
    public String save(User user) {
        try{
            String id = userRepo.save(user).getUserId();
            log.info("*** Usuario guardado con exito: " + LocalDateTime.now());
            return id;

        }catch(Exception e){
            log.warning("*** ERROR GUARDANDO USUARIO: " + e);
            throw new UserSaveException("*** ERROR GUARDANDO USUARIO");
        }
    }

    @Override
    public Boolean update(User newUser) {
        try{
            Optional<User> entityFound = userRepo.findById(newUser.getUserId());
            if (entityFound.isPresent()){
                userRepo.save(newUser);
                log.info("Usuario actualizado con exito");
                return true;
            }
            log.info("No se encontro el usuario con id " + newUser.getUserId());
            return false;

        }catch(Exception e){
            log.warning("*** ERROR ACTUALIZANDO USUARIO: " + e);
            throw new UserUpdateException("*** ERROR ACTUALIZANDO USUARIO");
        }

    }

    @Override
    public List<User> findAll(String countryName) { return userRepo.findByResidenceZone_CountryName(countryName); }

    @Override
    public Optional<User> findId(String id) { return userRepo.findById(id); }

    @Override
    public User login(LoginObject loginObject) {
        User found = userRepo.findByUserName(loginObject.getUserName());
        return (found.getPass().equals(loginObject.getPass()) ? found : null);
    }

    @Override
    public Boolean existUserName(String userName) { return userRepo.findByUserName(userName) != null; }

    /* @Override
     public Optional<User> findIdentificationDocument(Integer identificationDocument, String countryName) {
         return Optional.ofNullable(
                 userRepo.findByIdentificationDocumentAndZone_CountryName(identificationDocument, countryName));
     }
 */
    @Override
    public Optional<User> findUserName(String userName) {return Optional.ofNullable(userRepo.findByUserName(userName));}

    @Override
    public List<User> findCity(String cityName, String departmentName, String countryName) {
        return userRepo.findByResidenceZone_CountryNameAndResidenceZone_DepartmentNameAndResidenceZone_CityName(
                countryName, departmentName, cityName);
    }

    @Override
    public List<User> findDepartment(String departmentName, String countryName) {
        return userRepo.findByResidenceZone_CountryNameAndResidenceZone_DepartmentName(countryName, departmentName);
    }

}
