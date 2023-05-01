package uy.com.pf.care.exceptions;

import uy.com.pf.care.model.documents.FormalCaregiver;

public class FormalCaregiverSaveException extends RuntimeException {
    public FormalCaregiverSaveException(String msg) { super(msg); }
}
