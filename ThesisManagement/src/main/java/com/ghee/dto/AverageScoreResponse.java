/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author giahu
 */
public class AverageScoreResponse {
    private BigDecimal averageScore;
    private List<MemberScoreDTO> memberScores;

    public AverageScoreResponse(BigDecimal averageScore, List<MemberScoreDTO> memberScores) {
        this.averageScore = averageScore;
        this.memberScores = memberScores;
    }
    
    public static class MemberScoreDTO {
        private ThesisUserDTO councilMember;
        private BigDecimal totalScore;
        private List<CriteriaScoreDTO> criteriaScores;

        public MemberScoreDTO(ThesisUserDTO councilMember, BigDecimal totalScore, List<CriteriaScoreDTO> criteriaScores) {
            this.councilMember = councilMember;
            this.totalScore = totalScore;
            this.criteriaScores = criteriaScores;
        }
        
        /**
         * @return the councilMember
         */
        public ThesisUserDTO getCouncilMember() {
            return councilMember;
        }

        /**
         * @param councilMember the councilMember to set
         */
        public void setCouncilMember(ThesisUserDTO councilMember) {
            this.councilMember = councilMember;
        }

        /**
         * @return the totalScore
         */
        public BigDecimal getTotalScore() {
            return totalScore;
        }

        /**
         * @param totalScore the totalScore to set
         */
        public void setTotalScore(BigDecimal totalScore) {
            this.totalScore = totalScore;
        }

        /**
         * @return the criteriaScores
         */
        public List<CriteriaScoreDTO> getCriteriaScores() {
            return criteriaScores;
        }

        /**
         * @param criteriaScores the criteriaScores to set
         */
        public void setCriteriaScores(List<CriteriaScoreDTO> criteriaScores) {
            this.criteriaScores = criteriaScores;
        }
        
        
    }
    
    public static class CriteriaScoreDTO {
        private long criteriaId;
        private String criteriaName;
        private BigDecimal maxScore;
        private BigDecimal score;

        public CriteriaScoreDTO(long criteriaId, String criteriaName, BigDecimal maxScore, BigDecimal score) {
            this.criteriaId = criteriaId;
            this.criteriaName = criteriaName;
            this.maxScore = maxScore;
            this.score = score;
        }
        
        /**
         * @return the criteriaId
         */
        public long getCriteriaId() {
            return criteriaId;
        }

        /**
         * @param criteriaId the criteriaId to set
         */
        public void setCriteriaId(long criteriaId) {
            this.criteriaId = criteriaId;
        }

        /**
         * @return the criteriaName
         */
        public String getCriteriaName() {
            return criteriaName;
        }

        /**
         * @param criteriaName the criteriaName to set
         */
        public void setCriteriaName(String criteriaName) {
            this.criteriaName = criteriaName;
        }

        /**
         * @return the maxScore
         */
        public BigDecimal getMaxScore() {
            return maxScore;
        }

        /**
         * @param maxScore the maxScore to set
         */
        public void setMaxScore(BigDecimal maxScore) {
            this.maxScore = maxScore;
        }

        /**
         * @return the score
         */
        public BigDecimal getScore() {
            return score;
        }

        /**
         * @param score the score to set
         */
        public void setScore(BigDecimal score) {
            this.score = score;
        }
        
        
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
     * @return the memberScores
     */
    public List<MemberScoreDTO> getMemberScores() {
        return memberScores;
    }

    /**
     * @param memberScores the memberScores to set
     */
    public void setMemberScores(List<MemberScoreDTO> memberScores) {
        this.memberScores = memberScores;
    }
}
