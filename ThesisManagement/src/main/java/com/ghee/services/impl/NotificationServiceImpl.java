/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services.impl;

import com.ghee.dto.NotificationRequest;
import com.ghee.enums.NotificationStatus;
import com.ghee.enums.NotificationType;
import com.ghee.pojo.Notifications;
import com.ghee.repositories.NotificationRepository;
import com.ghee.repositories.UserRepository;
import com.ghee.services.NotificationService;
import com.ghee.utils.DateUtils;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author giahu
 */
@Service
public class NotificationServiceImpl implements NotificationService{
    private static final Logger logger = Logger.getLogger(NotificationServiceImpl.class.getName());
    
    @Autowired
    private NotificationRepository notiRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    @Override
    public Notifications getNotificationById(long id) {
        return this.notiRepo.getNotificationByID(id);
    }

    @Override
    public List<Notifications> getNotificationses(Map<String, String> params) {
        return this.notiRepo.getNotificationses(params);
    }

    @Override
    public Notifications createNotification(NotificationRequest dto, String username) {
        logger.log(Level.INFO, "Creating notification with content: {0}", dto.getContent());
        
        Notifications newNotifications = new Notifications();
        newNotifications.setUserId(dto.getUserID());
        newNotifications.setContent(dto.getContent());
        newNotifications.setType(String.valueOf(NotificationType.EMAIL));
        newNotifications.setStatus(String.valueOf(NotificationStatus.PENDING));
        newNotifications.setCreatedAt(DateUtils.getTodayWithoutTime());
        
        Notifications createdNotifications = this.notiRepo.createNotification(newNotifications);
        logger.log(Level.INFO, "Notification created successfully: {0}", createdNotifications.getContent());
        return createdNotifications;
    }

    @Override
    public Notifications updateNotification(long id, NotificationRequest dto, String username) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteNotification(long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
