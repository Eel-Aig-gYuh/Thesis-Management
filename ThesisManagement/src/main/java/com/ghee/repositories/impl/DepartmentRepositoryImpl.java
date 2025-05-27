/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.repositories.impl;

import com.ghee.pojo.Departments;
import com.ghee.repositories.DepartmentRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
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
public class DepartmentRepositoryImpl implements DepartmentRepository{
    private static final Logger logger = Logger.getLogger(DepartmentRepositoryImpl.class.getName());

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Map<String, Object> getDepartments() {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();

        // lấy danh sách khóa luận.
        CriteriaQuery<Departments> q = b.createQuery(Departments.class);
        Root root = q.from(Departments.class);
        q.select(root);
        
        Query query = s.createQuery(q);
        
        List<Departments> departments = query.getResultList();

        Map<String, Object> result = new HashMap<>();
        result.put("departments", departments);

        return result;
    }
    
}
