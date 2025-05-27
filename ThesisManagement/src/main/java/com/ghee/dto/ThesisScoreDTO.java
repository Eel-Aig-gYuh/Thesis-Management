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
public class ThesisScoreDTO {

    private Long thesisId;
    private BigDecimal averageScore;
    private Long councilId;
    private List<CriteriaScoreDTO> scores;

    public ThesisScoreDTO() {
    }

    public ThesisScoreDTO(Long thesisId, BigDecimal averageScore, Long councilId, List<CriteriaScoreDTO> scores) {
        this.thesisId = thesisId;
        this.averageScore = averageScore;
        this.councilId = councilId;
        this.scores = scores;
    }

    // Getters and setters
    public Long getThesisId() {
        return thesisId;
    }

    public void setThesisId(Long thesisId) {
        this.thesisId = thesisId;
    }

    public BigDecimal getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(BigDecimal averageScore) {
        this.averageScore = averageScore;
    }

    public Long getCouncilId() {
        return councilId;
    }

    public void setCouncilId(Long councilId) {
        this.councilId = councilId;
    }

    public List<CriteriaScoreDTO> getScores() {
        return scores;
    }

    public void setScores(List<CriteriaScoreDTO> scores) {
        this.scores = scores;
    }
}
