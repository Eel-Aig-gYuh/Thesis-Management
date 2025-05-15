/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import com.ghee.enums.ThesisStatus;
import java.util.Date;
import java.util.List;

/**
 *
 * @author giahu
 */
public class ThesisResponse {
    private Long id;
    private String title;
    private ThesisStatus status;
    private List<UserDTO> students;
    private List<UserDTO> supervisors;
    private UserDTO createdBy;
    private Date createdAt;

    public ThesisResponse(Long id, String title, ThesisStatus status, List<UserDTO> students, List<UserDTO> supervisors, UserDTO createdBy, Date createdAt) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.students = students;
        this.supervisors = supervisors;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
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
    public List<UserDTO> getStudents() {
        return students;
    }

    /**
     * @param students the students to set
     */
    public void setStudents(List<UserDTO> students) {
        this.students = students;
    }

    /**
     * @return the supervisors
     */
    public List<UserDTO> getSupervisors() {
        return supervisors;
    }

    /**
     * @param supervisors the supervisors to set
     */
    public void setSupervisors(List<UserDTO> supervisors) {
        this.supervisors = supervisors;
    }

    /**
     * @return the createdBy
     */
    public UserDTO getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(UserDTO createdBy) {
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
    
    
}
