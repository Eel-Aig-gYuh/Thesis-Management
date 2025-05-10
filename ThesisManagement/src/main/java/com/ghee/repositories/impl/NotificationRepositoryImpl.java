/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.repositories.impl;

import com.ghee.pojo.Notifications;
import com.ghee.repositories.NotificationRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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
public class NotificationRepositoryImpl implements NotificationRepository{
    private static final Logger logger = Logger.getLogger(NotificationRepositoryImpl.class.getName());
    
    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private Environment env;

    /**
     * Lấy thông báo thông qua id.
     * @param id
     * @return 
     */
    @Override
    public Notifications getNotificationByID(long id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Notifications.class, id);
    }

    @Override
    public List<Notifications> getNotificationses(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Notifications> q = b.createQuery(Notifications.class);
        Root root = q.from(Notifications.class);
        q.select(root);
        
        Query query = s.createQuery(q);
        
        // phân trang.
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
    public Notifications createNotification(Notifications n) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            logger.log(Level.INFO, "Starting transaction for creating notification: {0}", n.getId());
            
            s.persist(n);
            s.flush();
            s.refresh(n);
            
            logger.log(Level.INFO, "Notification created successfully: {0}", n.getId());
            return n;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create notification: " + e.getMessage(), e);
        }
    }

    @Override
    public Notifications updateNotification(Notifications n) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            logger.log(Level.INFO, "Starting transaction for updating notification: {0}", n.getId());
            
            s.merge(n);
            s.refresh(n);
            
            logger.log(Level.INFO, "Notification updated successfully: {0}", n.getId());
            return n;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update notification: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteNotification(long id) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            logger.log(Level.INFO, "Starting transaction for deleting notification ID: {0}", id);
            
            Notifications n = s.get(Notifications.class, id);
            if (n != null) {
                s.remove(n);
            }
            
            logger.log(Level.INFO, "Notification deleted successfully: {0}", id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete notification: " + e.getMessage(), e);
        }
    }
    
}
