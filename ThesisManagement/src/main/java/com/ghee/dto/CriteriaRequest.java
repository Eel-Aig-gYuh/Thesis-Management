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
public class CriteriaRequest {
    private List<CriteriaDTO> criterias;
    private List<Long> thesisId;
    private List<Long> createdBy;

    /**
     * @return the criterias
     */
    public List<CriteriaDTO> getCriterias() {
        return criterias;
    }

    /**
     * @param criterias the criterias to set
     */
    public void setCriterias(List<CriteriaDTO> criterias) {
        this.criterias = criterias;
    }

    /**
     * @return the thesisId
     */
    public List<Long> getThesisId() {
        return thesisId;
    }

    /**
     * @param thesisId the thesisId to set
     */
    public void setThesisId(List<Long> thesisId) {
        this.thesisId = thesisId;
    }

    /**
     * @return the createdBy
     */
    public List<Long> getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(List<Long> createdBy) {
        this.createdBy = createdBy;
    }

    
    
    
}
