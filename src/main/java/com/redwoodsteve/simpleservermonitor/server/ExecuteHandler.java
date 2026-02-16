package com.redwoodsteve.simpleservermonitor.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ExecuteHandler implements HttpHandler {

    MinecraftServer server;

    public ExecuteHandler(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        CommandSourceStack stack = server.createCommandSourceStack();
        try {
            server.getCommands().performPrefixedCommand(stack, new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, 0);
        } finally {
            exchange.sendResponseHeaders(204, 0);
        }
    }
}
