package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionsModel {
    private int id_visit;
    private int med_id;
    private int amount;
    private double price;
    private String dosing;
}
