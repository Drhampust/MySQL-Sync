package io.github.drhampust.mysql_sync;

import com.oroarmor.config.ConfigItemGroup;
import io.github.drhampust.mysql_sync.events.PlayerConnectionHandler;
import io.github.drhampust.mysql_sync.util.SQL;
import net.fabricmc.api.DedicatedServerModInitializer;

import static io.github.drhampust.mysql_sync.Main.CONFIG;
import static io.github.drhampust.mysql_sync.Main.LOGGER;

public class MainServer implements DedicatedServerModInitializer {

	//TODO: Add Mixins for join and disconnect to be able to sync player when leaving and joining
	private static SQL SQL;
	private static PlayerConnectionHandler playerConnectionHandler;

	@Override
	public void onInitializeServer() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		String host = (String) ((ConfigItemGroup)CONFIG.getConfigs().get(0).getConfigs().get(0)).getConfigs().get(0).getValue();
		String port = (String) ((ConfigItemGroup)CONFIG.getConfigs().get(0).getConfigs().get(0)).getConfigs().get(1).getValue();
		String database = (String) ((ConfigItemGroup)CONFIG.getConfigs().get(0).getConfigs().get(0)).getConfigs().get(2).getValue();
		String username = (String) ((ConfigItemGroup)CONFIG.getConfigs().get(0).getConfigs().get(0)).getConfigs().get(3).getValue();
		String password = (String) ((ConfigItemGroup)CONFIG.getConfigs().get(0).getConfigs().get(0)).getConfigs().get(4).getValue();

		LOGGER.debug("Found this in config: Host:" + host + ", port:" + port + ", database:" + database + ", username:" + username + ", password:" + password);

		SQL = new SQL();

		Main.LOGGER.info("Trying to verify SQL credentials");
		if (!SQL.connectSQL()) System.exit(0);
		else SQL.disconnectSQL();
		Main.LOGGER.info("Credentials are valid!");

		String table =
				"UID INT NOT NULL,"
						+ "NAME VARCHAR(45) NOT NULL,"
						+ "DOB DATE NOT NULL,"
						+ "EMAIL VARCHAR(45) NOT NULL,"
						+ "PRIMARY KEY (UID)";
		SQL.createTable("test", table);


		playerConnectionHandler = new PlayerConnectionHandler();
		playerConnectionHandler.register();
	}
}
