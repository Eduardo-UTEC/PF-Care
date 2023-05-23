package uy.com.pf.care.exceptions;

import uy.com.pf.care.model.documents.EmergencyService;

public class EmergencyServiceSaveException extends RuntimeException {
    public EmergencyServiceSaveException(String msg) { super(msg); }
}
