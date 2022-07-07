package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("MySQL-Sync");
	private static sql SQL;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		SQL = new sql();
		if (!SQL.connectSQL()){
			LOGGER.error("SQL FAILED TO CONNECT!");
		} else {
			LOGGER.info("SQL connected successfully");
		}
		String table =
				  "UID INT NOT NULL,"
				+ "NAME VARCHAR(45) NOT NULL,"
				+ "DOB DATE NOT NULL,"
				+ "EMAIL VARCHAR(45) NOT NULL,"
				+ "PRIMARY KEY (UID)";
		SQL.createTable("test", table);
		LOGGER.info("Hello Fabric world!");
	}
}
