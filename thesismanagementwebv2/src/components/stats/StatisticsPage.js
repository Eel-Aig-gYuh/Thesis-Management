import { useState, useContext, useEffect } from "react";
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
import { Bar } from "react-chartjs-2";
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from "chart.js";

// Đăng ký các thành phần cần thiết của Chart.js
ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const StatisticsPage = () => {
    const { t } = useTranslation();
    const user = useContext(MyUserContext);
    const [startYear, setStartYear] = useState("");
    const [endYear, setEndYear] = useState("");
    const [pdfYear, setPdfYear] = useState("");
    const [scoreStats, setScoreStats] = useState([]);
    const [participationStats, setParticipationStats] = useState([]);

    const [showScoreStats, setShowScoreStats] = useState(false);
    const [showParticipationStats, setShowParticipationStats] = useState(false);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const toggleShowScoreStats = () => {
        setShowScoreStats(!showScoreStats);
    }

    const toggleShowParticipationStats = () => {
        setShowParticipationStats(!showParticipationStats);
    }

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
            const params = { yearStart: startYear, yearEnd: endYear };
            console.log("Gửi request thông kê điểm: ", params);
            const response = await authApis().post(endpoints["statistic/score"], params);
            setScoreStats(response.data);
            setError("");
            console.log("Score stats response:", response.data);
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
            const params = { yearStart: startYear, yearEnd: endYear };
            console.log("Gửi request thông kê tần suất: ", params);
            const response = await authApis().post(endpoints["statistic/pariticipation"], params);
            setParticipationStats(response.data);
            setError("");
            console.log("Participation stats response:", response.data);
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
            const response = await authApis().post(
                endpoints["statistic/get-pdf"],
                { year: pdfYear },
                { responseType: "blob" }
            );
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
            {loading && <div className="text-center text-white">{t("loading")}</div>}

            {/* Form nhập năm và nút thống kê */}
            <YearRangeForm
                startYear={startYear}
                setStartYear={setStartYear}
                endYear={endYear}
                setEndYear={setEndYear}
                onFetchScoreStats={fetchScoreStats}
                onFetchParticipationStats={fetchParticipationStats}
                showScoreStats={toggleShowScoreStats}
                showParticipantStats={toggleShowParticipationStats}
            />

            {/* Form tải PDF */}
            <PdfDownloadForm pdfYear={pdfYear} setPdfYear={setPdfYear} onDownloadPdf={downloadPdf} />

            {/* Bảng và biểu đồ thống kê điểm */}
            {showScoreStats && (
                <ScoreStatsTable scoreStats={scoreStats} />
            )}

            {/* Bảng và biểu đồ thống kê tần suất */}
            {showParticipationStats && (
                <ParticipationStatsTable participationStats={participationStats} />
            )}
        </div>
    );
};

export default StatisticsPage;