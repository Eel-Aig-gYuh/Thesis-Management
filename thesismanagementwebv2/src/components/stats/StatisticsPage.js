import { useState, useContext } from "react";
import { useTranslation } from "react-i18next";
import { Alert } from "react-bootstrap";
import { MyUserContext } from "../../configs/MyContexts";
import YearRangeForm from "./YearRangeForm";
import PdfDownloadForm from "./PdfDownloadForm";
import ParticipationStatsTable from "./ParticipationStatsTable";
import ScoreStatsTable from "./ScoreStatsTable";
import { saveAs } from "file-saver";
import { authApis, endpoints } from "../../configs/Apis";
import "./style.css";

// Component StatisticsPage: Quản lý thống kê điểm, tần suất, và tải PDF
const StatisticsPage = () => {
    const { t } = useTranslation(); // Dịch văn bản
    const user = useContext(MyUserContext); // Người dùng hiện tại
    const [startYear, setStartYear] = useState(""); // Năm bắt đầu
    const [endYear, setEndYear] = useState(""); // Năm kết thúc
    const [pdfYear, setPdfYear] = useState(""); // Năm tải PDF
    const [scoreStats, setScoreStats] = useState([]); // Thống kê điểm
    const [participationStats, setParticipationStats] = useState([]); // Thống kê tần suất
    const [error, setError] = useState(""); // Lỗi
    const [loading, setLoading] = useState(false); // Loading

    // Kiểm tra quyền
    const canView = user?.role === "ROLE_GIAOVU" || user?.role === "ROLE_ADMIN";
    if (!canView) {
        return <Alert variant="warning">{t("no-access-permission")}</Alert>;
    }

    // Gọi API thống kê điểm
    const fetchScoreStats = async () => {
        if (!startYear || !endYear) {
            setError(t("enter-year-range"));
            return;
        }
        if (parseInt(startYear) > parseInt(endYear)) {
            setError(t("invalid-year-range"));
            return;
        }
        setLoading(true);
        try {
            console.log("Gửi request để thống kê điểm");
            const response = await authApis().post(endpoints['statistic/score'], {
                params: { startYear, endYear },
            });
            setScoreStats(response.data);
            setError("");
            console.log("Phản hồi từ API", response.data);
        } catch (err) {
            setError(err.response?.data?.message || t("fetch-score-stats-error"));
        } finally {
            setLoading(false);
        }
    };

    // Gọi API thống kê tần suất
    const fetchParticipationStats = async () => {
        if (!startYear || !endYear) {
            setError(t("enter-year-range"));
            return;
        }
        if (parseInt(startYear) > parseInt(endYear)) {
            setError(t("invalid-year-range"));
            return;
        }
        setLoading(true);
        try {
            console.log("Gửi request để thống kê tần suất");

            const response = await authApis().post(endpoints['statistic/pariticipation'], {
                params: { startYear, endYear },
            });
            setParticipationStats(response.data);
            setError("");

            console.log("Phản hổi từ API", response.data);
        } catch (err) {
            setError(err.response?.data?.message || t("fetch-participation-stats-error"));
        } finally {
            setLoading(false);
        }
    };

    // Tải PDF
    const downloadPdf = async () => {
        if (!pdfYear) {
            setError(t("enter-year"));
            return;
        }
        if (!/^\d{4}$/.test(pdfYear) || parseInt(pdfYear) > 2025) {
            setError(t("invalid-year"));
            return;
        }
        setLoading(true);
        try {
            const response = await authApis().post(endpoints['statistic/get-pdf'], {
                params: { year: pdfYear },
                responseType: "blob",
            });
            const blob = new Blob([response.data], { type: "application/pdf" });
            saveAs(blob, `average_scores_${pdfYear}.pdf`);
            setError("");
        } catch (err) {
            setError(t("download-pdf-error"));
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container mt-4 p-4 bg-black rounded-4">

            <h2 className="stats-title-list text-center mb-5 fw-bold" style={{ color: "white" }}>
                {t("statistics")}
            </h2>

            {error && <Alert variant="danger">{error}</Alert>}
            {loading && <div className="text-center">{t("loading")}</div>}

            {/* Form nhập năm và nút thống kê */}
            <YearRangeForm
                startYear={startYear}
                setStartYear={setStartYear}
                endYear={endYear}
                setEndYear={setEndYear}
                onFetchScoreStats={fetchScoreStats}
                onFetchParticipationStats={fetchParticipationStats}
            />

            {/* Form tải PDF */}
            <PdfDownloadForm
                pdfYear={pdfYear}
                setPdfYear={setPdfYear}
                onDownloadPdf={downloadPdf}
            />

            {/* Bảng thống kê điểm */}
            <ScoreStatsTable scoreStats={scoreStats} />

            {/* Bảng thống kê tần suất */}
            <ParticipationStatsTable participationStats={participationStats} />
        </div>
    );
};

export default StatisticsPage;