package uy.com.pf.care.exceptions;

import uy.com.pf.care.model.documents.Residential;

public class ResidentialSaveException extends RuntimeException {
    public ResidentialSaveException(String msg) {super(msg);}
}
