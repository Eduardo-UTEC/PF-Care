package uy.com.pf.care.exceptions;

import uy.com.pf.care.model.documents.EmergencyService;

public class FormalCaregiverFindAllException extends RuntimeException {
    public FormalCaregiverFindAllException(String msg) { super(msg); }
}
