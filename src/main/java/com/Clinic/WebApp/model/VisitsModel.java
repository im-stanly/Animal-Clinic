package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitsModel {
    private Integer id;
    private Integer pet_id;
    private Integer vet_id;
    private LocalDate visit_date;
    private LocalTime visit_time;
    private Integer type_id;
    private String description;
    private Double rate;
}