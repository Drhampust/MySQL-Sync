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
		LOGGER.info("Player Joined loading inventory from db...");
		ServerPlayerEntity player = handler.getPlayer();
		ResultSet result = simpleSelectWhere("inventory", new String[]{},"uuid", toBase64(player.getUuidAsString()));
		try {
			if(result!=null){
				while (result.next()) {
					fromBase64(result.getString(2), player); // decode Inventory and replace player inventory
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
