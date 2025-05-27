import { useTranslation } from "react-i18next";
import { Form, Button, Card, Row, Col } from "react-bootstrap";

// Component YearRangeForm: Form nhập năm để lấy thống kê
// Props:
// - startYear, setStartYear: State và setter cho năm bắt đầu
// - endYear, setEndYear: State và setter cho năm kết thúc
// - onFetchScoreStats: Callback gọi thống kê điểm
// - onFetchParticipationStats: Callback gọi thống kê tần suất
const YearRangeForm = ({
    startYear,
    setStartYear,
    endYear,
    setEndYear,
    onFetchScoreStats,
    onFetchParticipationStats,
}) => {
    const { t } = useTranslation();

    return (
        <Card className="mb-4">
            <Card.Body>
                <Form>
                    <Form.Group className="mb-3">
                        <Form.Label>{t("year-range")}</Form.Label>
                        <Row>
                            <Col>
                                <Form.Control
                                    type="number"
                                    placeholder={t("start-year")}
                                    value={startYear}
                                    onChange={(e) => setStartYear(e.target.value)}
                                />
                            </Col>
                            <Col>
                                <Form.Control
                                    type="number"
                                    placeholder={t("end-year")}
                                    value={endYear}
                                    onChange={(e) => setEndYear(e.target.value)}
                                />
                            </Col>
                        </Row>
                    </Form.Group>
                    <Button onClick={onFetchScoreStats} className="me-2">
                        {t("view-score-stats")}
                    </Button>
                    <Button onClick={onFetchParticipationStats}>
                        {t("view-participation-stats")}
                    </Button>
                </Form>
            </Card.Body>
        </Card>
    );
};

export default YearRangeForm;