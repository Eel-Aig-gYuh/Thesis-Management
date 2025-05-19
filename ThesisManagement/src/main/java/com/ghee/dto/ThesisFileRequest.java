/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author giahu
 */
public class ThesisFileRequest {
    private Long thesisId;
    private MultipartFile file;

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
     * @return the file
     */
    public MultipartFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(MultipartFile file) {
        this.file = file;
    }
    
    
}
