/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.enums;

/**
 *
 * @author giahu
 */
public enum UserMajor {
    CNTT("Công nghệ thông tin"),
    CNSH("Công nghệ sinh học"),
    KHCB("Khoa học cơ bản"),
    QTKD("Quản trị kinh doanh"),
    XD("xây dựng");

    private final String displayName;

    UserMajor(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
