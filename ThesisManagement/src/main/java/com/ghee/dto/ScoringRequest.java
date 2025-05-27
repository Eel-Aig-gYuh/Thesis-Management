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
public class ScoringRequest {

    private Long councilId;
    private Long thesisId;
    private Long userId;
    private List<ScoringScore> scores;

    /**
     * @return the councilId
     */
    public Long getCouncilId() {
        return councilId;
    }

    /**
     * @param councilId the councilId to set
     */
    public void setCouncilId(Long councilId) {
        this.councilId = councilId;
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
     * @return the scores
     */
    public List<ScoringScore> getScores() {
        return scores;
    }

    /**
     * @param scores the scores to set
     */
    public void setScores(List<ScoringScore> scores) {
        this.scores = scores;
    }
    
    
}
