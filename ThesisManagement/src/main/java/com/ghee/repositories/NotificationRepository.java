/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ghee.repositories;

import com.ghee.pojo.Notifications;
import java.util.List;
import java.util.Map;

/**
 *
 * @author giahu
 */
public interface NotificationRepository {
    Notifications getNotificationByID(long id);
    List<Notifications> getNotificationses (Map<String, String> params);
    
    Notifications createOrUpdate(Notifications n);
    
    void deleteNotification(long id);
}
