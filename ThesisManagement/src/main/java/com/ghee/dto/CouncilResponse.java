/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import com.ghee.enums.CouncilStatus;
import com.ghee.pojo.CouncilTheses;
import java.util.Date;
import java.util.List;

/**
 *
 * @author giahu
 */
public class CouncilResponse {
    private long id;
    private String name;
    private Date defenseDate;
    private CouncilStatus status;
    private List<CouncilMemberResponseDTO> members;
    private List<CouncilThesisDTO> theses;
    private ThesisUserDTO createdBy;
    private Date createdAt;

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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the defenseDate
     */
    public Date getDefenseDate() {
        return defenseDate;
    }

    /**
     * @param defenseDate the defenseDate to set
     */
    public void setDefenseDate(Date defenseDate) {
        this.defenseDate = defenseDate;
    }

    /**
     * @return the status
     */
    public CouncilStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(CouncilStatus status) {
        this.status = status;
    }

    /**
     * @return the members
     */
    public List<CouncilMemberResponseDTO> getMembers() {
        return members;
    }

    /**
     * @param members the members to set
     */
    public void setMembers(List<CouncilMemberResponseDTO> members) {
        this.members = members;
    }

    /**
     * @return the theses
     */
    public List<CouncilThesisDTO> getTheses() {
        return theses;
    }

    /**
     * @param theses the theses to set
     */
    public void setTheses(List<CouncilThesisDTO> theses) {
        this.theses = theses;
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
    
    
    
}
