/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.repositories;

import com.ghee.pojo.Criteria;

/**
 *
 * @author giahu
 */
public interface CriteriaRepository {
    Criteria getCriteriaById(long id);
    
    Criteria createOrUpdate(Criteria criteria);
    void deleteCriteria(long id);
    boolean isCriteriaUsed(long id);
}
