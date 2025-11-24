import axios from "axios";

let isInterceptorsSetup = false;
const NO_AUTH_ENDPOINTS = new Set([
  "/api/login",
  "/api/signup",
  "/api/token/refresh",
]);

export default function SetUpAxiosInstance() {
  if (isInterceptorsSetup) {
    return;
  }

  isInterceptorsSetup = true;

  axios.interceptors.request.use((config) => {
    const pathname = new URL(config.url, window.location.origin).pathname;

    if (!NO_AUTH_ENDPOINTS.has(pathname)) {
      const accessToken = localStorage.getItem("accessToken");
      if (accessToken) {
        config.headers.Authorization = `Bearer ${accessToken}`;
      }
    }
    return config;
  });

  axios.interceptors.response.use(
    (response) => response,
    (error) => {
      const status = error.response?.status;
      if (status === 401 || status === 403) {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");

        alert("로그인이 만료되어 로그인 화면으로 이동합니다.");

        setTimeout(() => {
          window.location.href = "/login";
        }, 0);
      }
      return Promise.reject(error);
    }
  );
}
