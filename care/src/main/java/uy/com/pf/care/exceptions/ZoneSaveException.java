package uy.com.pf.care.exceptions;

import uy.com.pf.care.model.documents.Zone;

public class ZoneSaveException extends RuntimeException {
    public ZoneSaveException(Zone zone) {
        super(String.valueOf(zone));
    }
}
