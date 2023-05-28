package com.Clinic.WebApp.webClient;

import com.Clinic.WebApp.model.FunfactModel;
import com.Clinic.WebApp.webClient.dto.FunFactsDto;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class FunfactDtoMapper implements Function<FunfactModel, FunFactsDto> {
    @Override
    public FunFactsDto apply(FunfactModel fact) {
        return new FunFactsDto(
                fact.get_id(),
                fact.getStatus().isVerified(),
                fact.getText(),
                fact.getSource(),
                fact.getType(),
                fact.isDeleted()
        );
    }
}