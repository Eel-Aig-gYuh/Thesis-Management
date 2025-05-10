/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.controllers.api;

import com.ghee.dto.ThesisRequest;
import com.ghee.enums.UserRole;
import com.ghee.pojo.Theses;
import com.ghee.services.ThesisService;
import com.ghee.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
     * @param httpRequest: Việc sử dụng này giúp cho việc lấy thông tin username một cách an toàn do đã có từ khi JWT token được giải mã.
     * @return 
     */
    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestBody ThesisRequest thesisRequest,
            HttpServletRequest httpRequest) {
        logger.log(Level.INFO, "Received request to create thesis: {0}", thesisRequest.getTitle());
        
        // Kiểm tra quyền giáo vụ.
        String username = (String) httpRequest.getAttribute("username");
        if (username == null || !this.userService.getUserByUsername(username).getRole().equals(String.valueOf(UserRole.ROLE_GIAOVU))) {
            logger.log(Level.WARNING, "User {0} is not authorized to create thesis", username);
            return new ResponseEntity<>("Only GIAOVU role can create thesis", HttpStatus.FORBIDDEN);
        }
        
        try {
            Theses thesis = this.thesisService.createThesis(thesisRequest, username);
            logger.log(Level.INFO, "Thesis created successfully: {0}", thesis.getTitle());
            return new ResponseEntity<>(thesis, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create thesis: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
