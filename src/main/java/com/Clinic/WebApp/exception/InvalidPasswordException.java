package com.Clinic.WebApp.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Wrong password.");
    }
    public InvalidPasswordException(String message) {
        super(message);
    }
}
