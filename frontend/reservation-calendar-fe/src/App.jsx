import { useEffect, useState, useCallback } from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Home from "./pages/Home";
import LoginPage from "./pages/LoginPage";
import Modal from "react-modal";
import apiUrl from "./config/ApiUrl";
import SetUpAxiosInstance from "./component/SetUpAxiosInstance";
import axios from "axios";

Modal.setAppElement("#root");
SetUpAxiosInstance();

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [isLoadingInitial, setIsLoadingInitial] = useState(true);

  const checkToken = () => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      setIsLoggedIn(true);
    }
    setIsLoadingInitial(false);
  };

  const handleLoginSuccess = useCallback(() => {
    setIsLoggedIn(true);
  }, []);

  const handleLogout = useCallback(async () => {
    const refreshToken = localStorage.getItem("refreshToken");

    try {
      if (refreshToken) {
        const response = await axios.post(`${apiUrl}/api/token/logout`, {
          refreshToken,
        });
      }
    } catch (e) {
      console.error("로그아웃 중 서버 통신 오류 또는 실패 응답:", e);
    }

    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    setIsLoggedIn(false);
  }, []);

  useEffect(() => {
    checkToken();
  }, []);

  if (isLoadingInitial) {
    return (
      <div className="flex items-center justify-center min-h-screen bg-white">
        <p className="text-gray-600">인증 상태 확인 중...</p>
      </div>
    );
  }

  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/login"
          element={
            isLoggedIn ? (
              <Navigate to="/home" replace />
            ) : (
              <LoginPage onLoginSuccess={handleLoginSuccess}></LoginPage>
            )
          }
        />
        <Route
          path="/home"
          element={
            isLoggedIn ? (
              <Home onLogout={handleLogout}></Home>
            ) : (
              <Navigate to="/login" replace />
            )
          }
        />
        <Route path="/" element={<Navigate to="login" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
