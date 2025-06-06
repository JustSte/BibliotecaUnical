# Usa un'immagine base di Java 17 (o la tua versione)
FROM eclipse-temurin:21-jdk

# Crea una directory per l'app
WORKDIR /app

# Copia il JAR generato nella directory
COPY target/*.jar app.jar

# Espone la porta (cambia se usi una diversa in Spring Boot)
EXPOSE 8080

# Comando per avviare l'applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]
