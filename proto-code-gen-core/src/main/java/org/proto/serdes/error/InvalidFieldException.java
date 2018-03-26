package org.proto.serdes.error;

public class InvalidFieldException extends RuntimeException {
    public InvalidFieldException(String msg) {
        super(msg);
    }
}
