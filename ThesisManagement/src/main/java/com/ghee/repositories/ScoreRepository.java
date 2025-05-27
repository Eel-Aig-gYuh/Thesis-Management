/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.repositories;

import com.ghee.dto.AverageScoreDTO;
import com.ghee.dto.ScoringRequest;
import com.ghee.dto.ScoringResponse;
import com.ghee.dto.StatisticDTO;
import com.ghee.enums.CouncilMemberRole;
import com.ghee.pojo.Scores;
import java.util.List;
import java.util.Map;

/**
 *
 * @author giahu
 */
public interface ScoreRepository {
    Map<String, Object> getScoreDetailsByThesisId (long thesisId);
    AverageScoreDTO calculateAverageScoreByThesis(long thesisId);
    
    Map<String, Object> getScoringCriteria(Long councilId, List<Long> thesisIds, long userId);
    ScoringResponse submitScores(ScoringRequest request);
    
    List<StatisticDTO.ThesisScoreStatsResponse> getThesisScoreStatistics(String startYear, String endYear);
    List<StatisticDTO.ThesisParticipationResponse> getThesisParticipationByDepartment(String startYear, String endYear);
    List<StatisticDTO.ThesisAverageScoreReport> getThesisAverageScoresForReport(String year);
    
    List<Scores> createOrUpdateScores (List<Scores> scores);
    Scores getScoreById(long id);
    List<Scores> getScoresByThesisId(long thesisId);
    
    List<Scores> getScoreByThesisAndRole(long thesisId, List<CouncilMemberRole> role);
}
