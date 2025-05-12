/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services;

import com.ghee.pojo.Users;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author giahu
 */
public interface UserService extends UserDetailsService {
    Users getUserById(long id);
    Users getUserByUsername(String username);
    List<Users> getUsers(Map<String, String> params);
    
    Users createUser(Map<String, String>params, MultipartFile avatar);
    Users createOrUpdate(Users u);
    
    Users updateUser(long id, Map<String, String>params, MultipartFile avatar);
    void changePassword(long id, String oldPass, String newPass);
    
    void deleteUser(long id);
    
    String login(String username, String password);
    boolean authenticated(String username, String password);
}
