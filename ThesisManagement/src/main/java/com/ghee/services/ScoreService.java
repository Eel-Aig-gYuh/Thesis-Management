/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services;

import com.ghee.dto.AverageScoreDTO;
import com.ghee.dto.ScoreRequest;
import com.ghee.dto.ScoreResponse;
import com.ghee.dto.ScoringCriteriaResponse;
import com.ghee.dto.ScoringRequest;
import com.ghee.dto.ScoringResponse;
import com.ghee.dto.StatisticDTO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author giahu
 */
public interface ScoreService {
    ScoreResponse createScore(long id, ScoreRequest dto, String username);
    
    AverageScoreDTO calculateAverageScoreByThesis(long thesisId, String username);
    Map<String, Object> getScoreDetailsByThesisId (long thesisId);
    
    Map<String, Object> getScoringCriteria(Long councilId, List<Long> thesisIds, long userId);
    ScoringResponse submitScores(ScoringRequest request);
    List<StatisticDTO.ThesisScoreStatsResponse> getThesisScoreStatistics(String startYear, String endYear);
    List<StatisticDTO.ThesisParticipationResponse> getThesisParticipationByDepartment(String startYear, String endYear);
    String generateAverageScoresPdf(String year);
}
