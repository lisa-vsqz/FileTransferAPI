package com.example;

import org.apache.camel.builder.RouteBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.stream.Collectors;

public class App extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("netty-http")
                .port(8080)
                .contextPath("/api")
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "API de Envios")
                .apiProperty("api.version", "1.0.0")
                .enableCORS(true);

        // Endpoints REST
        rest("/envios").description("Gestión de envios")
                .get().to("direct:listarEnvios")
                .post().to("direct:crearEnvio");

        rest("/envios/{id}")
                .get().to("direct:obtenerEnvio");

        // Ruta: listar todos los envíos
        from("direct:listarEnvios")
                .process(exchange -> {
                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(FileTransferRoute.enviosList);
                    exchange.getIn().setBody(json);
                    exchange.getIn().setHeader("Content-Type", "application/json");
                    System.out.println("[INFO] Listando todos los envios en JSON");
                });

        // Ruta: crear un nuevo envío
        from("direct:crearEnvio")
                .unmarshal().json()
                .process(exchange -> {
                    Map<String, Object> nuevoEnvioObj = exchange.getIn().getBody(Map.class);
                    Map<String, String> nuevoEnvioStr = nuevoEnvioObj.entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    e -> e.getValue().toString()));
                    FileTransferRoute.enviosList.add(nuevoEnvioStr);

                    ObjectMapper mapper = new ObjectMapper();
                    String jsonResponse = mapper
                            .writeValueAsString(Map.of("mensaje", "Envio registrado correctamente"));
                    exchange.getIn().setBody(jsonResponse);
                    exchange.getIn().setHeader("Content-Type", "application/json");
                });

        // Ruta: obtener envío por ID
        from("direct:obtenerEnvio")
                .process(exchange -> {
                    String id = exchange.getIn().getHeader("id", String.class);
                    Map<String, String> envio = FileTransferRoute.enviosList.stream()
                            .filter(e -> e.get("id").equals(id))
                            .findFirst()
                            .orElse(Map.of("mensaje", "Envio no encontrado"));

                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(envio);
                    exchange.getIn().setBody(json);
                    exchange.getIn().setHeader("Content-Type", "application/json");
                    System.out.println("[INFO] Consultando envio con ID: " + id);
                });

    }
}
