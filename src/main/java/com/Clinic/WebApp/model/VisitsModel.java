package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitsModel {
    private Integer id;
    private Integer pet_id;
    private Integer vet_id;
    private LocalDate visit_date;
    private Time visit_time;
    private Integer type_id;
    private String description;
    private Double rate;
}