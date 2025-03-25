# Temurin OpenJDK 17을 베이스 이미지로 사용
# FROM eclipse-temurin:17

# 빌드된 애플리케이션 파일 저장 위치 설정
#WORKDIR /springboot-app

# 'JAR_FILE'이라는 빌드 인수를 정의(빌드된 JAR 파일의 경로)
#COPY --from=build /target/*.jar funniture-0.0.1-SNAPSHOT.jar
# 컨테이너가 시작될 때 실행할 명령을 정의(즉, SpringBoot 애플리케이션 실행)
#ENTRYPOINT ["java", "-jar", "app.jar"]

# base image
FROM eclipse-temurin:17

# set the working directory, 컨테이너 내부의 작업 디렉토리 설정이다. 네이밍은 자유.
WORKDIR /app

# copy the jar file, 위에서 정한 /app 디렉터리에 app.jar 라는 이름으로 jar가 복사된다.
COPY buildfile/funniture-0.0.1-SNAPSHOT.jar app.jar

# run the jar file, 컨테이너가 시작될 때 실행할 명령어를 설정한다. 여기서는 app.jar를 실행한다.
ENTRYPOINT ["java", "-jar", "app.jar"]