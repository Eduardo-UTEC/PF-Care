package uy.com.pf.care.exceptions;

import uy.com.pf.care.model.documents.Patient;

public class PatientUpdateException extends RuntimeException {
    public PatientUpdateException(String msg) {super(msg);}

}
