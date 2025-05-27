/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services;

import com.ghee.dto.CriteriaRequest;
import com.ghee.dto.CriteriaResponse;
import com.ghee.pojo.Criteria;

/**
 *
 * @author giahu
 */
public interface CriteriaService {
    CriteriaResponse createCriteria(CriteriaRequest dto, String username);
    
    void deleteCriteria(long id, String username);
    Criteria getCriteriaById(long id);
}
