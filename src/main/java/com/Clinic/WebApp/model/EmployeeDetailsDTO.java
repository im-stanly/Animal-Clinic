package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetailsDTO{
    private String first_name;
    private String last_name;
    private Integer salary;
    private LocalDate date_start;
    private LocalDate date_fire;
    private Double rating;
}
