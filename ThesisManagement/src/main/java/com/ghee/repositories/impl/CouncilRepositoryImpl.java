/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.repositories.impl;

import com.ghee.pojo.Councils;
import com.ghee.repositories.CouncilRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author giahu
 */
@Repository
@Transactional
@PropertySource("classpath:application.properties")
public class CouncilRepositoryImpl implements CouncilRepository{
    private static final Logger logger = Logger.getLogger(CouncilRepositoryImpl.class.getName());
    
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Autowired
    private Environment env;
    
    @Override
    public Map<String, Object> getCouncils(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();

        // lấy danh sách hội đồng.
        CriteriaQuery<Councils> q = b.createQuery(Councils.class);
        Root root = q.from(Councils.class);
        q.select(root);
        
        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            // Lọc theo name
            String name = params.get("name");
            if (name != null && !name.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", name)));
            }

            // Lọc theo status
            String status = params.get("status");
            if (status != null && !status.isEmpty()) {
                predicates.add(b.equal(root.get("status"), status));
            }

            // Sắp xếp.
            q.where(predicates.toArray(Predicate[]::new));
            String order = params.get("order");
            if ("asc".equalsIgnoreCase(order)) {
                q.orderBy(b.asc(root.get("id")));
            } else {
                q.orderBy(b.desc(root.get("id"))); // mặc định
            }
        }
        
        Query query = s.createQuery(q);

        // phân trang.
        int pageSize = Integer.parseInt(env.getProperty("page.size"));
        if (params != null && params.containsKey("page")) {
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;

            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }
        List<Councils> councils = query.getResultList();
        
        // tính tổng số bản ghi
        CriteriaQuery<Long> countQuery = b.createQuery(Long.class);
        countQuery.select(b.count(countQuery.from(Councils.class)));
        Long totalRecords = s.createQuery(countQuery).getSingleResult();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        
        Map<String, Object> result = new HashMap<>();
        result.put("theses", councils);
        result.put("totalPages", totalPages);

        return result;
    }

    @Override
    public Councils createOrUpdateCouncil(Councils council) {
        Session s = this.factory.getObject().getCurrentSession();
        
        if (council.getId() == null) {
            s.persist(council);
            s.flush();
        } else {
            s.merge(council);
        }
        s.refresh(council);
        
        return council;
    }

    @Override
    public Councils getCouncilById(long id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Councils.class, id);
    }

    @Override
    public void deleteCouncil(long id) {
        Session s = this.factory.getObject().getCurrentSession();
        
        try {
            Councils c = s.get(Councils.class, id);
            
            if (c != null) {
                s.remove(c);
                s.refresh(c);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete thesis: " + e.getMessage(), e);
        }
    }
    
}
