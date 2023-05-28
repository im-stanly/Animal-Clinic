package com.Clinic.WebApp.exception;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException() {
        super("Account with given login not found.");
    }
    public UsernameNotFoundException(String message) {
        super(message);
    }
}
