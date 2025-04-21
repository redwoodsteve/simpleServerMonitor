package com.redwoodsteve.simpleservermonitor.server;

import com.google.gson.Gson;
import com.redwoodsteve.simpleservermonitor.Config;
import com.redwoodsteve.simpleservermonitor.Simpleservermonitor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.slf4j.Logger;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ServerStatsHandler implements HttpHandler {

    private final MinecraftServer server;
    private Logger logger = Simpleservermonitor.logger;

    public ServerStatsHandler(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ( Authentication.isAuthorized(exchange.getRequestHeaders().getFirst("auth")) ) {
            returnInfo(exchange);
        } else {
            exchange.sendResponseHeaders(401, 0);
        }
    }

    @SuppressWarnings({})
    private void returnInfo(HttpExchange e) throws IOException {

        logger.info("requesting");

        PlayerList playerList = server.getPlayerList();

        Map<String, Map<String, Object>> players = new HashMap<>();

        for (ServerPlayer p : playerList.getPlayers()) {
            Map<String, Object> plrStats = new HashMap<>();
            plrStats.put("name", p.getDisplayName().getString());
            plrStats.put("op", Arrays.asList(playerList.getOps().getUserList()).contains(p.getDisplayName().getString()));
            plrStats.put("gamemode", p.gameMode.getGameModeForPlayer().getName());

            players.put(p.getUUID().toString(), plrStats);
        }

        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        Runtime runtime = Runtime.getRuntime();
        double processCPU = osBean.getProcessCpuLoad() * 100;
        double systemCPU = osBean.getCpuLoad() * 100;
        double systemRAM = 100 / ((double) osBean.getTotalMemorySize() / osBean.getFreeMemorySize());
        double processRAM = 100 / ((double) runtime.totalMemory() / runtime.freeMemory());

        Map<String, Object> statsMap = new HashMap<>();
        statsMap.put("players", players);
        statsMap.put("processCPU", processCPU);
        statsMap.put("processRAM", processRAM);
        statsMap.put("systemCPU", systemCPU);
        statsMap.put("systemRAM", systemRAM);
        statsMap.put("name", Config.getFromKey("serverName"));

        Gson gson = new Gson();
        String responseText = gson.toJson(statsMap);

        logger.debug("Sending string {}", responseText);

        e.sendResponseHeaders(200, responseText.getBytes(Charset.defaultCharset()).length);
        e.getResponseHeaders().add("Content-Type", "application/json");
        e.getResponseBody().write(responseText.getBytes());
        e.getResponseBody().close();
    }
}
