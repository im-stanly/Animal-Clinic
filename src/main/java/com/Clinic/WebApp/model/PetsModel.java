package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetsModel {
    private int id;
    private String name;
    private Character sex;
    private String type;
    private String race;
    private LocalDate birth_day;
    private Integer person_id;
    private Double weight;
    private Boolean dangerous;
    private Boolean estimate;
}
