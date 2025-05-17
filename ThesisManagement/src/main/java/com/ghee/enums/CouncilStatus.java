/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.enums;

/**
 *
 * @author giahu
 */
public enum CouncilStatus {
    SCHEDULED("Dự kiến"),
    COMPLETED("Đã khóa"),
    CANCELED("Đã hoãn");
    
    private final String displayName;

    CouncilStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
