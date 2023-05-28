package com.Clinic.WebApp.webClient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FunFactsDto {
    public String _id;
    public boolean verified;
    public String text;
    public String source;
    public String type;
    public boolean deleted;
}
