import { useEffect, useRef, useState, useMemo } from "react";
import { useTranslation } from "react-i18next";
import { fetchUsers } from "../../services/userService";
import useLazyLoad from "../hooks/useLazyLoad";
import { Form, InputGroup, ListGroup, Alert } from "react-bootstrap";
import MySpinner from "../layouts/MySpinner";
import "./style.css";

const SearchableUserSelectBox = ({ role, selectedIds, onChange, onUserSelect, label }) => {
    const { t } = useTranslation();
    const [search, setSearch] = useState("");
    const [debouncedSearch, setDebouncedSearch] = useState(search);
    const [error, setError] = useState(null);
    const containerRef = useRef(null);

    // Debounce search input
    useEffect(() => {
        const handler = setTimeout(() => {
            setDebouncedSearch(search);
        }, 300);
        return () => clearTimeout(handler);
    }, [search]);

    // Ổn định filters bằng useMemo
    const filters = useMemo(() => ({
        role,
        firstname: debouncedSearch,
    }), [role, debouncedSearch]);

    // Debug filters
    useEffect(() => {
        console.log("Filters:", filters);
    }, [filters]);

    // Sử dụng useLazyLoad
    const { items: users, loadMore, hasMore, loading, resetItems } = useLazyLoad(fetchUsers, filters);

    // Gọi loadMore khi mount và khi filters thay đổi
    useEffect(() => {
        if (users.length === 0 && hasMore && !loading) {
            loadMore().catch((err) => {
                console.error("Lỗi khi loadMore (initial):", err);
                setError(t("fetch-users-failure"));
            });
        }
    }, [users, hasMore, loading, loadMore, t]);

    // Xử lý chọn/bỏ chọn người dùng
    const handleSelect = (userId) => {
        const newSelectedIds = selectedIds.includes(userId)
            ? selectedIds.filter((id) => id !== userId)
            : [...selectedIds, userId];
        onChange(newSelectedIds);

        // Truyền thông tin người dùng qua onUserSelect
        const selectedUser = users.find(user => user.id === userId);
        if (onUserSelect && selectedUser) {
            onUserSelect({
                id: selectedUser.id,
                lastname: selectedUser.lastname,
                firstname: selectedUser.firstname,
            });
        } else if (onUserSelect && !selectedUser) {
            onUserSelect(null); // Trường hợp bỏ chọn
        }
    };

    // Lazy loading khi cuộn
    const handleScroll = () => {
        if (
            containerRef.current &&
            containerRef.current.scrollTop + containerRef.current.clientHeight >=
            containerRef.current.scrollHeight - 20 &&
            hasMore &&
            !loading
        ) {
            console.log("Scrolling, calling loadMore");
            loadMore().catch((err) => {
                console.error("Lỗi khi loadMore (scroll):", err);
                setError(t("fetch-users-failure"));
            });
        }
    };

    const uniqueUsers = useMemo(() => {
        return Array.from(new Map(users.map(user => [user.id, user])).values());
    }, [users]);

    return (
        <Form.Group className="mb-3">
            <Form.Label className="fw-bold">
                {label}
            </Form.Label>


            {error && <Alert variant="danger">{error}</Alert>}
            <InputGroup>
                <Form.Control
                    type="text"
                    placeholder={t("search-users")}
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    onKeyDown={(e) => {
                        if (e.key === 'Enter') {
                            e.preventDefault(); // Ngăn submit form khi nhấn Enter trong ô tìm kiếm
                        }
                    }}
                />
            </InputGroup>
            <ListGroup
                style={{ maxHeight: "200px", overflowY: "auto", minHeight: "100px" }}
                ref={containerRef}
                className="mt-2 border rounded"
                onScroll={handleScroll}
            >
                {users.length === 0 && !loading && !error && (
                    <ListGroup.Item className="text-center text-muted">
                        {t("no-users-found")}
                    </ListGroup.Item>
                )}
                {uniqueUsers.map((user) => (
                    <ListGroup.Item
                        key={user?.id}
                        as="div"
                        action
                        active={selectedIds.includes(user.id)}
                        onClick={() => handleSelect(user.id)}
                    >
                        {`${user.lastname} ${user.firstname}`}
                    </ListGroup.Item>
                ))}
                {loading && (
                    <ListGroup.Item className="text-center">
                        <MySpinner size="sm" />
                    </ListGroup.Item>
                )}
                {!hasMore && users.length > 0 && (
                    <ListGroup.Item className="text-center text-muted">
                        {t("no-more-users")}
                    </ListGroup.Item>
                )}
            </ListGroup>
        </Form.Group>
    );
};

export default SearchableUserSelectBox;