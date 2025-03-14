# 1단계: 빌드 단계
FROM eclipse-temurin:21 AS build

# 빌드된 애플리케이션 파일 저장 위치 설정
WORKDIR /springboot-app

# 프로젝트 파일을 복사
COPY . /springboot-app

# gradlew 파일에 실행 권한을 부여
RUN chmod +x ./gradlew

# Gradle 빌드 실행 (Gradle Daemon을 비활성화)
RUN ./gradlew clean build --no-daemon

# 2단계: 실행 단계
FROM eclipse-temurin:21

WORKDIR /springboot-app

# 'JAR_FILE'이라는 빌드 인수를 정의(빌드된 JAR 파일의 경로)
ARG JAR_FILE=build/libs/*.jar

# JAR_FILE 경로에 있는 파일(.jar)을 위에서 설정한 작업 디렉토리로 복사
COPY ${JAR_FILE} app.jar

# 포트 번호 변경
EXPOSE 8887

# 컨테이너가 시작될 때 실행할 명령을 정의(즉, Spring Boot 애플리케이션 실행)
ENTRYPOINT ["java", "-jar", "app.jar"]
