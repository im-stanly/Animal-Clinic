package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetsModel {
    private int id;
    private String name;
    private char sex;
    private String type;
    private String race;
    private int birth_day;
    private int person_id;
    private double weight;
    private boolean dangerous;
    private boolean estimate;
}
