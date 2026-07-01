package com.system.paperflow.application.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String email) {
        super("There is already a user registered with email: " + email);
    }
}
