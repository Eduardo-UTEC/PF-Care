package uy.com.pf.care.model.globalFunctions;

import uy.com.pf.care.model.documents.Role;
import uy.com.pf.care.model.enums.RoleEnum;

//Fuerzo la asignación del un tipo RoleEnum válido para la key 'roleName', evitando nulos en la base de datos
public class ForceEnumsToRole {
    public static void execute(Role role){
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
