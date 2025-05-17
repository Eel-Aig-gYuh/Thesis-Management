/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.validators;

import java.util.Collection;

/**
 *
 * @author giahu
 */
public class ThesisValidatior {
    public static void validateNotEmpty(Collection<?> collection, String errMsg) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(errMsg);
        }
    }
}
