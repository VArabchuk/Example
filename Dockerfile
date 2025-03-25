# Builder stage
FROM debian:12.10-slim-local as builder
WORKDIR /application

# Встановлення gnupg для додавання ключів
RUN sudo apt-get update && sudo apt-get install -y gnupg

# Встановлення JDK конкретної версії
ARG JDK_PACKAGE=17.0.14+7-1~deb12u1

# Додаємо відсутні ключі для репозиторіїв (якщо це потрібно)
RUN apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 0E98404D386FA1D9 6ED0E7B82643E131 F8D2585B8783D481 54404762BBB6E853 BDE6D2B9216EC7A8 && \
    apt-get update --allow-unauthenticated && \
    apt-get install --no-install-recommends -y --allow-unauthenticated \
        openjdk-17-jdk=${JDK_PACKAGE} \
        openjdk-17-jre=${JDK_PACKAGE} \
        openjdk-17-jdk-headless=${JDK_PACKAGE} \
        openjdk-17-jre-headless=${JDK_PACKAGE} && \
    rm -rf /var/lib/apt/lists/*

# Копіюємо JAR-файл
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# Final stage
FROM debian:12.10-slim
WORKDIR /application

# Встановлення gnupg для додавання ключів
RUN sudo apt-get update && sudo apt-get install -y gnupg

# Встановлення JDK у фінальному образі
ARG JDK_PACKAGE=17.0.14+7-1~deb12u1

# Додаємо відсутні ключі для репозиторіїв (якщо це потрібно)
RUN apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 0E98404D386FA1D9 6ED0E7B82643E131 F8D2585B8783D481 54404762BBB6E853 BDE6D2B9216EC7A8 && \
    apt-get update --allow-unauthenticated && \
    apt-get install --no-install-recommends -y --allow-unauthenticated \
        openjdk-17-jdk=${JDK_PACKAGE} \
        openjdk-17-jre=${JDK_PACKAGE} \
        openjdk-17-jdk-headless=${JDK_PACKAGE} \
        openjdk-17-jre-headless=${JDK_PACKAGE} && \
    rm -rf /var/lib/apt/lists/*

# Копіюємо файли з builder-стадії
COPY --from=builder /application/dependencies/ ./dependencies/
COPY --from=builder /application/spring-boot-loader/ ./spring-boot-loader/
COPY --from=builder /application/snapshot-dependencies/ ./snapshot-dependencies/
COPY --from=builder /application/application/ ./application/

# Вказуємо точку входу для запуску програми
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

# Відкриваємо порт 8080
EXPOSE 8080

# Можна також додати цікавий параметр для виконання після запуску контейнера
CMD ["--server.port=8080"]
