/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services.impl;

import com.ghee.dto.CriteriaDTO;
import com.ghee.dto.CriteriaRequest;
import com.ghee.dto.CriteriaResponse;
import com.ghee.dto.ThesisUserDTO;
import com.ghee.enums.UserRole;
import com.ghee.pojo.Criteria;
import com.ghee.pojo.Theses;
import com.ghee.pojo.ThesisCriteria;
import com.ghee.pojo.Users;
import com.ghee.repositories.CriteriaRepository;
import com.ghee.repositories.ThesisRepository;
import com.ghee.repositories.UserRepository;
import com.ghee.services.CriteriaService;
import com.ghee.utils.DateUtils;
import com.ghee.validators.UserValidator;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
    
    @Autowired
    private ThesisRepository thesisRepo;
    
    @Override
    public CriteriaResponse createCriteria(CriteriaRequest dto, String username) {
        logger.log(Level.INFO, "Creating criteria for theses: {0}", dto.getThesisId());

        // Kiểm tra người dùng
        Users user = userRepo.getUserByUsername(username);
        UserValidator.checkRole(user, UserRole.ROLE_GIAOVU);

        // Kiểm tra createdBy
        if (dto.getCreatedBy() == null || dto.getCreatedBy().size() != 1) {
            logger.log(Level.WARNING, "Invalid createdBy: {0}", dto.getCreatedBy());
            throw new IllegalArgumentException("Exactly one createdBy ID is required");
        }
        Long createdById = dto.getCreatedBy().get(0);
        Users createdBy = userRepo.getUserById(createdById);
        if (createdBy == null) {
            logger.log(Level.WARNING, "CreatedBy user not found: {0}", createdById);
            throw new IllegalArgumentException("CreatedBy user not found");
        }

        // Kiểm tra thesisId
        List<Theses> theses = this.thesisRepo.findByIds(dto.getThesisId());
        if (theses.size() != dto.getThesisId().size()) {
            logger.log(Level.WARNING, "Some thesis IDs not found: {0}", dto.getThesisId());
            throw new IllegalArgumentException("One or more theses not found");
        }

        // Kiểm tra tiêu chí
        if (dto.getCriterias() == null || dto.getCriterias().isEmpty()) {
            logger.log(Level.WARNING, "No criteria provided");
            throw new IllegalArgumentException("At least one criteria is required");
        }

        for (CriteriaDTO scoreDTO : dto.getCriterias()) {
            if (scoreDTO.getCriteriaName() == null || scoreDTO.getCriteriaName().trim().isEmpty() || scoreDTO.getCriteriaName().length() > 100) {
                logger.log(Level.WARNING, "Invalid criteria name: {0}", scoreDTO.getCriteriaName());
                throw new IllegalArgumentException("Criteria name must be non-empty and at most 100 characters");
            }
            if (scoreDTO.getMaxScore() == null || scoreDTO.getMaxScore().compareTo(BigDecimal.ZERO) <= 0 || scoreDTO.getMaxScore().compareTo(BigDecimal.TEN) > 0) {
                logger.log(Level.WARNING, "Invalid max score: {0}", scoreDTO.getMaxScore());
                throw new IllegalArgumentException("Max score must be between 0 and 10");
            }
        }

        // Kiểm tra tổng maxScore
        BigDecimal totalMaxScore = dto.getCriterias().stream()
                .map(CriteriaDTO::getMaxScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalMaxScore.compareTo(BigDecimal.TEN) != 0) {
            logger.log(Level.WARNING, "Total maxScore is {0}, must be 10", totalMaxScore);
            throw new IllegalArgumentException("Total maxScore must equal 10");
        }

        // Lưu tiêu chí
        List<Criteria> savedCriteria = dto.getCriterias().stream().map(dtos -> {
            Criteria criteria = new Criteria();
            if (dtos.getCriteriaId() != null) {
                criteria.setId(dtos.getCriteriaId());
            }
            criteria.setName(dtos.getCriteriaName());
            criteria.setMaxScore(dtos.getMaxScore());
            criteria.setCreatedBy(createdBy);
            criteria.setCreatedAt(DateUtils.getTodayWithoutTime());
            return criteRepo.createOrUpdate(criteria);
        }).collect(Collectors.toList());

        // Liên kết tiêu chí với khóa luận
        for (Theses thesis : theses) {
            // Khởi tạo thesisCriteriaSet
            for (Criteria criteria : savedCriteria) {
                // Kiểm tra trùng lặp
                boolean exists = thesis.getThesisCriteriasSet().stream()
                        .anyMatch(tc -> tc.getCriteriaId() != null && tc.getCriteriaId().getId().equals(criteria.getId()));
                if (exists) {
                    logger.log(Level.WARNING, "Criteria {0} already assigned to thesis {1}", new Object[]{criteria.getId(), thesis.getId()});
                    continue;
                }
                ThesisCriteria thesisCriteria = new ThesisCriteria();
                thesisCriteria.setThesisId(thesis);
                thesisCriteria.setCriteriaId(criteria);
                thesis.getThesisCriteriasSet().add(thesisCriteria);
            }
            this.thesisRepo.createOrUpdate(thesis);
        }

        // Chuẩn bị response
        List<CriteriaDTO> criteriaDTOs = savedCriteria.stream().map(c -> {
            CriteriaDTO dtos = new CriteriaDTO();
            dtos.setCriteriaId(c.getId());
            dtos.setCriteriaName(c.getName());
            dtos.setMaxScore(c.getMaxScore());
            return dtos;
        }).collect(Collectors.toList());

        CriteriaResponse data = new CriteriaResponse();
        data.setCriterias(criteriaDTOs);
        data.setThesisIds(dto.getThesisId());

        logger.log(Level.INFO, "Criteria created successfully for theses: {0}", dto.getThesisId());
        return data;
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
    public Criteria getCriteriaById(long id) {
        Criteria criteria = this.criteRepo.getCriteriaById(id);
        if (criteria == null) {
            logger.log(Level.WARNING, "Criteria not found: {0}", id);
            throw new IllegalArgumentException("Criteria not found");
        }
        return criteria;
    }
   
}
