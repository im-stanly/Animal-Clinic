package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vets_SpecialitiesModel {
    private Integer vet_id;
    private String name;
    private LocalDate date_start;
}
