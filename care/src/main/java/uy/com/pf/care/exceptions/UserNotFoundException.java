package uy.com.pf.care.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String msg) {super(msg);}
}
