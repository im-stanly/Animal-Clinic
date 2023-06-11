package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkHoursModel {
    private Integer employee_id;
    private String weekDay;
    private LocalTime start_time;
    private LocalTime end_time;
}
