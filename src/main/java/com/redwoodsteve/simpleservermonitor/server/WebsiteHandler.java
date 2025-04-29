package com.redwoodsteve.simpleservermonitor.server;

import com.redwoodsteve.simpleservermonitor.EventListeners;
import com.redwoodsteve.simpleservermonitor.Simpleservermonitor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class WebsiteHandler implements HttpHandler {

    private static final Logger logger = Simpleservermonitor.logger;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Map<String, String> fileTypes = new HashMap<>();
        fileTypes.put("html", "text/html");
        fileTypes.put("css", "text/css");
        fileTypes.put("js", "text/javascript");
        fileTypes.put("png", "image/png");
        fileTypes.put("json", "application/json");

        if (!Files.exists(EventListeners.websiteFolderPath.resolve(exchange.getRequestURI().getPath().substring(1)))) {
            exchange.sendResponseHeaders(404, 0);
            return;
        }

        InputStream fileStream;
        if (exchange.getRequestURI().getPath().equalsIgnoreCase("/") || exchange.getRequestURI().getPath().isEmpty()) {
            fileStream = Files.newInputStream(EventListeners.websiteFolderPath.resolve("index.html"));
        } else {
            fileStream = Files.newInputStream(EventListeners.websiteFolderPath.resolve(exchange.getRequestURI().getPath().substring(1)));
        }


        byte[] fileBytes = fileStream.readAllBytes();
        //String fileContent = new String(fileBytes, Charset.defaultCharset());
        fileStream.close();
        String ext;
        if (exchange.getRequestURI().toString().equalsIgnoreCase("/")) {
            ext = "html";
        } else {
            String[] arr = exchange.getRequestURI().toString().split("\\.");
            ext = arr[arr.length - 1];
            ext = fileTypes.getOrDefault(ext, "text/plain");
        }
        exchange.getResponseHeaders().add("Content-Type", ext);

        exchange.sendResponseHeaders(200, fileBytes.length);
        exchange.getResponseBody().write(fileBytes);
        exchange.getResponseBody().close();
    }
}
