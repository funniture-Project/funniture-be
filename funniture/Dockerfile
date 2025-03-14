# 1단계: 빌드 단계
FROM eclipse-temurin:21 AS build

# 빌드된 애플리케이션 파일 저장 위치 설정
WORKDIR /springboot-app

# Gradle 설치 및 빌드 단계 추가
COPY . /springboot-app
RUN ./gradlew clean build

# 2단계: 실행 단계
FROM eclipse-temurin:21

# JAR_FILE 경로에 있는 파일(.jar)을 위에서 설정한 작업 디렉토리로 복사
COPY --from=build /app/build/libs/*.jar app.jar
# 포트 번호 변경
EXPOSE 8887

# 컨테이너가 시작될 때 실행할 명령을 정의(즉, SpringBoot 애플리케이션 실행)
ENTRYPOINT ["java", "-jar", "app.jar"]