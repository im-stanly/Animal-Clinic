package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkHoursModel {
    private Integer worker_id;
    private String weekDay;
    private LocalDate start_date;
    private LocalDate end_date;
}
