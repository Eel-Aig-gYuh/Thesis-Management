/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services.impl;

import com.ghee.dto.ThesisRequest;
import com.ghee.enums.NotificationStatus;
import com.ghee.enums.NotificationType;
import com.ghee.enums.ThesisStatus;
import com.ghee.enums.UserRole;
import com.ghee.pojo.Notifications;
import com.ghee.pojo.Theses;
import com.ghee.pojo.ThesisAdvisors;
import com.ghee.pojo.ThesisStudents;
import com.ghee.pojo.Users;
import com.ghee.repositories.NotificationRepository;
import com.ghee.repositories.ThesisRepository;
import com.ghee.repositories.UserRepository;
import com.ghee.services.ThesisService;
import com.ghee.utils.DateUtils;
import com.ghee.validators.UserValidator;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
public class ThesisServiceImpl implements ThesisService {
    private static final Logger logger = Logger.getLogger(ThesisServiceImpl.class.getName());
    
    @Autowired
    private ThesisRepository thesisRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private NotificationRepository notiRepo;
    
    // ====================== GHEE
    
    @Override
    public Theses getThesisById(long id) {
        return this.thesisRepo.getThesisById(id);
    }

    @Override
    public List<Theses> getThese(Map<String, String> params) {
        return this.thesisRepo.getTheses(params);
    }

    @Override
    public Theses createThesis(ThesisRequest dto, String username) {
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
        thesis.setStatus(dto.getStatus() != null ? String.valueOf(dto.getStatus()) : String.valueOf(ThesisStatus.DRAFT));
        thesis.setCreatedBy(createdBy);
        thesis.setCreatedAt(DateUtils.getTodayWithoutTime());
        
        // Tạo ThesisStudent cho mỗi sinh viên 
        Set<ThesisStudents> studentAsignments = new HashSet<>();
        for (Long id: dto.getStudentIds()) {
            Users student = this.userRepo.getUserById(id);
            UserValidator.checkRole(student, UserRole.ROLE_SINHVIEN);
            
            ThesisStudents thesisStudents = new ThesisStudents();
            thesisStudents.setThesisId(thesis);
            thesisStudents.setStudentId(student);
            thesisStudents.setRegisteredAt(DateUtils.getTodayWithoutTime());
            
            studentAsignments.add(thesisStudents);
        }
        thesis.setThesisStudentsSet(studentAsignments);
        
        // Tạo ThesisSupervisor cho mỗi giáo viên.
        Set<ThesisAdvisors> advisorAsignments = new HashSet<>();
        for (Long id: dto.getSupervisorIds()) {
            Users supervisor = this.userRepo.getUserById(id);
            UserValidator.checkRole(supervisor, UserRole.ROLE_GIANGVIEN);
            
            ThesisAdvisors thesisAdvisors = new ThesisAdvisors();
            thesisAdvisors.setThesisId(thesis);
            thesisAdvisors.setAdvisorId(supervisor);
            thesisAdvisors.setRegisteredAt(DateUtils.getTodayWithoutTime());
            
            advisorAsignments.add(thesisAdvisors);
        }
        thesis.setThesisStudentsSet(studentAsignments);
        
        Theses createdTheses = this.thesisRepo.createThesis(thesis);
        
        // lưu thông báo
        saveNotification(createdBy, "Tạo khóa luận: " + thesis.getTitle());
        
        logger.log(Level.INFO, "Thesis created successfully: {0}", createdTheses.getTitle());
        return createdTheses;
    }

    @Override
    public Theses updateThesis(long id, ThesisRequest dto, String username) {
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
        if (dto.getTitle() != null && !dto.getTitle().equals(thesis.getTitle())) {
            thesis.setTitle(dto.getTitle());
        }
        
        if (dto.getStatus() != null && !thesis.getStatus().equals(String.valueOf(dto.getStatus()))){
            thesis.setStatus(String.valueOf(dto.getStatus()));
        }
        
        // Cập nhật danh sách sinh viên
        if (dto.getStudentIds() != null) {
            thesis.getThesisStudentsSet().clear();
            Set<ThesisStudents> studentAssignments = new HashSet<>();
            for (Long studentId : dto.getStudentIds()) {
                Users student = this.userRepo.getUserById(studentId);
                // check role
                UserValidator.checkRole(student, UserRole.ROLE_SINHVIEN);
                
                ThesisStudents thesisStudent = new ThesisStudents();
                thesisStudent.setThesisId(thesis);
                thesisStudent.setStudentId(student);
                thesisStudent.setRegisteredAt(DateUtils.getTodayWithoutTime());
                studentAssignments.add(thesisStudent);
            }
            thesis.setThesisStudentsSet(studentAssignments);
        }

        // Cập nhật danh sách giảng viên
        if (dto.getSupervisorIds() != null) {
            thesis.getThesisAdvisorsSet().clear();
            Set<ThesisAdvisors> supervisorAssignments = new HashSet<>();
            for (Long supervisorId : dto.getSupervisorIds()) {
                Users supervisor = this.userRepo.getUserById(supervisorId);
                // check role
                UserValidator.checkRole(supervisor, UserRole.ROLE_GIANGVIEN);
                
                ThesisAdvisors thesisSupervisor = new ThesisAdvisors();
                thesisSupervisor.setThesisId(thesis);
                thesisSupervisor.setAdvisorId(supervisor);
                thesisSupervisor.setRegisteredAt(DateUtils.getTodayWithoutTime());
                supervisorAssignments.add(thesisSupervisor);
            }
            thesis.setThesisAdvisorsSet(supervisorAssignments);
        }
        
        Theses updatedThesis = this.thesisRepo.updateThesis(thesis);
        
        // Lưu thông báo
        saveNotification(updatedBy, "Cập nhật khóa luận: " + thesis.getTitle());
        
        logger.log(Level.INFO, "Thesis updated successfully: {0}", updatedThesis.getTitle());
        return updatedThesis;
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
        saveNotification(deletedBy, "Đã xóa khóa luận: " + thesis.getTitle());
    }
    
    private void saveNotification(Users user, String content) {
        Notifications n = new Notifications();
        n.setUserId(user);
        n.setContent(content);
        n.setCreatedAt(DateUtils.getTodayWithoutTime());
        n.setStatus(String.valueOf(NotificationStatus.PENDING));
        n.setType(String.valueOf(NotificationType.EMAIL));
        
        Notifications newNotification = this.notiRepo.createNotification(n);
        if (newNotification == null) {
            throw new IllegalArgumentException("Failed to create notification for creating thesis: " + content);
        }
    }
}
