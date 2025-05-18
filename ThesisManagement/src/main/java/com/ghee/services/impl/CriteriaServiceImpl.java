/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services.impl;

import com.ghee.dto.CriteriaRequest;
import com.ghee.dto.CriteriaResponse;
import com.ghee.dto.ThesisUserDTO;
import com.ghee.enums.UserRole;
import com.ghee.pojo.Criteria;
import com.ghee.pojo.Users;
import com.ghee.repositories.CriteriaRepository;
import com.ghee.repositories.UserRepository;
import com.ghee.services.CriteriaService;
import com.ghee.utils.DateUtils;
import com.ghee.validators.UserValidator;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author giahu
 */
@Service
@Transactional
public class CriteriaServiceImpl implements CriteriaService{
    private static final Logger logger = Logger.getLogger(CriteriaServiceImpl.class.getName());

    @Autowired
    private CriteriaRepository criteRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    @Override
    public CriteriaResponse createCriteria(CriteriaRequest dto, String username) {
        logger.log(Level.INFO, "Creating criteria: {0}", dto.getName());
        
        Users createdBy = this.userRepo.getUserByUsername(username);
        UserValidator.checkRole(createdBy, UserRole.ROLE_GIAOVU);
        
        if (dto.getName() == null || dto.getName().trim().isEmpty() || dto.getName().length() > 100) {
            logger.log(Level.WARNING, "Invalid criteria name");
            throw new IllegalArgumentException("Criteria name must be non-empty and at most 100 characters");
        }
        
        if (dto.getMaxScore().compareTo(BigDecimal.ZERO) < 0 || dto.getMaxScore().compareTo(BigDecimal.valueOf(10)) > 0) {
            logger.log(Level.WARNING, "Invalid max score: {0}", dto.getMaxScore());
            throw new IllegalArgumentException("Max score must be between 0 and 10");
        }
        
        Criteria criteria = new Criteria();
        criteria.setName(dto.getName());
        criteria.setMaxScore(dto.getMaxScore());
        criteria.setCreatedBy(createdBy);
        criteria.setCreatedAt(DateUtils.getTodayWithoutTime());
        
        Criteria createdCriteria = this.criteRepo.createOrUpdate(criteria);
        
        logger.log(Level.INFO, "Criteria created successfully: {0}", createdCriteria.getName());
        return mapToResponseDTO(createdCriteria);
    }

    @Override
    public CriteriaResponse updateCriteria(long id, CriteriaRequest dto, String username) {
        logger.log(Level.INFO, "Updating criteria ID: {0}", id);
        
        Users updatedUser = this.userRepo.getUserByUsername(username);
        UserValidator.checkRole(updatedUser, UserRole.ROLE_GIAOVU);
        
        Criteria criteria = this.criteRepo.getCriteriaById(id);
        if (criteria == null) {
            logger.log(Level.WARNING, "Criteria not found: {0}", id);
            throw new IllegalArgumentException("Criteria not found");
        }
        
        if (dto.getName() != null) {
            if (dto.getName().trim().isEmpty() || dto.getName().length() > 100) {
                logger.log(Level.WARNING, "Invalid criteria name");
                throw new IllegalArgumentException("Criteria name must be non-empty and at most 100 characters");
            }
            criteria.setName(dto.getName());
        }
        
        if (dto.getMaxScore() != null) {
            if (dto.getMaxScore().compareTo(BigDecimal.ZERO) < 0 || dto.getMaxScore().compareTo(BigDecimal.valueOf(10)) > 0) {
            logger.log(Level.WARNING, "Invalid max score: {0}", dto.getMaxScore());
            throw new IllegalArgumentException("Max score must be between 0 and 10");
        }
            criteria.setMaxScore(dto.getMaxScore());
        }
        
        Criteria updatedCriteria = this.criteRepo.createOrUpdate(criteria);
        logger.log(Level.INFO, "Criteria updated successfully: {0}", updatedCriteria.getName());
        return mapToResponseDTO(updatedCriteria);
    }

    @Override
    public void deleteCriteria(long id, String username) {
        logger.log(Level.INFO, "Deleting criteria ID: {0}", id);

        Users deletedBy = this.userRepo.getUserByUsername(username);
        UserValidator.checkRole(deletedBy, UserRole.ROLE_GIAOVU);

        Criteria criteria = this.criteRepo.getCriteriaById(id);
        if (criteria == null) {
            logger.log(Level.WARNING, "Criteria not found: {0}", id);
            throw new IllegalArgumentException("Criteria not found");
        }

        if (this.criteRepo.isCriteriaUsed(id)) {
            logger.log(Level.WARNING, "Criteria {0} is in use and cannot be deleted", id);
            throw new IllegalArgumentException("Criteria is in use and cannot be deleted");
        }

        this.criteRepo.deleteCriteria(id);
        logger.log(Level.INFO, "Criteria deleted successfully: {0}", id);
    }

    @Override
    public CriteriaResponse getCriteriaById(long id) {
        Criteria criteria = this.criteRepo.getCriteriaById(id);
        if (criteria == null) {
            logger.log(Level.WARNING, "Criteria not found: {0}", id);
            throw new IllegalArgumentException("Criteria not found");
        }
        return mapToResponseDTO(criteria);
    }
    
    private CriteriaResponse mapToResponseDTO(Criteria criteria) {
        Users createdBy = criteria.getCreatedBy();
        return new CriteriaResponse(
            criteria.getId(),
            criteria.getName(),
            criteria.getMaxScore(),
            new ThesisUserDTO(
                createdBy.getId(),
                createdBy.getFirstname(),
                createdBy.getLastname(),
                createdBy.getEmail(),
                null
            ),
            criteria.getCreatedAt()
        );
    }
}
