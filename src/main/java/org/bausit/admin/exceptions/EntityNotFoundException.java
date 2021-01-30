package org.bausit.admin.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
