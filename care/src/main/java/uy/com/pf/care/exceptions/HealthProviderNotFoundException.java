package uy.com.pf.care.exceptions;

public class HealthProviderNotFoundException extends RuntimeException {
    public HealthProviderNotFoundException(String msg) { super(msg); }
}
