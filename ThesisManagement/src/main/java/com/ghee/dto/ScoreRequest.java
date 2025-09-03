/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author giahu
 */
public class ScoreRequest {
    private List<CriteriaScoreDTO> criteriaScores;

    /**
     * @return the criteriaScores
     */
    public List<CriteriaScoreDTO> getCriteriaScores() {
        return criteriaScores;
    }

    /**
     * @param criteriaScores the criteriaScores to set
     */
    public void setCriteriaScores(List<CriteriaScoreDTO> criteriaScores) {
        this.criteriaScores = criteriaScores;
    }
    
    public static class CriteriaScoreDTO {
        private long criteriaId;
        private BigDecimal score;

        // Getters and Setters
        public long getCriteriaId() { return criteriaId; }
        public void setCriteriaId(long criteriaId) { this.criteriaId = criteriaId; }

        public BigDecimal getScore() { return score; }
        public void setScore(BigDecimal score) { this.score = score; }
    }
    
    
}
