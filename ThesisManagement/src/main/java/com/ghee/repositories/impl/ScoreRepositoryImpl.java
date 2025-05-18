/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.repositories.impl;

import com.ghee.enums.CouncilMemberRole;
import com.ghee.pojo.Scores;
import com.ghee.repositories.ScoreRepository;
import java.util.Arrays;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author giahu
 */
@Repository
@Transactional
public class ScoreRepositoryImpl implements ScoreRepository{
    
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Scores> createOrUpdateScores(List<Scores> scores) {
        Session s = this.factory.getObject().getCurrentSession();
        for (Scores score: scores) {
            if (score.getId() == null) {
                s.persist(score);
            }
            else {
                s.merge(score);
            }
            s.refresh(score);
        }
        
        return scores;
    }

    @Override
    public Scores getScoreById(long id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Scores.class, id);
    }

    @Override
    public List<Scores> getScoreByThesisAndRole(long thesisId, List<CouncilMemberRole> role) {
        Session s = this.factory.getObject().getCurrentSession();
        
        Query<Scores> scoreQuery = s.createQuery(
                "FROM Scores s WHERE s.thesisId.id = :thesisId AND EXISTS ("
                + "SELECT 1 FROM CouncilMembers cm WHERE cm.councilId IN ("
                + "SELECT ct.council FROM CouncilTheses ct WHERE ct.thesisId.id = :thesisId) "
                + "AND cm.memberId.id = s.councilMember.id AND cm.role IN (:roles))",
                 Scores.class
        );
        
        scoreQuery.setParameter("thesisId", thesisId);
        scoreQuery.setParameter("roles", Arrays.asList(CouncilMemberRole.REVIEWER.name(), CouncilMemberRole.MEMBER.name()));
        List<Scores> scores = scoreQuery.list();
        
        return scores;
    }
    
    
}
