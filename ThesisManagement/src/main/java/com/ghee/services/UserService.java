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
    public Users getUserById(long id);
    public Users getUserByUsername(String username);
    public List<Users> getUsers(Map<String, String> params);
    
    public Users createUser(Map<String, String>params, MultipartFile avatar);
    
    public Users updateUser(long id, Map<String, String>params, MultipartFile avatar);
    public void changePassword(long id, String oldPass, String newPass);
    
    public void deleteUser(long id);
    
    public String login(String username, String password);
    public boolean authenticated(String username, String password);
}
