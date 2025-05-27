import { BrowserRouter, Route, Routes } from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';
import { MyDispatcherContext, MyUserContext } from "./configs/MyContexts";
import { useEffect, useReducer } from "react";
import MyUserReducer from "./reducers/MyUserReducer";
import Home from "./components/Home";
import Login from "./components/auths/Login";
import { ToastProvider } from "./components/contexts/ToastProvider";
import Layout from "./components/layouts/Layout";
import ChangePassword from "./components/auths/ChangePassword";
import ThesisListPage from "./components/theses/ThesisListPage";
import PageLayout from "./components/layouts/PageLayout";
import CouncilCreatePage from "./components/council/CouncilCreatePage";
import Cookies from "js-cookie";
import { authApis, endpoints } from "./configs/Apis";
import ThesisFormPage from "./components/theses/ThesisFormPage";
import CouncilManagePage from "./components/council/CouncilManagePage";
import CouncilDetailPage from "./components/council/CouncilDetailPage";
import ThesisDetailPage from "./components/theses/ThesisDetailPage";
import CriteriaCreatePage from "./components/scores/CriteriaCreatePage";
import StatisticsPage from "./components/stats/StatisticsPage";

const App = () => {
  const [user, dispatch] = useReducer(MyUserReducer, null);

  useEffect(() => {
    const loadCurrentUser = async () => {
      const token = Cookies.get("token");
      if (token) {
        try {
          const res = await authApis().get(endpoints["current-user"]);
          dispatch({
            type: "login",
            payload: res.data,
          });
        } catch (ex) {
          console.error("Failed to load current user:", ex);
          Cookies.remove("token");
          dispatch({ type: "logout" });
        }
      }
    };
    loadCurrentUser();
  }, []);

  return (
    <MyUserContext.Provider value={user}>
      <MyDispatcherContext.Provider value={dispatch}>
        <ToastProvider>
          <BrowserRouter>

            <Routes>
              <Route path="/" element={<Layout />}>
                <Route path="/" element={<Home />} />
                <Route path="auth/login" element={<Login />} />
                <Route path="auth/change-password" element={<ChangePassword />} />

                <Route path="/" element={<PageLayout />}>
                  <Route path="thesis/" element={<ThesisListPage />} />
                  <Route path="/thesis/create" element={<ThesisFormPage />} />
                  <Route path="/thesis/:thesisId" element={<ThesisDetailPage />} />
                  <Route path="/thesis/edit/:thesisId" element={<ThesisFormPage />} />
                  
                  <Route path="criteria/create" element={<CriteriaCreatePage />} />
                  
                  <Route path="council/" element={<CouncilManagePage />} />
                  <Route path="council/my-council/" element={<CouncilManagePage />} />
                  <Route path="council/create" element={<CouncilCreatePage />} />
                  <Route path="/council/:councilId" element={<CouncilDetailPage />} />
                  <Route path="/council/edit/:councilId" element={<CouncilCreatePage />} />

                  <Route path="/statistics" element={<StatisticsPage />} />

                </Route>
              </Route>

            </Routes>

          </BrowserRouter>
        </ToastProvider>
      </MyDispatcherContext.Provider>
    </MyUserContext.Provider>
  );
}

export default App;