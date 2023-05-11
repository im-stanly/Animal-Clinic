package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonsModel {
    private int id;
    private String first_name;
    private String last_name;
    private String address;
    private String city;
    private String telephone;
    private String email;

}
