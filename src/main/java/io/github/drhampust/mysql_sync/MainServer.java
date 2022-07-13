package io.github.drhampust.mysql_sync;

import io.github.drhampust.mysql_sync.events.PlayerConnectionHandler;
import io.github.drhampust.mysql_sync.util.sql.SQLHelper;
import io.github.drhampust.mysql_sync.util.objects.SQLColumn;
import io.github.drhampust.mysql_sync.util.SQLDataType;
import net.fabricmc.api.DedicatedServerModInitializer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static io.github.drhampust.mysql_sync.Main.*;

public class MainServer implements DedicatedServerModInitializer {

	@Override
	public void onInitializeServer() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("{} Trying to verify SQL credentials", LOGGER_PREFIX);
		try (
			Connection con = SQLHelper.getConnection();){
			if (!con.isValid(0)) {
				System.exit(0);
			}
		} catch (SQLException e) {
			LOGGER.warn("{} SQL Connection is not valid! Please check that your configuration is correct!", LOGGER_PREFIX);
			throw new RuntimeException(e);
		}
		LOGGER.info("{} Credentials are valid!", LOGGER_PREFIX);

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

		SQLHelper.createTable("inventory", columns, "CONSTRAINT `uuid` PRIMARY KEY (`uuid`)");

		PlayerConnectionHandler playerConnectionHandler = new PlayerConnectionHandler();
		playerConnectionHandler.register();
	}
}
