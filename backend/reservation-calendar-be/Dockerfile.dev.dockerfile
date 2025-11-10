# 개발용 JDK 이미지
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Gradle wrapper 포함 전체 프로젝트 복사
COPY . .

# gradlew 권한 문제 방지
RUN chmod +x ./gradlew

# CMD로 dev profile로 실행
CMD ["./gradlew", "bootRun", "--args=--spring.profiles.active=dev"]

# 포트
EXPOSE 8080