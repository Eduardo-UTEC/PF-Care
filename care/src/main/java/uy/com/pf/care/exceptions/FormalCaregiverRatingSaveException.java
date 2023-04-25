package uy.com.pf.care.exceptions;

import uy.com.pf.care.model.documents.FormalCaregiverScore;

public class FormalCaregiverRatingSaveException extends RuntimeException {
    public FormalCaregiverRatingSaveException(FormalCaregiverScore formalCaregiverScore) {
        super(String.valueOf(formalCaregiverScore)); }
}
