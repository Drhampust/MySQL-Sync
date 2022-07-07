package io.github.drhampust.mysql_sync;

import com.oroarmor.config.Config;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {

	//TODO: Add Mixins for join and disconnect to be able to sync player when leaving and joining

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("MySQL Sync");
	public static Config CONFIG = new SQLConfig();


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Main Triggered!");
		CONFIG.readConfigFromFile();
		CONFIG.saveConfigToFile();
		ServerLifecycleEvents.SERVER_STOPPED.register(instance -> CONFIG.saveConfigToFile());
	}
}