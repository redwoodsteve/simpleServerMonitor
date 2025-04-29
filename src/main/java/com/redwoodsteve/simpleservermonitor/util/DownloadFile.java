package com.redwoodsteve.simpleservermonitor.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class DownloadFile {
    public static void downloadFromURL(URL url, Path downloadPath) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

            if (!Files.exists(downloadPath)) {
                Files.createDirectories(downloadPath.getParent());
                Files.createFile(downloadPath);
            }

            Files.write(downloadPath, connection.getInputStream().readAllBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        }
    }
}
