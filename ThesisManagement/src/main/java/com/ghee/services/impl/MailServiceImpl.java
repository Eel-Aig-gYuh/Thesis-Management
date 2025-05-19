/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services.impl;

import com.ghee.enums.NotificationStatus;
import com.ghee.enums.UserRole;
import com.ghee.pojo.Notifications;
import com.ghee.pojo.Users;
import com.ghee.repositories.NotificationRepository;
import com.ghee.services.MailService;
import com.ghee.utils.DateUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 *
 * @author giahu
 */
@Service
@PropertySource("classpath:javamail.properties")
public class MailServiceImpl implements MailService {

    private static final Logger logger = Logger.getLogger(MailServiceImpl.class.getName());

    @Autowired
    private NotificationRepository notiRepo;

    @Autowired
    private Environment env;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void sendNotificationEmail(Users recipient, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(env.getProperty("spring.mail.username"));
            helper.setTo(recipient.getEmail());
            helper.setSubject("Thesis Management Notification");

            // Xử lý template HTML
            Context context = new Context();
            context.setVariable("content", content);
            String htmlContent = templateEngine.process("/email/notification", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            Logger.getLogger(MailServiceImpl.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void sendEmailToUsers(List<Users> users, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        Users primary = users.stream()
                .filter(u -> UserRole.ROLE_GIAOVU.name().equals(u.getRole()))
                .findFirst()
                .orElse(users.get(0));

        try {
            helper.setFrom(env.getProperty("spring.mail.username"));
        } catch (MessagingException ex) {
            Logger.getLogger(MailServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            helper.setTo(primary.getEmail());
        } catch (MessagingException ex) {
            Logger.getLogger(MailServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<String> cc = users.stream()
                .filter(u -> !u.getEmail().equals(primary.getEmail()))
                .map(Users::getEmail)
                .collect(Collectors.toList());

        if (!cc.isEmpty()) {
            try {
                helper.setCc(cc.toArray(new String[0]));
            } catch (MessagingException ex) {
                Logger.getLogger(MailServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            helper.setSubject("Thesis Management Notification");
        } catch (MessagingException ex) {
            Logger.getLogger(MailServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        Context ctx = new Context();
        ctx.setVariable("content", content);
        String html = templateEngine.process("notification", ctx);

        try {
            helper.setText(html, true);
        } catch (MessagingException ex) {
            Logger.getLogger(MailServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        mailSender.send(message);
    }

    @Override
    public void sendEmail(String to, String subject, String content) {
        logger.log(Level.INFO, "Sending email to: {0}, subject: {1}", new Object[]{to, subject});

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, false);
            mailSender.send(message);
            logger.log(Level.INFO, "Email sent successfully to: {0}", to);
        } catch (MessagingException | MailException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}
