package uy.com.pf.care.exceptions;

import uy.com.pf.care.model.documents.Role;

public class RoleUpdateException extends RuntimeException {
    public RoleUpdateException(Role role) {
        super(String.valueOf(role));
    }

}
