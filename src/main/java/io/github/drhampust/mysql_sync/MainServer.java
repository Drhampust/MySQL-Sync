package io.github.drhampust.mysql_sync;

import io.github.drhampust.mysql_sync.events.PlayerConnectionHandler;
import io.github.drhampust.mysql_sync.util.sql.SQLHelper;
import io.github.drhampust.mysql_sync.util.objects.SQLColumn;
import io.github.drhampust.mysql_sync.util.SQLDataType;
import net.fabricmc.api.DedicatedServerModInitializer;

import java.util.ArrayList;
import java.util.List;

import static io.github.drhampust.mysql_sync.Main.*;

public class MainServer implements DedicatedServerModInitializer {

	@Override
	public void onInitializeServer() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		if(validSQL) {
			LOGGER.info("Credentials are valid!");

			List<SQLColumn> columns = new ArrayList<>();
			columns.add(new SQLColumn("uuid", SQLDataType.VARCHAR, 72, false));
			columns.add(new SQLColumn("player_inventory", SQLDataType.MEDIUMTEXT, false));
			columns.add(new SQLColumn("player_selected_slot", SQLDataType.INT, false));
			columns.add(new SQLColumn("player_health", SQLDataType.FLOAT, false));
			columns.add(new SQLColumn("player_hunger_exhaustion", SQLDataType.FLOAT, false));
			columns.add(new SQLColumn("player_hunger_saturation", SQLDataType.FLOAT, false));
			columns.add(new SQLColumn("player_hunger_level", SQLDataType.INT, false));
			columns.add(new SQLColumn("player_level", SQLDataType.INT, false));
			columns.add(new SQLColumn("player_experience", SQLDataType.INT, false));
			if (validSQL)
				SQLHelper.createTable("inventory", columns, "CONSTRAINT `uuid` PRIMARY KEY (`uuid`)");

			PlayerConnectionHandler playerConnectionHandler = new PlayerConnectionHandler();
			playerConnectionHandler.register();
		}
	}
}
