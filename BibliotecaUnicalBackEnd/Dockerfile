# Usa un'immagine di base con Java
FROM eclipse-temurin:21-jdk

# Imposta la directory di lavoro
WORKDIR /app

# Copia il jar costruito dentro il container
COPY target/*.jar app.jar

# Espone la porta su cui gira la tua app (modifica se diversa)
EXPOSE 8080

# Comando di avvio
ENTRYPOINT ["java", "-jar", "app.jar"]