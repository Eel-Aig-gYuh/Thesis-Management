/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.repositories.impl;

import com.ghee.pojo.Theses;
import com.ghee.repositories.ThesisRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
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
public class ThesisRepositoryImpl implements ThesisRepository{
    private static final Logger logger = Logger.getLogger(UserRepositoryImpl.class.getName());
    
    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private Environment env;

    // ==================================== GHEE
    
    @Override
    public Theses createOrUpdate(Theses theses) {
        Session s = this.factory.getObject().getCurrentSession();
        
        if (theses.getId() == null) {
            logger.log(Level.INFO, "Starting transaction for creating thesis: {0}", theses.getTitle());
            s.persist(theses);
            s.flush();
        }
        else {
            logger.log(Level.INFO, "Starting transaction for updating thesis: {0}", theses.getTitle());
            s.merge(theses);
        }
        s.refresh(theses);
        
        return theses;
    }
    
    /**
     * Tạo khóa luận.
     * @param theses: khóa luận muốn tạo.
     * @return 
     */
    @Override
    public Theses createThesis(Theses theses) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            logger.log(Level.INFO, "Starting transaction for creating thesis: {0}", theses.getTitle());
            
            s.persist(theses);
            s.flush();
            s.refresh(theses);
            
            logger.log(Level.INFO, "Thesis created successfully: {0}", theses.getTitle());
            return theses;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create thesis: " + e.getMessage(), e);
        }
    }
    
    /**
     * Cập nhật khóa luận.
     * @param theses
     * @return 
     */
    @Override
    public Theses updateThesis(Theses theses){
        Session s = this.factory.getObject().getCurrentSession();
        try {
            logger.log(Level.INFO, "Starting transaction for updating thesis: {0}", theses.getTitle());
            
            s.merge(theses);
            s.refresh(theses);
            
            logger.log(Level.INFO, "Thesis updated successfully: {0}", theses.getTitle());
            return theses;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update thesis: " + e.getMessage(), e);
        }
    }
    
    /**
     * Lấy khoá luận theo id.
     * @param id
     * @return 
     */
    @Override
    public Theses getThesisById(long id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Theses.class, id);
    }

    /**
     * Lấy danh sách các khóa luận có phân trang.
     * @param params
     * @return 
     */
    @Override
    public Map<String, Object> getTheses(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        
        // lấy danh sách khóa luận.
        CriteriaQuery<Theses> q = b.createQuery(Theses.class);
        Root root = q.from(Theses.class);
        q.select(root);
        
        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();
            
            // Lọc theo title
            String title = params.get("title");
            if (title != null && !title.isEmpty()) {
                predicates.add(b.like(root.get("title"), String.format("%%%s%%", title)));
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
            int page = Integer.parseInt(params.get("page"));
            int start = (page - 1) * pageSize;
            
            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }
        List<Theses> theses = query.getResultList();
        
        // tính tổng số bản ghi
        CriteriaQuery<Long> countQuery = b.createQuery(Long.class);
        countQuery.select(b.count(countQuery.from(Theses.class)));
        Long totalRecords = s.createQuery(countQuery).getSingleResult();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        
        Map<String, Object> result = new HashMap<>();
        result.put("theses", theses);
        result.put("totalPages", totalPages);
        
        return result;
    }

    /**
     * Kiểm tra tiêu đề của khóa luận có tồn tại hay chưa.
     * @param title
     * @return 
     */
    @Override
    public boolean existsByTitle(String title) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<Theses> root = q.from(Theses.class);
        q.select(b.count(root)).where(b.equal(root.get("title"), title));
        return s.createQuery(q).getSingleResult() > 0;
    }

    /**
     * Xoá khóa luận.
     * @param id 
     */
    @Override
    public void deleteThesis(long id) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            logger.log(Level.INFO, "Starting transaction for deleting thesis ID: {0}", id);
            
            Theses theses = s.get(Theses.class, id);
            if (theses != null) {
                s.remove(theses);
            }
            
            logger.log(Level.INFO, "Thesis deleted successfully: {0}", id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete thesis: " + e.getMessage(), e);
        }
    }
}
