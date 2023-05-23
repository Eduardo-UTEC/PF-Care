package uy.com.pf.care.exceptions;

import uy.com.pf.care.model.documents.Patient;

public class PatientSaveException extends RuntimeException {
    public PatientSaveException(String msg) {super(msg);}

}
