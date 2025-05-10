/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.controllers.api;

import com.ghee.pojo.Users;
import com.ghee.services.UserService;
import java.security.Principal;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author giahu
 */
@RestController
@RequestMapping("api")
@CrossOrigin
public class ApiAuthController {
    private static final Logger logger = Logger.getLogger(ApiAuthController.class.getName());
    
    @Autowired
    private UserService userService;
    
    @RequestMapping("/secure/profile")
    @ResponseBody
    public ResponseEntity<Users> getProfile(Principal principal) {
        return new ResponseEntity<>(this.userService.getUserByUsername(principal.getName()), HttpStatus.OK);
    }
    
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(
            @RequestParam(value = "username") String username, 
            @RequestParam(value = "password") String password) {
        logger.log(Level.INFO, "Received login request for username{0}", username);
        try {
            String token = this.userService.login(username, password);
            logger.log(Level.INFO, "Login successful for username: {0}", username);
            
            return ResponseEntity.ok().body(Collections.singletonMap("token", token));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Login failed: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    
    static class LoginRequest {
        private String username;
        private String password;
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
