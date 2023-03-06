package uy.com.pf.care.exceptions;

import uy.com.pf.care.model.documents.HealthProvider;

public class HealthProviderSaveException extends RuntimeException {
    public HealthProviderSaveException(HealthProvider healthProvider) { super(String.valueOf(healthProvider)); }
}
