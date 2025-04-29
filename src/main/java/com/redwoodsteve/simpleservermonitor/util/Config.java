package com.redwoodsteve.simpleservermonitor.util;

import com.redwoodsteve.simpleservermonitor.Simpleservermonitor;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class Config {

    public static final Path folderPath = Paths.get(FabricLoader.getInstance().getGameDir().toString() + "/simpleservermonitor");
    public static final Path configPath = folderPath.resolve("config.yml");
    static final Logger logger = Simpleservermonitor.logger;

    public static void setupConfig() {

        Yaml yaml = new Yaml(Simpleservermonitor.yamlOptions);

        Map<String, Object> defaultConfig = new HashMap<>();

        defaultConfig.put("serverPort", 8080);
        defaultConfig.put("serverAuth", "no");
        defaultConfig.put("serverName", "unnamed");

        if (!Files.exists(configPath)) {
            try {
                new File(folderPath.toString()).mkdirs();
                Files.writeString(configPath, yaml.dump(defaultConfig), StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Map<String, Object> currentConfig = getConfig();
            boolean isConfigSame = currentConfig.keySet().containsAll(defaultConfig.keySet());
            if (!isConfigSame) {
                try {
                    Files.writeString(configPath, yaml.dump(defaultConfig));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
    public static void writeConfig(String key, Object value) {

        Yaml yaml = new Yaml(Simpleservermonitor.yamlOptions);

        Map<String, Object> currentConfig = getConfig();
        if (currentConfig.containsKey(key)) {
            currentConfig.put(key, value);
        } else {
            logger.error("Could not write config: could not find key \"{}\" in config.", key);
        }

    }
    public static Map<String, Object> getConfig() {

        Yaml yaml = new Yaml(Simpleservermonitor.yamlOptions);

        try {
            String configString = Files.readString(configPath);
            return yaml.load(configString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static @Nullable Object getFromKey(String key) {
        Map<String, Object> currentConfig = getConfig();
        if (currentConfig.containsKey(key)) {
            return currentConfig.get(key);
        } else {
            logger.error("Could not read config: could not find key \"{}\" in config.", key);
            return null;
        }
    }
}
