import { useTranslation } from "react-i18next";
import { Table, Card } from "react-bootstrap";

const ScoreStatsTable = ({ scoreStats = [] }) => {
    const { t } = useTranslation();

    if (!scoreStats.length) return null;

    return (
        <Card className="mb-4">
            <Card.Body>
                <h4>{t("score-statistics")}</h4>
                <Table striped bordered hover responsive>
                    <thead>
                        <tr>
                            <th>{t("year")}</th>
                            <th>{t("total-theses")}</th>
                            <th>{t("average-score")}</th>
                            <th>{t("department-details")}</th>
                        </tr>
                    </thead>
                    <tbody>
                        {Array.isArray(scoreStats) && scoreStats?.map((stat) => (
                            <tr key={stat.year}>
                                <td>{stat.year}</td>
                                <td>{stat.totalTheses}</td>
                                <td>{stat.averageScore.toFixed(2)}</td>
                                <td>
                                    {stat.departmentScores.map((dept) => (
                                        <div key={dept.department}>
                                            {dept.department}: {dept.averageScore.toFixed(2)} ({dept.thesisCount} {t("theses")})
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

export default ScoreStatsTable;