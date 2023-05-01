package uy.com.pf.care.exceptions;

import uy.com.pf.care.model.documents.FormalCaregiverScore;

public class FormalCaregiverScoreDuplicateKeyException extends RuntimeException {
    public FormalCaregiverScoreDuplicateKeyException(String msg) {
        super(msg);
    }
}
