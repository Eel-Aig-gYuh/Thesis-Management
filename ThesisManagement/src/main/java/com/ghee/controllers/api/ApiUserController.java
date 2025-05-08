/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.controllers.api;

import com.ghee.pojo.Users;
import com.ghee.services.UserService;
import com.ghee.utils.JwtUtils;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author giahu
 */
@RestController
@RequestMapping("/api/secure/users")
@CrossOrigin
public class ApiUserController {
    private static final Logger logger = Logger.getLogger(ApiUserController.class.getName());
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> create(
            @RequestParam Map<String, String> params, 
            @RequestParam(value="avatar") MultipartFile avatar) {
        logger.log(Level.INFO, "Received request to create user: {0}", params.get("username"));
        try {
            Users u = this.userService.createUser(params, avatar);
            logger.log(Level.INFO, "User created successfully: {0}", u.getUsername());
        
            return new ResponseEntity<>(u, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create user: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable(value = "userId") long id){
        logger.log(Level.INFO, "Received request to delete user ID: {0}", id);
        try {
            this.userService.deleteUser(id);
            logger.log(Level.INFO, "User deleted successfully ID: {0}", id);
            
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to delete user: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> update(
        @PathVariable(value = "userId") long id,
        @RequestParam Map<String, String> params, 
        @RequestParam(value="avatar", required = false) MultipartFile avatar) {
        logger.log(Level.INFO, "Received request to update user ID: {0}", id);
        try {
            Users u = this.userService.updateUser(id, params, avatar);
            logger.log(Level.INFO, "User updated successfully: {0}", u.getUsername());
            
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to update user: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{userId}/password")
    public ResponseEntity<?> changePassword(
            @PathVariable(value = "userId") long id, 
            @RequestBody PasswordChangeRequest request ) {
        logger.log(Level.INFO, "Received request to change password for user ID: {0}", id);
        try {
            this.userService.changePassword(id, request.getOldPassword(), request.getNewPassword());
            logger.log(Level.INFO, "Password change successfully for user ID: {0}", id);
            
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to change password for user: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    static class PasswordChangeRequest {
        private String oldPassword;
        private String newPassword;
        
        public String getOldPassword() { return oldPassword; }
        public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}
