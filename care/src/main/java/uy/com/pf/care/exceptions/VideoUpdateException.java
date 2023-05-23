package uy.com.pf.care.exceptions;

import uy.com.pf.care.model.documents.Video;

public class VideoUpdateException extends RuntimeException {
    public VideoUpdateException(String msg) {super(msg);}

}
