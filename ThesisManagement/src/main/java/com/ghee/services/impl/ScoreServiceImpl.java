/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services.impl;

import com.ghee.dto.AverageScoreDTO;
import com.ghee.dto.ScoreRequest;
import com.ghee.dto.ScoreResponse;
import com.ghee.dto.ScoringCriteriaResponse;
import com.ghee.dto.ScoringRequest;
import com.ghee.dto.ScoringResponse;
import com.ghee.dto.StatisticDTO;
import com.ghee.dto.ThesisUserDTO;
import com.ghee.enums.CouncilMemberRole;
import com.ghee.enums.UserRole;
import com.ghee.pojo.Criteria;
import com.ghee.pojo.Scores;
import com.ghee.pojo.Theses;
import com.ghee.pojo.Users;
import com.ghee.repositories.CriteriaRepository;
import com.ghee.repositories.ScoreRepository;
import com.ghee.repositories.ThesisRepository;
import com.ghee.repositories.UserRepository;
import com.ghee.services.ScoreService;
import com.ghee.utils.DateUtils;
import com.ghee.validators.UserValidator;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class ScoreServiceImpl implements ScoreService {

    private static final Logger logger = Logger.getLogger(ScoreServiceImpl.class.getName());

    @Autowired
    private ScoreRepository scoreRepo;

    @Autowired
    private ThesisRepository thesisRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CriteriaRepository criteriaRepo;

    @Override
    public Map<String, Object> getScoreDetailsByThesisId(long thesisId) {
        Map<String, Object> result = this.scoreRepo.getScoreDetailsByThesisId(thesisId);

        return result;
    }

    @Override
    public Map<String, Object> getScoringCriteria(Long councilId, List<Long> thesisIds, long userId) {
        logger.log(Level.INFO, "Fetching scoring criteria for council {0}, theses {1}", new Object[]{councilId, thesisIds});
        return scoreRepo.getScoringCriteria(councilId, thesisIds, userId);
    }

    @Override
    public ScoringResponse submitScores(ScoringRequest request) {
        logger.log(Level.INFO, "Submitting scores for thesis {0} by user {1}", new Object[]{request.getThesisId(), request.getUserId()});
        return scoreRepo.submitScores(request);
    }

    @Override
    public List<StatisticDTO.ThesisScoreStatsResponse> getThesisScoreStatistics(String startYear, String endYear) {
        logger.log(Level.INFO, "Fetching score statistics for years {0} to {1}",
                new Object[]{startYear, endYear});
        return scoreRepo.getThesisScoreStatistics(startYear, endYear);
    }

    @Override
    public List<StatisticDTO.ThesisParticipationResponse> getThesisParticipationByDepartment(String startYear, String endYear) {
        logger.log(Level.INFO, "Fetching participation stats for years {0} to {1}",
                new Object[]{startYear, endYear});
        return scoreRepo.getThesisParticipationByDepartment(startYear, endYear);
    }

    @Override
    public byte[] generateAverageScoresPdf(String year) {
        logger.log(Level.INFO, "Generating PDF report for year {0}", year);

        // Lấy dữ liệu báo cáo
        List<StatisticDTO.ThesisAverageScoreReport> reports = scoreRepo.getThesisAverageScoresForReport(year);

        try {
            // Tạo font hỗ trợ tiếng Việt
            PdfFont font = PdfFontFactory.createFont("Times New Roman", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
            // Nếu không muốn dùng file font, có thể dùng font hệ thống
            // PdfFont font = PdfFontFactory.createFont("Times-Roman", PdfEncodings.IDENTITY_H, true);

            // Tạo luồng byte để lưu PDF
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Tiêu đề
            document.add(new Paragraph("BÁO CÁO ĐIỂM TRUNG BÌNH KHÓA LUẬN")
                    .setFont(font)
                    .setFontSize(16)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Năm học: " + year)
                    .setFont(font)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Ngày xuất báo cáo: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()))
                    .setFont(font)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(" ").setFont(font)); // Khoảng cách

            // Tạo bảng
            float[] columnWidths = {1, 4, 3, 2, 2}; // Tỷ lệ cột
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            // Tiêu đề bảng
            table.addHeaderCell(new Paragraph("STT").setFont(font).setBold());
            table.addHeaderCell(new Paragraph("Tên khóa luận").setFont(font).setBold());
            table.addHeaderCell(new Paragraph("Ngành").setFont(font).setBold());
            table.addHeaderCell(new Paragraph("Kỳ").setFont(font).setBold());
            table.addHeaderCell(new Paragraph("Điểm TB").setFont(font).setBold());

            // Dữ liệu bảng
            for (int i = 0; i < reports.size(); i++) {
                StatisticDTO.ThesisAverageScoreReport r = reports.get(i);
                table.addCell(new Paragraph(String.valueOf(i + 1)).setFont(font));
                table.addCell(new Paragraph(r.getTitle() != null ? r.getTitle() : "N/A").setFont(font));
                table.addCell(new Paragraph(r.getDepartment() != null ? r.getDepartment() : "N/A").setFont(font));
                table.addCell(new Paragraph(r.getSemester() != null ? r.getSemester() : "N/A").setFont(font));
                table.addCell(new Paragraph(r.getAverageScore() != null ? String.format("%.2f", r.getAverageScore()) : "N/A").setFont(font));
            }

            document.add(table);

            // Chữ ký
            document.add(new Paragraph(" ")
                    .setFont(font)
                    .setFixedLeading(20)); // Khoảng cách
            document.add(new Paragraph("Ngày " + new SimpleDateFormat("dd").format(new Date())
                    + " tháng " + new SimpleDateFormat("MM").format(new Date())
                    + " năm " + new SimpleDateFormat("yyyy").format(new Date()))
                    .setFont(font)
                    .setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("Người lập báo cáo")
                    .setFont(font)
                    .setBold()
                    .setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("(Ký và ghi rõ họ tên)")
                    .setFont(font)
                    .setTextAlignment(TextAlignment.RIGHT));

            // Đóng tài liệu
            document.close();

            logger.log(Level.INFO, "PDF report generated for year: {0}", year);
            return baos.toByteArray();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to generate PDF: {0}", e.getMessage());
            throw new RuntimeException("Failed to generate PDF", e);
        }
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

    @Override
    public AverageScoreDTO calculateAverageScoreByThesis(long thesisId, String username) {
        Users currentUser = this.userRepo.getUserByUsername(username);
        if (currentUser == null) {
            throw new IllegalArgumentException("User not found " + username);
        }
        UserValidator.checkRole(currentUser, UserRole.ROLE_GIAOVU);

        AverageScoreDTO averageScoreDTO = this.scoreRepo.calculateAverageScoreByThesis(thesisId);

        return averageScoreDTO;
    }

    @Override
    public ScoreResponse createScore(long id, ScoreRequest dto, String username) {
        logger.log(Level.INFO, "Creating score for thesis ID: {0} by user: {1}", new Object[]{id, username});

        Users councilMember = this.userRepo.getUserByUsername(username);
        UserValidator.checkRole(councilMember, UserRole.ROLE_GIANGVIEN);

        Theses thesis = this.thesisRepo.getThesisById(id);
        if (thesis == null) {
            logger.log(Level.WARNING, "Thesis not found: {0}", id);
            throw new IllegalArgumentException("Thesis not found");
        }

        boolean isCouncilMember = thesis.getCouncilThesesSet().stream()
                .flatMap(council -> council.getCouncilId().getCouncilMembersSet().stream())
                .anyMatch(member -> member.getMemberId().getId().equals(councilMember.getId())
                && (member.getRole().equals(CouncilMemberRole.REVIEWER.name()) || member.getRole().equals(CouncilMemberRole.MEMBER.name())));
        if (!isCouncilMember) {
            logger.log(Level.WARNING, "User {0} is not a REVIEWER or MEMBER of the council for thesis {1}",
                    new Object[]{username, id});
            throw new IllegalArgumentException("User must be a REVIEWER or MEMBER of the council");
        }

        List<Scores> scores = new ArrayList<>();
        List<ScoreResponse.CriteriaScoreDTO> criteriaScoreDTOs = new ArrayList<>();

        for (ScoreRequest.CriteriaScoreDTO criteriaScoreDTO : dto.getCriteriaScores()) {
            Criteria criteria = this.criteriaRepo.getCriteriaById(criteriaScoreDTO.getCriteriaId());
            if (criteria == null) {
                logger.log(Level.WARNING, "Criteria not found: {0}", criteriaScoreDTO.getCriteriaId());
                throw new IllegalArgumentException("Criteria not found: " + criteriaScoreDTO.getCriteriaId());
            }

            BigDecimal currScore = criteriaScoreDTO.getScore();
            BigDecimal maxScore = criteria.getMaxScore();

            if (currScore.compareTo(BigDecimal.ZERO) < 0 || currScore.compareTo(maxScore) > 0) {
                logger.log(Level.WARNING, "Invalid score {0} for criteria {1}, max score is {2}",
                        new Object[]{criteriaScoreDTO.getScore(), criteria.getName(), criteria.getMaxScore()});
                throw new IllegalArgumentException("Score for criteria " + criteria.getName()
                        + " must be between 0 and " + criteria.getMaxScore());
            }

            Scores score = new Scores();
            score.setThesisId(thesis);
            score.setCouncilMemberId(councilMember);
            score.setCriteriaId(criteria);
            score.setScore(criteriaScoreDTO.getScore());
            score.setCreatedAt(DateUtils.getTodayWithoutTime());
            scores.add(score);

            criteriaScoreDTOs.add(new ScoreResponse.CriteriaScoreDTO(
                    criteria.getId(),
                    criteria.getName(),
                    criteria.getMaxScore(),
                    criteriaScoreDTO.getScore()
            ));
        }

        List<Scores> createdScores = this.scoreRepo.createOrUpdateScores(scores);
        logger.log(Level.INFO, "Scores created successfully for thesis ID: {0}", id);

        // Cập nhật lại average score
        this.thesisRepo.updateAverageScore(thesis.getId());

        return mapToResponseDTO(createdScores);
    }

    private ScoreResponse mapToResponseDTO(List<Scores> scores) {
        if (scores == null || scores.isEmpty()) {
            return null;
        }

        Scores firstScore = scores.get(0);
        Users councilMember = firstScore.getCouncilMemberId();

        List<ScoreResponse.CriteriaScoreDTO> criteriaScoreDTOs = scores.stream()
                .map(score -> new ScoreResponse.CriteriaScoreDTO(
                score.getCriteriaId().getId(),
                score.getCriteriaId().getName(),
                score.getCriteriaId().getMaxScore(),
                score.getScore()
        ))
                .collect(Collectors.toList());

        return new ScoreResponse(
                firstScore.getId(),
                new ThesisUserDTO(
                        councilMember.getId(),
                        councilMember.getFirstname(),
                        councilMember.getLastname(),
                        councilMember.getEmail(),
                        null
                ),
                criteriaScoreDTOs,
                firstScore.getCreatedAt()
        );
    }
}
