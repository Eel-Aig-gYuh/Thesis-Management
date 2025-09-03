import { useEffect, useState } from "react";
import { useToast } from "../contexts/ToastProvider";
import { useTranslation } from "react-i18next";
import "../../i18n/index";

export const useLazyLoad = (fetchFunction, filters = {}) => {
    const { t } = useTranslation();
    const toast = useToast();
    
    const [items, setItems] = useState([]);
    const [page, setPage] = useState(1);
    const [hasMore, setHasMore] = useState(true);
    const [loading, setLoading] = useState(false);

    const loadMore = async () => {
        if (loading || !hasMore) return ;
        
        setLoading(true);
        try {
            const { data, totalPages } = await fetchFunction(page, filters);
            
            setItems(prev => [...prev, ...data]);
            setPage(prev => prev + 1);
            
            if (page + 1 >= totalPages) setHasMore(false);
        } catch (err) {
            console.error(err);
            toast(t("thesis-fetch-failure"), "danger");
        } finally {
            setLoading(false);
        }
    };

    const resetItems = () => {
        setItems([]);
        setPage(1);
        setHasMore(true);
    };

    // Reset lại items khi filters thay đổi
    useEffect(() => {
        resetItems();
    }, [filters]);

    return { items, loadMore, hasMore, loading, resetItems};
};

export default useLazyLoad;