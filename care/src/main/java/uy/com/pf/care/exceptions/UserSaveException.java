package uy.com.pf.care.exceptions;

import uy.com.pf.care.model.documents.User;

public class UserSaveException extends RuntimeException {
    public UserSaveException(String msg) {super(msg);}

}
