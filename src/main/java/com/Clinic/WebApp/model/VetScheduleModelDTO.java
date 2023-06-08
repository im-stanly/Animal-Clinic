package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VetScheduleModelDTO {
    Integer id;
    String first_name;
    String last_name;
    String specialization;
}
