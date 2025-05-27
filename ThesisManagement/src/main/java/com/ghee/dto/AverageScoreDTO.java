/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import java.util.List;

/**
 *
 * @author giahu
 */
public class AverageScoreDTO {
    private double averageScore;
    private List<AverageMemberDTO> memberAverages;

    public AverageScoreDTO(double averageScore, List<AverageMemberDTO> memberAverages) {
        this.averageScore = averageScore;
        this.memberAverages = memberAverages;
    }

    /**
     * @return the averageScore
     */
    public double getAverageScore() {
        return averageScore;
    }

    /**
     * @param averageScore the averageScore to set
     */
    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    /**
     * @return the memberAverages
     */
    public List<AverageMemberDTO> getMemberAverages() {
        return memberAverages;
    }

    /**
     * @param memberAverages the memberAverages to set
     */
    public void setMemberAverages(List<AverageMemberDTO> memberAverages) {
        this.memberAverages = memberAverages;
    }
    
    
}
