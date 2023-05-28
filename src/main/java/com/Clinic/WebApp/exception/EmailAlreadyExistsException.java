package com.Clinic.WebApp.exception;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException() {
        super("Account already exists .");
    }
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
