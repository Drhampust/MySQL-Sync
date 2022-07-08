package io.github.drhampust.mysql_sync.util;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class base64Util {
    /**
     * A method to serialize an inventory to Base64 string.
     *
     * <p />
     *
     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub.
     *
     * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
     *
     * @param inventory to serialize
     * @return Base64 string of the provided inventory
     * @throws IllegalStateException
     */
    public static String toBase64(DefaultedList<ItemStack> inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream dataOutput = new ObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.size());

            // Save every element in the list
            for (int i = 0; i < inventory.size(); i++) {
                dataOutput.writeObject(inventory.get(i));
            }

            // Serialize that array
            dataOutput.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static DefaultedList<ItemStack> fromBase64(String data){
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
            ObjectInputStream dataInput = new ObjectInputStream(inputStream);
            DefaultedList<ItemStack> inventory = new PlayerInventory(null).armor;

            // Read the serialized inventory
            for (int i = 0; i < dataInput.readInt(); i++) {
                inventory.add(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException | IOException ignored) {
        }
        return null;
    }

    /**
     * Works as intended converts a String in to a base64 encoded string
     * @param UUID Player UUID to encode using base64
     * @return base64 encoded UUID
     */
    public static String toBase64(String UUID) {
        return Base64.getEncoder().encodeToString(UUID.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decodes a base64 encoded string
     * @param data the encoded UUID
     * @return decoded UUID
     */
    public static String uuidFromBase64(String data) {
        return new String(Base64.getDecoder().decode(data));
    }
}
