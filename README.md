# EcoLogistics - Integración de Envíos

## Stack utilizado

- **Lenguaje:** Java 17
- **Framework / Librerías:** Apache Camel 4.14.1
- **Servidor HTTP embebido:** Netty HTTP
- **Manejo de logs:** SLF4J 2.0.17
- **Documentación API:** OpenAPI 3.0 (`openapi.yaml`)
- **Construcción y ejecución:** Maven 3.9+

## Estructura del proyecto

```
examen/
├── src/                      # Código fuente Java
├── input/envios.csv          # CSV de entrada (envios.csv)
├── output/                   # Archivos JSON generados
├── openapi.yaml              # Contrato OpenAPI 3.0
├── Dockerfile
├── postman_collection.json   # Pruebas Postman
├── README.md
└── reflexion.pdf
```

## Instalación y ejecución

### Opción A: Maven local

1. Compilar el proyecto:

```bash
mvn clean package
```

2. Ejecutar la aplicación:

```bash
mvn clean compile exec:java
```

3. La API estará disponible en:
   - API: http://localhost:8080/api
   - Documentación OpenAPI: https://editor.swagger.io/ -> Usar openapi.yaml

### Opción B: Docker

1. Construir la imagen:

```bash
docker build -t ecologistics .
```

2. Ejecutar el contenedor:

```bash
docker run -p 8080:8080 ecologistics
```

3. Con docker-compose:

```bash
docker-compose up --build
```

## Endpoints principales

| Método | Ruta           | Descripción             |
| ------ | -------------- | ----------------------- |
| GET    | `/envios`      | Lista todos los envíos  |
| GET    | `/envios/{id}` | Obtiene envío por ID    |
| POST   | `/envios`      | Registra un nuevo envío |

## Pruebas

1. Importar `postman_collection.json` en Postman.
2. Probar los endpoints y validar respuestas.
3. Validar la documentación OpenAPI visualmente

## Logs

Carga CSV, transformación a JSON y peticiones REST registradas en consola.
