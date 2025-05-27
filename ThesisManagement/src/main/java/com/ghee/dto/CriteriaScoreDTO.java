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
public class CriteriaScoreDTO {

        private Long criteriaId;
        private String criteriaName; // Chuẩn hóa từ "criteriaNam"
        private BigDecimal maxScore;
        private List<MemberScoreDTO> memberScore;

        public CriteriaScoreDTO() {
        }

        public CriteriaScoreDTO(Long criteriaId, String criteriaName, BigDecimal maxScore, List<MemberScoreDTO> memberScore) {
            this.criteriaId = criteriaId;
            this.criteriaName = criteriaName;
            this.maxScore = maxScore;
            this.memberScore = memberScore;
        }

        // Getters and setters
        public Long getCriteriaId() {
            return criteriaId;
        }

        public void setCriteriaId(Long criteriaId) {
            this.criteriaId = criteriaId;
        }

        public String getCriteriaName() {
            return criteriaName;
        }

        public void setCriteriaName(String criteriaName) {
            this.criteriaName = criteriaName;
        }

        public BigDecimal getMaxScore() {
            return maxScore;
        }

        public void setMaxScore(BigDecimal maxScore) {
            this.maxScore = maxScore;
        }

        public List<MemberScoreDTO> getMemberScore() {
            return memberScore;
        }

        public void setMemberScore(List<MemberScoreDTO> memberScore) {
            this.memberScore = memberScore;
        }
    }
