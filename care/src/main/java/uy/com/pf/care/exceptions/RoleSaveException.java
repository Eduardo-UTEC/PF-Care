package uy.com.pf.care.exceptions;

import uy.com.pf.care.model.documents.Role;
import uy.com.pf.care.model.documents.User;

public class RoleSaveException extends RuntimeException {
    public RoleSaveException(Role role) {
        super(String.valueOf(role));
    }

}
