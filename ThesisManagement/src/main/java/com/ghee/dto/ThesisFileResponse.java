/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import java.util.Date;

/**
 *
 * @author giahu
 */
public class ThesisFileResponse {
    private Long id;
    private Long thesisId;
    private Long studentId;
    private String fileUrl;
    private String fileName;
    private Date submittedAt;

    public ThesisFileResponse(Long id, Long thesisId, Long studentId, String fileUrl, String fileName, Date submittedAt) {
        this.id = id;
        this.thesisId = thesisId;
        this.studentId = studentId;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.submittedAt = submittedAt;
    }

    public ThesisFileResponse() {
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
     * @return the thesisId
     */
    public Long getThesisId() {
        return thesisId;
    }

    /**
     * @param thesisId the thesisId to set
     */
    public void setThesisId(Long thesisId) {
        this.thesisId = thesisId;
    }

    /**
     * @return the studentId
     */
    public Long getStudentId() {
        return studentId;
    }

    /**
     * @param studentId the studentId to set
     */
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    /**
     * @return the fileUrl
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * @param fileUrl the fileUrl to set
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the submittedAt
     */
    public Date getSubmittedAt() {
        return submittedAt;
    }

    /**
     * @param submittedAt the submittedAt to set
     */
    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }
    
    
    
}
