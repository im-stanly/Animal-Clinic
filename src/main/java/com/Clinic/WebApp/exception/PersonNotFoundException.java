package com.Clinic.WebApp.exception;


public class PersonNotFoundException extends RuntimeException{
    public PersonNotFoundException(int id){
        super("Person with id = " + id + " doesn't exist");
    }
}
