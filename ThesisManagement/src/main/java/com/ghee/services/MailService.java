/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services;

import com.ghee.pojo.Users;
import java.util.List;

/**
 *
 * @author giahu
 */
public interface MailService {
    void sendNotificationEmail(Users recipient, String content);
    void sendEmailToUsers(List<Users> users, String content);
    void sendEmail(String to, String subject, String content);
}
