/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.enums;

/**
 *
 * @author giahu
 */
public enum CouncilMemberRole {
    CHAIRMAN("Chủ tịch hội đồng"), // Người chủ trì buổi bảo vệ
    SECRETARY("Thư ký"), // Ghi biên bản buổi bảo vệ
    REVIEWER("Phản biện"), // Đọc trước khóa luận và đưa ra nhận xét
    MEMBER("Thành viên"); // Tham gia với vai trò chấm điểm và đặt câu hỏi.
    
    private final String displayName;

    CouncilMemberRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
