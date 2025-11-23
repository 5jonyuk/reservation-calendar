# ===== 1단계: 빌드용 이미지 ===== #
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
# gradle wrapper와 설정파일만 먼저 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
# 권한 부여
RUN chmod +x gradlew
# 의존성 캐시
RUN ./gradlew dependencies --no-daemon || true
# 나머지 소스 복사
COPY . .
# 빌드
RUN ./gradlew clean bootJar --no-daemon

# ===== 2단계: 실행용 이미지 ===== #
FROM eclipse-temurin:21-jre
WORKDIR /app
# Nginx 설정 복사
COPY nginx.conf /etc/nginx/conf.d/default.conf
# 빌드된 jar 복사
COPY --from=builder /app/build/libs/*.jar app.jar
# 서버 포트 (application.yml의 설정과 맞추기)
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]