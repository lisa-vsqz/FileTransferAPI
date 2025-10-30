package com.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class FileTransferRoute extends RouteBuilder {

    // Lista en memoria para guardar los envíos
    public static List<Map<String, String>> enviosList = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(new FileTransferRoute());
        main.run(args);
    }

    @Override
    public void configure() throws Exception {
        // Ruta para leer CSV, transformar a JSON y almacenar en memoria y archivo
        from("file:./input?noop=true&include=envios.csv")
                .log("[INFO] Archivo cargado: ${file:name}")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        File file = exchange.getIn().getBody(File.class);
                        List<String> lines = Files.readAllLines(file.toPath());

                        enviosList.clear();

                        for (int i = 1; i < lines.size(); i++) {
                            String[] fields = lines.get(i).split(",");
                            Map<String, String> envio = new HashMap<>();
                            envio.put("id", fields[0].trim());
                            envio.put("cliente", fields[1].trim());
                            envio.put("direccion", fields[2].trim());
                            envio.put("estado", fields[3].trim());
                            enviosList.add(envio);
                        }

                        ObjectMapper mapper = new ObjectMapper();
                        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(enviosList);
                        exchange.getIn().setBody(json);

                        System.out.println("[INFO] Datos transformados a formato JSON:");
                        System.out.println(json);

                        File outputDir = new File("./output");
                        if (!outputDir.exists()) {
                            outputDir.mkdirs();
                        }
                        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputDir, "envios.json"),
                                enviosList);
                        System.out.println("[INFO] Archivo envios.json creado en carpeta output");
                    }
                })
                .to("log:envios-json?level=INFO");
    }
}
