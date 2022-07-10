package io.github.drhampust.mysql_sync.events;

import io.github.drhampust.mysql_sync.Main;
import io.github.drhampust.mysql_sync.util.SQL;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static io.github.drhampust.mysql_sync.Main.LOGGER;
import static io.github.drhampust.mysql_sync.util.SQL.*;
import static io.github.drhampust.mysql_sync.util.base64Util.*;

public class PlayerConnectionHandler implements ServerPlayConnectionEvents.Init, ServerPlayConnectionEvents.Disconnect {
	public void register() {
		ServerPlayConnectionEvents.INIT.register(this);
		ServerPlayConnectionEvents.DISCONNECT.register(this);
	}


	@Override
	public void onPlayDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {
		LOGGER.info("Player disconnected saving inventory to db...");
		ServerPlayerEntity player = handler.getPlayer();
		String[] columns = {"uuid", "player_inventory"}; // Oracle
		Object[] values = {toBase64(player.getUuidAsString()), toBase64(player)};
		SQL.insertNoDuplicates("inventory", columns, values, columns[0]);
	}

	@Override
	public void onPlayInit(ServerPlayNetworkHandler handler, MinecraftServer server) {
		ServerPlayerEntity player = handler.getPlayer();

		connectSQL(); // Connect SQL
		try {
			// SQL Statement to get row of this player UUID
			PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM`minecraft_cross_server`.`inventory` WHERE UUID= ?");
			preparedStatement.setString(1, toBase64(player.getUuidAsString())); // Insert Key

			ResultSet result = preparedStatement.executeQuery(); // get Result set (should only be 1 row)
			int i = 0;
			while (result.next()) {
				fromBase64(result.getString(2), player); // decode Inventory and replace player inventory
				i++;
			}
			Main.LOGGER.info("All results decoded, {} entries", i);

		} catch (SQLException e) {
			disconnectSQL();
			Main.LOGGER.error("SQL State: {}\n{}", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			disconnectSQL();
			e.printStackTrace();
		}
		disconnectSQL();
	}
}
