package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkersModel {
    private int id;
    private Integer person_id;
    private String position;
    private Integer salary;
    private LocalDate date_start;
    private LocalDate date_fire;
    private String fav_animal;
}
