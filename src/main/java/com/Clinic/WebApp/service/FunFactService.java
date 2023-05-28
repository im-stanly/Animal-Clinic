package com.Clinic.WebApp.service;

import com.Clinic.WebApp.webClient.FunFactsClient;
import com.Clinic.WebApp.webClient.dto.FunFactsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FunFactService {

    @Autowired
    FunFactsClient funFactsClient;

    public List<FunFactsDto> getMeowfacts(int num){
        List<String> list =  funFactsClient.getMeowfacts(num).data;
        Random random = new Random();

        return list.stream()
                .map(fact ->
                                new FunFactsDto(
                                        random.nextInt(500) + "",
                                        true,
                                        fact,
                                        "API",
                                        "cat",
                                        false))
                .collect(Collectors.toList());
    }

    public List<FunFactsDto> getByLanguage(String lang){
        return funFactsClient.getByLanguage(lang);
    }

    public List<FunFactsDto> getByType(String animalType, int amount){
        return funFactsClient.getFacts(animalType, amount);
    }

}
