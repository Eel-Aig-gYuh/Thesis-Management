/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author giahu
 */
public class CriteriaResponse {
    private long id;
    private String name;
    private BigDecimal maxScore;
    
    private ThesisUserDTO createdBy;
    private Date createdAt;

    public CriteriaResponse() {
    }

    
    public CriteriaResponse(long id, String name, BigDecimal maxScore, ThesisUserDTO createdBy, Date createdAt) {
        this.id = id;
        this.name = name;
        this.maxScore = maxScore;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the maxScore
     */
    public BigDecimal getMaxScore() {
        return maxScore;
    }

    /**
     * @param maxScore the maxScore to set
     */
    public void setMaxScore(BigDecimal maxScore) {
        this.maxScore = maxScore;
    }

    /**
     * @return the createdBy
     */
    public ThesisUserDTO getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(ThesisUserDTO createdBy) {
        this.createdBy = createdBy;
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
