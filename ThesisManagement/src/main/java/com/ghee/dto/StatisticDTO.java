/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import java.math.BigDecimal;
import java.util.Map;

/**
 * for statistics and PDF data.
 * @author giahu
 */
public class StatisticDTO {
    
    public static class StatisticRequest {
        private String yearStart;
        private String yearEnd;
        private String year;

        /**
         * @return the yearStart
         */
        public String getYearStart() {
            return yearStart;
        }

        /**
         * @param yearStart the yearStart to set
         */
        public void setYearStart(String yearStart) {
            this.yearStart = yearStart;
        }

        /**
         * @return the yearEnd
         */
        public String getYearEnd() {
            return yearEnd;
        }

        /**
         * @param yearEnd the yearEnd to set
         */
        public void setYearEnd(String yearEnd) {
            this.yearEnd = yearEnd;
        }

        /**
         * @return the year
         */
        public String getYear() {
            return year;
        }

        /**
         * @param year the year to set
         */
        public void setYear(String year) {
            this.year = year;
        }
        
        
        
    }
    /**
     * params: 
     * private String year;
     * private BigDecimal averageScore;
     * private Map<String, Integer> scoreDistribution; // e.g., {"0-4": 5, "4-6": 10}
     */
    public static class ThesisScoreStatsResponse {
        private String year;
        private BigDecimal averageScore;
        private Map<String, Integer> scoreDistribution; // e.g., {"0-4": 5, "4-6": 10}

        /**
         * @return the year
         */
        public String getYear() {
            return year;
        }

        /**
         * @param year the year to set
         */
        public void setYear(String year) {
            this.year = year;
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
         * @return the scoreDistribution
         */
        public Map<String, Integer> getScoreDistribution() {
            return scoreDistribution;
        }

        /**
         * @param scoreDistribution the scoreDistribution to set
         */
        public void setScoreDistribution(Map<String, Integer> scoreDistribution) {
            this.scoreDistribution = scoreDistribution;
        }
    }

    /**
     * params: 
     * private String year;
     * private Map<String, Integer> participationByDepartment; // e.g., {"Công nghệ Thông tin": 20}
     */
    public static class ThesisParticipationResponse {
        private String year;
        private Map<String, Integer> participationByDepartment; // e.g., {"Công nghệ Thông tin": 20}

        /**
         * @return the year
         */
        public String getYear() {
            return year;
        }

        /**
         * @param year the year to set
         */
        public void setYear(String year) {
            this.year = year;
        }

        /**
         * @return the participationByDepartment
         */
        public Map<String, Integer> getParticipationByDepartment() {
            return participationByDepartment;
        }

        /**
         * @param participationByDepartment the participationByDepartment to set
         */
        public void setParticipationByDepartment(Map<String, Integer> participationByDepartment) {
            this.participationByDepartment = participationByDepartment;
        }

    }
    
    /**
     * params:
     * private Long thesisId;
     * private String title;
     * private String department;
     * private String semester;
     * private BigDecimal averageScore;
     */
    public static class ThesisAverageScoreReport {
        private Long thesisId;
        private String title;
        private String department;
        private String semester;
        private BigDecimal averageScore;

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
         * @return the department
         */
        public String getDepartment() {
            return department;
        }

        /**
         * @param department the department to set
         */
        public void setDepartment(String department) {
            this.department = department;
        }

        /**
         * @return the semester
         */
        public String getSemester() {
            return semester;
        }

        /**
         * @param semester the semester to set
         */
        public void setSemester(String semester) {
            this.semester = semester;
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
        
        
    }
}
