# 1단계: 빌드용 이미지
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
# gradle 또는 maven 프로젝트 복사
COPY . .
# gradle일 경우
RUN ./gradlew clean bootJar --no-daemon

# 2단계: 실행용 이미지
FROM eclipse-temurin:21-jre
WORKDIR /app
# 빌드된 jar 복사
COPY --from=builder /app/build/libs/*.jar app.jar
# 서버 포트 (application.yml의 설정과 맞추기)
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]