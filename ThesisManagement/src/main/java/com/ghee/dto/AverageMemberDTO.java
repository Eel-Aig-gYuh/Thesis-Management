/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

/**
 *
 * @author giahu
 */
public class AverageMemberDTO {
    private Long councilMemberId;
    private String councilMemberName;
    private double score;

    public AverageMemberDTO(Long councilMemberId, String councilMemberName, double score) {
        this.councilMemberId = councilMemberId;
        this.councilMemberName = councilMemberName;
        this.score = score;
    }

    /**
     * @return the councilMemberId
     */
    public Long getCouncilMemberId() {
        return councilMemberId;
    }

    /**
     * @param councilMemberId the councilMemberId to set
     */
    public void setCouncilMemberId(Long councilMemberId) {
        this.councilMemberId = councilMemberId;
    }

    /**
     * @return the councilMemberName
     */
    public String getCouncilMemberName() {
        return councilMemberName;
    }

    /**
     * @param councilMemberName the councilMemberName to set
     */
    public void setCouncilMemberName(String councilMemberName) {
        this.councilMemberName = councilMemberName;
    }

    /**
     * @return the averageScore
     */
    public double getScore() {
        return score;
    }

    /**
     * @param averageScore the averageScore to set
     */
    public void setScore(double score) {
        this.score = score;
    }
}
