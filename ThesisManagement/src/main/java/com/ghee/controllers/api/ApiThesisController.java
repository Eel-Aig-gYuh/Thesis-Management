/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.controllers.api;

import com.ghee.dto.ThesisRequest;
import com.ghee.dto.ThesisResponse;
import com.ghee.enums.UserRole;
import com.ghee.pojo.Theses;
import com.ghee.services.ThesisService;
import com.ghee.services.UserService;
import com.ghee.validators.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/secure/thesis")
@CrossOrigin
public class ApiThesisController {
    private static final Logger logger = Logger.getLogger(ApiThesisController.class.getName());
    
    @Autowired
    private ThesisService thesisService;
    
    @Autowired
    private UserService userService;
    
    
    /**
     * Hàm này dùng để tạo khóa luận
     * @param thesisRequest
     * @param principal
     * @return 
     */
    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestBody ThesisRequest thesisRequest,
            Principal principal) {
        logger.log(Level.INFO, "Received request to create thesis: {0}", thesisRequest.getTitle());
        
        // Kiểm tra quyền giáo vụ.
        String username = principal.getName();
        if (username == null || !this.userService.getUserByUsername(username).getRole().equals(String.valueOf(UserRole.ROLE_GIAOVU))) {
            logger.log(Level.WARNING, "User {0} is not authorized to create thesis", username);
            return new ResponseEntity<>("Only GIAOVU role can create thesis", HttpStatus.FORBIDDEN);
        }
        
        try {
            ThesisResponse thesis = this.thesisService.createThesis(thesisRequest, username);
            logger.log(Level.INFO, "Thesis created successfully: {0}", thesis.getTitle());
            return new ResponseEntity<>(thesis, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create thesis: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/")
    public ResponseEntity<?> getTheses(
            @RequestParam(name = "page", defaultValue = "1") int page, 
            Principal principal) {
        logger.log(Level.INFO, "Received request to get theses, page: {0}", page);
        
        String username = principal.getName();
        if (username == null || !this.userService.getUserByUsername(username).getRole().equals(String.valueOf(UserRole.ROLE_GIAOVU))) {
            logger.log(Level.WARNING, "User {0} is not authorized to view thesis", username);
            return new ResponseEntity<>("Only GIAOVU role can view thesis", HttpStatus.FORBIDDEN);
        }
        
        try {
            Map<String, String> params = new HashMap<>();
            params.put("page", String.valueOf(page));
            
            Map<String, Object> response = this.thesisService.getThese(params);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to get these: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getThesisById(
            @PathVariable(value = "id") long id, 
            Principal principal) {
        logger.log(Level.INFO, "Received request to get thesis ID: {0}", id);
        
        String username = principal.getName();
        if (username == null || !this.userService.getUserByUsername(username).getRole().equals(String.valueOf(UserRole.ROLE_GIAOVU))) {
            logger.log(Level.WARNING, "User {0} is not authorized to view thesis", username);
            return new ResponseEntity<>("Only GIAOVU role can view thesis", HttpStatus.FORBIDDEN);
        }
        
        try {
            ThesisResponse response = this.thesisService.getThesisById(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to get thesis: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable(value = "id") long id, 
            @RequestBody ThesisRequest dto, 
            Principal principal) {
        logger.log(Level.INFO, "Received request to update thesis ID: {0}", id);
        
        String username = principal.getName();
        if (username == null || !this.userService.getUserByUsername(username).getRole().equals(String.valueOf(UserRole.ROLE_GIAOVU))) {
            logger.log(Level.WARNING, "User {0} is not authorized to create thesis", username);
            return new ResponseEntity<>("Only GIAOVU role can update thesis", HttpStatus.FORBIDDEN);
        }
        
        try {
            ThesisResponse response = this.thesisService.updateThesis(id, dto, username);
            logger.log(Level.INFO, "Thesis updated successfully: {0}", response.getTitle());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to update thesis: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable(value = "id") long id, 
            Principal principal) {
        logger.log(Level.INFO, "Received request to delete thesis ID: {0}", id);
        
        String username = principal.getName();
        if (username == null || !this.userService.getUserByUsername(username).getRole().equals(String.valueOf(UserRole.ROLE_GIAOVU))) {
            logger.log(Level.WARNING, "User {0} is not authorized to create thesis", username);
            return new ResponseEntity<>("Only GIAOVU role can update thesis", HttpStatus.FORBIDDEN);
        }
        
        try {
            this.thesisService.deleteThesis(id, username);
            logger.log(Level.INFO, "Thesis deleted successfully: {0}", id);
            return new ResponseEntity<>("Thesis deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to delete thesis: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
