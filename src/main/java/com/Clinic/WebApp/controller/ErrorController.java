package com.Clinic.WebApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {

    @GetMapping("/error")
    public String shitHappens(){
        return "Whoops! \n Something went wrong";
    }
}
