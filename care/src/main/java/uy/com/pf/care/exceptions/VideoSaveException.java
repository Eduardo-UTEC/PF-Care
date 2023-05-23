package uy.com.pf.care.exceptions;

import uy.com.pf.care.model.documents.User;
import uy.com.pf.care.model.documents.Video;

public class VideoSaveException extends RuntimeException {
    public VideoSaveException(String msg) {super(msg);}

}
