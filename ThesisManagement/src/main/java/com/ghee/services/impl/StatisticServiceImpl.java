/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services.impl;

import com.ghee.dto.StatisticsResponse;
import com.ghee.enums.CouncilMemberRole;
import com.ghee.enums.UserRole;
import com.ghee.pojo.Scores;
import com.ghee.pojo.Theses;
import com.ghee.pojo.Users;
import com.ghee.repositories.ScoreRepository;
import com.ghee.repositories.ThesisRepository;
import com.ghee.repositories.UserRepository;
import com.ghee.services.StatisticService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author giahu
 */
@Service
@Transactional
public class StatisticServiceImpl implements StatisticService {

    private static final Logger logger = Logger.getLogger(StatisticServiceImpl.class.getName());

    @Autowired
    private ThesisRepository thesisRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ScoreRepository scoreRepo;

    @Override
    public StatisticsResponse getThesisStatistics(String year, String major, String username) {
        logger.log(Level.INFO, "Fetching thesis statistics for year: {0}, department: {1}", new Object[]{year, major});

        Users user = this.userRepo.getUserByUsername(username);
        if (user == null || (!user.getRole().equals(UserRole.ROLE_GIAOVU.name()) && !user.getRole().equals(UserRole.ROLE_ADMIN.name()))) {
            logger.log(Level.WARNING, "User {0} is not authorized to view statistics", username);
            throw new IllegalArgumentException("Only GIAOVU or ADMIN role can view statistics");
        }

        List<Theses> theses = this.thesisRepo.findThesisByYear(year);

        // Filter by department if provided
        List<Theses> filteredTheses = theses;
        if (major != null && !major.isEmpty()) {
            filteredTheses = theses.stream()
                    .filter(t -> t.getThesisStudentsSet().stream()
                    .anyMatch(ts -> ts.getStudentId().getMajor() != null
                    && ts.getStudentId().getMajor().equalsIgnoreCase(major)))
                    .collect(Collectors.toList());
        }

        // Calculate statistics
        double averageScore = filteredTheses.stream()
                .filter(t -> t.getAverageScore() != null)
                .map(Theses::getAverageScore)
                .mapToDouble(BigDecimal::doubleValue)
                .average()
                .orElse(0.0);

        long totalTheses = filteredTheses.size();

        Map<String, Long> departmentParticipation = filteredTheses.stream()
                .flatMap(t -> t.getThesisStudentsSet().stream())
                .filter(ts -> ts.getStudentId().getMajor() != null)
                .collect(Collectors.groupingBy(
                        ts -> ts.getStudentId().getMajor(),
                        Collectors.counting()
                ));

        logger.log(Level.INFO, "Statistics retrieved: average score = {0}, total theses = {1}",
                new Object[]{averageScore, totalTheses});

        return new StatisticsResponse(
                year != null ? year : "All",
                major != null ? major : "All",
                averageScore,
                totalTheses,
                departmentParticipation
        );
    }

    @Override
    public String generateScoreReportPDF(long thesisId, String username) {
        logger.log(Level.INFO, "Generating score report PDF for thesis ID: {0}", thesisId);

        Users user = this.userRepo.getUserByUsername(username);
        if (user == null || (!user.getRole().equals(UserRole.ROLE_GIAOVU.name()) && !user.getRole().equals(UserRole.ROLE_ADMIN.name()))) {
            logger.log(Level.WARNING, "User {0} is not authorized to view statistics", username);
            throw new IllegalArgumentException("Only GIAOVU or ADMIN role can view statistics");
        }

        Theses thesis = this.thesisRepo.getThesisById(thesisId);
        if (thesis == null) {
            logger.log(Level.WARNING, "Thesis not found: {0}", thesisId);
            throw new IllegalArgumentException("Thesis not found");
        }

        List<Scores> scores = this.scoreRepo.getScoreByThesisAndRole(thesisId, Arrays.asList(CouncilMemberRole.REVIEWER, CouncilMemberRole.MEMBER));
        double averageScore = thesis.getAverageScore() != null ? thesis.getAverageScore().doubleValue() : 0.0;

        StringBuilder latexContent = new StringBuilder();
        latexContent.append("\\documentclass[a4paper,12pt]{article}\n")
                .append("\\usepackage[utf8]{inputenc}\n")
                .append("\\usepackage{fontspec}\n")
                .append("\\setmainfont{Noto Serif}\n")
                .append("\\setmainfont{Times New Roman}\n")
                .append("\\usepackage{geometry}\n")
                .append("\\geometry{a4paper, margin=1in}\n")
                .append("\\usepackage{booktabs}\n")
                .append("\\usepackage{amsmath}\n")
                .append("\\usepackage{natbib}\n")
                .append("\\usepackage{graphicx}\n")
                .append("\\usepackage[T1]{fontenc}\n")
                .append("\\usepackage{lmodern}\n")
                .append("\\begin{document}\n")
                .append("\\title{Báo cáo điểm khóa luận}\n")
                .append("\\author{Ban Giáo vụ}\n")
                .append("\\date{\\today}\n")
                .append("\\maketitle\n")
                .append("\\section*{Thông tin khóa luận}\n")
                .append(String.format("Tên khóa luận: %s\\\\\n", escapeLatex(thesis.getTitle())))
                .append(String.format("Kỳ học: %s\\\\\n", escapeLatex(thesis.getSemester())))
                .append(String.format("Điểm trung bình: %.2f\\\\\n", averageScore))
                .append("\\section*{Chi tiết điểm}\n")
                .append("\\begin{tabular}{llrr}\n")
                .append("\\toprule\n")
                .append("Thành viên & Tiêu chí & Điểm tối đa & Điểm\\\\\n")
                .append("\\midrule\n");

        Map<Long, List<Scores>> scoresByMember = scores.stream()
                .collect(Collectors.groupingBy(score -> score.getCouncilMemberId().getId()));

        for (Map.Entry<Long, List<Scores>> entry : scoresByMember.entrySet()) {
            Users member = this.userRepo.getUserById(entry.getKey());
            String memberName = escapeLatex(member.getFirstname() + " " + member.getLastname());
            for (Scores score : entry.getValue()) {
                latexContent.append(String.format(
                        "%s & %s & %.1f & %.1f\\\\\n",
                        memberName,
                        escapeLatex(score.getCriteriaId().getName()),
                        score.getCriteriaId().getMaxScore().doubleValue(),
                        score.getScore()
                ));
            }
        }

        latexContent.append("\\bottomrule\n")
                .append("\\end{tabular}\n")
                .append("\\end{document}");

        logger.log(Level.INFO, "Score report PDF generated for thesis ID: {0}", thesisId);
        return latexContent.toString();
    }

    private String escapeLatex(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "\\&")
                .replace("%", "\\%")
                .replace("$", "\\$")
                .replace("#", "\\#")
                .replace("_", "\\_")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("~", "\\textasciitilde")
                .replace("^", "\\textasciicircum")
                .replace("\\", "\\textbackslash");
    }

}
