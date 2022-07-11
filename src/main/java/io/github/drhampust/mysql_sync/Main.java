package io.github.drhampust.mysql_sync;

import io.github.drhampust.mysql_sync.util.config.LoggerConfig;
import io.github.drhampust.mysql_sync.util.config.SQLConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "MySQL Sync";
	public static final String LOGGER_PREFIX = "[" + MOD_ID + "], LOGGER_PREFIX";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final SQLConfig SQL_CONFIG = new SQLConfig();
	public static final LoggerConfig LOGGER_CONFIG = new LoggerConfig();


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("{} Plugin is now loaded!", LOGGER_PREFIX);
		SQL_CONFIG.load();
		SQL_CONFIG.save();
		LOGGER_CONFIG.load();
		LOGGER_CONFIG.save();

		// If I need to save config on server stop
		// ServerLifecycleEvents.SERVER_STOPPED.register(instance -> SQL_CONFIG.save());
		// ServerLifecycleEvents.SERVER_STOPPED.register(instance -> LOGGER_CONFIG.save());
	}
}
