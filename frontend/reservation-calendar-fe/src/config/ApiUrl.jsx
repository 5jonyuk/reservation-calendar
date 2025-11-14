const dev = false; //배포 시 반드시 false로 변경

const apiUrl = dev
  ? "http://localhost:8080"
  : "https://yuri-snack-house.vercel.app";

export default apiUrl;
