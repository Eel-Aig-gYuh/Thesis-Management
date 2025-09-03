/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.controllers.api;

import com.ghee.enums.UserRole;
import com.ghee.pojo.Users;
import com.ghee.services.UserService;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/secure")
@CrossOrigin
public class ApiUserController {
    private static final Logger logger = Logger.getLogger(ApiUserController.class.getName());
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/users/")
    public ResponseEntity<?> getUsers( 
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "role", required = false) String role,
            @RequestParam(name = "firstname", required = false) String firstname,
            @RequestParam(name = "lastname", required = false) String lastname,
            Principal principal) {
        logger.log(Level.INFO, "Received request to get user, page: {0}", page);
        
        String username = principal.getName();
        if (username == null || !this.userService.getUserByUsername(username).getRole().equals(String.valueOf(UserRole.ROLE_GIAOVU))) {
            logger.log(Level.WARNING, "User {0} is not authorized to view user ", username);
            return new ResponseEntity<>("Only GIAOVU role can view user ", HttpStatus.FORBIDDEN);
        }
        
        try {
            Map<String, String> params = new HashMap<>();
            params.put("page", String.valueOf(page));
            if (role != null && !role.isEmpty()) params.put("role", role);
            if (firstname != null && !firstname.isEmpty()) params.put("firstname", firstname);
            if (lastname != null && !lastname.isEmpty()) params.put("lastname", lastname);
            
            Map<String, Object> response = this.userService.getAllUsers(params);
            logger.log(Level.INFO, "User created successfully: {0}");
        
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to get user: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/users/create")
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

    @PostMapping(path = "/users/upload-avatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadAvatar(
            @RequestParam(value="avatar") MultipartFile avatar,
            Principal principal
    ) {
        logger.log(Level.INFO, "Received request to upload avatar: {0}");
        
        Users u = this.userService.getUserByUsername(principal.getName());
        if (u == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            Users response = this.userService.uploadAvatar(u.getId(), avatar);
            logger.log(Level.INFO, "User created successfully: {0}", u.getUsername());
        
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create user: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/users/{userId}")
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
    
    @PutMapping("/users/{userId}")
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
    
    /**
     *
     * @param id
     * @param request
     * @param principal
     * @return
     */
    @PutMapping("/users/{userId}/password")
    public ResponseEntity<?> changePassword(
            @PathVariable(value = "userId") long id, 
            @RequestBody PasswordChangeRequest request,
            Principal principal) {
        logger.log(Level.INFO, "Received request to change password for user ID: {0}", id);
        String username = principal.getName();
        System.out.println(username);
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
