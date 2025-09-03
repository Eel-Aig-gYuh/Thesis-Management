import { useTranslation } from "react-i18next";
import { Table, Card } from "react-bootstrap";

const ThesisScoreDetail = ({ scores, averageScore }) => {
    const { t } = useTranslation();

    if (!scores || scores.length === 0) {
        return <p>{t("no-scores")}</p>;
    }

    // Get unique council members for dynamic columns
    const councilMembers = [];
    scores.forEach(criterion => {
        criterion.memberScore.forEach(member => {
            if (!councilMembers.find(m => m.councilMemberId === member.councilMemberId)) {
                councilMembers.push({
                    councilMemberId: member.councilMemberId,
                    councilMemberName: member.councilMemberName,
                });
            }
        });
    });

    return (
        <Card className="mt-3">
            <Card.Body>
                <Card.Title>{t("detailed-scores")}</Card.Title>
                <Table striped bordered hover responsive>
                    <thead>
                        <tr>
                            <th>{t("criteria")}</th>
                            <th>{t("max-score")}</th>
                            {councilMembers.map(member => (
                                <th key={member.councilMemberId}>
                                    {member.councilMemberName}
                                </th>
                            ))}
                        </tr>
                    </thead>
                    <tbody>
                        {scores.map(criterion => (
                            <tr key={criterion.criteriaId}>
                                <td>{criterion.criteriaName}</td>
                                <td>{criterion.maxScore.toFixed(2)}</td>
                                {councilMembers.map(member => {
                                    const score = criterion.memberScore.find(
                                        ms => ms.councilMemberId === member.councilMemberId
                                    )?.score || "-";
                                    return <td key={member.councilMemberId}>{score !== "-" ? score.toFixed(2) : score}</td>;
                                })}
                            </tr>
                        ))}
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colSpan={2}><strong>{t("average-score")}</strong></td>
                            <td colSpan={councilMembers.length}>
                                {averageScore !== null && averageScore !== 0 ? averageScore.toFixed(2) : t("not-calculated")}
                            </td>
                        </tr>
                    </tfoot>
                </Table>
            </Card.Body>
        </Card>
    );
};

export default ThesisScoreDetail;