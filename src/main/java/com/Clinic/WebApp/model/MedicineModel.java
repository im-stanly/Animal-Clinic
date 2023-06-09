package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineModel {
    private Integer id;
    private String name;
    private String company;
    private Integer basePrice;
    private String type;
}
