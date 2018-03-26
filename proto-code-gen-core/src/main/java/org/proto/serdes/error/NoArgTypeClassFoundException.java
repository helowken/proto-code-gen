package org.proto.serdes.error;

public class NoArgTypeClassFoundException extends RuntimeException {
    public NoArgTypeClassFoundException(String message) {
        super(message);
    }

    public NoArgTypeClassFoundException(String message, Throwable t) {
        super(message, t);
    }
}
