package com.Clinic.WebApp.webClient;

import com.Clinic.WebApp.model.FunfactModel;
import com.Clinic.WebApp.webClient.dto.FunFactsDto;
import com.Clinic.WebApp.webClient.dto.FunFactsMeowfactsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FunFactsClient {
    @Autowired
    FunfactDtoMapper funfactDtoMapper;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String QUESTION_URL = "https://cat-fact.herokuapp.com";
    private final String QUESTION_URL2 = "https://meowfacts.herokuapp.com";

    public FunFactsMeowfactsDto getMeowfacts(int number){
        return callGetMethod(
                QUESTION_URL2 + "/?count={number}",
                FunFactsMeowfactsDto.class,
                number);
    }

    public List<FunFactsDto> getByLanguage(String lang){
        return callGetMethodForArr(
                QUESTION_URL + "/facts/?lang={lang}",
                lang);
    }

    public List<FunFactsDto> getFacts(String animalType, int amount){
        return callGetMethodForArr(
                QUESTION_URL + "/facts/random?animal_type={animalType}&amount={amount}",
                animalType, amount);
    }

    private <T> T callGetMethod(String url, Class<T> responseType, Object ... objects) {
        return restTemplate.getForObject( url,
                responseType, objects);
    }

    private List<FunFactsDto> callGetMethodForArr(String url, Object... objects) {
        ResponseEntity<List<FunfactModel>> responseEntity =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<FunfactModel>>() {},
                        objects
                );
        List<FunfactModel> funfacts = responseEntity.getBody();
        return funfacts.stream()
                .map(funfactDtoMapper)
                .collect(Collectors.toList());
    }
}
