package com.redwoodsteve.simpleservermonitor.server;

import com.redwoodsteve.simpleservermonitor.EventListeners;
import com.redwoodsteve.simpleservermonitor.Simpleservermonitor;
import com.redwoodsteve.simpleservermonitor.util.DownloadFile;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class WebsiteHandler implements HttpHandler {

    private static final Logger logger = Simpleservermonitor.logger;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        InputStream fileStream = Files.newInputStream(EventListeners.websiteFolderPath.resolve("index.html"));
        byte[] fileBytes = fileStream.readAllBytes();
        //String fileContent = new String(fileBytes, Charset.defaultCharset());
        fileStream.close();

        exchange.sendResponseHeaders(200, fileBytes.length);
        exchange.getResponseBody().write(fileBytes);
        exchange.getResponseHeaders().add("Content-Type", "text/html");
        exchange.getResponseBody().close();
    }
}
