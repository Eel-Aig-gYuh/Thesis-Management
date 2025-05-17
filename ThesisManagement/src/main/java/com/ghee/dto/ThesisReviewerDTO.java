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
public class ThesisReviewerDTO {
    private List<Long> reviewerIds;

    /**
     * @return the reviewIds
     */
    public List<Long> getReviewerIds() {
        return reviewerIds;
    }

    /**
     * @param reviewIds the reviewIds to set
     */
    public void setReviewerIds(List<Long> reviewIds) {
        this.reviewerIds = reviewIds;
    }
    
}
