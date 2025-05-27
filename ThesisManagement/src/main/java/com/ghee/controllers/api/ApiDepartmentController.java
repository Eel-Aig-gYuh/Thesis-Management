/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.controllers.api;

import com.ghee.enums.UserRole;
import com.ghee.pojo.Users;
import com.ghee.services.DepartmentService;
import com.ghee.services.UserService;
import java.security.Principal;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author giahu
 */
@RestController
@RequestMapping("/api/secure/department")
@CrossOrigin
public class ApiDepartmentController {
    private static final Logger logger = Logger.getLogger(ApiDepartmentController.class.getName());

    @Autowired
    private DepartmentService departService;
    
    @Autowired 
    private UserService userService;
    
    @GetMapping("/") 
    public ResponseEntity<?> getDepartments (
            Principal principal
    ) {
        logger.log(Level.INFO, "Received request to get departments");
        Users u = this.userService.getUserByUsername(principal.getName());
        String role = u.getRole();
        
        if (!role.equals(UserRole.ROLE_GIAOVU.name())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        try {
            Map<String, Object> response = this.departService.getDepartments();
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to get departments: ", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        
    }
    
}
