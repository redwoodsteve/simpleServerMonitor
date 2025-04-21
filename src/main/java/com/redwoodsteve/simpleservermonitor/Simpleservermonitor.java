package com.redwoodsteve.simpleservermonitor;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;

public class Simpleservermonitor implements ModInitializer {

    public static final String MODID = "simpleservermonitor";
    public static final Logger logger = LoggerFactory.getLogger(MODID);
    public static final DumperOptions yamlOptions = new DumperOptions();

    @Override
    public void onInitialize() {
        yamlOptions.setIndent(2);
        yamlOptions.setPrettyFlow(true);
        yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        ServerLifecycleEvents.SERVER_STARTED.register(EventListeners::onServerStart);
        ServerLifecycleEvents.SERVER_STARTING.register(EventListeners::onServerBeginStart);
    }
}
