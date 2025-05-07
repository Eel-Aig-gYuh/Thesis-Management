/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ghee.pojo.Users;
import com.ghee.repositories.UserRepository;
import com.ghee.services.UserService;
import java.io.IOException;
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
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private Cloudinary cloudinary;
    
    @Override
    public List<Users> getUsers(Map<String, String> params) {
        return this.userRepo.getUsers(params);
    }

    @Override
    public Users getUserById(int id) {
        return this.userRepo.getUserById(id);
    }

    @Override
    public Users addOrUpdateUser(Users u) {
        if (!u.getFile().isEmpty()) {
            String contentType = u.getFile().getContentType();
            if (contentType != null && contentType.startsWith("image/")) {
                try {
                    Map res = cloudinary.uploader().upload(u.getFile().getBytes(),
                            ObjectUtils.asMap("resource_type", "auto"));
                    u.setAvatar(res.get("secure_url").toString());
                } catch (IOException ex) {
                    Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                throw new IllegalArgumentException("Chỉ được phép upload file hình ảnh!");
            }
        }
        
        return this.userRepo.addOrUpdateUser(u);
    }

    @Override
    public void deleteUser(int id) {
        this.userRepo.deleteUser(id);
    }
    
}
