/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.controllers.api;

import com.ghee.dto.CouncilResponse;
import com.ghee.dto.CouncilResquest;
import com.ghee.enums.UserRole;
import com.ghee.pojo.Users;
import com.ghee.services.CouncilService;
import com.ghee.services.UserService;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author giahu
 */
@RestController
@RequestMapping("/api/secure/council")
@CrossOrigin
public class ApiCouncilController {
    private static final Logger logger = Logger.getLogger(ApiCouncilController.class.getName());
    
    @Autowired
    private CouncilService councilService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/create")
    public ResponseEntity<?> create (
            @RequestBody CouncilResquest dto, 
            Principal principal) {
        logger.log(Level.INFO, "Received request to create council: {0}", dto.getName());
        
        String username = principal.getName();
        if (username == null || !this.userService.getUserByUsername(username).getRole().equals(String.valueOf(UserRole.ROLE_GIAOVU))) {
            logger.log(Level.WARNING, "User {0} is not authorized to create council", username);
            return new ResponseEntity<>("Only GIAOVU role can create council", HttpStatus.FORBIDDEN);
        }
        
        try {
            CouncilResponse response = this.councilService.createCouncil(dto, username);
            logger.log(Level.INFO, "Council created successfully: {0}", response.getName());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create council: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/{id}/lock") 
    public ResponseEntity<?> lockCouncil(
            @PathVariable(value = "id") long id, 
            Principal principal) {
        logger.log(Level.INFO, "Received request to lock council ID: {0}", id);

        String username = principal.getName();
        if (username == null || !userService.getUserByUsername(username).getRole().equals(UserRole.ROLE_GIAOVU.name())) {
            logger.log(Level.WARNING, "User {0} is not authorized to lock council", username);
            return new ResponseEntity<>("Only GIAOVU role can lock council", HttpStatus.FORBIDDEN);
        }

        try {
            CouncilResponse response = councilService.lockCouncil(id, username);
            logger.log(Level.INFO, "Council locked successfully: {0}", id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to lock council: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable(value = "id") long id, 
            @RequestBody CouncilResquest dto, 
            Principal principal) {
        logger.log(Level.INFO, "Received request to update council ID: {0}", id);
        
        String username = principal.getName();
        if (username == null || !userService.getUserByUsername(username).getRole().equals(UserRole.ROLE_GIAOVU.name())) {
            logger.log(Level.WARNING, "User {0} is not authorized to update council", username);
            return new ResponseEntity<>("Only GIAOVU role can update council", HttpStatus.FORBIDDEN);
        }
        
        try {
            CouncilResponse response = this.councilService.updateCouncil(id, dto, username);
            logger.log(Level.INFO, "Council updated successfully: {0}", response.getName());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to update council: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete (
            @PathVariable(value = "id") long id, 
            Principal principal) {
        logger.log(Level.INFO, "Received request to delete council ID: {0}", id);
        
        String username = principal.getName();
        if (username == null || !userService.getUserByUsername(username).getRole().equals(UserRole.ROLE_GIAOVU.name())) {
            logger.log(Level.WARNING, "User {0} is not authorized to delete council", username);
            return new ResponseEntity<>("Only GIAOVU role can delete council", HttpStatus.FORBIDDEN);
        }
        
        try {
            this.councilService.deleteCouncil(id, username);
            logger.log(Level.INFO, "Council deleted successfully: {0}", id);
            return new ResponseEntity<>("Council deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to delete council: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/")
    public ResponseEntity<?> getCouncils(
            @RequestParam(name = "page", defaultValue = "1") int page, 
            Principal principal) {
        logger.log(Level.INFO, "Received request to get councils, page: {0}", page);
        
        String username = principal.getName();
        if (username == null || !userService.getUserByUsername(username).getRole().equals(UserRole.ROLE_GIAOVU.name())) {
            logger.log(Level.WARNING, "User {0} is not authorized to view councils", username);
            return new ResponseEntity<>("Only GIAOVU role can view councils", HttpStatus.FORBIDDEN);
        }
        
        try {
            Map<String, String> params = new HashMap<>();
            params.put("page", String.valueOf(page));
            
            Map<String, Object> response = this.councilService.getCouncils(params);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to get these: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/my-council/")
    public ResponseEntity<?> getMyCouncils(
            @RequestParam(name = "page", defaultValue = "1") int page, 
            Principal principal) {
        logger.log(Level.INFO, "Received request to get councils, page: {0}", page);
        
        String username = principal.getName();
        Users u = this.userService.getUserByUsername(username);
        if (username == null || !userService.getUserByUsername(username).getRole().equals(UserRole.ROLE_GIANGVIEN.name())) {
            logger.log(Level.WARNING, "User {0} is not authorized to view councils", username);
            return new ResponseEntity<>("Only GIANGVIEN role can view councils", HttpStatus.FORBIDDEN);
        }
        
        try {
            Map<String, String> params = new HashMap<>();
            params.put("page", String.valueOf(page));
            
            Map<String, Object> response = this.councilService.getMyCouncils(u.getId(), params);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to get these: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getCouncilById(
            @PathVariable(value = "id") long id, 
            Principal principal) {
        logger.log(Level.INFO, "Received request to get council ID: {0}", id);
        
        String username = principal.getName();
        
        try {
            CouncilResponse response = this.councilService.getCouncilById(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Failed to get council: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    
    
}
