/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services.impl;

import com.ghee.dto.ScoreRequest;
import com.ghee.dto.ScoreResponse;
import com.ghee.dto.ThesisUserDTO;
import com.ghee.enums.CouncilMemberRole;
import com.ghee.enums.UserRole;
import com.ghee.pojo.Criteria;
import com.ghee.pojo.Scores;
import com.ghee.pojo.Theses;
import com.ghee.pojo.Users;
import com.ghee.repositories.CriteriaRepository;
import com.ghee.repositories.ScoreRepository;
import com.ghee.repositories.ThesisRepository;
import com.ghee.repositories.UserRepository;
import com.ghee.services.ScoreService;
import com.ghee.utils.DateUtils;
import com.ghee.validators.UserValidator;
import java.math.BigDecimal;
import java.util.ArrayList;
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
public class ScoreServiceImpl implements ScoreService{
    private static final Logger logger = Logger.getLogger(ScoreServiceImpl.class.getName());

    @Autowired
    private ScoreRepository scoreRepo;

    @Autowired
    private ThesisRepository thesisRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CriteriaRepository criteriaRepo;
    
    
    @Override
    public ScoreResponse createScore(long id, ScoreRequest dto, String username) {
        logger.log(Level.INFO, "Creating score for thesis ID: {0} by user: {1}", new Object[]{id, username});

        Users councilMember = this.userRepo.getUserByUsername(username);
        UserValidator.checkRole(councilMember, UserRole.ROLE_GIANGVIEN);
        
        Theses thesis = this.thesisRepo.getThesisById(id);
        if (thesis == null) {
            logger.log(Level.WARNING, "Thesis not found: {0}", id);
            throw new IllegalArgumentException("Thesis not found");
        }
        
        boolean isCouncilMember = thesis.getCouncilThesesSet().stream()
            .flatMap(council -> council.getCouncilId().getCouncilMembersSet().stream())
            .anyMatch(member -> member.getMemberId().getId().equals(councilMember.getId()) &&
                (member.getRole().equals(CouncilMemberRole.REVIEWER.name()) || member.getRole().equals(CouncilMemberRole.MEMBER.name())));
        if (!isCouncilMember) {
            logger.log(Level.WARNING, "User {0} is not a REVIEWER or MEMBER of the council for thesis {1}",
                new Object[]{username, id});
            throw new IllegalArgumentException("User must be a REVIEWER or MEMBER of the council");
        }
        
        List<Scores> scores = new ArrayList<>();
        List<ScoreResponse.CriteriaScoreDTO> criteriaScoreDTOs = new ArrayList<>();

        for (ScoreRequest.CriteriaScoreDTO criteriaScoreDTO : dto.getCriteriaScores()) {
            Criteria criteria = this.criteriaRepo.getCriteriaById(criteriaScoreDTO.getCriteriaId());
            if (criteria == null) {
                logger.log(Level.WARNING, "Criteria not found: {0}", criteriaScoreDTO.getCriteriaId());
                throw new IllegalArgumentException("Criteria not found: " + criteriaScoreDTO.getCriteriaId());
            }

            BigDecimal currScore = criteriaScoreDTO.getScore();
            BigDecimal maxScore = criteria.getMaxScore();
            
            if (currScore.compareTo(BigDecimal.ZERO) < 0 || currScore.compareTo(maxScore) > 0) {
                logger.log(Level.WARNING, "Invalid score {0} for criteria {1}, max score is {2}",
                    new Object[]{criteriaScoreDTO.getScore(), criteria.getName(), criteria.getMaxScore()});
                throw new IllegalArgumentException("Score for criteria " + criteria.getName() +
                    " must be between 0 and " + criteria.getMaxScore());
            }

            Scores score = new Scores();
            score.setThesisId(thesis);
            score.setCouncilMemberId(councilMember);
            score.setCriteriaId(criteria);
            score.setScore(criteriaScoreDTO.getScore());
            score.setCreatedAt(DateUtils.getTodayWithoutTime());
            scores.add(score);

            criteriaScoreDTOs.add(new ScoreResponse.CriteriaScoreDTO(
                criteria.getId(),
                criteria.getName(),
                criteria.getMaxScore(),
                criteriaScoreDTO.getScore()
            ));
        }

        List<Scores> createdScores = this.scoreRepo.createOrUpdateScores(scores);
        logger.log(Level.INFO, "Scores created successfully for thesis ID: {0}", id);
        
        // Cập nhật lại average score
        this.thesisRepo.updateAverageScore(thesis.getId());
        
        return mapToResponseDTO(createdScores);
    }
    
    
    private ScoreResponse mapToResponseDTO(List<Scores> scores) {
        if (scores == null || scores.isEmpty()) {
            return null;
        }
        
        Scores firstScore = scores.get(0);
        Users councilMember = firstScore.getCouncilMemberId();

        List<ScoreResponse.CriteriaScoreDTO> criteriaScoreDTOs = scores.stream()
            .map(score -> new ScoreResponse.CriteriaScoreDTO(
                score.getCriteriaId().getId(),
                score.getCriteriaId().getName(),
                score.getCriteriaId().getMaxScore(),
                score.getScore()
            ))
            .collect(Collectors.toList());

        return new ScoreResponse(
            firstScore.getId(),
            new ThesisUserDTO(
                councilMember.getId(),
                councilMember.getFirstname(),
                councilMember.getLastname(),
                councilMember.getEmail(),
                null
            ),
            criteriaScoreDTOs,
            firstScore.getCreatedAt()
        );
    }
}
