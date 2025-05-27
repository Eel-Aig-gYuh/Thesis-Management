/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import java.math.BigDecimal;

/**
 *
 * @author giahu
 */
public class ScoringScore {
    private Long criteriaId;
    private BigDecimal score;

    /**
     * @return the criteriaId
     */
    public Long getCriteriaId() {
        return criteriaId;
    }

    /**
     * @param criteriaId the criteriaId to set
     */
    public void setCriteriaId(Long criteriaId) {
        this.criteriaId = criteriaId;
    }

    /**
     * @return the score
     */
    public BigDecimal getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(BigDecimal score) {
        this.score = score;
    }
}
