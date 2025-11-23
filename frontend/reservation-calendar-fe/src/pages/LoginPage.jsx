import React, { useState } from "react";
import apiUrl from "../config/ApiUrl";
import logo from "../img/logo.png";
import Icon from "../component/Icon";

const UserIcon = (props) => (
  <Icon {...props}>
    <path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2" />
    <circle cx="12" cy="7" r="4" />
  </Icon>
);

const LockIcon = (props) => (
  <Icon {...props}>
    <rect x="3" y="11" width="18" height="11" rx="2" ry="2" />
    <path d="M7 11V7a5 5 0 0 1 10 0v4" />
  </Icon>
);

const Loader2Icon = (props) => (
  <Icon
    {...props}
    className={props.className + " animate-spin"}
    viewBox="0 0 24 24"
  >
    <path d="M21 12a9 9 0 1 1-6.219-8.56" />
  </Icon>
);

export default function LoginPage({ onLoginSuccess }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");

  const LOGIN_ENDPOINT = `${apiUrl}/api/login`;

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");
    setIsLoading(true);

    if (!username.trim() || !password.trim()) {
      setError("사용자 이름과 비밀번호를 모두 입력해주세요.");
      setIsLoading(false);
      return;
    }

    try {
      const response = await fetch(LOGIN_ENDPOINT, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
      });

      const apiResponse = await response.json();

      if (response.ok) {
        const accessToken = apiResponse.data?.accessToken;
        const refreshToken = apiResponse.data?.refreshToken;

        if (accessToken && refreshToken) {
          localStorage.setItem("accessToken", accessToken);
          localStorage.setItem("refreshToken", refreshToken);
          onLoginSuccess();
        } else {
          setError("로그인 성공했으나, 토큰 데이터를 받지 못했습니다.");
        }
      } else {
        setError(
          apiResponse.message ||
            "로그인에 실패했습니다. 서버 메시지를 확인해주세요."
        );
      }
    } catch (err) {
      console.error("API 통신 오류:", err);
      setError(
        "서버와 통신하는 중 오류가 발생했습니다. (네트워크/파싱 오류일 수 있음)"
      );
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-amber-50 p-4">
      <div className="w-full max-w-md">
        <h1 className="text-center text-4xl font-extrabold text-[#5C3A21] mb-12">
          유리네과자집 예약 스케줄
        </h1>
        {/* ===== 로그인 폼 ===== */}
        <div className="bg-white shadow-2xl rounded-xl p-8 md:p-10 space-y-8 border border-gray-100">
          <div className="flex flex-col items-center space-y-2">
            {/* ===== 로고 영역 ===== */}
            <div className="flex justify-center mb-8">
              <div className="w-40 h-40 bg-white rounded-full shadow-lg flex items-center justify-center overflow-hidden">
                <img
                  src={logo}
                  alt="yuri-snack-house-logo"
                  className="w-full h-full object-cover rounded-full"
                />
              </div>
            </div>

            <h1 className="text-3xl font-extrabold text-gray-900">로그인</h1>
          </div>

          <form onSubmit={handleLogin} className="space-y-6">
            {/* 사용자 이름 입력 필드 */}
            <div>
              <label
                htmlFor="username"
                className="block text-sm font-medium text-gray-700 mb-2"
              >
                사용자 이름
              </label>
              <div className="relative rounded-lg shadow-sm">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <UserIcon className="h-5 w-5 text-gray-400" />
                </div>
                <input
                  id="username"
                  name="username"
                  type="text"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  required
                  disabled={isLoading}
                  className="block w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-[#8B5E3C] focus:border-[#8B5E3C] transition duration-150 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  placeholder="사용자 이름 입력"
                />
              </div>
            </div>

            {/* 비밀번호 입력 필드 */}
            <div>
              <label
                htmlFor="password"
                className="block text-sm font-medium text-gray-700 mb-2"
              >
                비밀번호
              </label>
              <div className="relative rounded-lg shadow-sm">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <LockIcon className="h-5 w-5 text-gray-400" />
                </div>
                <input
                  id="password"
                  name="password"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                  disabled={isLoading}
                  className="block w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-[#8B5E3C] focus:border-[#8B5E3C] transition duration-150 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  placeholder="비밀번호 입력"
                />
              </div>
            </div>

            {/* 에러 메시지 */}
            {error && (
              <div
                className="p-3 text-sm font-medium text-red-700 bg-red-100 border border-red-300 rounded-lg"
                role="alert"
              >
                {error}
              </div>
            )}

            {/* 로그인 버튼 */}
            <button
              type="submit"
              disabled={isLoading}
              className={`
                w-full flex items-center justify-center p-3 text-white rounded-lg transition duration-200 font-semibold shadow-md focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-[#8B5E3C] cursor-pointer 
                ${
                  isLoading
                    ? "bg-[#8B5E3C] cursor-not-allowed"
                    : "bg-[#8B5E3C] hover:bg-[#5C3A21] shadow-[#8B5E3C]/50"
                }
                `}
            >
              {isLoading ? (
                <>
                  <Loader2Icon className="w-5 h-5 mr-2" />
                  로그인 중...
                </>
              ) : (
                "로그인"
              )}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
