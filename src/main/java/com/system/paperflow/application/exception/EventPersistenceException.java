package com.system.paperflow.application.exception;

public class EventPersistenceException extends RuntimeException {
    public EventPersistenceException(String message) {
        super(message);
    }
}
