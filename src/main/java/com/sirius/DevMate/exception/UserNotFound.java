package com.sirius.DevMate.exception;

public class UserNotFound extends IllegalStateException {
    public UserNotFound(String message) {
        super(message);
    }
}
