package com.redwoodsteve.simpleservermonitor;

import com.google.gson.Gson;
import com.redwoodsteve.simpleservermonitor.server.ExecuteHandler;
import com.redwoodsteve.simpleservermonitor.server.ServerStatsHandler;
import com.redwoodsteve.simpleservermonitor.server.WebsiteHandler;
import com.redwoodsteve.simpleservermonitor.util.Config;
import com.redwoodsteve.simpleservermonitor.util.DownloadFile;
import com.redwoodsteve.simpleservermonitor.util.GithubTreeResponse;
import com.sun.net.httpserver.HttpServer;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class EventListeners {
    static Logger logger = Simpleservermonitor.logger;
    public static Path websiteFolderPath;

    public static void onServerBeginStart(MinecraftServer server) {
        Config.setupConfig();
        new File(Config.folderPath + "/website").mkdirs();
        websiteFolderPath = Paths.get(Config.folderPath + "/website");


        try {

            String websiteRepo = "https://raw.githubusercontent.com/redwoodsteve/simpleServerMonitorWebsite/refs/heads/main/";

            logger.info("Downloading HTML files");
            HttpURLConnection connection = (HttpURLConnection) new URI("https://api.github.com/repos/redwoodsteve/simpleServerMonitorWebsite/git/trees/main?recursive=1").toURL().openConnection();
            connection.setRequestMethod("GET");

            String filesString = new String(connection.getInputStream().readAllBytes(), Charset.defaultCharset());
            connection.getInputStream().close();

            Gson gson = new Gson();
            GithubTreeResponse files = gson.fromJson(filesString, GithubTreeResponse.class);
            for (Map<String, Object> paths : files.tree) {

                URL fileURL = new URI(websiteRepo + paths.get("path")).toURL();

                DownloadFile.downloadFromURL(fileURL, Paths.get(websiteFolderPath.toString() + "/" + paths.get("path")));

            }


            DownloadFile.downloadFromURL(
                    new URI("https://raw.githubusercontent.com/redwoodsteve/simpleServerMonitor/refs/heads/master/simpleservermonitor.html").toURL(),
                    websiteFolderPath.resolve("index.html")
            );
        } catch (IOException e) {
            logger.error("Download failed.");
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress((Integer) Config.getFromKey("serverPort")), 0);

            httpServer.createContext("/performanceDetails", new ServerStatsHandler(server));
            httpServer.createContext("/execute", new ExecuteHandler(server));
            httpServer.createContext("/", new WebsiteHandler());

            httpServer.setExecutor(null);
            httpServer.start();

            logger.info("Started HTTP server on port {}.", Config.getFromKey("serverPort"));
        } catch (NoClassDefFoundError e) {
            throw new RuntimeException("Please switch to a different distribution of Java that includes the 'com.sun' package. Some Java distributions include OpenJDK and Amazon Corretto.");
        } catch (IOException e) {
            throw new RuntimeException("Error setting up HTTP server for server monitor.");
        }
    }
}
