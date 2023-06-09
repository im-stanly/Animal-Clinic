package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionsModel {
    private Integer id_visit;
    private Integer med_id;
    private Integer amount;
    private Double price;
    private String dosing;
}