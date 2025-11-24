import axios from "axios";
import apiUrl from "../config/ApiUrl";

let isInterceptorsSetup = false;

const NO_AUTH_ENDPOINTS = new Set(["/api/login", "/api/token/reissue"]);

// 재시도 여부를 체크하기 위한 플래그 이름
const RETRY_FLAG = "_retry";

export default function SetUpAxiosInstance() {
  if (isInterceptorsSetup) {
    return;
  }
  isInterceptorsSetup = true;

  // ======================================
  // 1. 요청 인터셉터 (토큰 삽입)
  // ======================================
  axios.interceptors.request.use((config) => {
    // config.url이 전체 URL이 아닌 상대 경로일 경우를 위해 baseURL을 사용하여 절대 경로를 만듭니다.
    // baseURL이 설정되지 않은 경우 window.location.origin을 fallback으로 사용합니다.
    const baseUrl = apiUrl;
    const pathname = new URL(config.url, baseUrl).pathname;

    if (!NO_AUTH_ENDPOINTS.has(pathname)) {
      const accessToken = localStorage.getItem("accessToken");
      if (accessToken) {
        config.headers.Authorization = `Bearer ${accessToken}`;
      }
    }
    return config;
  });

  // ======================================
  // 2. 응답 인터셉터 (토큰 재발급 및 에러 처리)
  // ======================================
  axios.interceptors.response.use(
    (response) => response,
    async (error) => {
      const originalRequest = error.config;
      const status = error.response?.status;

      // 401 Unauthorized 또는 403 Forbidden 에러 감지
      // 이미 재시도한 요청이 아닌 경우에만 재발급 로직 실행
      if ((status === 401 || status === 403) && !originalRequest[RETRY_FLAG]) {
        originalRequest[RETRY_FLAG] = true; // 재시도 플래그 설정

        const refreshToken = localStorage.getItem("refreshToken");

        if (!refreshToken) {
          // Refresh Token이 없으면 재발급 시도조차 할 수 없으므로 로그아웃 처리
          console.error("Refresh Token이 없습니다. 재로그인이 필요합니다.");
          localStorage.clear();
          alert("로그인이 만료되어 로그인 화면으로 이동합니다.");
          setTimeout(() => {
            window.location.href = "/login";
          }, 0);
          return Promise.reject(error);
        }

        try {
          // A. 토큰 재발급 API 호출
          const reissueResponse = await axios.post(
            `${apiUrl}/api/token/reissue`,
            {
              refreshToken: refreshToken,
            }
          );

          const newAccessToken = reissueResponse.data.data?.accessToken;

          if (!newAccessToken) {
            throw new Error(
              "재발급 실패: 새로운 Access Token을 받지 못했습니다."
            );
          }

          // B. 로컬 스토리지 및 기본 헤더 업데이트
          localStorage.setItem("accessToken", newAccessToken);

          // C. 실패했던 원래 요청에 새 토큰 적용 후 재시도
          originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;

          // axios 함수를 사용하여 실패했던 원래 요청을 다시 실행
          return axios(originalRequest);
        } catch (reissueError) {
          // D. 재발급 API 호출 자체가 실패한 경우 (Refresh Token 만료 등)
          console.error(
            "토근 재발급에 실패했습니다. 로그인 화면으로 돌아갑니다."
          );
          localStorage.clear();
          alert("로그인이 만료되어 다시 로그인해야 합니다.");
          setTimeout(() => {
            window.location.href = "/login";
          }, 0);
          return Promise.reject(reissueError);
        }
      }

      // 재시도 대상이 아니거나, 재발급 후에도 실패한 경우 원래 에러 반환
      return Promise.reject(error);
    }
  );
}
