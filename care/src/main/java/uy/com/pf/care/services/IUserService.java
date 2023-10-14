package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.User;
import uy.com.pf.care.model.enums.RoleEnum;
import uy.com.pf.care.model.objects.LoginObjectAuthenticate;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    String save(User user);
    Boolean update(User newUser);
    //Boolean addNewRol(String userId, String roleId, RoleEnum rol);
    Boolean addNewRol(String userId, RoleEnum rol);
    Boolean updateEntityIdInRolesList(String userId, RoleEnum roleOrdinal, String objectId);
    List<User> findAll(String countryName);
    User findId(String id);
    Optional<User> findIdentificationDocument(Integer identificationDocument);
    User login(LoginObjectAuthenticate loginObjectAuthenticate);
    //Boolean existUserName(String userName);
    //User findUserName(String userName);
    List<User> findCity(String cityName, String departmentName, String countryName);
    List<User> findDepartment(String departmentName, String countryName);
}
