package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterEmployeeModel {
    private String p_first_name;
    private String p_last_name;
    private String p_address;
    private String p_city;
    private String p_telephone;
    private String p_email;
    private String p_fav_animal;
    private Integer p_position;
    private Double p_salary;
    private LocalDate p_date_start;
}

//
//
//    SELECT add_employee_with_person(
//        'John',
//                'Doe',
//                '123 Main St',
//                'New York',
//                '123-456-7890',
//                'john.doe@example.com',
//                'Dog',
//                1,
//                5000.00,
//                '2023-06-06'
//    );
//    p_first_name VARCHAR(20),
//    p_last_name VARCHAR(40),
//    p_address VARCHAR(200),
//    p_city VARCHAR(40),
//    p_telephone VARCHAR(20),
//    p_email VARCHAR(60),
//    p_fav_animal VARCHAR(40),
//    p_position INTEGER,
//    p_salary NUMERIC(10, 2),
//    p_date_start DATE
//    CREATE OR REPLACE FUNCTION add_employee_with_person(
//
//)
