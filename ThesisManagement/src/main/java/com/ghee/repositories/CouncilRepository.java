/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.repositories;

import com.ghee.enums.CouncilMemberRole;
import com.ghee.pojo.CouncilMembers;
import com.ghee.pojo.Councils;
import java.util.List;
import java.util.Map;

/**
 *
 * @author giahu
 */
public interface CouncilRepository {
    Map<String, Object> getCouncils(Map<String, String> params);
    Map<String, Object> getMyCouncils(long userId, Map<String, String> params);
    List<CouncilMembers> findByCouncilIdAndRoles(Long councilId, List<CouncilMemberRole> roles);
    
    Councils createOrUpdateCouncil(Councils council);
    Councils getCouncilById(long id);
    
    void deleteCouncil(long id);
}
