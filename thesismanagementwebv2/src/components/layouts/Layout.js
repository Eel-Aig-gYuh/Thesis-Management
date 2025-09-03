import Header from "./Header";
import Footer from "./Footer";
import { Outlet } from "react-router-dom";

const Layout = () => {
    return (
        <div className="d-flex flex-column min-vh-100">
            <Header />

            <div className="d-flex flex-grow-1">
                <main className="flex-grow-1 p-3 bg-light">
                    <Outlet />
                </main>
            </div>

        </div>
    );
};

export default Layout;