package uy.com.pf.care.exceptions;

public class DonationRequestNotFoundException extends RuntimeException {
    public DonationRequestNotFoundException(String msg) { super(msg); }
}
