/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.repositories.impl;

import com.ghee.dto.AverageMemberDTO;
import com.ghee.dto.AverageScoreDTO;
import com.ghee.dto.CriteriaDTO;
import com.ghee.dto.CriteriaScoreDTO;
import com.ghee.dto.MemberScoreDTO;
import com.ghee.dto.ScoringCriteriaResponse;
import com.ghee.dto.ScoringRequest;
import com.ghee.dto.ScoringResponse;
import com.ghee.dto.ScoringScore;
import com.ghee.dto.StatisticDTO;
import com.ghee.enums.CouncilMemberRole;
import com.ghee.pojo.CouncilMembers;
import com.ghee.pojo.CouncilTheses;
import com.ghee.pojo.Councils;
import com.ghee.pojo.Criteria;
import com.ghee.pojo.Scores;
import com.ghee.pojo.Theses;
import com.ghee.pojo.ThesisCriteria;
import com.ghee.pojo.Users;
import com.ghee.repositories.CouncilRepository;
import com.ghee.repositories.ScoreRepository;
import com.ghee.repositories.ThesisRepository;
import com.ghee.repositories.UserRepository;
import com.ghee.utils.DateUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
public class ScoreRepositoryImpl implements ScoreRepository {

    private static final Logger logger = Logger.getLogger(ScoreRepositoryImpl.class.getName());

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private ThesisRepository thesisRepo;

    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private CouncilRepository councilRepo;

    @Override
    public List<Scores> createOrUpdateScores(List<Scores> scores) {
        Session s = this.factory.getObject().getCurrentSession();
        for (Scores score : scores) {
            if (score.getId() == null) {
                s.persist(score);
            } else {
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
    public List<Scores> getScoresByThesisId(long thesisId) {
        Session s = factory.getObject().getCurrentSession();

        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Scores> q = b.createQuery(Scores.class);
        Root<Scores> scoreRoot = q.from(Scores.class);

        // Lọc theo thesisId
        q.select(scoreRoot)
                .where(b.equal(scoreRoot.get("thesisId").get("id"), thesisId))
                // Sắp xếp theo criteriaId và councilMemberId
                .orderBy(
                        b.asc(scoreRoot.get("criteriaId").get("id")),
                        b.asc(scoreRoot.get("councilMemberId").get("id"))
                );

        Query<Scores> query = s.createQuery(q);
        return query.getResultList();
    }

    @Override
    public Map<String, Object> getScoringCriteria(Long councilId, List<Long> thesisIds, long userId) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();

        // Validate council
        Councils council = s.get(Councils.class, councilId);
        if (council == null) {
            logger.log(Level.WARNING, "Council not found: {0}", councilId);
            throw new IllegalArgumentException("Council not found: " + councilId);
        }

        // Validate theses and their association with the council
        CriteriaQuery<CouncilTheses> ctQuery = b.createQuery(CouncilTheses.class);
        Root<CouncilTheses> ctRoot = ctQuery.from(CouncilTheses.class);
        ctQuery.select(ctRoot)
               .where(
                   b.equal(ctRoot.get("councilId").get("id"), councilId),
                   ctRoot.get("thesisId").get("id").in(thesisIds)
               );
        List<CouncilTheses> councilTheses = s.createQuery(ctQuery).getResultList();

        List<Long> validThesisIds = councilTheses.stream()
                .map(ct -> ct.getThesisId().getId())
                .collect(Collectors.toList());

        if (validThesisIds.isEmpty()) {
            logger.log(Level.WARNING, "No valid theses found for council {0} and thesisIds {1}", new Object[]{councilId, thesisIds});
            throw new IllegalArgumentException("No valid theses found for the specified council");
        }

        // Fetch theses
        List<Theses> theses = thesisRepo.findByIds(validThesisIds);
        if (theses.isEmpty()) {
            logger.log(Level.WARNING, "Theses not found: {0}", validThesisIds);
            throw new IllegalArgumentException("Theses not found");
        }

        // Fetch criteria for theses
        CriteriaQuery<ThesisCriteria> tcQuery = b.createQuery(ThesisCriteria.class);
        Root<ThesisCriteria> tcRoot = tcQuery.from(ThesisCriteria.class);
        tcQuery.select(tcRoot)
               .where(tcRoot.get("thesisId").get("id").in(validThesisIds));
        List<ThesisCriteria> thesisCriteria = s.createQuery(tcQuery).getResultList();

        Map<Long, Map<Long, BigDecimal>> scoresByThesisAndCriteria = new HashMap<>();
        
        // Fetch scores for theses, criteria, and user
        CriteriaQuery<Scores> scoreQuery = b.createQuery(Scores.class);
        Root<Scores> scoreRoot = scoreQuery.from(Scores.class);
        scoreQuery.select(scoreRoot)
                  .where(
                      b.equal(scoreRoot.get("councilMemberId").get("id"), userId),
                      scoreRoot.get("thesisId").get("id").in(validThesisIds)
                  );
        List<Scores> scores = s.createQuery(scoreQuery).getResultList();
        
        for (Scores score : scores) {
            Long thesisId = score.getThesisId().getId();
            Long criteriaId = score.getCriteriaId().getId();
            scoresByThesisAndCriteria.computeIfAbsent(thesisId, k -> new HashMap<>())
                                    .put(criteriaId, score.getScore());
        }
        
        // Group criteria by thesis
        Map<Long, List<CriteriaDTO>> criteriaByThesis = new HashMap<>();
        for (ThesisCriteria tc : thesisCriteria) {
            Long thesisId = tc.getThesisId().getId();
            Criteria criteria = tc.getCriteriaId();
            
            CriteriaDTO dto = new CriteriaDTO();
            dto.setCriteriaId(criteria.getId());
            dto.setCriteriaName(criteria.getName());
            dto.setMaxScore(criteria.getMaxScore());
            dto.setScore(scoresByThesisAndCriteria.getOrDefault(thesisId, Collections.emptyMap())
                                                .get(criteria.getId()));
            criteriaByThesis.computeIfAbsent(thesisId, k -> new ArrayList<>()).add(dto);
        }

        // Build response as a Map
        Map<String, Object> response = new HashMap<>();
        for (Theses thesis : theses) {
            Map<String, Object> thesisDetails = new HashMap<>();
            thesisDetails.put("thesisTitle", thesis.getTitle());
            thesisDetails.put("criteria", criteriaByThesis.getOrDefault(thesis.getId(), Collections.emptyList()));
            response.put(thesis.getId().toString(), thesisDetails);
        }

        return response;
    }

    @Override
    public ScoringResponse submitScores(ScoringRequest request) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();

        // Validate council
        Councils council = s.get(Councils.class, request.getCouncilId());
        if (council == null) {
            logger.log(Level.WARNING, "Council not found: {0}", request.getCouncilId());
            throw new IllegalArgumentException("Council not found: " + request.getCouncilId());
        }

        // Validate user
        Users user = userRepo.getUserById(request.getUserId());
        if (user == null) {
            logger.log(Level.WARNING, "User not found: {0}", request.getUserId());
            throw new IllegalArgumentException("User not found: " + request.getUserId());
        }

        // Validate council membership
        CriteriaQuery<CouncilMembers> cmQuery = b.createQuery(CouncilMembers.class);
        Root<CouncilMembers> cmRoot = cmQuery.from(CouncilMembers.class);
        cmQuery.select(cmRoot)
               .where(
                   b.equal(cmRoot.get("councilId").get("id"), request.getCouncilId()),
                   b.equal(cmRoot.get("memberId").get("id"), request.getUserId())
               );
        if (s.createQuery(cmQuery).getResultList().isEmpty()) {
            logger.log(Level.WARNING, "User {0} is not a member of council {1}", new Object[]{request.getUserId(), request.getCouncilId()});
            throw new IllegalArgumentException("User is not a member of the council");
        }

        // Validate thesis
        Theses thesis = s.get(Theses.class, request.getThesisId());
        if (thesis == null) {
            logger.log(Level.WARNING, "Thesis not found: {0}", request.getThesisId());
            throw new IllegalArgumentException("Thesis not found: " + request.getThesisId());
        }

        // Validate thesis in council
        CriteriaQuery<CouncilTheses> ctQuery = b.createQuery(CouncilTheses.class);
        Root<CouncilTheses> ctRoot = ctQuery.from(CouncilTheses.class);
        ctQuery.select(ctRoot)
               .where(
                   b.equal(ctRoot.get("councilId").get("id"), request.getCouncilId()),
                   b.equal(ctRoot.get("thesisId").get("id"), request.getThesisId())
               );
        if (s.createQuery(ctQuery).getResultList().isEmpty()) {
            logger.log(Level.WARNING, "Thesis {0} is not in council {1}", new Object[]{request.getThesisId(), request.getCouncilId()});
            throw new IllegalArgumentException("Thesis is not assigned to the council");
        }

        // Validate criteria and scores
        List<Long> criteriaIds = request.getScores().stream()
                .map(ScoringScore::getCriteriaId)
                .collect(Collectors.toList());
        CriteriaQuery<ThesisCriteria> tcQuery = b.createQuery(ThesisCriteria.class);
        Root<ThesisCriteria> tcRoot = tcQuery.from(ThesisCriteria.class);
        tcQuery.select(tcRoot)
               .where(
                   b.equal(tcRoot.get("thesisId").get("id"), request.getThesisId()),
                   tcRoot.get("criteriaId").get("id").in(criteriaIds)
               );
        List<ThesisCriteria> validCriteria = s.createQuery(tcQuery).getResultList();
        Map<Long, Criteria> criteriaMap = validCriteria.stream()
                .collect(Collectors.toMap(tc -> tc.getCriteriaId().getId(), ThesisCriteria::getCriteriaId));

        int scoresSubmitted = 0;
        for (ScoringScore entry : request.getScores()) {
            Criteria criteria = criteriaMap.get(entry.getCriteriaId());
            if (criteria == null) {
                logger.log(Level.WARNING, "Invalid criteria {0} for thesis {1}", new Object[]{entry.getCriteriaId(), request.getThesisId()});
                throw new IllegalArgumentException("Invalid criteria: " + entry.getCriteriaId());
            }

            // Validate score
            if (entry.getScore() == null || entry.getScore().compareTo(BigDecimal.ZERO) < 0 ||
                entry.getScore().compareTo(criteria.getMaxScore()) > 0) {
                logger.log(Level.WARNING, "Invalid score {0} for criteria {1}", new Object[]{entry.getScore(), entry.getCriteriaId()});
                throw new IllegalArgumentException("Score must be between 0 and " + criteria.getMaxScore());
            }

            // Check for existing score
            CriteriaQuery<Scores> scoreQuery = b.createQuery(Scores.class);
            Root<Scores> scoreRoot = scoreQuery.from(Scores.class);
            scoreQuery.select(scoreRoot)
                      .where(
                          b.equal(scoreRoot.get("thesisId").get("id"), request.getThesisId()),
                          b.equal(scoreRoot.get("criteriaId").get("id"), entry.getCriteriaId()),
                          b.equal(scoreRoot.get("councilMemberId").get("id"), request.getUserId())
                      );
            if (!s.createQuery(scoreQuery).getResultList().isEmpty()) {
                logger.log(Level.WARNING, "Score already exists for thesis {0}, criteria {1}, user {2}",
                        new Object[]{request.getThesisId(), entry.getCriteriaId(), request.getUserId()});
                throw new IllegalArgumentException("Score already submitted for criteria: " + entry.getCriteriaId());
            }

            // Save score
            Scores score = new Scores();
            score.setScore(entry.getScore());
            score.setThesisId(thesis);
            score.setCriteriaId(criteria);
            score.setCouncilMemberId(user);
            score.setCreatedAt(DateUtils.getTodayWithoutTime());
            s.persist(score);
            scoresSubmitted++;
        }

        s.flush();

        ScoringResponse response = new ScoringResponse();
        response.setMessage("Scores submitted successfully");
        response.setThesisId(request.getThesisId());
        response.setUserId(request.getUserId());
        response.setScoresSubmitted(scoresSubmitted);

        logger.log(Level.INFO, "Submitted {0} scores for thesis {1} by user {2}",
                new Object[]{scoresSubmitted, request.getThesisId(), request.getUserId()});
        return response;
    }
    
    @Override
    public AverageScoreDTO calculateAverageScoreByThesis(long thesisId) {
        Session s = this.factory.getObject().getCurrentSession();

        Theses thesis = thesisRepo.getThesisById(thesisId);

        // Lấy tất cả điểm số
        List<Scores> scores = this.getScoresByThesisId(thesisId);
        List<AverageMemberDTO> memberAverages = new ArrayList<>();
        double averageScore = 0.0;

        if (!scores.isEmpty()) {
            // Lấy councilId từ council_theses
            Long councilId = thesis.getCouncilThesesSet().stream()
                    .findFirst()
                    .map(ct -> ct.getCouncilId().getId())
                    .orElseThrow(() -> new IllegalStateException("No council associated with thesis"));

            // Lấy danh sách thành viên MEMBER và REVIEWER
            List<CouncilMembers> eligibleMembers = this.councilRepo.findByCouncilIdAndRoles(
                    councilId, Arrays.asList(CouncilMemberRole.MEMBER, CouncilMemberRole.REVIEWER));

            // Nhóm điểm theo thành viên hội đồng
            Map<Long, List<Scores>> scoresByMember = scores.stream()
                    .collect(Collectors.groupingBy(score -> score.getCouncilMemberId().getId()));

            // Tạo memberAverages với tổng điểm
            for (Map.Entry<Long, List<Scores>> entry : scoresByMember.entrySet()) {
                Long memberId = entry.getKey();
                List<Scores> memberScores = entry.getValue();
                Users member = userRepo.getUserById(memberId);

                // Tính tổng điểm của thành viên
                double memberTotal = memberScores.stream()
                        .map(Scores::getScore)
                        .mapToDouble(BigDecimal::doubleValue)
                        .sum();

                String memberName = (member.getFirstname() != null && member.getLastname() != null)
                        ? member.getFirstname() + " " + member.getLastname()
                        : member.getUsername();

                memberAverages.add(new AverageMemberDTO(memberId, memberName, memberTotal));
            }

            // Tính averageScore từ thành viên MEMBER và REVIEWER
            List<Double> memberTotals = eligibleMembers.stream()
                    .filter(cm -> scoresByMember.containsKey(cm.getMemberId().getId()))
                    .map(cm -> scoresByMember.get(cm.getMemberId().getId()).stream()
                            .map(Scores::getScore)
                            .mapToDouble(BigDecimal::doubleValue)
                            .sum())
                    .collect(Collectors.toList());

            averageScore = memberTotals.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);

            // Lưu vào Theses
            thesis.setAverageScore(BigDecimal.valueOf(averageScore));
            this.thesisRepo.updateThesis(thesis);
        }

        return new AverageScoreDTO(averageScore, memberAverages);
    }
    
    @Override
    public List<StatisticDTO.ThesisScoreStatsResponse> getThesisScoreStatistics(String startYear, String endYear) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();

        // Fetch average scores and group by year
        Query<Object[]> query = s.createQuery(
            "SELECT SUBSTRING(t.semester, 1, 4) AS year, AVG(s.score) AS avgScore " +
            "FROM Theses t JOIN Scores s ON t.id = s.thesisId.id " +
            "WHERE SUBSTRING(t.semester, 1, 4) BETWEEN :startYear AND :endYear " +
            "GROUP BY year ORDER BY year", Object[].class
        );
        query.setParameter("startYear", startYear);
        query.setParameter("endYear", endYear);

        List<Object[]> results = query.getResultList();
        List<StatisticDTO.ThesisScoreStatsResponse> response = new ArrayList<>();

        for (Object[] result : results) {
            String year = (String) result[0];
            BigDecimal avgScore = (BigDecimal) result[1];

            // Fetch score distribution
            Query<Object[]> distQuery = s.createQuery(
                "SELECT CASE " +
                "WHEN s.score < 4 THEN '0-4' " +
                "WHEN s.score < 6 THEN '4-6' " +
                "WHEN s.score < 8 THEN '6-8' " +
                "ELSE '8-10' END AS range, COUNT(*) AS count " +
                "FROM Theses t JOIN Scores s ON t.id = s.thesisId.id " +
                "WHERE SUBSTRING(t.semester, 1, 4) = :year " +
                "GROUP BY range", Object[].class
            );
            distQuery.setParameter("year", year);
            List<Object[]> distResults = distQuery.getResultList();

            Map<String, Integer> scoreDistribution = new HashMap<>();
            for (Object[] dist : distResults) {
                scoreDistribution.put((String) dist[0], ((Number) dist[1]).intValue());
            }

            StatisticDTO.ThesisScoreStatsResponse stats = new StatisticDTO.ThesisScoreStatsResponse();
            stats.setYear(year);
            stats.setAverageScore(avgScore.setScale(2, BigDecimal.ROUND_HALF_UP));
            stats.setScoreDistribution(scoreDistribution);
            response.add(stats);
        }

        logger.log(Level.INFO, "Retrieved score statistics for years {0} to {1}", 
                   new Object[]{startYear, endYear});
        return response;
    }

    @Override
    public List<StatisticDTO.ThesisParticipationResponse> getThesisParticipationByDepartment(String startYear, String endYear) {
        Session s = factory.getObject().getCurrentSession();
        Query<Object[]> query = s.createQuery(
            "SELECT SUBSTRING(t.semester, 1, 4) AS year, d.name, COUNT(t.id) AS count " +
            "FROM Theses t JOIN Departments d ON t.departmentId.id = d.id " +
            "WHERE SUBSTRING(t.semester, 1, 4) BETWEEN :startYear AND :endYear " +
            "GROUP BY year, d.name ORDER BY year, d.name", Object[].class
        );
        query.setParameter("startYear", startYear);
        query.setParameter("endYear", endYear);

        List<Object[]> results = query.getResultList();
        Map<String, StatisticDTO.ThesisParticipationResponse> responseMap = new HashMap<>();

        for (Object[] result : results) {
            String year = (String) result[0];
            String department = (String) result[1];
            Integer count = ((Number) result[2]).intValue();

            StatisticDTO.ThesisParticipationResponse resp = responseMap.computeIfAbsent(year, k -> {
                StatisticDTO.ThesisParticipationResponse r = new StatisticDTO.ThesisParticipationResponse();
                r.setYear(year);
                r.setParticipationByDepartment(new HashMap<>());
                return r;
            });
            resp.getParticipationByDepartment().put(department, count);
        }

        logger.log(Level.INFO, "Retrieved participation stats for years {0} to {1}", 
                   new Object[]{startYear, endYear});
        return new ArrayList<>(responseMap.values());
    }

    @Override
    public List<StatisticDTO.ThesisAverageScoreReport> getThesisAverageScoresForReport(String year) {
        Session s = factory.getObject().getCurrentSession();
        Query<StatisticDTO.ThesisAverageScoreReport> query = s.createQuery(
            "SELECT NEW com.ghee.dto.StatisticsDTOs.ThesisAverageScoreReport(" +
            "t.id, t.title, d.name, t.semester, AVG(s.score)) " +
            "FROM Theses t JOIN Departments d ON t.departmentId.id = d.id " +
            "JOIN Scores s ON t.id = s.thesisId.id " +
            "WHERE SUBSTRING(t.semester, 1, 4) = :year " +
            "GROUP BY t.id, t.title, d.name, t.semester " +
            "ORDER BY t.id", StatisticDTO.ThesisAverageScoreReport.class
        );
        query.setParameter("year", year);

        List<StatisticDTO.ThesisAverageScoreReport> results = query.getResultList();
        results.forEach(r -> {
            if (r.getAverageScore() != null) {
                r.setAverageScore(r.getAverageScore().setScale(2, BigDecimal.ROUND_HALF_UP));
            }
        });

        logger.log(Level.INFO, "Retrieved {0} theses for average score report for year {1}", 
                   new Object[]{results.size(), year});
        return results;
    }

    @Override
    public List<Scores> getScoreByThesisAndRole(long thesisId, List<CouncilMemberRole> role) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<Scores> scoreQuery = s.createQuery(
                "FROM Scores s WHERE s.thesisId.id = :thesisId AND EXISTS ("
                + "SELECT 1 FROM CouncilMembers cm WHERE cm.councilId IN ("
                + "SELECT ct.councilId FROM CouncilTheses ct WHERE ct.thesisId.id = :thesisId) "
                + "AND cm.memberId.id = s.councilMemberId.id AND cm.role IN (:roles))",
                Scores.class
        );

        scoreQuery.setParameter("thesisId", thesisId);
        scoreQuery.setParameter("roles", Arrays.asList(CouncilMemberRole.REVIEWER.name(), CouncilMemberRole.MEMBER.name()));
        List<Scores> scores = scoreQuery.list();

        return scores;
    }

    @Override
    public Map<String, Object> getScoreDetailsByThesisId(long thesisId) {
        Session s = this.factory.getObject().getCurrentSession();

        // Kiểm tra khóa luận
        Theses thesis = this.thesisRepo.getThesisById(thesisId);
        if (thesis == null) {
            logger.log(Level.SEVERE, "Thesis not found: {}", thesisId);
            throw new IllegalArgumentException("Thesis not found: " + thesisId);
        }

        // Lấy councilId từ CouncilTheses
        Long councilId = null;
        Set<CouncilTheses> councilThesesSet = thesis.getCouncilThesesSet();
        if (councilThesesSet != null && !councilThesesSet.isEmpty()) {
            councilId = councilThesesSet.iterator().next().getCouncilId().getId();
        }
        if (councilId == null) {
            logger.log(Level.SEVERE, "No council associated with thesisId: {}", thesisId);
            throw new IllegalStateException("Thesis is not associated with any council");
        }

        // Sử dụng Criteria API để lấy Scores
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Scores> q = b.createQuery(Scores.class);
        Root<Scores> scoreRoot = q.from(Scores.class);

        // Join với Criteria và Users
        Join<Scores, Criteria> criteriaJoin = scoreRoot.join("criteriaId");
        Join<Scores, Users> userJoin = scoreRoot.join("councilMemberId");

        // Lọc theo thesisId
        q.select(scoreRoot)
                .where(b.equal(scoreRoot.get("thesisId").get("id"), thesisId))
                .orderBy(b.asc(criteriaJoin.get("id")), b.asc(userJoin.get("id")));

        // Thực thi query
        Query<Scores> query = s.createQuery(q);
        List<Scores> scores = query.getResultList();

        // Nhóm dữ liệu theo criteriaId
        Map<Long, List<Scores>> scoresByCriteria = scores.stream()
                .collect(Collectors.groupingBy(score -> score.getCriteriaId().getId()));

        // Map sang CriteriaScoreDTO
        List<CriteriaScoreDTO> criteriaScoreDTOs = scoresByCriteria.entrySet().stream()
                .map(entry -> {
                    Long criteriaId = entry.getKey();
                    List<Scores> criteriaScores = entry.getValue();
                    Criteria criteria = criteriaScores.get(0).getCriteriaId();

                    // Map memberScore
                    List<MemberScoreDTO> memberScoreDTOs = criteriaScores.stream()
                            .map(score -> {
                                Users member = score.getCouncilMemberId();
                                String memberName = (member.getFirstname() != null && member.getLastname() != null)
                                        ? member.getFirstname() + " " + member.getLastname()
                                        : member.getUsername();
                                return new MemberScoreDTO(
                                        score.getScore(),
                                        memberName,
                                        member.getId()
                                );
                            })
                            .collect(Collectors.toList());

                    return new CriteriaScoreDTO(
                            criteriaId,
                            criteria.getName(),
                            criteria.getMaxScore(),
                            memberScoreDTOs
                    );
                })
                .sorted(Comparator.comparing(CriteriaScoreDTO::getCriteriaId))
                .collect(Collectors.toList());

        // Chuẩn bị kết quả
        Map<String, Object> result = new HashMap<>();
        result.put("thesisId", thesisId);
        result.put("averageScore", thesis.getAverageScore() != null ? thesis.getAverageScore() : BigDecimal.ZERO);
        result.put("councilId", councilId);
        result.put("scores", criteriaScoreDTOs);

        logger.log(Level.INFO, String.format("Found {%d} criteria with scores for thesisId: {%d}", criteriaScoreDTOs.size(), thesisId));
        return result;
    }

}
