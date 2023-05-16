package uy.com.pf.care.exceptions;

import uy.com.pf.care.model.documents.EmergencyService;

public class EmergencyServiceUpdateException extends RuntimeException {
    public EmergencyServiceUpdateException(EmergencyService emergencyService) {super(String.valueOf(emergencyService));}
}
