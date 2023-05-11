package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitsModel {
    private int id;
    private int pet_id;
    private int vet_id;
    private LocalDate visit_date;
    private String description;
    private double rate;
    private double price;
}
