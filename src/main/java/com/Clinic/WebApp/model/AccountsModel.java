package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountsModel {
    private Integer id;
    private String email;
    private String username;
    private String password;
    private String user_permissions;
}