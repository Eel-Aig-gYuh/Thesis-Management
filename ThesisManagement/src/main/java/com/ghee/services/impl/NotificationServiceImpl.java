/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services.impl;

import com.ghee.dto.NotificationRequest;
import com.ghee.enums.NotificationStatus;
import com.ghee.enums.NotificationType;
import com.ghee.pojo.Notifications;
import com.ghee.pojo.Users;
import com.ghee.repositories.NotificationRepository;
import com.ghee.repositories.UserRepository;
import com.ghee.services.MailService;
import com.ghee.services.NotificationService;
import com.ghee.utils.DateUtils;
import jakarta.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author giahu
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = Logger.getLogger(NotificationServiceImpl.class.getName());

    @Autowired
    private NotificationRepository notiRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MailService mailService;

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

        Notifications createdNotifications = this.notiRepo.createOrUpdate(newNotifications);
        logger.log(Level.INFO, "Notification created successfully: {0}", createdNotifications.getContent());
        return createdNotifications;
    }

    @Override
    public void sendNotification(Users user, String content) {
        Notifications notification = new Notifications();
        notification.setUserId(user);
        notification.setType(String.valueOf(NotificationType.EMAIL));
        notification.setContent(content);
        notification.setStatus(String.valueOf(NotificationStatus.PENDING));
        notification.setCreatedAt(DateUtils.getTodayWithoutTime());

        this.notiRepo.createOrUpdate(notification);

        try {
            this.mailService.sendNotificationEmail(user, content);
            notification.setStatus(String.valueOf(NotificationStatus.SENT));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error sending message to {0}: {1}", new Object[]{user.getEmail(), e.getMessage()});
            notification.setStatus(String.valueOf(NotificationStatus.FAILED));
        }

        this.notiRepo.createOrUpdate(notification);
    }

    @Override
    public void sendBulkNotification(List<Users> recipients, String content) {
        if (recipients == null || recipients.isEmpty()) {
            logger.warning("No recipients for bulk notification");
            return;
        }

        Date now = DateUtils.getTodayWithoutTime(); // dùng cùng một thời điểm

        List<Notifications> notifications = recipients.stream().map(user -> {
            Notifications n = new Notifications();
            n.setUserId(user);
            n.setContent(content);
            n.setType(NotificationType.EMAIL.name());
            n.setStatus(NotificationStatus.PENDING.name());
            n.setCreatedAt(now);
            return n;
        }).collect(Collectors.toList());

        // Lưu tất cả notification trước khi gửi
        notifications.forEach(notiRepo::createOrUpdate);

        try {
            this.mailService.sendEmailToUsers(recipients, content);
            notifications.forEach(n -> n.setStatus(NotificationStatus.SENT.name()));
            logger.info("Bulk email sent successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Bulk email sending failed: {0}", e.getMessage());
            notifications.forEach(n -> n.setStatus(NotificationStatus.FAILED.name()));
        }

        // Cập nhật lại trạng thái sau khi gửi
        notifications.forEach(notiRepo::createOrUpdate);
    }


}
