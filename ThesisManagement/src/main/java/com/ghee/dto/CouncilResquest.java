/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import com.ghee.enums.CouncilStatus;
import java.util.Date;
import java.util.List;

/**
 *
 * @author giahu
 */
public class CouncilResquest {
    private String name;
    private Date defenseDate;
    private String defenseLocation;
    private CouncilStatus status;
    private boolean isLocked;
    private List<CouncilMemberDTO> members;
    private List<Long> thesisIds;

    public CouncilResquest() {
    }

    public CouncilResquest(String name, Date defenseDate, CouncilStatus status, List<CouncilMemberDTO> members, List<Long> thesisIds) {
        this.name = name;
        this.defenseDate = defenseDate;
        this.status = status;
        this.members = members;
        this.thesisIds = thesisIds;
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
    public List<CouncilMemberDTO> getMembers() {
        return members;
    }

    /**
     * @param members the members to set
     */
    public void setMembers(List<CouncilMemberDTO> members) {
        this.members = members;
    }

    /**
     * @return the thesisIds
     */
    public List<Long> getThesisIds() {
        return thesisIds;
    }

    /**
     * @param thesisIds the thesisIds to set
     */
    public void setThesisIds(List<Long> thesisIds) {
        this.thesisIds = thesisIds;
    }

    /**
     * @return the defenseLocation
     */
    public String getDefenseLocation() {
        return defenseLocation;
    }

    /**
     * @param defenseLocation the defenseLocation to set
     */
    public void setDefenseLocation(String defenseLocation) {
        this.defenseLocation = defenseLocation;
    }

    /**
     * @return the isLocked
     */
    public boolean isIsLocked() {
        return isLocked;
    }

    /**
     * @param isLocked the isLocked to set
     */
    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }
}
