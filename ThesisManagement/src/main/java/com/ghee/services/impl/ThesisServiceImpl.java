/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services.impl;

import com.ghee.dto.ThesisRequest;
import com.ghee.dto.ThesisResponse;
import com.ghee.dto.ThesisReviewerDTO;
import com.ghee.dto.ThesisStatusDTO;
import com.ghee.dto.ThesisUserDTO;
import com.ghee.enums.ThesisStatus;
import com.ghee.enums.UserRole;
import com.ghee.pojo.Theses;
import com.ghee.pojo.ThesisAdvisors;
import com.ghee.pojo.ThesisReviewers;
import com.ghee.pojo.ThesisStudents;
import com.ghee.pojo.Users;
import com.ghee.repositories.ThesisRepository;
import com.ghee.repositories.UserRepository;
import com.ghee.services.MailService;
import com.ghee.services.NotificationService;
import com.ghee.services.ThesisService;
import com.ghee.utils.DateUtils;
import com.ghee.validators.ThesisValidatior;
import com.ghee.validators.UserValidator;
import java.util.ArrayList;
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
public class ThesisServiceImpl implements ThesisService {
    private static final Logger logger = Logger.getLogger(ThesisServiceImpl.class.getName());

    @Autowired
    private Environment env;
    
    @Autowired
    private ThesisRepository thesisRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private NotificationService notiService;
    
    @Autowired
    private MailService mailService;

    // ====================== GHEE
    @Override
    public ThesisResponse getThesisById(long id) {
        Theses thesis = this.thesisRepo.getThesisById(id);
        if (thesis == null) {
            logger.log(Level.WARNING, "Thesis not found: {0}", id);
            throw new IllegalArgumentException("Thesis not found");
        }
        return mapToResponseThesisDTO(thesis);
    }

    @Override
    public Map<String, Object> getThese(Map<String, String> params) {
        Map<String, Object> result = this.thesisRepo.getTheses(params);
        List<Theses> theses = (List<Theses>) result.get("theses");
        result.put("theses", theses.stream().map(this::mapToResponseThesisDTO).collect(Collectors.toList()));

        return result;
    }

    @Override
    public ThesisResponse createThesis(ThesisRequest dto, String username) {
        logger.log(Level.INFO, "Creating thesis with title: {0}", dto.getTitle());

        // Kiểm tra quyền giáo vụ.
        Users createdBy = this.userRepo.getUserByUsername(username);
        UserValidator.checkRole(createdBy, UserRole.ROLE_GIAOVU);

        // Kiểm tra trùng lặp tiêu đề.
        if (this.thesisRepo.existsByTitle(dto.getTitle())) {
            logger.log(Level.WARNING, "Thesis title already exist: {0}", dto.getTitle());
            throw new IllegalArgumentException("Thesis title already exist");
        }

        // Kiểm tra số lượng giảng viên hướng dẫn
        if (dto.getSupervisorIds().size() > 2) {
            logger.log(Level.WARNING, "Thesis cannot have more than 2 supervisors.", username);
            throw new IllegalArgumentException("Maximum 2 supervisors allowed");
        }

        // Tạo thesis
        Theses thesis = new Theses();
        thesis.setTitle(dto.getTitle());
        thesis.setSemester(dto.getSemester());
        thesis.setStatus(dto.getStatus() != null ? String.valueOf(dto.getStatus()) : String.valueOf(ThesisStatus.DRAFT));
        thesis.setCreatedBy(createdBy);
        thesis.setCreatedAt(DateUtils.getTodayWithoutTime());

        Set<ThesisStudents> studentAssignments = new HashSet<>();
        for (Long id : dto.getStudentIds()) {
            Users student = this.userRepo.getUserById(id);
            UserValidator.checkRole(student, UserRole.ROLE_SINHVIEN);

            logger.log(Level.INFO, "student is adding: {0}", student.getUsername());

            ThesisStudents thesisStudent = new ThesisStudents();
            thesisStudent.setThesisId(thesis);
            thesisStudent.setStudentId(student);
            thesisStudent.setRegisteredAt(DateUtils.getTodayWithoutTime());

            studentAssignments.add(thesisStudent);
            logger.log(Level.INFO, "student assignments is adding: {0}", studentAssignments.size());
        }
        thesis.setThesisStudentsSet(studentAssignments);

        for (ThesisStudents ts : studentAssignments) {
            logger.log(Level.INFO, "Final student assignment: {0}", ts.getStudentId().getUsername());
        }

        Set<ThesisAdvisors> supervisorAssignments = new HashSet<>();
        for (Long id : dto.getSupervisorIds()) {
            Users supervisor = this.userRepo.getUserById(id);
            UserValidator.checkRole(supervisor, UserRole.ROLE_GIANGVIEN);

            logger.log(Level.INFO, "advisor is adding: {0}", supervisor.getUsername());

            ThesisAdvisors thesisSupervisor = new ThesisAdvisors();
            thesisSupervisor.setThesisId(thesis);
            thesisSupervisor.setAdvisorId(supervisor);
            thesisSupervisor.setRegisteredAt(DateUtils.getTodayWithoutTime());

            supervisorAssignments.add(thesisSupervisor);
            logger.log(Level.INFO, "supervisor assignments is adding: {0}", supervisorAssignments.size());
        }
        thesis.setThesisAdvisorsSet(supervisorAssignments);

        for (ThesisAdvisors as : supervisorAssignments) {
            logger.log(Level.INFO, "Final student assignment: {0}", as.getAdvisorId().getUsername());
        }

        logger.log(Level.INFO, "Thesis updated successfully dto student: {0}", dto.getStudentIds().size());
        logger.log(Level.INFO, "Thesis updated successfully dto advisor: {0}", dto.getSupervisorIds().size());
        logger.log(Level.INFO, "Thesis updated successfully thesis student: {0}", thesis.getThesisStudentsSet().size());
        logger.log(Level.INFO, "Thesis updated successfully thesis advisor: {0}", thesis.getThesisAdvisorsSet().size());

        Theses createdTheses = this.thesisRepo.createThesis(thesis);

        // lưu thông báo : recipients: những nguời sẽ được gửi mail.
        List<Users> recipients = new ArrayList<>();
        recipients.add(createdBy);
        recipients.addAll(studentAssignments.stream().map(ThesisStudents::getStudentId).collect(Collectors.toList()));
        recipients.addAll(supervisorAssignments.stream().map(ThesisAdvisors::getAdvisorId).collect(Collectors.toList()));
        this.notiService.sendBulkNotification(recipients, "Tạo khóa luận: " + thesis.getTitle());

        logger.log(Level.INFO, "Thesis created successfully: {0}", createdTheses.getTitle());

        return mapToResponseThesisDTO(createdTheses);
    }

    @Override
    public ThesisResponse updateThesis(long id, ThesisRequest dto, String username) {
        logger.log(Level.INFO, "Updating thesis ID: {0}", id);

        // Kiểm tra quyền giáo vụ.
        Users updatedBy = this.userRepo.getUserByUsername(username);
        UserValidator.checkRole(updatedBy, UserRole.ROLE_GIAOVU);

        Theses thesis = this.thesisRepo.getThesisById(id);
        if (thesis == null) {
            logger.log(Level.WARNING, "Thesis not found: {0}", id);
            throw new IllegalArgumentException("Thesis not found");
        }

        // Kiểm tra trùng lặp tiêu đề. (nếu thay đổi)
        if (dto.getTitle() != null && !dto.getTitle().equals(thesis.getTitle()) && this.thesisRepo.existsByTitle(dto.getTitle())) {
            logger.log(Level.WARNING, "Thesis title already exist: {0}", dto.getTitle());
            throw new IllegalArgumentException("Thesis title already exist");
        }

        // Kiểm tra số lượng giảng viên hướng dẫn
        if (dto.getSupervisorIds().size() > 2) {
            logger.log(Level.WARNING, "Thesis cannot have more than 2 supervisors.", username);
            throw new IllegalArgumentException("Maximum 2 supervisors allowed");
        }

        // Cập nhật thông tin
        if (dto.getTitle() != null) {
            thesis.setTitle(dto.getTitle());
        }

        if (dto.getStatus() != null) {
            thesis.setStatus(String.valueOf(dto.getStatus()));
        }
        
        if (dto.getSemester()!= null) {
            thesis.setSemester(dto.getSemester());
        }

        // Cập nhật danh sách sinh viên
        if (dto.getStudentIds() != null) {
            Set<ThesisStudents> students = thesis.getThesisStudentsSet();
            students.clear();

            for (Long studentId : dto.getStudentIds()) {
                Users student = this.userRepo.getUserById(studentId);
                // check role
                UserValidator.checkRole(student, UserRole.ROLE_SINHVIEN);

                ThesisStudents thesisStudent = new ThesisStudents();
                thesisStudent.setThesisId(thesis);
                thesisStudent.setStudentId(student);
                thesisStudent.setRegisteredAt(DateUtils.getTodayWithoutTime());
                students.add(thesisStudent);
            }
            thesis.setThesisStudentsSet(students);
        }

        // Cập nhật danh sách giảng viên
        if (dto.getSupervisorIds() != null) {
            Set<ThesisAdvisors> advisors = thesis.getThesisAdvisorsSet();
            advisors.clear();

            for (Long supervisorId : dto.getSupervisorIds()) {
                Users supervisor = this.userRepo.getUserById(supervisorId);
                // check role
                UserValidator.checkRole(supervisor, UserRole.ROLE_GIANGVIEN);

                ThesisAdvisors thesisSupervisor = new ThesisAdvisors();
                thesisSupervisor.setThesisId(thesis);
                thesisSupervisor.setAdvisorId(supervisor);
                thesisSupervisor.setRegisteredAt(DateUtils.getTodayWithoutTime());
                advisors.add(thesisSupervisor);
            }
            thesis.setThesisAdvisorsSet(advisors);
        }

        Theses updatedThesis = this.thesisRepo.updateThesis(thesis);

        // Lưu thông báo
        this.notiService.sendNotification(updatedBy, "Cập nhật khóa luận: " + thesis.getTitle());

        logger.log(Level.INFO, "Thesis updated successfully: {0}", updatedThesis.getTitle());

        return mapToResponseThesisDTO(updatedThesis);
    }

    @Override
    public ThesisResponse updateThesisStatus(long id, ThesisStatusDTO dto, String username) {
        logger.log(Level.INFO, "Updating status for thesis ID: {0} to {1}", new Object[]{id, dto.getStatus()});

        Users updatedBy = this.userRepo.getUserByUsername(username);
        UserValidator.checkRole(updatedBy, UserRole.ROLE_GIAOVU);

        Theses thesis = this.thesisRepo.getThesisById(id);
        if (thesis == null) {
            logger.log(Level.WARNING, "Thesis not found: {0}", id);
            throw new IllegalArgumentException("Thesis not found");
        }

        ThesisStatus currentStatus = ThesisStatus.valueOf(thesis.getStatus());
        ThesisStatus newStatus = dto.getStatus();

        // Kiểm tra luồng trạng thái
        if (currentStatus.equals(newStatus)) {
            logger.log(Level.WARNING, "Thesis is already in status: {0}", newStatus);
            throw new IllegalArgumentException("Thesis is already in status: " + newStatus);
        }

        if (currentStatus.equals(ThesisStatus.DRAFT) && newStatus.equals(ThesisStatus.REGISTERED)) {
            ThesisValidatior.validateNotEmpty(thesis.getThesisStudentsSet(), "At least one student must be assigned");
            ThesisValidatior.validateNotEmpty(thesis.getThesisAdvisorsSet(), "At least one supervisor must be assigned");
        } else if (currentStatus.equals(ThesisStatus.REGISTERED) && newStatus.equals(ThesisStatus.APPROVED)) {
            ThesisValidatior.validateNotEmpty(thesis.getThesisReviewersSet(), "At least one reviewer must be assigned");
        } else {
            String msg = String.format("Invalid status transition from %s to %s", currentStatus, newStatus);
            logger.warning(msg);
            throw new IllegalArgumentException(msg);
        }
        
        thesis.setStatus(String.valueOf(newStatus));
        Theses updatedThesis = this.thesisRepo.updateThesis(thesis);
        
        // Gửi thông báo đến giáo vụ, sinh viên, hướng dẫn, phản biện
        List<Users> recipients = new ArrayList<>();
        recipients.add(updatedBy);
        recipients.addAll(thesis.getThesisStudentsSet().stream().map(ThesisStudents::getStudentId).collect(Collectors.toList()));
        recipients.addAll(thesis.getThesisAdvisorsSet().stream().map(ThesisAdvisors::getAdvisorId).collect(Collectors.toList()));
        recipients.addAll(thesis.getThesisReviewersSet().stream().map(ThesisReviewers::getReviewerId).collect(Collectors.toList()));
        this.notiService.sendBulkNotification(recipients, "Khóa luận '" + thesis.getTitle() + "' đã được chuyển sang trạng thái: " + newStatus);
        
        logger.log(Level.INFO, "Thesis status updated successfully: {0}", updatedThesis.getTitle());
        return mapToResponseThesisDTO(updatedThesis);
    }

    @Override
    public void deleteThesis(long id, String username) {
        logger.log(Level.INFO, "Deleting thesis ID: {0}", id);

        // Kiểm tra quyền giáo vụ
        Users deletedBy = this.userRepo.getUserByUsername(username);
        UserValidator.checkRole(deletedBy, UserRole.ROLE_GIAOVU);

        Theses thesis = this.thesisRepo.getThesisById(id);
        if (thesis == null) {
            logger.log(Level.WARNING, "Thesis not found: {0}", id);
            throw new IllegalArgumentException("Thesis not found");
        }

        this.thesisRepo.deleteThesis(id);

        // Lưu thông báo
        this.notiService.sendNotification(deletedBy, "Đã xóa khóa luận: " + thesis.getTitle());
    }
    
    /**
     * Phân công phản biện...
     *
     * @param id
     * @param dto
     * @param username
     * @return
     */
    @Override
    public ThesisResponse assignReviewers(long id, ThesisReviewerDTO dto, String username) {
        logger.log(Level.INFO, "Assigning reviewers for thesis ID: {0}", id);
        int max_reviewer_assign_per_semester = Integer.parseInt(env.getProperty("MAX_REVIEWER_ASSIGNMENTS_PER_SEMESTER"));
        
        // Kiểm tra quyền giáo vụ.
        Users assignedBy = this.userRepo.getUserByUsername(username);
        UserValidator.checkRole(assignedBy, UserRole.ROLE_GIAOVU);

        // Kiểm tra khóa luận có tồn tại
        Theses thesis = this.thesisRepo.getThesisById(id);
        if (thesis == null) {
            logger.log(Level.WARNING, "Thesis not found: {0}", id);
            throw new IllegalArgumentException("Thesis not found");
        }

        if (dto.getReviewerIds() != null && dto.getReviewerIds().size() > 2) {
            logger.log(Level.WARNING, "Thesis cannot have more than 2 reviewers");
            throw new IllegalArgumentException("Maximum 2 reviewers allowed");
        }

        // Kiểm tra trùng với giảng viên hướng dẫn.
        Set<Long> supervisorIds = thesis.getThesisAdvisorsSet().stream()
                .map(assignment -> assignment.getAdvisorId().getId())
                .collect(Collectors.toSet());

        for (Long reviewId : dto.getReviewerIds()) {
            if (supervisorIds.contains(reviewId)) {
                logger.log(Level.WARNING, "User ID {0} is already a supervisor", reviewId);
                throw new IllegalArgumentException("Reviewer cannot be a supervisor for the same thesis");
            }
        }

        // Lấy collection hiện tại
        Set<ThesisReviewers> reviewerAssignments = thesis.getThesisReviewersSet();
        List<Users> recipients = new ArrayList<>();
        if (reviewerAssignments == null) {
            reviewerAssignments = new HashSet<>();
            recipients.add(assignedBy);
            thesis.setThesisReviewersSet(reviewerAssignments);
        } else {
            // Xóa hết phần tử cũ
            reviewerAssignments.clear();
        }

        // check điều kiện.
        String semester = thesis.getSemester();
        for (Long reviewerId : dto.getReviewerIds()) {
            Users reviewer = this.userRepo.getUserById(reviewerId);
            UserValidator.checkRole(reviewer, UserRole.ROLE_GIANGVIEN);

            long currentAssignments = this.thesisRepo.countReviewAssignmentsByUserAndSemester(reviewer, semester);
            if (currentAssignments >= max_reviewer_assign_per_semester) {
                logger.log(Level.WARNING, "Reviewer {0} has reached the limit of {1} assignments in semester {2}",
                    new Object[]{reviewer.getFirstname() + " " + reviewer.getLastname(), max_reviewer_assign_per_semester, semester});
                throw new IllegalArgumentException("Giảng viên " + reviewer.getFirstname() + " " + reviewer.getLastname() +
                    " đã đạt giới hạn phản biện trong kỳ " + semester);
            }
        }
        
        // Tạo phân công mới
        for (Long reviewerId: dto.getReviewerIds()) {
            Users reviewer = this.userRepo.getUserById(reviewerId);
            
            ThesisReviewers thesisReviewers = new ThesisReviewers();
            thesisReviewers.setThesisId(thesis);
            thesisReviewers.setReviewerId(reviewer);
            thesisReviewers.setAssignedAt(DateUtils.getTodayWithoutTime());

            reviewerAssignments.add(thesisReviewers);
            recipients.add(reviewer);
        }
        thesis.setThesisReviewersSet(reviewerAssignments);

        Theses updatedThesis = this.thesisRepo.updateThesis(thesis);

        // Lưu thông báo cho giáo vụ
        this.notiService.sendBulkNotification(recipients, "Phân công phản biện cho khóa luận: " + thesis.getTitle());

        logger.log(Level.INFO, "Reviewers assigned successfully for thesis: {0}", updatedThesis.getTitle());

        return mapToResponseThesisDTO(updatedThesis);
    }
    
    
    @Override
    public ThesisResponse removeReviewers(long id, ThesisReviewerDTO dto, String username) {
        logger.log(Level.INFO, "Removing reviewers for thesis ID: {0}", id);

        Users removedBy = this.userRepo.getUserByUsername(username);
        UserValidator.checkRole(removedBy, UserRole.ROLE_GIAOVU);

        Theses thesis = this.thesisRepo.getThesisById(id);
        if (thesis == null) {
            logger.log(Level.WARNING, "Thesis not found: {0}", id);
            throw new IllegalArgumentException("Thesis not found");
        }

        List<Users> recipients = new ArrayList<>();
        recipients.add(removedBy);

        Set<ThesisReviewers> currentReviewers = thesis.getThesisReviewersSet();
        Set<Long> reviewerIdsToRemove = new HashSet<>(dto.getReviewerIds());
        currentReviewers.removeIf(assignment -> {
            if (reviewerIdsToRemove.contains(assignment.getReviewerId().getId())) {
                recipients.add(assignment.getReviewerId());
                return true;
            }
            return false;
        });

        Theses updatedThesis = this.thesisRepo.updateThesis(thesis);

        this.notiService.sendBulkNotification(recipients, "Bạn đã bị xóa khỏi vai trò phản biện cho khóa luận: " + thesis.getTitle());

        logger.log(Level.INFO, "Reviewers removed successfully for thesis: {0}", updatedThesis.getTitle());
        return mapToResponseThesisDTO(updatedThesis);
    }

    @Override
    public ThesisResponse updateReviewers(long id, ThesisReviewerDTO dto, String username) {
        logger.log(Level.INFO, "Updating reviewers for thesis ID: {0}", id);
        int max_reviewer_assign_per_semester = Integer.parseInt(env.getProperty("MAX_REVIEWER_ASSIGNMENTS_PER_SEMESTER"));
        
        Users updatedBy = this.userRepo.getUserByUsername(username);
        UserValidator.checkRole(updatedBy, UserRole.ROLE_GIAOVU);

        Theses thesis = this.thesisRepo.getThesisById(id);
        if (thesis == null) {
            logger.log(Level.WARNING, "Thesis not found: {0}", id);
            throw new IllegalArgumentException("Thesis not found");
        }

        if (dto.getReviewerIds().size() > 2) {
            logger.log(Level.WARNING, "Thesis cannot have more than 2 reviewers");
            throw new IllegalArgumentException("Maximum 2 reviewers allowed");
        }
        
        // check gvhd không được là ghpb.
        Set<Long> supervisorIds = thesis.getThesisAdvisorsSet().stream()
            .map(assignment -> assignment.getAdvisorId().getId())
            .collect(Collectors.toSet());
        for (Long reviewerId : dto.getReviewerIds()) {
            if (supervisorIds.contains(reviewerId)) {
                logger.log(Level.WARNING, "User ID {0} is already a supervisor", reviewerId);
                throw new IllegalArgumentException("Reviewer cannot be a supervisor for the same thesis");
            }
        }
        
        // check điều kiện.
        String semester = thesis.getSemester();
        for (Long reviewerId : dto.getReviewerIds()) {
            Users reviewer = this.userRepo.getUserById(reviewerId);
            UserValidator.checkRole(reviewer, UserRole.ROLE_GIANGVIEN);

            long currentAssignments = this.thesisRepo.countReviewAssignmentsByUserAndSemester(reviewer, semester);
            if (currentAssignments >= max_reviewer_assign_per_semester) {
                logger.log(Level.WARNING, "Reviewer {0} has reached the limit of {1} assignments in semester {2}",
                    new Object[]{reviewer.getFirstname() + " " + reviewer.getLastname(), max_reviewer_assign_per_semester, semester});
                throw new IllegalArgumentException("Giảng viên " + reviewer.getFirstname() + " " + reviewer.getLastname() +
                    " đã đạt giới hạn phản biện trong kỳ " + semester);
            }
        }
        
        List<Users> removedReviewers = thesis.getThesisReviewersSet().stream()
            .map(ThesisReviewers::getReviewerId)
            .filter(reviewer -> !dto.getReviewerIds().contains(reviewer.getId()))
            .collect(Collectors.toList());
        
        // Tạo phân công mới
        Set<ThesisReviewers> reviewerAssignments = thesis.getThesisReviewersSet();
        List<Users> recipients = new ArrayList<>();
        if (reviewerAssignments == null) {
            reviewerAssignments = new HashSet<>();
            recipients.add(updatedBy);
            thesis.setThesisReviewersSet(reviewerAssignments);
        } else {
            // Xóa hết phần tử cũ
            reviewerAssignments.clear();
        }
        
        for (Long reviewerId : dto.getReviewerIds()) {
            Users reviewer = this.userRepo.getUserById(reviewerId);
            ThesisReviewers thesisReviewer = new ThesisReviewers();
            thesisReviewer.setThesisId(thesis);
            thesisReviewer.setReviewerId(reviewer);
            thesisReviewer.setAssignedAt(DateUtils.getTodayWithoutTime());
            reviewerAssignments.add(thesisReviewer);
            recipients.add(reviewer);
        }
        recipients.addAll(removedReviewers);
        thesis.setThesisReviewersSet(reviewerAssignments);

        Theses updatedThesis = this.thesisRepo.updateThesis(thesis);

        this.notiService.sendBulkNotification(recipients, "Danh sách phản biện cho khóa luận " + thesis.getTitle() + " đã được cập nhật.");
    
        logger.log(Level.INFO, "Reviewers updated successfully for thesis: {0}", updatedThesis.getTitle());
        return mapToResponseThesisDTO(updatedThesis);
    }


    private ThesisResponse mapToResponseThesisDTO(Theses thesis) {
        ThesisResponse dto = new ThesisResponse();
        dto.setId(thesis.getId());
        dto.setTitle(thesis.getTitle());
        dto.setSemester(thesis.getSemester());
        dto.setStatus(ThesisStatus.valueOf(thesis.getStatus()));
        dto.setCreatedAt(thesis.getCreatedAt());

        // Ánh xạ sinh viên
        dto.setStudents(thesis.getThesisStudentsSet().stream()
                .map(assignment -> new ThesisUserDTO(
                assignment.getStudentId().getId(),
                assignment.getStudentId().getFirstname(),
                assignment.getStudentId().getLastname(),
                assignment.getStudentId().getEmail(),
                assignment.getRegisteredAt()
        ))
                .collect(Collectors.toList())
        );

        // Ánh xạ giảng viên hướng dẫn
        dto.setSupervisors(thesis.getThesisAdvisorsSet().stream()
                .map(assignment -> new ThesisUserDTO(
                assignment.getAdvisorId().getId(),
                assignment.getAdvisorId().getFirstname(),
                assignment.getAdvisorId().getLastname(),
                assignment.getAdvisorId().getEmail(),
                assignment.getRegisteredAt()
        ))
                .collect(Collectors.toList())
        );

        // Ánh xạ giảng viên phản biện.
        dto.setReviewers(thesis.getThesisReviewersSet().stream()
                .map(assignment -> new ThesisUserDTO(
                assignment.getReviewerId().getId(),
                assignment.getReviewerId().getFirstname(),
                assignment.getReviewerId().getLastname(),
                assignment.getReviewerId().getEmail(),
                assignment.getAssignedAt()
        ))
                .collect(Collectors.toList())
        );

        // Ánh xạ createdBy
        Users createdBy = thesis.getCreatedBy();
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
