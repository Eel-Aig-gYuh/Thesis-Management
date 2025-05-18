/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.repositories;

import com.ghee.enums.CouncilMemberRole;
import com.ghee.pojo.Scores;
import java.util.List;

/**
 *
 * @author giahu
 */
public interface ScoreRepository {
    List<Scores> createOrUpdateScores (List<Scores> scores);
    
    Scores getScoreById(long id);
    List<Scores> getScoreByThesisAndRole(long thesisId, List<CouncilMemberRole> role);
}
