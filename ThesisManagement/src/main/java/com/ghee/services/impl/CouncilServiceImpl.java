/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services.impl;

import com.ghee.dto.CouncilMemberDTO;
import com.ghee.dto.CouncilMemberResponseDTO;
import com.ghee.dto.CouncilResponse;
import com.ghee.dto.CouncilResquest;
import com.ghee.dto.CouncilThesisDTO;
import com.ghee.dto.ThesisUserDTO;
import com.ghee.enums.CouncilMemberRole;
import com.ghee.enums.CouncilStatus;
import com.ghee.enums.ThesisStatus;
import com.ghee.enums.UserRole;
import com.ghee.pojo.CouncilMembers;
import com.ghee.pojo.CouncilTheses;
import com.ghee.pojo.Councils;
import com.ghee.pojo.Theses;
import com.ghee.pojo.ThesisStudents;
import com.ghee.pojo.Users;
import com.ghee.repositories.CouncilRepository;
import com.ghee.repositories.ThesisRepository;
import com.ghee.repositories.UserRepository;
import com.ghee.services.CouncilService;
import com.ghee.services.MailService;
import com.ghee.services.NotificationService;
import com.ghee.utils.DateUtils;
import com.ghee.validators.UserValidator;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author giahu
 */
@Service
@Transactional
@PropertySource("classpath:application.properties")
public class CouncilServiceImpl implements CouncilService {
    
    private static final Logger logger = Logger.getLogger(CouncilServiceImpl.class.getName());
    
    @Autowired
    private Environment env;
    
    @Autowired
    private CouncilRepository councilRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private ThesisRepository thesisRepo;
    
    @Autowired
    private NotificationService notiService;
    
    @Autowired
    private MailService mailService;
    
    @Override
    public CouncilResponse getCouncilById(long id) {
        Councils council = this.councilRepo.getCouncilById(id);
        if (council == null) {
            logger.log(Level.WARNING, "Council not found: {0}", id);
            throw new IllegalArgumentException("Council not found");
        }
        return mapToResponseDTO(council);
    }
    
    @Override
    public Map<String, Object> getCouncils(Map<String, String> params) {
        Map<String, Object> result = this.councilRepo.getCouncils(params);
        List<Councils> councils = (List<Councils>) result.get("councils");
        result.put("councils", councils.stream().map(this::mapToResponseDTO).collect(Collectors.toList()));

        return result;
    }
    
    @Override
    public CouncilResponse createCouncil(CouncilResquest dto, String username) {
        logger.log(Level.INFO, "Creating council with name: {0}", dto.getName());
        int maxCouncilMember = Integer.parseInt(env.getProperty("MAX_COUNCIL_MEMBER"));
        
        Users createdBy = this.userRepo.getUserByUsername(username);
        UserValidator.checkRole(createdBy, UserRole.ROLE_GIAOVU);
        
        if (dto.getMembers().size() > maxCouncilMember) {
            logger.log(Level.WARNING, "Council cannot have more than {0} members", maxCouncilMember);
            throw new IllegalArgumentException("Maximum " + maxCouncilMember + " members allowed");
        }
        
        long chairCount = dto.getMembers().stream().filter(m -> m.getRole().equals(CouncilMemberRole.CHAIRMAN)).count();
        long secretaryCount = dto.getMembers().stream().filter(m -> m.getRole().equals(CouncilMemberRole.SECRETARY)).count();
        
        if (chairCount != 1 || secretaryCount != 1) {
            logger.log(Level.WARNING, "Council must have exactly one CHAIR and one SECRETARY");
            throw new IllegalArgumentException("Council must have exactly one CHAIR and one SECRETARY");
        }
        
        if (dto.getDefenseDate().before(new Date())) {
            logger.log(Level.WARNING, "Defense date must be in the future");
            throw new IllegalArgumentException("Defense date must be in the future");
        }
        
        if (dto.getDefenseLocation() == null || dto.getDefenseLocation().trim().isEmpty() || dto.getDefenseLocation().length() > 255) {
            logger.log(Level.WARNING, "Invalid defense location");
            throw new IllegalArgumentException("Defense location must be non-empty and at most 255 characters");
        }
        
        // Tạo hội đồng.
        Councils council = new Councils();
        council.setName(dto.getName());
        council.setDefenseDate(dto.getDefenseDate());
        council.setDefenseLocation(dto.getDefenseLocation());
        council.setStatus(dto.getStatus() != null ? dto.getStatus().name(): CouncilStatus.SCHEDULED.name());
        council.setCreatedBy(createdBy);
        council.setCreatedAt(DateUtils.getTodayWithoutTime());
        
        Set<CouncilMembers> members = new HashSet<>();
        List<Users> recipents = new ArrayList<>();
        recipents.add(createdBy);
        
        for (CouncilMemberDTO memberDTO: dto.getMembers()) {
            Users user = this.userRepo.getUserById(memberDTO.getUserId());
            UserValidator.checkRole(user, UserRole.ROLE_GIANGVIEN);
            
            CouncilMembers member = new CouncilMembers();
            member.setCouncilId(council);
            member.setMemberId(user);
            member.setRole(memberDTO.getRole().name());
            
            members.add(member);
            recipents.add(user);
        }
        council.setCouncilMembersSet(members);
        
        Set<CouncilTheses> councilTheses = new HashSet<>();
        for (Long thesisId: dto.getThesisIds()) {
            Theses thesis = this.thesisRepo.getThesisById(thesisId);
            if (thesis == null) {
                logger.log(Level.WARNING, "Thesis not found: {0}", thesisId);
                throw new IllegalArgumentException("Thesis not found: " + thesisId);
            }
            if (!thesis.getStatus().equals(ThesisStatus.APPROVED.name())) {
                logger.log(Level.WARNING, "Thesis {0} is not in APPROVED status", thesisId);
                throw new IllegalArgumentException("Thesis must be in APPROVED status: " + thesisId);
            }
            
            Set<Long> supervisorIds = thesis.getThesisAdvisorsSet().stream()
                    .map(assignment -> assignment.getAdvisorId().getId())
                    .collect(Collectors.toSet());
            
            Set<Long> reviewerIds = thesis.getThesisReviewersSet().stream()
                    .map(assignment -> assignment.getReviewerId().getId())
                    .collect(Collectors.toSet());
            
            for (CouncilMemberDTO memberDTO : dto.getMembers()) {
                if (supervisorIds.contains(memberDTO.getUserId()) || reviewerIds.contains(memberDTO.getUserId())) {
                    logger.log(Level.WARNING, "User ID {0} is already a supervisor or reviewer for thesis {1}",
                        new Object[]{memberDTO.getUserId(), thesisId});
                    throw new IllegalArgumentException("Council member cannot be a supervisor or reviewer for thesis: " + thesisId);
                }
            }
            
            CouncilTheses councilThesis = new CouncilTheses();
            councilThesis.setCouncilId(council);
            councilThesis.setThesisId(thesis);
            councilTheses.add(councilThesis);
        }
        council.setCouncilThesesSet(councilTheses);
        
        Councils createdCouncil = this.councilRepo.createOrUpdateCouncil(council);
        
        this.notiService.sendBulkNotification(recipents, 
            String.format("Bạn được phân công vào hội đồng bảo vệ %s vào ngày %s tại %s",
                dto.getName(), dto.getDefenseDate().toString(), dto.getDefenseLocation()));
        
        logger.log(Level.INFO, "Council created successfully: {0}", createdCouncil.getName());
        return mapToResponseDTO(createdCouncil);
    }
    
    @Override
    public CouncilResponse updateCouncil(long id, CouncilResquest dto, String username) {
        logger.log(Level.INFO, "Updating council ID: {0}", id);
        int maxCouncilMember = Integer.parseInt(env.getProperty("MAX_COUNCIL_MEMBER"));
        
        Users updatedBy = this.userRepo.getUserById(id);
        UserValidator.checkRole(updatedBy, UserRole.ROLE_GIAOVU);
        
        Councils council = this.councilRepo.getCouncilById(id);
        if (council == null) {
            logger.log(Level.WARNING, "Council not found: {0}", id);
            throw new IllegalArgumentException("Council not found");
        }
        
        if (council.getStatus().equals(CouncilStatus.LOCKED.name())) {
            logger.log(Level.WARNING, "Council {0} is locked and cannot be updated", id);
            throw new IllegalArgumentException("Council is locked");
        }
        
        if (dto.getMembers() != null) {
            if (dto.getMembers().size() > maxCouncilMember) {
                logger.log(Level.WARNING, "Council cannot have more {0} members", maxCouncilMember);
                throw new IllegalArgumentException("Maximum " + maxCouncilMember + " members allowed");
            }
            
            long chairmanCount = dto.getMembers().stream().filter(m -> m.getRole().equals(CouncilMemberRole.CHAIRMAN.name())).count();
            long secretaryCount = dto.getMembers().stream().filter(m -> m.getRole().equals(CouncilMemberRole.SECRETARY.name())).count();
            
            if (chairmanCount != 1 || secretaryCount != 1) {
                logger.log(Level.WARNING, "Council must have exactly one CHAIRMAN and one SECRETARY");
                throw new IllegalArgumentException("Council must have exactly one CHAIRMAN and one SECRETARY");
            }
        }
        
        if (dto.getDefenseDate() != null && dto.getDefenseDate().before(new Date())) {
            logger.log(Level.WARNING, "Defense date must be in the future");
            throw new IllegalArgumentException("Defense date must be in the future");
        }
        
        if (dto.getDefenseLocation() != null && (dto.getDefenseLocation().trim().isEmpty() || dto.getDefenseLocation().length() > 255)) {
            logger.log(Level.WARNING, "Invalid defense location");
            throw new IllegalArgumentException("Defense location must be non-empty and at most 255 characters");
        }
        
        if (dto.getName() != null) {
            council.setName(dto.getName());
        }
        if (dto.getDefenseDate() != null) {
            council.setDefenseDate(dto.getDefenseDate());
        }
        if (dto.getDefenseLocation() != null) {
            council.setDefenseLocation(dto.getDefenseLocation());
        }
        if (dto.getStatus() != null) {
            council.setStatus(dto.getStatus().name());
        }
        
        
        // cập nhật thành viên.
        if (dto.getMembers() != null) {
            council.getCouncilMembersSet().clear();
            
            Set<CouncilMembers> members = new HashSet<>();
            for (CouncilMemberDTO memberDTO: dto.getMembers()) {
                Users member = this.userRepo.getUserById(memberDTO.getUserId());
                UserValidator.checkRole(member, UserRole.ROLE_GIANGVIEN);
                
                CouncilMembers councilMember = new CouncilMembers();
                councilMember.setCouncilId(council);
                councilMember.setMemberId(member);
                councilMember.setRole(memberDTO.getRole().name());
                
                members.add(councilMember);
            }
            council.setCouncilMembersSet(members);
        }
        
        // cập nhật khóa luận.
        if (dto.getThesisIds() != null) {
            council.getCouncilThesesSet().clear();
            
            Set<CouncilTheses> councilTheses = new HashSet<>();
            for (Long thesisId: dto.getThesisIds()) {
                Theses thesis = this.thesisRepo.getThesisById(thesisId);
                
                if (thesis == null) {
                    logger.log(Level.WARNING, "Thesis not found: {0}", thesisId);
                    throw new IllegalArgumentException("Thesis not found " + thesisId);
                }
                if (!thesis.getStatus().equals(ThesisStatus.APPROVED.name())) {
                    logger.log(Level.WARNING, "Thesis {0} is not in APPROVED status", thesisId);
                    throw new IllegalArgumentException("Thesis must be in APPROVED status: " + thesisId);
                }
                
                Set<Long> supervisorIds = thesis.getThesisAdvisorsSet().stream()
                        .map(assignments -> assignments.getAdvisorId().getId())
                        .collect(Collectors.toSet());
                
                Set<Long> reviewerIds = thesis.getThesisReviewersSet().stream()
                        .map(assignments -> assignments.getReviewerId().getId())
                        .collect(Collectors.toSet());
                
                for (CouncilMembers member: council.getCouncilMembersSet()) {
                    if (supervisorIds.contains(member.getMemberId().getId()) || reviewerIds.contains(member.getMemberId().getId())) {
                        logger.log(Level.WARNING, "Member ID {0} is already a supervisor or reviewer for thesis {1}",
                            new Object[]{member.getMemberId().getId(), thesisId});
                        throw new IllegalArgumentException("Council member cannot be a supervisor or reviewer for thesis: " + thesisId);
                    }
                }
                
                CouncilTheses councilThesis = new CouncilTheses();
                councilThesis.setCouncilId(council);
                councilThesis.setThesisId(thesis);
                councilTheses.add(councilThesis);
            }
            council.setCouncilThesesSet(councilTheses);
        }
        
        Councils updatedCouncil = this.councilRepo.createOrUpdateCouncil(council);
        
        List<Users> recipients = new ArrayList<>();
        recipients.add(updatedBy);
        recipients.addAll(council.getCouncilMembersSet().stream().map(CouncilMembers::getMemberId).collect(Collectors.toList()));
        this.notiService.sendBulkNotification(recipients,
            String.format("Hội đồng bảo vệ %s đã được cập nhật vào ngày %s tại %s",
                council.getName(), council.getDefenseDate().toString(), council.getDefenseLocation()));

        logger.log(Level.INFO, "Council updated successfully: {0}", updatedCouncil.getName());
        return mapToResponseDTO(updatedCouncil);
    }

    @Override
    public void deleteCouncil(long id, String username) {
        logger.log(Level.INFO, "Deleting council ID: {0}", id);

        Users deletedBy = this.userRepo.getUserByUsername(username);
        UserValidator.checkRole(deletedBy, UserRole.ROLE_GIAOVU);

        Councils council = this.councilRepo.getCouncilById(id);
        if (council == null) {
            logger.log(Level.WARNING, "Council not found: {0}", id);
            throw new IllegalArgumentException("Council not found");
        }

        if (council.getStatus().equals(CouncilStatus.LOCKED.name())) {
            logger.log(Level.WARNING, "Council {0} is locked and cannot be deleted", id);
            throw new IllegalArgumentException("Council is locked");
        }

        this.councilRepo.deleteCouncil(id);
        logger.log(Level.INFO, "Council deleted successfully: {0}", id);
    }
    
    @Override
    public CouncilResponse lockCouncil(long id, String username) {
    logger.log(Level.INFO, "Locking council ID: {0}", id);

        Users lockedBy = this.userRepo.getUserByUsername(username);
        if (lockedBy == null || !lockedBy.getRole().equals(UserRole.ROLE_GIAOVU.name())) {
            logger.log(Level.WARNING, "User {0} is not authorized to lock council", username);
            throw new IllegalArgumentException("Only GIAOVU role can lock council");
        }

        Councils council = this.councilRepo.getCouncilById(id);
        if (council == null) {
            logger.log(Level.WARNING, "Council not found: {0}", id);
            throw new IllegalArgumentException("Council not found");
        }

        if (council.getStatus().equals(CouncilStatus.LOCKED.name())) {
            logger.log(Level.WARNING, "Council {0} is already locked", id);
            throw new IllegalArgumentException("Council is already locked");
        }

        council.setStatus(CouncilStatus.LOCKED.name());
        Councils lockedCouncil = this.councilRepo.createOrUpdateCouncil(council);
        
        // Send email notifications to students
        for (CouncilTheses ct : council.getCouncilThesesSet()) {
            Theses thesis = ct.getThesisId();
            double averageScore = thesis.getAverageScore() != null ? thesis.getAverageScore().doubleValue() : 0.0;
            for (ThesisStudents ts : thesis.getThesisStudentsSet()) {
                Users student = ts.getStudentId();
                String subject = "Thông báo điểm trung bình khóa luận";
                String content = String.format(
                    "Kính gửi %s %s,\n\n" +
                    "Điểm trung bình chính thức của khóa luận \"%s\" là: %.2f.\n" +
                    "Cảm ơn bạn đã hoàn thành khóa luận.\n\n" +
                    "Trân trọng,\nBan Giáo vụ",
                    student.getFirstname(), student.getLastname(), thesis.getTitle(), averageScore
                );
                this.mailService.sendEmail(student.getEmail(), subject, content);
            }
        }
        
        logger.log(Level.INFO, "Council locked successfully: {0}", id);
        return mapToResponseDTO(lockedCouncil);
    }
    
    private CouncilResponse mapToResponseDTO(Councils council) {
        CouncilResponse dto = new CouncilResponse();
        dto.setId(council.getId());
        dto.setName(council.getName());
        dto.setDefenseDate(council.getDefenseDate());
        dto.setDefenseLocation(council.getDefenseLocation());
        dto.setStatus(CouncilStatus.valueOf(council.getStatus()));
        dto.setCreatedAt(council.getCreatedAt());
        
        dto.setMembers(council.getCouncilMembersSet().stream()
            .map(member -> new CouncilMemberResponseDTO(
                new ThesisUserDTO(
                    member.getMemberId().getId(), 
                    member.getMemberId().getFirstname(), 
                    member.getMemberId().getLastname(),
                    member.getMemberId().getEmail(),
                    null
                ),
                CouncilMemberRole.valueOf(member.getRole())
            ))
            .collect(Collectors.toList())
        );
        
        dto.setTheses(council.getCouncilThesesSet().stream()
            .map(ct -> new CouncilThesisDTO(
                ct.getThesisId().getId(),
                ct.getThesisId().getTitle(),
                ThesisStatus.valueOf(ct.getThesisId().getStatus())
            ))
            .collect(Collectors.toList()));
        
        Users createdBy = council.getCreatedBy();
        dto.setCreatedBy(new ThesisUserDTO(
                createdBy.getId(),
                createdBy.getFirstname(),
                createdBy.getLastname(),
                createdBy.getEmail(),
                null
        ));
        
        return dto;
    }

}
