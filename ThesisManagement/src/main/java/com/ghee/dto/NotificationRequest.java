/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import com.ghee.enums.NotificationStatus;
import com.ghee.pojo.Users;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author giahu
 */
public class NotificationRequest {
    @NotNull private String content;
    @NotNull private Users userID;
    private NotificationStatus status;

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the userID
     */
    public Users getUserID() {
        return userID;
    }

    /**
     * @param userID the userID to set
     */
    public void setUserID(Users userID) {
        this.userID = userID;
    }

    /**
     * @return the status
     */
    public NotificationStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(NotificationStatus status) {
        this.status = status;
    }
    
}
