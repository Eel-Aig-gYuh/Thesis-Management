/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.controllers.api;

import com.ghee.dto.CriteriaResponse;
import com.ghee.dto.ScoringCriteriaResponse;
import com.ghee.dto.ScoringDTO;
import com.ghee.dto.ScoringRequest;
import com.ghee.dto.ScoringResponse;
import com.ghee.dto.StatisticDTO;
import com.ghee.enums.UserRole;
import com.ghee.pojo.Users;
import com.ghee.repositories.ScoreRepository;
import com.ghee.services.ScoreService;
import com.ghee.services.UserService;
import com.ghee.utils.ReportUtils;
import java.io.File;
import java.nio.file.Files;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author giahu
 */
@RestController
@RequestMapping("/api/secure/score")
@CrossOrigin
public class ApiScoreController {

    private static final Logger logger = Logger.getLogger(ApiScoreController.class.getName());

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private UserService userService;

    @PostMapping("/prepare")
    public ResponseEntity<?> getScoringCriteria(
            @RequestBody ScoringDTO request,
            Principal principal) {
        Users u = this.userService.getUserByUsername(principal.getName());

        try {
            Map<String, Object> response = this.scoreService.getScoringCriteria(request.getCouncilId(), request.getThesisIds(), u.getId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to get criteria thesis score: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/submit-score")
    public ResponseEntity<?> submitScores(
            @PathVariable(value = "id") long councilId,
            @RequestBody ScoringRequest request,
            Principal principal) {
        try {
            ScoringResponse response = this.scoreService.submitScores(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to submitted score: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/statistic/scores")
    public ResponseEntity<?> getThesisScoreStatistics(
            @RequestBody StatisticDTO.StatisticRequest request,
            Principal principal
    ) {
        Users u = this.userService.getUserByUsername(principal.getName());
        String role = u.getRole();

        if (!(role.equals(UserRole.ROLE_GIAOVU.name()) || role.equals(UserRole.ROLE_ADMIN.name()))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            List<StatisticDTO.ThesisScoreStatsResponse> response = scoreService.getThesisScoreStatistics(request.getYearStart(), request.getYearEnd());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create criteria: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/statistic/participation")
    public ResponseEntity<?> getThesisParticipationByDepartment(
            @RequestBody StatisticDTO.StatisticRequest request,
            Principal principal) {
        Users u = this.userService.getUserByUsername(principal.getName());
        String role = u.getRole();

        if (!(role.equals(UserRole.ROLE_GIAOVU.name()) || role.equals(UserRole.ROLE_ADMIN.name()))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            List<StatisticDTO.ThesisParticipationResponse> response = scoreService.getThesisParticipationByDepartment(request.getYearStart(), request.getYearEnd());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create criteria: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/report/average-scores/")
    public ResponseEntity<?> generateAverageScoresPdf(
            @RequestBody StatisticDTO.StatisticRequest request,
            Principal principal
    ) {
        Users u = this.userService.getUserByUsername(principal.getName());
        String role = u.getRole();

        if (!(role.equals(UserRole.ROLE_GIAOVU.name()) || role.equals(UserRole.ROLE_ADMIN.name()))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            byte[] pdfBytes = this.scoreService.generateAverageScoresPdf(request.getYear());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "average_scores_" + request.getYear() + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create criteria: {0}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
