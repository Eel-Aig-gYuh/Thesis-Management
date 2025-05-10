/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ghee.services;

import com.ghee.dto.NotificationRequest;
import com.ghee.pojo.Notifications;
import java.util.List;
import java.util.Map;

/**
 *
 * @author giahu
 */
public interface NotificationService {
    Notifications getNotificationById(long id);
    List<Notifications> getNotificationses(Map<String, String> params);
    
    Notifications createNotification(NotificationRequest dto, String username);
    Notifications updateNotification(long id, NotificationRequest dto, String username);
    void deleteNotification(long id);
}
