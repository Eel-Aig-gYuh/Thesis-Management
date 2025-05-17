/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.repositories;

import com.ghee.pojo.Councils;
import java.util.Map;

/**
 *
 * @author giahu
 */
public interface CouncilRepository {
    Map<String, Object> getCouncils(Map<String, String> params);
    
    Councils createOrUpdateCouncil(Councils council);
    Councils getCouncilById(long id);
    
    void deleteCouncil(long id);
}
