# Temurin OpenJDK 17을 베이스 이미지로 사용
FROM eclipse-temurin:17

# 빌드된 애플리케이션 파일 저장 위치 설정
WORKDIR /springboot-app

# JAR 파일을 복사할 때, Dockerfile이 위치한 디렉토리에서 상대 경로로 지정
COPY build/libs/*.jar app.jar

# 컨테이너가 시작될 때 실행할 명령을 정의(즉, SpringBoot 애플리케이션 실행)
ENTRYPOINT ["java", "-jar", "app.jar"]
