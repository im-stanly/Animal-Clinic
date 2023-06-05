package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeesModel {
    private Integer id;
    private Integer person_id;
    private Integer position;
    private Double salary;
    private LocalDate date_start;
    private LocalDate date_fire;
    private String Fav_animal;
}
