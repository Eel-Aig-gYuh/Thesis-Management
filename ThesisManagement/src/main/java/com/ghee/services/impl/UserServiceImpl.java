/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ghee.dto.UserReponseDTO;
import com.ghee.enums.UserRole;
import com.ghee.pojo.Theses;
import com.ghee.pojo.Users;
import com.ghee.repositories.UserRepository;
import com.ghee.services.UserService;
import com.ghee.utils.JwtUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author giahu
 */
@Service("userDetailService")
@Transactional
public class UserServiceImpl implements UserService{
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private BCryptPasswordEncoder passEncoder;
    @Autowired
    private Cloudinary cloudinary;
    
    @Override
    public List<Users> getUsers(Map<String, String> params) {
        return this.userRepo.getUsers(params);
    }
    
    @Override
    public Map<String, Object> getAllUsers(Map<String, String> params) {
        Map<String, Object> result = this.userRepo.getUsersForThesis(params);
        List<Users> users = (List<Users>) result.get("users");
        result.put("users", users.stream().map(this::mapToResponseDTO).collect(Collectors.toList()));
        
        
        return result;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users u = this.userRepo.getUserByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("Invalid username!");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(u.getRole()));
        
        return new org.springframework.security.core.userdetails.User(
                u.getUsername(), u.getPassword(), authorities);
    }
    
    @Override
    public Users getUserByUsername(String username) {
        return this.userRepo.getUserByUsername(username);
    }
    
    @Override
    public List<Users> getUserByUserRole(String userRole) {
        return this.userRepo.getUserByUserRole(userRole);
    }

    @Override
    public boolean authenticated(String username, String password) {
        return this.userRepo.authenticated(username, password);
    }

    @Override
    public Users getUserById(long id) {
        return this.userRepo.getUserById(id);
    }

    // ========================== GHEE

    @Override
    public Users createOrUpdate(Users u){
        logger.log(Level.INFO, "Creating user with username: {0}", u.getUsername());
        
        u.setPassword(this.passEncoder.encode(u.getPassword()));
        
        if (!u.getFile().isEmpty()) {
            String contentType = u.getFile().getContentType();
            if (contentType != null && contentType.startsWith("image/")) {
                try {
                    logger.log(Level.INFO, "Uploading avatar to Cloudinary for user: {0}", u.getUsername());
                    Map res = cloudinary.uploader().upload(u.getFile().getBytes(),
                            ObjectUtils.asMap("resource_type", "auto"));
                    u.setAvatar(res.get("secure_url").toString());
                    logger.log(Level.INFO, "New avatar uploaded successfully: {0}", u.getAvatar());
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Failed to upload avatar for user: {0}" + u.getUsername(), ex);
                    throw new RuntimeException("Failed to upload avatar", ex);
                }
            } else {
                throw new IllegalArgumentException("Chỉ được phép upload file hình ảnh!");
            }
        }
        
        Users createdUser = this.userRepo.createOrUpdate(u);
        logger.log(Level.INFO, "User created successfully {0}", createdUser.getUsername());
        return createdUser;
    }
    
    @Override
    public Users createUser(Map<String, String>params, MultipartFile avatar) {
        logger.log(Level.INFO, "Creating user with username: {0}", params.get("username"));
        
        Users u = new Users();
        u.setFirstname(params.get("firstName"));
        u.setLastname(params.get("lastName"));
        u.setEmail(params.get("email"));
        u.setRole(params.get("role"));
        u.setUsername(params.get("username"));
        u.setPassword(this.passEncoder.encode(params.get("password")));
        u.setIsActive(Boolean.TRUE);
        
        if (!avatar.isEmpty()) {
            String contentType = avatar.getContentType();
            if (contentType != null && contentType.startsWith("image/")) {
                try {
                    logger.log(Level.INFO, "Uploading avatar to Cloudinary for user: {0}", params.get("username"));
                    Map res = cloudinary.uploader().upload(avatar.getBytes(),
                            ObjectUtils.asMap("resource_type", "auto"));
                    u.setAvatar(res.get("secure_url").toString());
                    logger.log(Level.INFO, "New avatar uploaded successfully: {0}", u.getAvatar());
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Failed to upload avatar for user: {0}" + params.get("username"), ex);
                    throw new RuntimeException("Failed to upload avatar", ex);
                }
            } else {
                throw new IllegalArgumentException("Chỉ được phép upload file hình ảnh!");
            }
        }
        
        Users createdUser = this.userRepo.createUser(u);
        logger.log(Level.INFO, "User created successfully {0}", createdUser.getUsername());
        return createdUser;
    }
    
    @Override
    public Users updateUser(long id, Map<String, String>params, MultipartFile avatar) {
        logger.log(Level.INFO, "Updating user with ID: {0}", id);
        
        Users u = this.userRepo.getUserById(id);
        if (u == null) {
            logger.log(Level.WARNING, "User not found with ID: {0}", id);
            throw new IllegalArgumentException("User not found");
        }
        
        u.setFirstname(params.get("firstName"));
        u.setLastname(params.get("lastName"));
        u.setEmail(params.get("email"));
        u.setRole(params.get("role"));
        u.setUsername(params.get("username"));
        u.setIsActive(Boolean.TRUE);
        
        if (!avatar.isEmpty()) {
            String contentType = avatar.getContentType();
            if (contentType != null && contentType.startsWith("image/")) {
                try {
                    logger.log(Level.INFO, "Uploading avatar to Cloudinary for user: {0}", params.get("username"));
                    Map res = cloudinary.uploader().upload(avatar.getBytes(),
                            ObjectUtils.asMap("resource_type", "auto"));
                    u.setAvatar(res.get("secure_url").toString());
                    logger.log(Level.INFO, "New avatar uploaded successfully: {0}", u.getAvatar());
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Failed to upload avatar for user: {0}" + params.get("username"), ex);
                    throw new RuntimeException("Failed to upload avatar", ex);
                }
            } else {
                throw new IllegalArgumentException("Chỉ được phép upload file hình ảnh!");
            }
        }
        Users updatedUser = this.userRepo.updateUser(u);
        logger.log(Level.INFO, "User created successfully {0}", updatedUser.getUsername());
        return updatedUser;
    }
    
    @Override
    public void deleteUser(long id) {
        logger.log(Level.INFO, "Deleting user with ID: {0}", id);
        Users u = this.userRepo.getUserById(id);
        if (u == null) {
            logger.log(Level.WARNING, "User not found with ID: {0}", id);
            throw new IllegalArgumentException("User not found");
        }
        
        this.userRepo.deleteUser(id);
        logger.log(Level.INFO, "User deleted successfully {0}", u.getIsActive());
    } 
    
    @Override
    public void changePassword(long id, String oldPass, String newPass){
        logger.log(Level.INFO, "Changing password for user with ID: {0}", id);
        
        Users u = this.userRepo.getUserById(id);
        if (u == null) {
            logger.log(Level.WARNING, "User not found with ID: {0}", id);
            throw new IllegalArgumentException("User not found");
        }
        
        if (!passEncoder.matches(oldPass, u.getPassword())){
            logger.log(Level.INFO, "Incorrect old password for user: {0}", u.getUsername());
            throw new IllegalArgumentException("Old password is incorrect");
        }
        
        u.setPassword(passEncoder.encode(newPass));
        this.userRepo.updateUser(u);
        logger.log(Level.INFO, "Password changed successfully for user: {0}", u.getUsername());
    }
    
    public UserReponseDTO mapToResponseDTO (Users u) {
        UserReponseDTO userResponse = new UserReponseDTO();
        userResponse.setId(u.getId());
        userResponse.setFirstname(u.getFirstname());
        userResponse.setLastname(u.getLastname());
        userResponse.setRole(UserRole.valueOf(u.getRole()));
        
        return userResponse;
    }
}
