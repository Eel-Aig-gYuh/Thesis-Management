/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import java.util.List;

/**
 *
 * @author giahu
 */
public class ScoringResponse {

    private String message;
    private Long thesisId;
    private Long userId;
    private int scoresSubmitted;

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the thesisId
     */
    public Long getThesisId() {
        return thesisId;
    }

    /**
     * @param thesisId the thesisId to set
     */
    public void setThesisId(Long thesisId) {
        this.thesisId = thesisId;
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
     * @return the scoresSubmitted
     */
    public int getScoresSubmitted() {
        return scoresSubmitted;
    }

    /**
     * @param scoresSubmitted the scoresSubmitted to set
     */
    public void setScoresSubmitted(int scoresSubmitted) {
        this.scoresSubmitted = scoresSubmitted;
    }

}
