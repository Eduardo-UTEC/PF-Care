package uy.com.pf.care.exceptions;

import uy.com.pf.care.model.documents.Patient;

public class PatientSaveException extends RuntimeException {
    public PatientSaveException(Patient patient) {
        super(String.valueOf(patient));
    }

}
