package org.proto.serdes.error;

public class DuplicatedFieldIndexException extends RuntimeException{
    public DuplicatedFieldIndexException(String message) {
        super(message);
    }
}
