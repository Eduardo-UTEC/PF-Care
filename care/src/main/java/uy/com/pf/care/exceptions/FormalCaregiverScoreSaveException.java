package uy.com.pf.care.exceptions;

import uy.com.pf.care.model.documents.FormalCaregiverScore;

public class FormalCaregiverScoreSaveException extends RuntimeException {
    public FormalCaregiverScoreSaveException(String msg) {
        super(msg);
    }
}
