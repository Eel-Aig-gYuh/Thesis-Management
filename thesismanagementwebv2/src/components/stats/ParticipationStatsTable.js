import { useTranslation } from "react-i18next";
import { Table, Card } from "react-bootstrap";

// Component ParticipationStatsTable: Hiển thị bảng thống kê tần suất
// Props:
// - participationStats: Dữ liệu tần suất [{year, departmentParticipation}]
const ParticipationStatsTable = ({ participationStats = [] }) => {
    const { t } = useTranslation();

    if (!participationStats.length) return null;

    return (
        <Card className="mb-4">
            <Card.Body>
                <h4>{t("participation-statistics")}</h4>
                <Table striped bordered hover responsive>
                    <thead>
                        <tr>
                            <th>{t("year")}</th>
                            <th>{t("department-details")}</th>
                        </tr>
                    </thead>
                    <tbody>
                        {Array.isArray(participationStats) && participationStats.map((stat) => (
                            <tr key={stat.year}>
                                <td>{stat.year}</td>
                                <td>
                                    {stat.departmentParticipation.map((dept) => (
                                        <div key={dept.departmentId}>
                                            {dept.department}: {dept.participationCount} {t("theses")} ({dept.participationPercentage.toFixed(1)}%)
                                        </div>
                                    ))}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            </Card.Body>
        </Card>
    );
};

export default ParticipationStatsTable;