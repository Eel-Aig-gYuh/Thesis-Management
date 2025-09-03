/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.repositories.impl;

import com.ghee.dto.ThesisFileResponse;
import com.ghee.enums.CouncilMemberRole;
import com.ghee.pojo.CouncilMembers;
import com.ghee.pojo.CouncilTheses;
import com.ghee.pojo.Scores;
import com.ghee.pojo.Theses;
import com.ghee.pojo.ThesisAdvisors;
import com.ghee.pojo.ThesisCriteria;
import com.ghee.pojo.ThesisFiles;
import com.ghee.pojo.ThesisReviewers;
import com.ghee.pojo.ThesisStudents;
import com.ghee.pojo.Users;
import com.ghee.repositories.ScoreRepository;
import com.ghee.repositories.ThesisRepository;
import com.ghee.repositories.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author giahu
 */
@Repository
@Transactional
@PropertySource("classpath:application.properties")
public class ThesisRepositoryImpl implements ThesisRepository {

    private static final Logger logger = Logger.getLogger(UserRepositoryImpl.class.getName());

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private Environment env;

    @Autowired
    private ScoreRepository scoreRepo;

    @Autowired
    private UserRepository userRepo;

    // ==================================== GHEE
    @Override
    public Theses createOrUpdate(Theses theses) {
        Session s = this.factory.getObject().getCurrentSession();

        if (theses.getId() == null) {
            logger.log(Level.INFO, "Starting transaction for creating thesis: {0}", theses.getTitle());
            s.persist(theses);
            s.flush();
        } else {
            logger.log(Level.INFO, "Starting transaction for updating thesis: {0}", theses.getTitle());
            s.merge(theses);
        }
        s.refresh(theses);

        return theses;
    }

    /**
     * Tạo khóa luận.
     *
     * @param theses: khóa luận muốn tạo.
     * @return
     */
    @Override
    public Theses createThesis(Theses theses) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            logger.log(Level.INFO, "Starting transaction for creating thesis: {0}", theses.getTitle());

            s.persist(theses);
            s.flush();
            s.refresh(theses);

            logger.log(Level.INFO, "Thesis created successfully: {0}", theses.getTitle());
            return theses;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create thesis: " + e.getMessage(), e);
        }
    }

    /**
     * Cập nhật khóa luận.
     *
     * @param theses
     * @return
     */
    @Override
    public Theses updateThesis(Theses theses) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            logger.log(Level.INFO, "Starting transaction for updating thesis: {0}", theses.getTitle());

            s.merge(theses);
            s.flush();
            s.refresh(theses);

            logger.log(Level.INFO, "Thesis updated successfully: {0}", theses.getTitle());
            return theses;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update thesis: " + e.getMessage(), e);
        }
    }

    /**
     * Lấy khoá luận theo id.
     *
     * @param id
     * @return
     */
    @Override
    public Theses getThesisById(long id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Theses.class, id);
    }

    @Override
    public Map<String, Object> getFileUrlsByThesisId(long thesisId) {
        Session s = this.factory.getObject().getCurrentSession();

        Theses thesis = s.get(Theses.class, thesisId);
        if (thesis == null) {
            logger.log(Level.WARNING, "Thesis not found: {}", thesisId);
            throw new IllegalArgumentException("Thesis not found: " + thesisId);
        }

        // Sử dụng Criteria API để lấy ThesisFiles
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<ThesisFiles> q = b.createQuery(ThesisFiles.class);
        Root<ThesisFiles> fileRoot = q.from(ThesisFiles.class);

        // Lọc theo thesisId
        q.select(fileRoot)
                .where(b.equal(fileRoot.get("thesisId").get("id"), thesisId))
                .orderBy(b.asc(fileRoot.get("submittedAt")));

        // Thực thi query
        Query<ThesisFiles> query = s.createQuery(q);
        List<ThesisFiles> files = query.getResultList();

        // Map sang ThesisFileDTO
        List<ThesisFileResponse> fileDTOs = files.stream()
                .map(file -> new ThesisFileResponse(
                file.getId(),
                file.getThesisId().getId(),
                file.getStudentId().getId(),
                file.getFileUrl(),
                file.getFileName(),
                file.getSubmittedAt())
                ).collect(Collectors.toList());

        // Chuẩn bị kết quả
        Map<String, Object> result = new HashMap<>();
        result.put("thesisId", thesisId);
        result.put("files", fileDTOs);

        return result;
    }

    /**
     * Lấy danh sách các khóa luận có phân trang.
     *
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> getTheses(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();

        // lấy danh sách khóa luận.
        CriteriaQuery<Theses> q = b.createQuery(Theses.class);
        Root root = q.from(Theses.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            // Lọc theo title
            String title = params.get("title");
            if (title != null && !title.isEmpty()) {
                predicates.add(b.like(root.get("title"), String.format("%%%s%%", title)));
            }

            // Lọc theo status
            String status = params.get("status");
            if (status != null && !status.isEmpty()) {
                predicates.add(b.equal(root.get("status"), status));
            }

            // Sắp xếp.
            q.where(predicates.toArray(Predicate[]::new));
            String order = params.get("order");
            if ("asc".equalsIgnoreCase(order)) {
                q.orderBy(b.asc(root.get("id")));
            } else {
                q.orderBy(b.desc(root.get("id"))); // mặc định
            }
        }

        Query query = s.createQuery(q);

        // phân trang.
        int pageSize = Integer.parseInt(env.getProperty("page.size"));
        if (params != null && params.containsKey("page")) {
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;

            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }
        List<Theses> theses = query.getResultList();

        // tính tổng số bản ghi
        CriteriaQuery<Long> countQuery = b.createQuery(Long.class);
        countQuery.select(b.count(countQuery.from(Theses.class)));
        Long totalRecords = s.createQuery(countQuery).getSingleResult();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        Map<String, Object> result = new HashMap<>();
        result.put("theses", theses);
        result.put("totalPages", totalPages);

        return result;
    }

    @Override
    public Map<String, Object> getTheseWithoutCriteria(Map<String, String> params) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();

        // Main query
        CriteriaQuery<Theses> q = b.createQuery(Theses.class);
        Root<Theses> root = q.from(Theses.class);
        Subquery<Long> subquery = q.subquery(Long.class);
        Root<ThesisCriteria> subRoot = subquery.from(ThesisCriteria.class);

        subquery.select(subRoot.get("thesisId").get("id"))
                .where(b.equal(subRoot.get("thesisId").get("id"), root.get("id")));

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.not(b.exists(subquery)));

        if (params != null) {
            // Filter by title
            String title = params.get("title");
            if (title != null && !title.trim().isEmpty()) {
                predicates.add(b.like(b.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }

            // Filter by status
            String status = params.get("status");
            if (status != null && !status.trim().isEmpty()) {
                predicates.add(b.equal(root.get("status"), status));
            }

            // Sorting
            String order = params.getOrDefault("order", "desc");
            if ("asc".equalsIgnoreCase(order)) {
                q.orderBy(b.asc(root.get("id")));
            } else {
                q.orderBy(b.desc(root.get("id")));
            }
        }

        q.select(root)
                .where(predicates.toArray(Predicate[]::new));

        Query<Theses> query = s.createQuery(q);

        // Pagination
        int pageSize = Integer.parseInt(env.getProperty("page.size", "10"));
        int page = 1;
        try {
            page = params != null && params.containsKey("page") ? Integer.parseInt(params.getOrDefault("page", "1")) : 1;
            page = Math.max(1, page);
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid page number: {0}", params.get("page"));
        }

        int start = (page - 1) * pageSize;
        query.setMaxResults(pageSize);
        query.setFirstResult(start);

        List<Theses> theses = query.getResultList();

        // Tính tổng số bản ghi
        CriteriaQuery<Long> countQuery = b.createQuery(Long.class);
        Root<Theses> countRoot = countQuery.from(Theses.class);
        Join<Theses, ThesisCriteria> countJoin = countRoot.join("thesisCriteriasSet", JoinType.LEFT);
        countQuery.select(b.count(countRoot)).where(b.isNull(countJoin.get("id")));
        Long totalRecords = s.createQuery(countQuery).getSingleResult();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        
        // Prepare response
        Map<String, Object> result = new HashMap<>();
        result.put("theses", theses);
        result.put("totalRecords", totalRecords);
        result.put("totalPages", totalPages);
        result.put("page", page);
        result.put("pageSize", pageSize);

        logger.log(Level.INFO, "Found {0} theses without criteria", totalRecords);
        return result;
    }

    @Override
    public Map<String, Object> getMyThesis(long userId, Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();

        Users u = this.userRepo.getUserById(userId);
        if (u == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        // Truy vấn danh sách khóa luận chưa có hội đồng
        CriteriaQuery<Theses> q = b.createQuery(Theses.class);
        Root<Theses> root = q.from(Theses.class);
        q.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();

        // Subqueries để lấy thesisId theo vai trò
        // 1. Sinh viên (ThesisStudents)
        Subquery<Long> studentSubquery = q.subquery(Long.class);
        Root<ThesisStudents> studentRoot = studentSubquery.from(ThesisStudents.class);
        studentSubquery.select(studentRoot.get("thesisId").get("id"))
                .where(b.equal(studentRoot.get("studentId").get("id"), userId));

        // 2. Giảng viên hướng dẫn (ThesisAdvisors)
        Subquery<Long> advisorSubquery = q.subquery(Long.class);
        Root<ThesisAdvisors> advisorRoot = advisorSubquery.from(ThesisAdvisors.class);
        advisorSubquery.select(advisorRoot.get("thesisId").get("id"))
                .where(b.equal(advisorRoot.get("advisorId").get("id"), userId));
        

        // Kết hợp các vai trò
        predicates.add(b.or(
                root.get("id").in(studentSubquery),
                root.get("id").in(advisorSubquery)
        ));

        // Lọc theo title
        String title = params != null ? params.get("title") : null;
        if (title != null && !title.trim().isEmpty()) {
            predicates.add(b.like(b.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        // Lọc theo status
        String status = params != null ? params.get("status") : null;
        if (status != null && !status.trim().isEmpty()) {
            predicates.add(b.equal(root.get("status"), status.toUpperCase()));
        }

        // Áp dụng predicates
        q.where(predicates.toArray(new Predicate[0]));

        // Sắp xếp
        String order = params != null ? params.getOrDefault("order", "desc") : "desc";
        if ("asc".equalsIgnoreCase(order)) {
            q.orderBy(b.asc(root.get("id")));
        } else {
            q.orderBy(b.desc(root.get("id")));
        }

        // Tạo query
        Query<Theses> query = s.createQuery(q);

        // Phân trang
        int pageSize = Integer.parseInt(env.getProperty("page.size", "10"));
        int page = 1;
        if (params != null && params.containsKey("page")) {
            try {
                page = Integer.parseInt(params.get("page"));
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                logger.log(Level.WARNING, "Invalid page number: {}, defaulting to 1", params.get("page"));
            }
            int start = (page - 1) * pageSize;
            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        // Lấy danh sách luận văn
        List<Theses> theses = query.getResultList();

        // Tính tổng số bản ghi
        CriteriaQuery<Long> countQuery = b.createQuery(Long.class);
        Root<Theses> countRoot = countQuery.from(Theses.class);
        countQuery.select(b.countDistinct(countRoot));

        List<Predicate> countPredicates = new ArrayList<>();
        countPredicates.add(b.or(
                countRoot.get("id").in(studentSubquery),
                countRoot.get("id").in(advisorSubquery)
        ));

        if (title != null && !title.trim().isEmpty()) {
            countPredicates.add(b.like(b.lower(countRoot.get("title")), "%" + title.toLowerCase() + "%"));
        }
        if (status != null && !status.trim().isEmpty()) {
            countPredicates.add(b.equal(countRoot.get("status"), status.toUpperCase()));
        }

        countQuery.where(countPredicates.toArray(new Predicate[0]));
        Long totalItems = s.createQuery(countQuery).getSingleResult();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        // Chuẩn bị kết quả
        Map<String, Object> result = new HashMap<>();
        result.put("theses", theses);
        result.put("totalItems", totalItems);
        result.put("totalPages", totalPages);
        result.put("currentPage", page);
        result.put("pageSize", pageSize);

        return result;
    }

    @Override
    public Map<String, Object> getMyThesisInCouncil(long userId, Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();

        Users u = this.userRepo.getUserById(userId);
        if (u == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        // Main query
        CriteriaQuery<Theses> q = b.createQuery(Theses.class);
        Root<Theses> root = q.from(Theses.class);
        Join<Theses, CouncilTheses> councilThesesJoin = root.join("councilThesesSet", JoinType.INNER);
        Join<CouncilTheses, CouncilMembers> councilMembersJoin = councilThesesJoin.join("councilId", JoinType.INNER).join("councilMembersSet", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.equal(councilMembersJoin.get("memberId").get("id"), userId));

        // Lọc theo title
        String title = params != null ? params.get("title") : null;
        if (title != null && !title.trim().isEmpty()) {
            predicates.add(b.like(b.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        // Lọc theo status
        String status = params != null ? params.get("status") : null;
        if (status != null && !status.trim().isEmpty()) {
            predicates.add(b.equal(root.get("status"), status.toUpperCase()));
        }

        // Áp dụng predicates
        q.where(predicates.toArray(new Predicate[0]));

        // Sắp xếp
        String order = params != null ? params.getOrDefault("order", "desc") : "desc";
        if ("asc".equalsIgnoreCase(order)) {
            q.orderBy(b.asc(root.get("id")));
        } else {
            q.orderBy(b.desc(root.get("id")));
        }

        // Tạo query
        Query<Theses> query = s.createQuery(q);

        // Phân trang
        int pageSize = Integer.parseInt(env.getProperty("page.size", "10"));
        int page = 1;
        if (params != null && params.containsKey("page")) {
            try {
                page = Integer.parseInt(params.get("page"));
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                logger.log(Level.WARNING, "Invalid page number: {}, defaulting to 1", params.get("page"));
            }
            int start = (page - 1) * pageSize;
            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        // Lấy danh sách luận văn
        List<Theses> theses = query.getResultList();

        // Tính tổng số bản ghi
        CriteriaQuery<Long> countQuery = b.createQuery(Long.class);
        Root<Theses> countRoot = countQuery.from(Theses.class);
        countQuery.select(b.countDistinct(countRoot));

        List<Predicate> countPredicates = new ArrayList<>();
        countPredicates.add(b.or(
                countRoot.get("id").in(councilThesesJoin)
        ));

        if (title != null && !title.trim().isEmpty()) {
            countPredicates.add(b.like(b.lower(countRoot.get("title")), "%" + title.toLowerCase() + "%"));
        }
        if (status != null && !status.trim().isEmpty()) {
            countPredicates.add(b.equal(countRoot.get("status"), status.toUpperCase()));
        }

        countQuery.where(countPredicates.toArray(new Predicate[0]));
        Long totalItems = s.createQuery(countQuery).getSingleResult();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        // Chuẩn bị kết quả
        Map<String, Object> result = new HashMap<>();
        result.put("theses", theses);
        result.put("totalItems", totalItems);
        result.put("totalPages", totalPages);
        result.put("currentPage", page);
        result.put("pageSize", pageSize);

        return result;
    }
    
    @Override
    public Map<String, Object> getThesesWithoutCouncil(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();

        // Truy vấn danh sách khóa luận chưa có hội đồng
        CriteriaQuery<Theses> q = b.createQuery(Theses.class);
        Root<Theses> thesisRoot = q.from(Theses.class);
        Join<Theses, CouncilTheses> councilThesesJoin = thesisRoot.join("councilThesesSet", JoinType.LEFT);

        // Điều kiện: Không có bản ghi trong council_theses
        q.select(thesisRoot).where(b.isNull(councilThesesJoin.get("id")));

        // Xử lý tham số lọc
        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            // Lọc theo semester
            String semester = params.get("semester");
            if (semester != null && !semester.isEmpty()) {
                predicates.add(b.equal(thesisRoot.get("semester"), semester));
            }

            // Lọc theo status
            String status = params.get("status");
            if (status != null && !status.isEmpty()) {
                predicates.add(b.equal(thesisRoot.get("status"), status));
            }

            // Lọc theo title
            String title = params.get("title");
            if (title != null && !title.isEmpty()) {
                predicates.add(b.like(thesisRoot.get("title"), String.format("%%%s%%", title)));
            }

            // Sắp xếp
            if (!predicates.isEmpty()) {
                q.where(predicates.toArray(new Predicate[0]));
            }
            String order = params.get("order");
            if ("asc".equalsIgnoreCase(order)) {
                q.orderBy(b.asc(thesisRoot.get("id")));
            } else {
                q.orderBy(b.desc(thesisRoot.get("id"))); // Mặc định
            }
        }

        Query query = s.createQuery(q);

        // Phân trang
        int pageSize = Integer.parseInt(env.getProperty("page.size"));
        if (params != null && params.containsKey("page")) {
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;

            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        List<Theses> theses = query.getResultList();

        // Tính tổng số bản ghi
        CriteriaQuery<Long> countQuery = b.createQuery(Long.class);
        Root<Theses> countRoot = countQuery.from(Theses.class);
        Join<Theses, CouncilTheses> countJoin = countRoot.join("councilThesesSet", JoinType.LEFT);
        countQuery.select(b.count(countRoot)).where(b.isNull(countJoin.get("id")));
        Long totalRecords = s.createQuery(countQuery).getSingleResult();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        Map<String, Object> result = new HashMap<>();
        result.put("theses", theses);
        result.put("totalPages", totalPages);

        return result;
    }

    /**
     * Kiểm tra tiêu đề của khóa luận có tồn tại hay chưa.
     *
     * @param title
     * @return
     */
    @Override
    public boolean existsByTitle(String title) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<Theses> root = q.from(Theses.class);
        q.select(b.count(root)).where(b.equal(root.get("title"), title));
        return s.createQuery(q).getSingleResult() > 0;
    }

    /**
     * Xoá khóa luận.
     *
     * @param id
     */
    @Override
    public void deleteThesis(long id) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            logger.log(Level.INFO, "Starting transaction for deleting thesis ID: {0}", id);

            Theses theses = s.get(Theses.class, id);
            if (theses != null) {
                s.remove(theses);
            }

            logger.log(Level.INFO, "Thesis deleted successfully: {0}", id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete thesis: " + e.getMessage(), e);
        }
    }

    @Override
    public long countReviewAssignmentsByUserAndSemester(Users user, String semester) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<Long> q = s.createQuery(
                "SELECT COUNT(*) FROM ThesisReviewers tr "
                + "WHERE tr.reviewerId.id = :userId AND tr.thesisId.semester = :semester",
                Long.class
        );
        q.setParameter("userId", user.getId());
        q.setParameter("semester", semester);
        return q.getSingleResult();
    }

    @Override
    public void updateAverageScore(long id) {
        Session s = this.factory.getObject().getCurrentSession();

        Theses thesis = s.get(Theses.class, id);
        if (thesis == null) {
            throw new IllegalArgumentException("Thesis not found");
        }

        List<Scores> scores = this.scoreRepo.getScoreByThesisAndRole(id, Arrays.asList(CouncilMemberRole.REVIEWER, CouncilMemberRole.MEMBER));

        if (scores.isEmpty()) {
            logger.log(Level.INFO, "No scores found for thesis ID: {0}, setting average score to null", thesis.getId());
            thesis.setAverageScore(null);
            s.merge(thesis);
            return;
        }

        Map<Long, List<Scores>> scoresByMember = scores.stream()
                .collect(Collectors.groupingBy(score -> score.getCouncilMemberId().getId()));

        double totalAverage = 0.0;
        int memberCount = 0;

        for (List<Scores> memberScores : scoresByMember.values()) {
            double totalScore = memberScores.stream()
                    .map(Scores::getScore)
                    .mapToDouble(BigDecimal::doubleValue)
                    .sum();
            totalAverage += totalScore;
            memberCount++;
        }

        double averageScore = memberCount > 0 ? totalAverage / memberCount : 0.0;
        logger.log(Level.INFO, "Calculated average score for thesis ID: {0}: {1}", new Object[]{thesis.getId(), averageScore});

        thesis.setAverageScore(BigDecimal.valueOf(averageScore));
        s.merge(thesis);

    }

    @Override
    public ThesisFiles createOrUpdate(ThesisFiles thesisFiles) {
        Session s = this.factory.getObject().getCurrentSession();

        if (thesisFiles.getId() == null) {
            s.persist(thesisFiles);
        } else {
            s.merge(thesisFiles);
        }
        s.flush();
        return thesisFiles;
    }

    @Override
    public ThesisFiles getThesisFileById(long id) {
        Session s = this.factory.getObject().getCurrentSession();

        return s.get(ThesisFiles.class, id);
    }

    @Override
    public List<Theses> findByIds(List<Long> ids) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Theses> query = builder.createQuery(Theses.class);
        Root<Theses> root = query.from(Theses.class);

        query.select(root)
                .where(root.get("id").in(ids));

        return session.createQuery(query).getResultList();
    }

    @Override
    public List<Theses> findThesisByYear(String year) {
        Session s = factory.getObject().getCurrentSession();
        StringBuilder hql = new StringBuilder("FROM Theses t WHERE 1=1");
        if (year != null && !year.isEmpty()) {
            hql.append(" AND t.semester LIKE :year");
        }

        org.hibernate.query.Query<Theses> query = s.createQuery(hql.toString(), Theses.class);
        if (year != null && !year.isEmpty()) {
            query.setParameter("year", year + "%");
        }

        List<Theses> theses = query.list();
        logger.log(Level.INFO, "Found {0} theses for year: {1}", new Object[]{theses.size(), year});
        return theses;
    }
}
