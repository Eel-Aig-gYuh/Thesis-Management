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
public class ScoringDTO {
    private Long councilId;
    private List<Long> thesisIds;

    public ScoringDTO() {
    }

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
     * @return the thesisIds
     */
    public List<Long> getThesisIds() {
        return thesisIds;
    }

    /**
     * @param thesisIds the thesisIds to set
     */
    public void setThesisIds(List<Long> thesisIds) {
        this.thesisIds = thesisIds;
    }
    
    
}
