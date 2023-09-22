package uy.com.pf.care.model.globalFunctions;

import uy.com.pf.care.model.documents.User;
import uy.com.pf.care.model.enums.RoleEnum;

public class ForceEnumsToUser {
    public static void execute(User user){
        user.getRoles().forEach(userObject -> {
            switch (userObject.getRole().getRol()) {
                case PATIENT -> userObject.getRole().setRol(RoleEnum.PATIENT);
                case REFERENCE_CARE -> userObject.getRole().setRol(RoleEnum.REFERENCE_CARE);
                case FORMAL_CARE -> userObject.getRole().setRol(RoleEnum.FORMAL_CARE);
                case VOLUNTEER_PERSON -> userObject.getRole().setRol(RoleEnum.VOLUNTEER_PERSON);
                case VOLUNTEER_COMPANY -> userObject.getRole().setRol(RoleEnum.VOLUNTEER_COMPANY);
            }
        });
    }
}
