package io.github.drhampust.mysql_sync.events;

import io.github.drhampust.mysql_sync.Main;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;

import static io.github.drhampust.mysql_sync.util.base64Util.*;

public class PlayerConnectionHandler implements ServerPlayConnectionEvents.Init, ServerPlayConnectionEvents.Disconnect {
	public void register() {
		ServerPlayConnectionEvents.INIT.register(this);
		ServerPlayConnectionEvents.DISCONNECT.register(this);
	}


	@Override
	public void onPlayDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {
		ServerPlayerEntity player = handler.getPlayer();

		DefaultedList<ItemStack> invMain = player.getInventory().main;
		DefaultedList<ItemStack> decodedInvMain = DefaultedList.copyOf(ItemStack.EMPTY);
		decodedInvMain.clear();
		String encodedMain = toBase64(invMain);
		decodedInvMain = fromBase64(encodedMain);
		Main.LOGGER.info("inv Main:{}, to bas64:{}, decoded inv:{}", invMain, encodedMain, decodedInvMain);


		DefaultedList<ItemStack> invArmor = player.getInventory().armor;
		String encodedArmor = toBase64(invArmor);
		DefaultedList<ItemStack> decodedInvArmor = DefaultedList.copyOf(ItemStack.EMPTY);
		decodedInvArmor.clear();
		decodedInvArmor = fromBase64(encodedArmor);
		Main.LOGGER.info("inv Main:{}, to bas64:{}, decoded inv:{}", invArmor, encodedArmor, decodedInvArmor);

		String UUID = player.getUuidAsString();
		String encoded = toBase64(UUID);
		String decoded = uuidFromBase64(encoded);
		Main.LOGGER.info("Encoded UUID:{}, to bas64:{}, decoded UUID:{}", UUID, encoded, decoded);
	}

	@Override
	public void onPlayInit(ServerPlayNetworkHandler handler, MinecraftServer server) {
		ServerPlayerEntity player = handler.getPlayer();

		DefaultedList<ItemStack> invMain = player.getInventory().main;
		DefaultedList<ItemStack> decodedInvMain = DefaultedList.copyOf(ItemStack.EMPTY);
		decodedInvMain.clear();
		String encodedMain = toBase64(invMain);
		decodedInvMain = fromBase64(encodedMain);
		Main.LOGGER.info("inv Main:{}, to bas64:{}, decoded inv:{}", invMain, encodedMain, decodedInvMain);


		DefaultedList<ItemStack> invArmor = player.getInventory().armor;
		String encodedArmor = toBase64(invArmor);
		DefaultedList<ItemStack> decodedInvArmor = DefaultedList.copyOf(ItemStack.EMPTY);
		decodedInvArmor.clear();
		decodedInvArmor = fromBase64(encodedArmor);
		Main.LOGGER.info("inv Main:{}, to bas64:{}, decoded inv:{}", invArmor, encodedArmor, decodedInvArmor);

		String UUID = player.getUuidAsString();
		String encoded = toBase64(UUID);
		String decoded = uuidFromBase64(encoded);
		Main.LOGGER.info("Encoded UUID:{}, to bas64:{}, decoded UUID:{}", UUID, encoded, decoded);
	}
}
