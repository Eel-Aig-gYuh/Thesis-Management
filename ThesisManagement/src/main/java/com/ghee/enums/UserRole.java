/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.enums;

/**
 *
 * @author giahu
 */
public enum UserRole {
    ROLE_ADMIN("Admin"),
    ROLE_GIAOVU("Giáo vụ"),
    ROLE_GIANGVIEN("Giảng viên"),
    ROLE_SINHVIEN("Sinh viên");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

