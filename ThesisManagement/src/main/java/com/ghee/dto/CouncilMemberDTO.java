/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import com.ghee.enums.CouncilMemberRole;

/**
 *
 * @author giahu
 */
public class CouncilMemberDTO {
    private Long userId;
    private CouncilMemberRole role;

    public CouncilMemberDTO(Long userId, CouncilMemberRole role) {
        this.userId = userId;
        this.role = role;
    }

    public CouncilMemberDTO() {
    }

    
    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
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
