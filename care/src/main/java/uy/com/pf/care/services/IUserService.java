package uy.com.pf.care.services;

import uy.com.pf.care.model.documents.User;
import uy.com.pf.care.model.enums.RoleEnum;
import uy.com.pf.care.model.objects.LoginObjectAuthenticate;

import java.util.List;

public interface IUserService {
    String save(User user);
    Boolean update(User newUser);
    Boolean addNewRol(String userId, String roleId, RoleEnum rol);
    Boolean updateEntityIdInRolesList(String userId, RoleEnum roleOrdinal, String objectId);
    List<User> findAll(String countryName);
    User findId(String id);
    //Optional<User> findIdentificationDocument(Integer identificationDocument, String countryName);
    User login(LoginObjectAuthenticate loginObjectAuthenticate);
    Boolean existUserName(String userName);
    User findUserName(String userName);
    List<User> findCity(String cityName, String departmentName, String countryName);
    List<User> findDepartment(String departmentName, String countryName);
}
