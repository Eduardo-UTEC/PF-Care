package uy.com.pf.care.exceptions;

import uy.com.pf.care.model.documents.User;

public class UserUpdateException extends RuntimeException {
    public UserUpdateException(String msg) {super(msg);}

}
