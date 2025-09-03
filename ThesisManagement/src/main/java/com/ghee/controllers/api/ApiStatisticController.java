/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.controllers.api;

import com.ghee.dto.StatisticsResponse;
import com.ghee.enums.UserRole;
import com.ghee.services.StatisticService;
import com.ghee.services.UserService;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author giahu
 */
@RestController
@RequestMapping("/api/secure/statistic")
@CrossOrigin
public class ApiStatisticController {
    private static final Logger logger = Logger.getLogger(ApiStatisticController.class.getName());
    
    @Autowired
    private StatisticService statsService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/theses")
    public ResponseEntity<?> getThesisStatistics(
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String major,
            Principal principal) {
        logger.log(Level.INFO, "Received request to get thesis statistics, year: {0}, department: {1}", new Object[]{year, major});

        String username = principal.getName();
        if (username == null || (!userService.getUserByUsername(username).getRole().equals(UserRole.ROLE_GIAOVU.name())  &&
                                 !userService.getUserByUsername(username).getRole().equals(UserRole.ROLE_ADMIN.name()))) {
            logger.log(Level.WARNING, "User {0} is not authorized to view statistics", username);
            return new ResponseEntity<>("Only GIAOVU or ADMIN role can view statistics", HttpStatus.FORBIDDEN);
        }

        try {
            StatisticsResponse statistics = this.statsService.getThesisStatistics(year, major, username);
            logger.log(Level.INFO, "Thesis statistics retrieved successfully");
            return new ResponseEntity<>(statistics, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to get thesis statistics: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
