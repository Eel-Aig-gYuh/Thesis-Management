/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import com.ghee.enums.CouncilMemberRole;

/**
 * Mục đích: DTO cho thành viên hội đồng trong response.
 * Nội dung: Thông tin người dùng, vai trò.
 * @author giahu
 */
public class CouncilMemberResponseDTO {
    private ThesisUserDTO user;
    private CouncilMemberRole role;

    /**
     * @return the user
     */
    public ThesisUserDTO getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(ThesisUserDTO user) {
        this.user = user;
    }

    /**
     * @return the role
     */
    public CouncilMemberRole getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(CouncilMemberRole role) {
        this.role = role;
    }
    
    
}
