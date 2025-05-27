/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.repositories.impl;

import com.ghee.enums.CouncilMemberRole;
import com.ghee.pojo.CouncilMembers;
import com.ghee.pojo.Councils;
import com.ghee.pojo.Users;
import com.ghee.repositories.CouncilRepository;
import com.ghee.repositories.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class CouncilRepositoryImpl implements CouncilRepository {

    private static final Logger logger = Logger.getLogger(CouncilRepositoryImpl.class.getName());

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private Environment env;

    @Autowired
    private UserRepository userRepo;

    @Override
    public Map<String, Object> getCouncils(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();

        // lấy danh sách hội đồng.
        CriteriaQuery<Councils> q = b.createQuery(Councils.class);
        Root root = q.from(Councils.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            // Lọc theo name
            String name = params.get("name");
            if (name != null && !name.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", name)));
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
        List<Councils> councils = query.getResultList();

        // tính tổng số bản ghi
        CriteriaQuery<Long> countQuery = b.createQuery(Long.class);
        countQuery.select(b.count(countQuery.from(Councils.class)));
        Long totalRecords = s.createQuery(countQuery).getSingleResult();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        Map<String, Object> result = new HashMap<>();
        result.put("councils", councils);
        result.put("totalPages", totalPages);

        return result;
    }

    @Override
    public Map<String, Object> getMyCouncils(long userId, Map<String, String> params) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();

        // Validate user
        Users user = this.userRepo.getUserById(userId);

        // Main query
        CriteriaQuery<Councils> q = b.createQuery(Councils.class);
        Root<Councils> root = q.from(Councils.class);
        Join<Councils, CouncilMembers> membersJoin = root.join("councilMembersSet", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.equal(membersJoin.get("memberId").get("id"), userId));

        if (params != null) {
            // Filter by name
            String name = params.get("name");
            if (name != null && !name.trim().isEmpty()) {
                predicates.add(b.like(b.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
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
                .where(predicates.toArray(new Predicate[0]))
                .distinct(true);

        Query<Councils> query = (Query<Councils>) s.createQuery(q);
        
        // phân trang.
        int pageSize = Integer.parseInt(env.getProperty("page.size"));
        if (params != null && params.containsKey("page")) {
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;

            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        List<Councils> councils = query.getResultList();

        // Count query
        CriteriaQuery<Long> countQuery = b.createQuery(Long.class);
        Root<Councils> countRoot = countQuery.from(Councils.class);
        Join<Councils, CouncilMembers> countMembersJoin = countRoot.join("councilMembersSet", JoinType.INNER);

        List<Predicate> countPredicates = new ArrayList<>();
        countPredicates.add(b.equal(countMembersJoin.get("memberId").get("id"), userId));

        if (params != null) {
            String name = params.get("name");
            if (name != null && !name.trim().isEmpty()) {
                countPredicates.add(b.like(b.lower(countRoot.get("name")), "%" + name.toLowerCase() + "%"));
            }
            String status = params.get("status");
            if (status != null && !status.trim().isEmpty()) {
                countPredicates.add(b.equal(countRoot.get("status"), status));
            }
        }

        countQuery.select(b.countDistinct(countRoot))
                .where(countPredicates.toArray(new Predicate[0]));

        Long totalRecords = s.createQuery(countQuery).getSingleResult();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        Map<String, Object> result = new HashMap<>();
        result.put("councils", councils);
        result.put("totalRecords", totalRecords);
        result.put("totalPages", totalPages);
        result.put("pageSize", pageSize);

        return result;
    }

    @Override
    public List<CouncilMembers> findByCouncilIdAndRoles(Long councilId, List<CouncilMemberRole> roles) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<CouncilMembers> query = builder.createQuery(CouncilMembers.class);
        Root<CouncilMembers> root = query.from(CouncilMembers.class);

        List<String> roleDTO = roles.stream().map(Enum::name).collect(Collectors.toList());

        query.select(root)
                .where(
                        builder.and(
                                builder.equal(root.get("councilId").get("id"), councilId),
                                root.get("role").in(roleDTO)
                        )
                );

        return session.createQuery(query).getResultList();
    }

    @Override
    public Councils createOrUpdateCouncil(Councils council) {
        Session s = this.factory.getObject().getCurrentSession();

        if (council.getId() == null) {
            s.persist(council);
        } else {
            s.merge(council);
        }
        s.flush();
        s.refresh(council);

        return council;
    }

    @Override
    public Councils getCouncilById(long id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Councils.class, id);
    }

    @Override
    public void deleteCouncil(long id) {
        Session s = this.factory.getObject().getCurrentSession();

        try {
            Councils c = s.get(Councils.class, id);

            if (c != null) {
                s.remove(c);
                s.refresh(c);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete thesis: " + e.getMessage(), e);
        }
    }

}
