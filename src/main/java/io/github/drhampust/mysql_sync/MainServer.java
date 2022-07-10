package io.github.drhampust.mysql_sync;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import com.oroarmor.config.ConfigItemGroup;
import io.github.drhampust.mysql_sync.events.PlayerConnectionHandler;
import io.github.drhampust.mysql_sync.util.SQL;
import io.github.drhampust.mysql_sync.util.SQLColumn;
import io.github.drhampust.mysql_sync.util.sqlDataType;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.collection.DefaultedList;

import java.io.*;
import java.util.Base64;

import static io.github.drhampust.mysql_sync.Main.CONFIG;
import static io.github.drhampust.mysql_sync.Main.LOGGER;

public class MainServer implements DedicatedServerModInitializer {

	//TODO: Add Mixins for join and disconnect to be able to sync player when leaving and joining
	private static PlayerConnectionHandler playerConnectionHandler;

	@Override
	public void onInitializeServer() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// ###############
		// #Start of Test#
		// ###############

		// #############
		// #End of Test#
		//##############

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

		String table =    "uuid VARCHAR(100) NOT NULL, "
						+ "player_inventory MEDIUMTEXT NOT NULL, "
						+ "PRIMARY KEY  (`uuid`)";
		SQLColumn[] columns = new SQLColumn[2];
		columns[0] = new SQLColumn("uuid", sqlDataType.VARCHAR, 100, false);
		columns[1] = new SQLColumn("player_inventory", sqlDataType.MEDIUMTEXT, false);

		SQL.createTable("inventory", columns);


		playerConnectionHandler = new PlayerConnectionHandler();
		playerConnectionHandler.register();
	}
}
