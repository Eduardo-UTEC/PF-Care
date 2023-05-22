package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.Patient;
import uy.com.pf.care.model.documents.User;
import uy.com.pf.care.model.documents.Zone;
import uy.com.pf.care.model.objects.LoginObject;
import uy.com.pf.care.model.objects.NeighborhoodObject;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    String save(User user);
    Boolean update(User newUser);
    List<User> findAll(String countryName);
    Optional<User> findId(String id);
    //Optional<User> findIdentificationDocument(Integer identificationDocument, String countryName);
    User login(LoginObject loginObject);
    Boolean existUserName(String userName);
    Optional<User> findUserName(String userName);
    List<User> findCity(String cityName, String departmentName, String countryName);
    List<User> findDepartment(String departmentName, String countryName);
}
