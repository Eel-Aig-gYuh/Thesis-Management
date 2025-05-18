/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.repositories.impl;

import com.ghee.pojo.Criteria;
import com.ghee.repositories.CriteriaRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author giahu
 */
@Repository
@Transactional
public class CriteriaRepositoryImpl implements CriteriaRepository{

    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Override
    public Criteria getCriteriaById(long id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Criteria.class, id);
    }

    @Override
    public Criteria createOrUpdate(Criteria criteria) {
        Session s = this.factory.getObject().getCurrentSession();
        if(criteria.getId() == null) {
            s.persist(criteria);
            s.flush();
        } else {
            s.merge(criteria);
            s.flush();
        }
   
        return criteria;
    }

    @Override
    public void deleteCriteria(long id) {
        Session s = this.factory.getObject().getCurrentSession();
        Criteria criteria = s.get(Criteria.class, id);
        s.remove(criteria);
    }

    @Override
    public boolean isCriteriaUsed(long id) {
        Session s = this.factory.getObject().getCurrentSession();
        
        Query<Long> query = s.createQuery("SELECT COUNT(*) FROM Scores s WHERE s.criteriaId.id = :criteriaId", Long.class);
        query.setParameter("criteriaId", id);
        return query.uniqueResult() > 0;
    }
    
}
