package org.proto.serdes.error;

public class DuplicatedFieldNameException extends RuntimeException{
    public DuplicatedFieldNameException(String message) {
        super(message);
    }
}
