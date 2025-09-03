/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.validators;

import com.ghee.enums.UserRole;
import com.ghee.pojo.Users;

/**
 *
 * @author giahu
 */
public class UserValidator {
    public static void checkRole(Users user, UserRole expectedRole) {
        if (user == null || !expectedRole.name().equals(user.getRole())) {
            throw new IllegalArgumentException("User must have role: " + expectedRole);
        }
    }

    public static void checkNotNull(Users user, String errorMsg) {
        if (user == null) {
            throw new IllegalArgumentException(errorMsg);
        }
    }
}
