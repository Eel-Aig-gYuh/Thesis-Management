/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services;

import com.ghee.dto.StatisticsResponse;

/**
 *
 * @author giahu
 */
public interface StatisticService {
    StatisticsResponse getThesisStatistics(String year, String major, String username);
    String generateScoreReportPDF(long thesisId, String username);
}
