package io.github.drhampust.mysql_sync;

import com.oroarmor.config.ConfigItemGroup;
import io.github.drhampust.mysql_sync.events.PlayerConnectionHandler;
import io.github.drhampust.mysql_sync.util.SQL;
import io.github.drhampust.mysql_sync.util.objects.SQLColumn;
import io.github.drhampust.mysql_sync.util.sqlDataType;
import net.fabricmc.api.DedicatedServerModInitializer;

import java.util.ArrayList;
import java.util.List;

import static io.github.drhampust.mysql_sync.Main.CONFIG;
import static io.github.drhampust.mysql_sync.Main.LOGGER;

public class MainServer implements DedicatedServerModInitializer {

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


		Main.LOGGER.info("Trying to verify SQL credentials");
		if (!SQL.connectSQL()) System.exit(0);
		else SQL.disconnectSQL();
		Main.LOGGER.info("Credentials are valid!");

		List<SQLColumn> columns = new ArrayList<>();
		columns.add(new SQLColumn("uuid", sqlDataType.VARCHAR, 72, false));
		columns.add(new SQLColumn("player_inventory", sqlDataType.MEDIUMTEXT, false));
		columns.add(new SQLColumn("player_health", sqlDataType.FLOAT, false));
		columns.add(new SQLColumn("player_hunger_exhaustion", sqlDataType.FLOAT, false));
		columns.add(new SQLColumn("player_hunger_saturation", sqlDataType.FLOAT, false));
		columns.add(new SQLColumn("player_hunger_level", sqlDataType.INT, false));
		columns.add(new SQLColumn("player_level", sqlDataType.INT, false));
		columns.add(new SQLColumn("player_experience", sqlDataType.INT, false));

		SQL.createTable("inventory", columns, "CONSTRAINT `uuid` PRIMARY KEY (`uuid`)");

		PlayerConnectionHandler playerConnectionHandler = new PlayerConnectionHandler();
		playerConnectionHandler.register();
	}
}
