import { useState, useEffect, useRef } from "react";
import { Row, Col, Button, Form } from "react-bootstrap";

const ThesisDualList = ({
    availableTheses = [],
    selectedTheses = [],
    setSelectedTheses,
    maxHeight = 300,
    loadMore, // Added prop
    hasMore,  // Added prop
    loading,   // Added prop
    resetItems
}) => {
    const [searchAvailable, setSearchAvailable] = useState("");
    const [searchSelected, setSearchSelected] = useState("");
    const [selectedAvailableIds, setSelectedAvailableIds] = useState([]);
    const [selectedSelectedIds, setSelectedSelectedIds] = useState([]);
    const loadMoreTriggerRef = useRef(null); // Ref for the trigger element

    // Lọc danh sách khóa luận có sẵn (chưa được chọn)
    const filteredAvailable = availableTheses.filter(t =>
        t.title.toLowerCase().includes(searchAvailable.toLowerCase()) &&
        !selectedTheses.some(st => st.id === t.id)
    );

    // Lọc danh sách khóa luận đã chọn để chấm
    const filteredSelected = selectedTheses.filter(t =>
        t.title.toLowerCase().includes(searchSelected.toLowerCase())
    );

    const moveToSelected = () => {
        const toAdd = availableTheses.filter(t => selectedAvailableIds.includes(t.id));
        const updated = [...selectedTheses, ...toAdd];
        setSelectedTheses(updated);
        setSelectedAvailableIds([]);
    };

    const moveToAvailable = () => {
        const updated = selectedTheses.filter(t => !selectedSelectedIds.includes(t.id));
        setSelectedTheses(updated);
        setSelectedSelectedIds([]);
        resetItems();
    };

    // Xóa checkbox bị giữ lại khi danh sách thay đổi
    useEffect(() => {
        setSelectedAvailableIds(prev =>
            prev.filter(id => availableTheses.some(t => t.id === id))
        );
    }, [availableTheses]);

    useEffect(() => {
        setSelectedSelectedIds(prev =>
            prev.filter(id => selectedTheses.some(t => t.id === id))
        );
    }, [selectedTheses]);

    // Set up Intersection Observer for lazy loading
    useEffect(() => {
        if (!loadMore || !hasMore || loading) return;

        const observer = new IntersectionObserver(
            (entries) => {
                if (entries[0].isIntersecting) {
                    loadMore();
                }
            },
            { threshold: 0.1 } // Trigger when 10% of the element is visible
        );

        if (loadMoreTriggerRef.current) {
            observer.observe(loadMoreTriggerRef.current);
        }

        return () => {
            if (loadMoreTriggerRef.current) {
                observer.unobserve(loadMoreTriggerRef.current);
            }
        };
    }, [loadMore, hasMore, loading]);

    return (
        <Row>
            {/* DANH SÁCH KHÓA LUẬN CHƯA PHÂN HỘI ĐỒNG */}
            <Col md={5}>
                <h5>Danh sách khóa luận</h5>
                <Form.Control
                    type="text"
                    placeholder="Tìm kiếm..."
                    className="mb-2"
                    value={searchAvailable}
                    onChange={e => setSearchAvailable(e.target.value)}
                />
                <div className="border p-2" style={{ maxHeight, overflowY: "auto" }}>
                    {filteredAvailable.length === 0 && !loading ? (
                        <div className="text-muted fst-italic">Không có khóa luận phù hợp</div>
                    ) : (
                        filteredAvailable.map(thesis => (
                            <Form.Check
                                key={thesis.id}
                                type="checkbox"
                                label={thesis.title}
                                checked={selectedAvailableIds.includes(thesis.id)}
                                onChange={e => {
                                    const checked = e.target.checked;
                                    setSelectedAvailableIds(prev =>
                                        checked
                                            ? [...prev, thesis.id]
                                            : prev.filter(id => id !== thesis.id)
                                    );
                                }}
                            />
                        ))
                    )}
                    {/* Loading indicator */}
                    {loading && (
                        <div className="text-center my-2">
                            <span>Đang tải...</span>
                        </div>
                    )}
                    {/* Trigger element for lazy loading */}
                    {hasMore && !loading && (
                        <div ref={loadMoreTriggerRef} style={{ height: "10px" }}></div>
                    )}
                </div>
            </Col>

            {/* NÚT DI CHUYỂN */}
            <Col
                md={2}
                className="d-flex flex-column justify-content-center align-items-center"
            >
                <Button
                    variant="secondary"
                    className="mb-2"
                    onClick={moveToSelected}
                    disabled={selectedAvailableIds.length === 0}
                >
                    {"=>"}
                </Button>
                <Button
                    variant="warning"
                    onClick={moveToAvailable}
                    disabled={selectedSelectedIds.length === 0}
                >
                    {"<="}
                </Button>
            </Col>

            {/* DANH SÁCH KHÓA LUẬN CẦN CHẤM */}
            <Col md={5}>
                <h5>Danh sách cần chấm</h5>
                <Form.Control
                    type="text"
                    placeholder="Tìm kiếm..."
                    className="mb-2"
                    value={searchSelected}
                    onChange={e => setSearchSelected(e.target.value)}
                />
                <div className="border p-2" style={{ maxHeight, overflowY: "auto" }}>
                    {filteredSelected.length === 0 ? (
                        <div className="text-muted fst-italic">Chưa chọn khóa luận nào</div>
                    ) : (
                        filteredSelected.map(thesis => (
                            <Form.Check
                                key={thesis.id}
                                type="checkbox"
                                label={thesis.title}
                                checked={selectedSelectedIds.includes(thesis.id)}
                                onChange={e => {
                                    const checked = e.target.checked;
                                    setSelectedSelectedIds(prev =>
                                        checked
                                            ? [...prev, thesis.id]
                                            : prev.filter(id => id !== thesis.id)
                                    );
                                }}
                            />
                        ))
                    )}
                </div>
            </Col>
        </Row>
    );
};

export default ThesisDualList;