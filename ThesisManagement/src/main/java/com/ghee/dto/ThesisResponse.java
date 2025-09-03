/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import com.ghee.enums.ThesisStatus;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author giahu
 */
public class ThesisResponse {
    private Long id;
    private String title;
    private String semester;
    private ThesisStatus status;
    private List<ThesisUserDTO> students;
    private List<ThesisUserDTO> supervisors;
    private List<ThesisUserDTO> reviewers;
    private ThesisUserDTO createdBy;
    private Date createdAt;
    private BigDecimal averageScore;
    private DepartmentDTO.DepartmentResponse department;

    public ThesisResponse(Long id, String title, ThesisStatus status, List<ThesisUserDTO> students, List<ThesisUserDTO> supervisors, ThesisUserDTO createdBy, Date createdAt, BigDecimal averageScore) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.students = students;
        this.supervisors = supervisors;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.averageScore = averageScore;
    }

    public ThesisResponse() {
    }

    
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the status
     */
    public ThesisStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(ThesisStatus status) {
        this.status = status;
    }

    /**
     * @return the students
     */
    public List<ThesisUserDTO> getStudents() {
        return students;
    }

    /**
     * @param students the students to set
     */
    public void setStudents(List<ThesisUserDTO> students) {
        this.students = students;
    }

    /**
     * @return the supervisors
     */
    public List<ThesisUserDTO> getSupervisors() {
        return supervisors;
    }

    /**
     * @param supervisors the supervisors to set
     */
    public void setSupervisors(List<ThesisUserDTO> supervisors) {
        this.supervisors = supervisors;
    }

    /**
     * @return the createdBy
     */
    public ThesisUserDTO getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(ThesisUserDTO createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the createdAt
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return the reviewers
     */
    public List<ThesisUserDTO> getReviewers() {
        return reviewers;
    }

    /**
     * @param reviewers the reviewers to set
     */
    public void setReviewers(List<ThesisUserDTO> reviewers) {
        this.reviewers = reviewers;
    }

    /**
     * @return the semester
     */
    public String getSemester() {
        return semester;
    }

    /**
     * @param semester the semester to set
     */
    public void setSemester(String semester) {
        this.semester = semester;
    }

    /**
     * @return the averageScore
     */
    public BigDecimal getAverageScore() {
        return averageScore;
    }

    /**
     * @param averageScore the averageScore to set
     */
    public void setAverageScore(BigDecimal averageScore) {
        this.averageScore = averageScore;
    }

    /**
     * @return the department
     */
    public DepartmentDTO.DepartmentResponse getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(DepartmentDTO.DepartmentResponse department) {
        this.department = department;
    }
    
    
}
