const dev = true; //배포 시 반드시 false로 변경

const apiUrl = dev ? "http://localhost:8080" : "https://api.example.com";

export default apiUrl;
