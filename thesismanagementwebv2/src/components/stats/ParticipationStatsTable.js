import { useTranslation } from "react-i18next";
import { Table } from "react-bootstrap";
import { useRef, useEffect } from "react";
import Chart from "chart.js/auto";
import "./style.css";

const ParticipationStatsTable = ({ participationStats }) => {
    const { t } = useTranslation();
    const chartRefs = useRef({});

    useEffect(() => {
        // Tạo biểu đồ
        participationStats.forEach((stat) => {
            // Biểu đồ cột
            const barCanvas = document.getElementById(`bar-participation-${stat.year}`);
            if (barCanvas) {
                chartRefs.current[`bar-participation-${stat.year}`] = new Chart(barCanvas, {
                    type: "bar",
                    data: {
                        labels: Object.keys(stat.participationByDepartment),
                        datasets: [
                            {
                                label: `${t("participation-by-department")} ${stat.year}`,
                                data: Object.values(stat.participationByDepartment),
                                backgroundColor: ["#407F3E", "#89B449", "#DBD468", "#E7E0C4"], // Màu sắc neon/tươi sáng
                                borderColor: ["#FFFFFF"],
                                borderWidth: 1.5,
                            },
                        ],
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        scales: {
                            y: {
                                beginAtZero: true,
                                title: { display: true, text: t("number-of-theses"), color: "#FFFFFF", font: { size: 14 } },
                                grid: { color: "rgba(255, 255, 255, 0.1)" },
                                ticks: { color: "#FFFFFF", font: { size: 12 } },
                            },
                            x: {
                                title: { display: true, text: t("department"), color: "#FFFFFF", font: { size: 14 } },
                                ticks: { color: "#FFFFFF", font: { size: 12 } },
                            },
                        },
                        plugins: {
                            legend: {
                                display: true,
                                labels: { color: "#FFFFFF", font: { size: 12 } },
                            },
                            tooltip: {
                                backgroundColor: "rgba(0, 0, 0, 0.8)",
                                titleColor: "#FFFFFF",
                                bodyColor: "#FFFFFF",
                                borderColor: "#FFFFFF",
                                borderWidth: 1,
                            },
                        },
                    },
                });
            }

            // Biểu đồ tròn
            const pieCanvas = document.getElementById(`pie-participation-${stat.year}`);
            if (pieCanvas) {
                chartRefs.current[`pie-participation-${stat.year}`] = new Chart(pieCanvas, {
                    type: "pie",
                    data: {
                        labels: Object.keys(stat.participationPercentage),
                        datasets: [
                            {
                                label: `${t("participation-by-department")} ${stat.year} (%)`,
                                data: Object.values(stat.participationPercentage),
                                backgroundColor: ["#407F3E", "#89B449", "#DBD468", "#E7E0C4"],
                                borderColor: ["#FFFFFF"],
                                borderWidth: 1.5,
                            },
                        ],
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: {
                                display: true,
                                position: "top",
                                labels: { color: "#FFFFFF", font: { size: 12 } },
                            },
                            tooltip: {
                                backgroundColor: "rgba(0, 0, 0, 0.8)",
                                titleColor: "#FFFFFF",
                                bodyColor: "#FFFFFF",
                                borderColor: "#FFFFFF",
                                borderWidth: 1,
                                callbacks: {
                                    label: (context) => `${context.label}: ${context.raw.toFixed(2)}%`,
                                },
                            },
                        },
                    },
                });
            }
        });

        // Cleanup: Hủy tất cả biểu đồ khi component unmount hoặc participationStats thay đổi
        return () => {
            Object.values(chartRefs.current).forEach((chart) => {
                if (chart) chart.destroy();
            });
            chartRefs.current = {};
        };
    }, [participationStats, t]);

    if (!participationStats || participationStats.length === 0) {
        return <div className="text-white">{t("no-participation-stats")}</div>;
    }

    return (
        <div className="mt-4 p-4">
            <div className="mb-4" style={{ textAlign: "center", borderRadius: "0.7rem", borderBottom: "2px solid white" }}>
                <h3 className="text-white mb-3 fw-bold">{t("participation-statistics")}</h3>
            </div>

            {participationStats.map((stat) => (
                <div key={stat.year} className="mb-5">
                    <h4 className="text-white">{t("year")}: {stat.year}</h4>
                    <Table striped bordered hover variant="dark">
                        <thead>
                            <tr>
                                <th>{t("total-theses")}</th>
                                <th>{t("participation-by-department")}</th>
                                <th>{t("participation-count")}</th>
                                <th>{t("participation-percent")}</th>
                            </tr>
                        </thead>
                        <tbody>
                            {Object.entries(stat.participationByDepartment).map(([dept, count], index) => (
                                <tr key={dept}>
                                    {index === 0 && (
                                        <td rowSpan={Object.keys(stat.participationByDepartment).length}>
                                            {stat.totalTheses}
                                        </td>
                                    )}
                                    <td>{dept}</td>
                                    <td>{count}</td>
                                    <td>
                                        {stat.participationPercentage[dept]
                                            ? `${stat.participationPercentage[dept].toFixed(2)}%`
                                            : "0.00%"}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                    <div className="chart-container d-flex justify-content-between">
                        <div style={{ maxWidth: "68%", height: "300px" }}>
                            <h5 className="text-white text-center">{t("participation-bar")}</h5>
                            <canvas id={`bar-participation-${stat.year}`} />
                        </div>
                        <div style={{ maxWidth: "30%", height: "300px" }}>
                            <h5 className="text-white text-center">{t("participation-pie")}</h5>
                            <canvas id={`pie-participation-${stat.year}`} />
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
};

export default ParticipationStatsTable;