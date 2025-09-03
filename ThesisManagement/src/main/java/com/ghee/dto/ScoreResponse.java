/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author giahu
 */
public class ScoreResponse {
    private long id;
    private ThesisUserDTO councilMember;
    private List<CriteriaScoreDTO> criteriaScores;
    private Date createdAt;
    
    public ScoreResponse(long id, ThesisUserDTO councilMember, List<CriteriaScoreDTO> criteriaScores, Date createdAt) {
        this.id = id;
        this.councilMember = councilMember;
        this.criteriaScores = criteriaScores;
        this.createdAt = createdAt;
    }

    public ScoreResponse() {
    }
    
    public static class CriteriaScoreDTO {
        private long criteriaId;
        private String criteriaName;
        private BigDecimal maxScore;
        private BigDecimal score;

        // Constructor
        public CriteriaScoreDTO(long criteriaId, String criteriaName, BigDecimal maxScore, BigDecimal score) {
            this.criteriaId = criteriaId;
            this.criteriaName = criteriaName;
            this.maxScore = maxScore;
            this.score = score;
        }

        // Getters and Setters
        public long getCriteriaId() { return criteriaId; }
        public void setCriteriaId(long criteriaId) { this.criteriaId = criteriaId; }

        public String getCriteriaName() { return criteriaName; }
        public void setCriteriaName(String criteriaName) { this.criteriaName = criteriaName; }

        public BigDecimal getMaxScore() { return maxScore; }
        public void setMaxScore(BigDecimal maxScore) { this.maxScore = maxScore; }

        public BigDecimal getScore() { return score; }
        public void setScore(BigDecimal score) { this.score = score; }
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the councilMember
     */
    public ThesisUserDTO getCouncilMember() {
        return councilMember;
    }

    /**
     * @param councilMember the councilMember to set
     */
    public void setCouncilMember(ThesisUserDTO councilMember) {
        this.councilMember = councilMember;
    }

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

    /**
     * @return the createdAt
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
