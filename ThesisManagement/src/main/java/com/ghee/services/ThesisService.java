/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services;

import com.ghee.dto.AverageScoreResponse;
import com.ghee.dto.ThesisRequest;
import com.ghee.dto.ThesisResponse;
import com.ghee.dto.ThesisReviewerDTO;
import com.ghee.dto.ThesisStatusDTO;
import com.ghee.pojo.Theses;
import java.util.List;
import java.util.Map;

/**
 *
 * @author giahu
 */
public interface ThesisService {
    ThesisResponse getThesisById(long id);
    Map<String, Object> getThese(Map<String, String> params);
    AverageScoreResponse getAverageScore(long thesisId, String username);
    
    ThesisResponse assignReviewers(long id, ThesisReviewerDTO dto, String username);
    ThesisResponse removeReviewers(long id, ThesisReviewerDTO dto, String username);
    ThesisResponse updateReviewers(long id, ThesisReviewerDTO dto, String username);
    
    ThesisResponse createThesis(ThesisRequest dto, String username);
    ThesisResponse updateThesis(long id, ThesisRequest dto, String username);
    ThesisResponse updateThesisStatus(long id, ThesisStatusDTO dto, String username);
    
    void deleteThesis(long id, String username);
}
