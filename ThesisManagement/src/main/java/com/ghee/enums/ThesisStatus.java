/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.enums;

/**
 *
 * @author giahu
 */
public enum ThesisStatus {
    DRAFT("Tạo mới"),
    REGISTERED("Đăng ký"),
    APPROVED("Chấp nhận"),
    REJECTED("Từ chối"),
    CANCELLED("Hủy bỏ");
    
    private final String displayName;

    ThesisStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
