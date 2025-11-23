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
      if (error.response?.status === 401) {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        window.location.href = "/login";
      }
      return Promise.reject(error);
    }
  );
}
