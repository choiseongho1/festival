# 플랫폼 명시적으로 설정
FROM --platform=linux/amd64 openjdk:17-jdk-alpine

# 임시 디렉토리 설정 및 JAR 파일 복사
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 앱 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
