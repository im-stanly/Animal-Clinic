package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaxxModel {
    private int id;
    private Integer pet_id;
    private LocalDate period_start;
    private LocalDate period_end;
    private Boolean done;
    private String type;
}
