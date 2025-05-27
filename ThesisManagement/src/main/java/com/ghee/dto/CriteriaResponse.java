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
public class CriteriaResponse {
    private List<CriteriaDTO> criterias;
    private List<Long> thesisIds;

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
