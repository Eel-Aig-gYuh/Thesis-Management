import { useTranslation } from "react-i18next";
import { Table } from "react-bootstrap";
import { useRef, useEffect } from "react";
import Chart from "chart.js/auto";

const ScoreStatsTable = ({ scoreStats }) => {
    const { t } = useTranslation();
    const chartRefs = useRef({});

    useEffect(() => {
        // Tạo biểu đồ
        scoreStats.forEach((stat) => {
            // Biểu đồ cột
            const barCanvas = document.getElementById(`bar-score-${stat.year}`);
            if (barCanvas) {
                chartRefs.current[`bar-${stat.year}`] = new Chart(barCanvas, {
                    type: "bar",
                    data: {
                        labels: Object.keys(stat.scoreDistribution),
                        datasets: [
                            {
                                label: `${t("score-distribution")} ${stat.year}`,
                                data: Object.values(stat.scoreDistribution),
                                backgroundColor: ["#FF6384", "#36A2EB", "#FFCE56", "#4BC0C0"],
                                borderColor: ["#FF6384", "#36A2EB", "#FFCE56", "#4BC0C0"],
                                borderWidth: 1,
                            },
                        ],
                    },
                    options: {
                        scales: {
                            y: { beginAtZero: true, title: { display: true, text: t("number-of-theses") } },
                            x: { title: { display: true, text: t("score-range") } },
                        },
                        plugins: { legend: { display: true } },
                    },
                });
            }

            // Biểu đồ tròn
            const pieCanvas = document.getElementById(`pie-score-${stat.year}`);
            if (pieCanvas) {
                chartRefs.current[`pie-${stat.year}`] = new Chart(pieCanvas, {
                    type: "pie",
                    data: {
                        labels: Object.keys(stat.scoreDistributionPercentage),
                        datasets: [
                            {
                                label: `${t("score-distribution")} ${stat.year} (%)`,
                                data: Object.values(stat.scoreDistributionPercentage),
                                backgroundColor: ["#FF6384", "#36A2EB", "#FFCE56", "#4BC0C0"],
                                borderColor: ["#FFFFFF"],
                                borderWidth: 1,
                            },
                        ],
                    },
                    options: {
                        plugins: {
                            legend: { display: true, position: "top" },
                            tooltip: {
                                callbacks: {
                                    label: (context) => `${context.label}: ${context.raw.toFixed(2)}%`,
                                },
                            },
                        },
                    },
                });
            }
        });

        // Cleanup: Hủy tất cả biểu đồ khi component unmount hoặc scoreStats thay đổi
        return () => {
            Object.values(chartRefs.current).forEach((chart) => {
                if (chart) chart.destroy();
            });
            chartRefs.current = {};
        };
    }, [scoreStats, t]);

    if (!scoreStats || scoreStats.length === 0) {
        return <div className="text-white">{t("no-score-stats")}</div>;
    }

    return (
        <div className="mt-4">
            <h3 className="text-white mb-3">{t("score-statistics")}</h3>
            {scoreStats.map((stat) => (
                <div key={stat.year} className="mb-5">
                    <h4 className="text-white">{t("year")}: {stat.year}</h4>
                    <Table striped bordered hover variant="dark">
                        <thead>
                            <tr>
                                <th>{t("average-score")}</th>
                                <th>{t("min-score")}</th>
                                <th>{t("max-score")}</th>
                                <th>{t("total-theses")}</th>
                                <th>{t("score-distribution")}</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>{stat.averageScore.toFixed(2)}</td>
                                <td>{stat.minScore.toFixed(2)}</td>
                                <td>{stat.maxScore.toFixed(2)}</td>
                                <td>{stat.totalTheses}</td>
                                <td>
                                    {Object.entries(stat.scoreDistribution).map(([range, count]) => (
                                        <div key={range}>
                                            {range}: {count} ({stat.scoreDistributionPercentage[range].toFixed(2)}%)
                                        </div>
                                    ))}
                                </td>
                            </tr>
                        </tbody>
                    </Table>
                    <div className="chart-container d-flex justify-content-between">
                        <div style={{ maxWidth: "48%" }}>
                            <h5 className="text-white text-center">{t("score-distribution-bar")}</h5>
                            <canvas id={`bar-score-${stat.year}`} />
                        </div>
                        <div style={{ maxWidth: "48%" }}>
                            <h5 className="text-white text-center">{t("score-distribution-pie")}</h5>
                            <canvas id={`pie-score-${stat.year}`} />
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
};

export default ScoreStatsTable;