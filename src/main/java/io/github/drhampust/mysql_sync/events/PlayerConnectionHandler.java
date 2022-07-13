package io.github.drhampust.mysql_sync.events;

import io.github.drhampust.mysql_sync.util.sql.SQLHelper;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static io.github.drhampust.mysql_sync.Main.LOGGER;
import static io.github.drhampust.mysql_sync.Main.LOGGER_PREFIX;
import static io.github.drhampust.mysql_sync.util.sql.SQLHelper.*;
import static io.github.drhampust.mysql_sync.util.Base64Util.*;

public class PlayerConnectionHandler implements ServerPlayConnectionEvents.Init, ServerPlayConnectionEvents.Disconnect {
	public void register() {
		ServerPlayConnectionEvents.INIT.register(this);
		ServerPlayConnectionEvents.DISCONNECT.register(this);
	}


	@Override
	public void onPlayDisconnect(@NotNull ServerPlayNetworkHandler handler, MinecraftServer server) {
		LOGGER.info("{} Player disconnected saving inventory to db...", LOGGER_PREFIX);
		ServerPlayerEntity player = handler.getPlayer();

		// Create an Array of columns to save
		String[] columns = {"uuid", "player_inventory", "player_selected_slot", "player_health", "player_hunger_exhaustion", "player_hunger_saturation", "player_hunger_level", "player_level", "player_experience"}; // Oracle

		// Create an Array of values to populate columns
		Object[] values = {player.getUuidAsString(), invToBase64(player), player.getInventory().selectedSlot, player.getHealth(), player.getHungerManager().getExhaustion(), player.getHungerManager().getSaturationLevel(), player.getHungerManager().getFoodLevel(), player.experienceLevel, (int) (player.experienceProgress * player.getNextLevelExperience())};

		// Call function to create an insert statement, if uuid value already exist update row with new values.
		SQLHelper.insertNoDuplicates("inventory", columns, values, columns[0]);
	}

	@Override
	public void onPlayInit(@NotNull ServerPlayNetworkHandler handler, MinecraftServer server) {
		LOGGER.info("{} Player Joined loading information from db...", LOGGER_PREFIX);
		// Initialize variables in method scope, so they can be used within method.
		ServerPlayerEntity player = handler.getPlayer();
		PlayerInventory inv = player.getInventory();
		HungerManager hunger = player.getHungerManager();

		// Set default value for player with no entries in database:
		inv.clear(); 							// Empty inventory
		player.getInventory().selectedSlot = 0; // 1st Slot selected
		player.setHealth(20f); 					// Full health
		hunger.setExhaustion(0f); 				// No exhaustion
		hunger.setSaturationLevel(20f);			// full saturation
		hunger.setFoodLevel(20); 				// Full food bar
		player.setExperienceLevel(0); 			// No levels
		player.setExperiencePoints(0); 			// No xp points

		// Get data from database
		List<Map<String, Object>> result = simpleSelectWhere("inventory", new String[]{},"uuid", player.getUuidAsString());
		if (result.size() != 0) {// If we received at least 1 row of information from Query (only need 1, so we skip the rest)
			List<ItemStack> inventory = invFromBase64(String.valueOf(result.get(0).get("player_inventory"))); // decode Inventory and replace player inventory
			for (int i = 0; i < inventory.size(); i++) {
				inv.insertStack(i, inventory.get(i));
			}
			player.getInventory().selectedSlot = (int) result.get(0).get("player_selected_slot");

			// Get player data and set it to the joining player
			player.setHealth((float) result.get(0).get("player_health"));

			hunger.setExhaustion((float) result.get(0).get("player_hunger_exhaustion"));
			hunger.setSaturationLevel((float) result.get(0).get("player_hunger_saturation"));
			hunger.setFoodLevel((int) result.get(0).get("player_hunger_level"));

			player.setExperienceLevel((int) result.get(0).get("player_level"));
			player.setExperiencePoints((int) result.get(0).get("player_experience"));
		}
	}
}
