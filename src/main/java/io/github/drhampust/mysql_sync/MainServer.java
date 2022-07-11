package io.github.drhampust.mysql_sync;

import io.github.drhampust.mysql_sync.events.PlayerConnectionHandler;
import io.github.drhampust.mysql_sync.util.SQL;
import io.github.drhampust.mysql_sync.util.objects.SQLColumn;
import io.github.drhampust.mysql_sync.util.sqlDataType;
import net.fabricmc.api.DedicatedServerModInitializer;

import java.util.ArrayList;
import java.util.List;

import static io.github.drhampust.mysql_sync.Main.LOGGER;
import static io.github.drhampust.mysql_sync.Main.LOGGER_PREFIX;

public class MainServer implements DedicatedServerModInitializer {

	@Override
	public void onInitializeServer() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("{} Trying to verify SQL credentials", LOGGER_PREFIX);
		if (!SQL.connectSQL()) System.exit(0);
		else SQL.disconnectSQL();
		LOGGER.info("{} Credentials are valid!", LOGGER_PREFIX);

		List<SQLColumn> columns = new ArrayList<>();
		columns.add(new SQLColumn("uuid", sqlDataType.VARCHAR, 72, false));
		columns.add(new SQLColumn("player_inventory", sqlDataType.MEDIUMTEXT, false));
		columns.add(new SQLColumn("player_selected_slot", sqlDataType.INT, false));
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
