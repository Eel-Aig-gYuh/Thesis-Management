/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.repositories.impl;

import com.ghee.pojo.Users;
import com.ghee.repositories.UserRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author giahu
 */
@Repository
@Transactional
@PropertySource("classpath:application.properties")
public class UserRepositoryImpl implements UserRepository{
    private static final Logger logger = Logger.getLogger(UserRepositoryImpl.class.getName());
    
    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private Environment env;

    @Override
    public List<Users> getUsers(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Users> q = b.createQuery(Users.class);
        Root root = q.from(Users.class);
        q.select(root);
        
        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();
            
            // Lọc theo username
            String username = params.get("username");
            if (username != null && !username.isEmpty()) {
                predicates.add(b.like(root.get("username"), String.format("%%%s%%", username)));
            }
            
            // Lọc theo role
            String role = params.get("role");
            if (role != null && !role.isEmpty()) {
                predicates.add(b.equal(root.get("role"), role));
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
        
        // Phân trang.
        int pageSize = Integer.parseInt(env.getProperty("page.size"));
        if (params != null && params.containsKey("page")) {
            int page = Integer.parseInt(params.get("page"));
            int start = (page - 1) * pageSize;
            
            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }
        
        return query.getResultList();
    } 

    @Override
    public Users getUserById(long id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Users.class, id);
    }

    @Override
    public Users getUserByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Users.findByUsername", Users.class);
        q.setParameter("username", username);

        return (Users) q.getSingleResult();
    }

    @Override
    public boolean authenticated(String username, String password) {
        Users u = this.getUserByUsername(username);
        
        return this.passwordEncoder.matches(password, u.getPassword());
    }
    
    // ====================== GHEE
    @Override
    public Users createOrUpdate(Users u) {
        Session s = this.factory.getObject().getCurrentSession();
        
        if (u.getId() == null) {
            logger.log(Level.INFO, "Starting transaction for creating user: {0}", u.getUsername());
            s.persist(u);
            s.flush();
        }
        else {
            logger.log(Level.INFO, "Starting transaction for updating user: {0}", u.getUsername());
            s.merge(u);
        }
        s.refresh(u);
        
        return u;
    }
    
    @Override
    public Users createUser(Users u) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            logger.log(Level.INFO, "Starting transaction for creating user: {0}", u.getUsername());
            
            s.persist(u);
            s.flush();
            s.refresh(u);
            
            logger.log(Level.INFO, "User created successfully: {0}", u.getUsername());
            return u;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Users updateUser(Users u) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            logger.log(Level.INFO, "Starting transaction for updating user: {0}", u.getUsername());
            
            s.merge(u);
            s.refresh(u);
        
            logger.log(Level.INFO, "User updated successfully: {0}", u.getUsername());
            return u;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update user: " + e.getMessage(), e);
        }
    }
}
