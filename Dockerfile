# ビルド用ステージ（Maven + JDK 21）
# 2行目をこれに変えてみてください
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests
# 実行用ステージ（軽量化）
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Render が使う PORT 環境変数を Spring Boot に渡す
ENV PORT=10000
EXPOSE 10000

# ここを変更
ENTRYPOINT ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]
