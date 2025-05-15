/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services;

import com.ghee.dto.ThesisRequest;
import com.ghee.dto.ThesisResponse;
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
    
    ThesisResponse createThesis(ThesisRequest dto, String username);
    ThesisResponse updateThesis(long id, ThesisRequest dto, String username);
    void deleteThesis(long id, String username);
}
