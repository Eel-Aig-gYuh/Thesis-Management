/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import java.util.Map;

/**
 *
 * @author giahu
 */
public class StatisticsResponse {
    private String year;
    private String major;
    private double averageScore;
    private long totalTheses;
    private Map<String, Long> departmentParticipation;

    public StatisticsResponse(String year, String major, double averageScore, long totalTheses, Map<String, Long> departmentParticipation) {
        this.year = year;
        this.major = major;
        this.averageScore = averageScore;
        this.totalTheses = totalTheses;
        this.departmentParticipation = departmentParticipation;
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

    /**
     * @return the major
     */
    public String getMajor() {
        return major;
    }

    /**
     * @param major the major to set
     */
    public void setMajor(String major) {
        this.major = major;
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
     * @return the totalTheses
     */
    public long getTotalTheses() {
        return totalTheses;
    }

    /**
     * @param totalTheses the totalTheses to set
     */
    public void setTotalTheses(long totalTheses) {
        this.totalTheses = totalTheses;
    }

    /**
     * @return the departmentParticipation
     */
    public Map<String, Long> getDepartmentParticipation() {
        return departmentParticipation;
    }

    /**
     * @param departmentParticipation the departmentParticipation to set
     */
    public void setDepartmentParticipation(Map<String, Long> departmentParticipation) {
        this.departmentParticipation = departmentParticipation;
    }
    
    
}
