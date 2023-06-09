package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetsModel {
    private Integer id;
    private String name;
    private Character sex;
    private Integer type;
    private LocalDate birth_day;
    private Double weight;
    private Boolean dangerous;
    private Boolean estimate;
}
