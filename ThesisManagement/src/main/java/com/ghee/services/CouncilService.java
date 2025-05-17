/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services;

import com.ghee.dto.CouncilResponse;
import com.ghee.dto.CouncilResquest;
import java.util.Map;

/**
 *
 * @author giahu
 */
public interface CouncilService {
    CouncilResponse getCouncilById(long id);
    Map<String, Object> getCouncils(Map<String, String> params);
    
    CouncilResponse createCouncil(CouncilResquest dto, String username);
    CouncilResponse updateCouncil(long id, CouncilResquest dto, String username);
    
    void deleteCouncil(long id, String username);
}
