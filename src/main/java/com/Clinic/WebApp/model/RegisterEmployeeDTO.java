package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterEmployeeDTO {
    private String p_first_name;
    private String p_last_name;
    private String p_address;
    private String p_city;
    private String p_telephone;
    private String p_email;
    private String p_fav_animal;
    private Integer p_position;
    private Double p_salary;
    private LocalDate p_date_start;
}