package com.Clinic.WebApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountPermissionDTO {
    private int id;
    private UserPermission permissionLevel;
}
