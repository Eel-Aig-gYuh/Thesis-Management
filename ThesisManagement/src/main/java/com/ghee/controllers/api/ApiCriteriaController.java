/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.controllers.api;

import com.ghee.dto.CriteriaRequest;
import com.ghee.dto.CriteriaResponse;
import com.ghee.enums.UserRole;
import com.ghee.services.CriteriaService;
import com.ghee.services.UserService;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author giahu
 */
@RestController
@RequestMapping("/api/secure/criteria")
@CrossOrigin
public class ApiCriteriaController {
    private static final Logger logger = Logger.getLogger(ApiCriteriaController.class.getName());
    
    @Autowired
    private CriteriaService criteService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestBody CriteriaRequest dto, 
            Principal principal) {
        logger.log(Level.INFO, "Received request to create criteria: {0}", dto.getName());
        
        String username = principal.getName();
        if (username == null || !userService.getUserByUsername(username).getRole().equals(UserRole.ROLE_GIAOVU.name())) {
            logger.log(Level.WARNING, "User {0} is not authorized to create criteria", username);
            return new ResponseEntity<>("Only GIAOVU role can create criteria", HttpStatus.FORBIDDEN);
        }

        try {
            CriteriaResponse criteria = this.criteService.createCriteria(dto, username);
            logger.log(Level.INFO, "Criteria created successfully: {0}", criteria.getName());
            return new ResponseEntity<>(criteria, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create criteria: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable(value = "id") long id, 
            @RequestBody CriteriaRequest dto, 
            Principal principal
            ) {
        logger.log(Level.INFO, "Received request to create criteria: {0}", dto.getName());
        
        String username = principal.getName();
        if (username == null || !userService.getUserByUsername(username).getRole().equals(UserRole.ROLE_GIAOVU.name())) {
            logger.log(Level.WARNING, "User {0} is not authorized to update criteria", username);
            return new ResponseEntity<>("Only GIAOVU role can update criteria", HttpStatus.FORBIDDEN);
        }
        
        try {
            CriteriaResponse criteria = this.criteService.updateCriteria(id, dto, username);
            logger.log(Level.INFO, "Criteria updated successfully: {0}", criteria.getName());
            return new ResponseEntity<>(criteria, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to update criteria: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable(value = "id") long id, 
            Principal principal) {
        logger.log(Level.INFO, "Received request to delete criteria ID: {0}", id);
        
        String username = principal.getName();
        if (username == null || !userService.getUserByUsername(username).getRole().equals(UserRole.ROLE_GIAOVU.name())) {
            logger.log(Level.WARNING, "User {0} is not authorized to delete criteria", username);
            return new ResponseEntity<>("Only GIAOVU role can delete criteria", HttpStatus.FORBIDDEN);
        }
        
        try {
            this.criteService.deleteCriteria(id, username);
            logger.log(Level.INFO, "Criteria deleted successfully: {0}", id);
            return new ResponseEntity<>("Criteria deleted successfully", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to delete criteria: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getCriteriaById(
            @PathVariable(value = "id") long id, 
            Principal principal) {
        logger.log(Level.INFO, "Received request to get criteria ID: {0}", id);
        
        String username = principal.getName();
        if (username == null || !userService.getUserByUsername(username).getRole().equals(UserRole.ROLE_GIAOVU.name())) {
            logger.log(Level.WARNING, "User {0} is not authorized to view criteria", username);
            return new ResponseEntity<>("Only GIAOVU role can view criteria", HttpStatus.FORBIDDEN);
        }
        
        try {
            CriteriaResponse response = this.criteService.getCriteriaById(id);
            logger.log(Level.INFO, "Criteria deleted successfully: {0}", response.getName());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to get criteria: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
