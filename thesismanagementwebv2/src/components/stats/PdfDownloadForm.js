import { useTranslation } from "react-i18next";
import { Form, Button, Card } from "react-bootstrap";

// Component PdfDownloadForm: Form nhập năm và tải PDF
// Props:
// - pdfYear, setPdfYear: State và setter cho năm PDF
// - onDownloadPdf: Callback tải PDF
const PdfDownloadForm = ({ pdfYear, setPdfYear, onDownloadPdf }) => {
    const { t } = useTranslation();

    return (
        <Card className="mb-4">
            <Card.Body>
                <Form>
                    <Form.Group className="mb-3">
                        <Form.Label>{t("pdf-year")}</Form.Label>
                        <Form.Control
                            type="number"
                            placeholder={t("year-for-pdf")}
                            value={pdfYear}
                            onChange={(e) => setPdfYear(e.target.value)}
                        />
                    </Form.Group>
                    <Button onClick={onDownloadPdf}>
                        {t("download-pdf")}
                    </Button>
                </Form>
            </Card.Body>
        </Card>
    );
};

export default PdfDownloadForm;