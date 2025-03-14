# 1단계: 빌드 단계
FROM eclipse-temurin:21 AS build

# 빌드된 애플리케이션 파일 저장 위치 설정
WORKDIR /springboot-app

# 프로젝트 파일을 복사
COPY . /springboot-app

# gradlew 파일에 실행 권한을 부여
RUN chmod +x ./gradlew

# Gradle 빌드 실행
RUN ./gradlew clean build

# 2단계: 실행 단계
FROM eclipse-temurin:21

WORKDIR /springboot-app

# 빌드된 .jar 파일을 복사
COPY --from=build /springboot-app/build/libs/*.jar app.jar

# 포트 번호 변경
EXPOSE 8887

# 컨테이너가 시작될 때 실행할 명령을 정의(즉, Spring Boot 애플리케이션 실행)
ENTRYPOINT ["java", "-jar", "app.jar"]
