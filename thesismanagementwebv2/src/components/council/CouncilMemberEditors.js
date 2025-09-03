import { Button, Col, Form, Row } from "react-bootstrap";
import SearchableUserSelectBox from "../utils/SearchableUserSelectBox";
import "./style.css";

const CouncilMemberRole = [
    { value: "", label: "-- Chọn vai trò --" },
    { value: "CHAIRMAN", label: "Chủ tịch" },
    { value: "SECRETARY", label: "Thư ký" },
    { value: "REVIEWER", label: "Phản biện" },
    { value: "MEMBER", label: "Thành viên"},
];

const CouncilMembersEditor = ({ members, onChange, onAddMember, onRemoveMember }) => {
    // Kiểm tra trùng userId
    const getDuplicateUserIds = () => {
        const idCounts = members.reduce((acc, member) => {
            if (member.userId) {
                acc[member.userId] = (acc[member.userId] || 0) + 1;
            }
            return acc;
        }, {});
        return Object.keys(idCounts).filter(id => idCounts[id] > 1).map(Number);
    };

    const duplicateUserIds = getDuplicateUserIds();

    const handleSelectLecturer = (index, selectedId, selectedUser) => {
        console.log("Selected lecturer ID:", selectedId, "User:", selectedUser); // Debug
        const updated = [...members];

        if (selectedId && selectedUser) {
            updated[index] = {
                ...updated[index],
                userId: selectedId,
                name: `${selectedUser.lastname} ${selectedUser.firstname}`,
                showSelect: false,
            };
        } else {
            updated[index] = {
                ...updated[index],
                userId: null,
                name: "",
                showSelect: false,
            };
        }

        console.log("Updated member:", updated[index]); // Debug
        onChange(updated);
    };

    const toggleShowSelect = (index) => {
        const updated = [...members];
        updated[index].showSelect = !updated[index].showSelect;
        onChange(updated);
    };

    const handleMemberFieldChange = (index, field, value) => {
        const updated = [...members];
        updated[index][field] = value;
        onChange(updated);
    };

    return (
        <div className="council-members-editor">
            {members.map((member, index) => (
                <Row key={index} className="mb-3 align-items-center member-row">
                    <Col md={5}>
                        {!member.showSelect ? (
                            <div
                                className={`lecturer-display ${member.name ? "has-value" : ""} ${
                                    member.userId && duplicateUserIds.includes(member.userId) ? "duplicate" : ""
                                }`}
                                onClick={() => toggleShowSelect(index)}
                                role="button"
                                aria-label={member.name || "Chọn giảng viên"}
                            >
                                {member.name || "Chọn giảng viên"}
                            </div>
                        ) : (
                            <SearchableUserSelectBox
                                placeholder="Chọn giảng viên"
                                role="ROLE_GIANGVIEN"
                                isMulti={false}
                                selectedIds={member.userId ? [member.userId] : []}
                                onChange={(ids) => {
                                    const selectedId = ids[0];
                                    const selectedUser = selectedId
                                        ? members.find(m => m.userId === selectedId) || null
                                        : null;
                                    handleSelectLecturer(index, selectedId, selectedUser);
                                }}
                                onUserSelect={(user) => handleSelectLecturer(index, user?.id, user)}
                            />
                        )}
                    </Col>
                    <Col md={5}>
                        <Form.Select
                            value={member.role}
                            onChange={(e) => handleMemberFieldChange(index, "role", e.target.value)}
                            className="role-select"
                            aria-label="Chọn vai trò"
                        >
                            {CouncilMemberRole.map((role) => (
                                <option key={role.value} value={role.value}>
                                    {role.label}
                                </option>
                            ))}
                        </Form.Select>
                    </Col>
                    {index >= 3 && (
                        <Col md={2} className="text-end">
                            <Button
                                variant="danger"
                                size="sm"
                                onClick={() => onRemoveMember(index)}
                                className="remove-btn"
                                aria-label="Xóa thành viên"
                            >
                                ✕
                            </Button>
                        </Col>
                    )}
                </Row>
            ))}
            <div className="mt-3">
                <Button
                    variant="secondary"
                    size="sm"
                    onClick={onAddMember}
                    aria-label="Thêm thành viên"
                >
                    + Thêm thành viên
                </Button>
            </div>
        </div>
    );
};

export default CouncilMembersEditor;