package com.system.paperflow.application.exception;

public class InvalidCommitteeInvitationException extends RuntimeException {

    public InvalidCommitteeInvitationException(String message) {
        super(message);
    }

    public InvalidCommitteeInvitationException(String message, Throwable cause) {
        super(message, cause);
    }
}
