package com.redwoodsteve.simpleservermonitor;

import com.redwoodsteve.simpleservermonitor.server.ServerStatsHandler;
import com.redwoodsteve.simpleservermonitor.server.WebsiteHandler;
import com.redwoodsteve.simpleservermonitor.util.DownloadFile;
import com.sun.net.httpserver.HttpServer;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EventListeners {
    static Logger logger = Simpleservermonitor.logger;
    public static Path websiteFolderPath;

    public static void onServerStart(MinecraftServer server) {

    }
    public static void onServerBeginStart(MinecraftServer server) {
        Config.setupConfig();
        new File(Config.folderPath.toString() + "/website").mkdirs();
        websiteFolderPath = Paths.get(Config.folderPath.toString() + "/website");


        try {
            logger.info("Downloading HTML file from https://raw.githubusercontent.com/redwoodsteve/simpleServerMonitor/refs/heads/master/simpleservermonitor.html");
            DownloadFile.downloadFromURL(
                    new URL("https://raw.githubusercontent.com/redwoodsteve/simpleServerMonitor/refs/heads/master/simpleservermonitor.html"),
                    websiteFolderPath.resolve("index.html")
            );
        } catch (IOException e) {
            logger.error("Download failed.");
            throw new RuntimeException(e);
        }

        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress((Integer) Config.getFromKey("serverPort")), 0);

            httpServer.createContext("/performanceDetails", new ServerStatsHandler(server));
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
