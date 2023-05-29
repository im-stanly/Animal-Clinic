package com.Clinic.WebApp.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException() {
        super("Username already exists.");
    }
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
