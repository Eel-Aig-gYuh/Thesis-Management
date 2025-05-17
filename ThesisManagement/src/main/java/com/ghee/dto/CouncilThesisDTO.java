/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import com.ghee.enums.ThesisStatus;

/**
 *
 * @author giahu
 */
public class CouncilThesisDTO {
    private long id;
    private String title;
    private ThesisStatus status;

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
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

}
