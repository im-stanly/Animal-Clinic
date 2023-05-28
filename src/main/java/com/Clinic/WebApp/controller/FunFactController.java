package com.Clinic.WebApp.controller;

import com.Clinic.WebApp.service.FunFactService;
import com.Clinic.WebApp.webClient.dto.FunFactsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FunFactController {
    @Autowired
    FunFactService funFactService;

    @GetMapping("/funfacts2/amount={amount}")
    public List<FunFactsDto> getMeowFF(@PathVariable("amount") int amount){
        return funFactService.getMeowfacts(amount);
    }

    @GetMapping("/funfacts/lang={lang}")
    public List<FunFactsDto> getFunfacts(@PathVariable("lang") String lang){
        return funFactService.getByLanguage(lang);
    }

    @GetMapping("/funfacts/animal_type={animalType}/amount={amount}")
    public List<FunFactsDto> getFunfacts(@PathVariable("animalType") String animalType,
                                         @PathVariable("amount") int amount){
        return funFactService.getByType(animalType, amount);
    }

}
