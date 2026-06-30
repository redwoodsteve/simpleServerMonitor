package com.redwoodsteve;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.yaml.snakeyaml.DumperOptions;

public class SimpleServerMonitor implements ModInitializer {
	public static final String MODID = "simpleservermonitor";

	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static final DumperOptions YAML_OPTIONS = new DumperOptions();

	@Override
	public void onInitialize() {
		LOGGER.info("SimpleServerMonitor was here");
		ServerLifecycleEvents.SERVER_STARTING.register(EventListeners::onServerBeginStart);

		YAML_OPTIONS.setIndent(2);
		YAML_OPTIONS.setPrettyFlow(true);
		YAML_OPTIONS.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MODID, path);
	}
}
