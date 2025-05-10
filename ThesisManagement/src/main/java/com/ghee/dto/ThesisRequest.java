/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import com.ghee.enums.ThesisStatus;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * @author giahu
 */
public class ThesisRequest {
    @NotNull private String title;
    @NotNull private List<Long> studentIds;
    @NotNull private List<Long> supervisorIds;
    private ThesisStatus status;

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
     * @return the studentIds
     */
    public List<Long> getStudentIds() {
        return studentIds;
    }

    /**
     * @param studentIds the studentIds to set
     */
    public void setStudentIds(List<Long> studentIds) {
        this.studentIds = studentIds;
    }

    /**
     * @return the supervisorIds
     */
    public List<Long> getSupervisorIds() {
        return supervisorIds;
    }

    /**
     * @param supervisorIds the supervisorIds to set
     */
    public void setSupervisorIds(List<Long> supervisorIds) {
        this.supervisorIds = supervisorIds;
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
}
