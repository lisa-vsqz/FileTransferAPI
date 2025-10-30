# Imagen base con Java y Maven
FROM maven:3.9.1-eclipse-temurin-17

# Carpeta de trabajo
WORKDIR /app

# Copia pom y src
COPY pom.xml .
COPY src ./src
COPY openapi.yaml ./openapi.yaml

# Carpeta input (CSV)
COPY input ./input

# Compila proyecto
RUN mvn clean package -DskipTests

# Puerto expuesto
EXPOSE 8080

# Ejecuta la aplicación
CMD ["java", "-cp", "target/ecologistics-1.0-SNAPSHOT.jar", "com.example.MainApp"]
