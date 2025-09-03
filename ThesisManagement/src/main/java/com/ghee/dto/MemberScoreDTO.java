/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import java.math.BigDecimal;

/**
 *
 * @author giahu
 */
public class MemberScoreDTO {

        private BigDecimal score;
        private String councilMemberName; // Chuẩn hóa từ "councilmemberName"
        private Long councilMemberId;

        public MemberScoreDTO() {
        }

        public MemberScoreDTO(BigDecimal score, String councilMemberName, Long councilMemberId) {
            this.score = score;
            this.councilMemberName = councilMemberName;
            this.councilMemberId = councilMemberId;
        }

        // Getters and setters
        public BigDecimal getScore() {
            return score;
        }

        public void setScore(BigDecimal score) {
            this.score = score;
        }

        public String getCouncilMemberName() {
            return councilMemberName;
        }

        public void setCouncilMemberName(String councilMemberName) {
            this.councilMemberName = councilMemberName;
        }

        public Long getCouncilMemberId() {
            return councilMemberId;
        }

        public void setCouncilMemberId(Long councilMemberId) {
            this.councilMemberId = councilMemberId;
        }
    }
