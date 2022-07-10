package io.github.drhampust.mysql_sync.util;

import io.github.drhampust.mysql_sync.Main;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.network.ServerPlayerEntity;

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
    public static String toBase64(ServerPlayerEntity player) throws IllegalStateException {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream dataOutput = new ObjectOutputStream(outputStream);

            int length = player.getInventory().size();
            dataOutput.writeInt(length);

            for (int i = 0; i < length; i++) {
                NbtCompound originalNbt = player.getInventory().getStack(i).writeNbt(new NbtCompound()); //itemStack is placeholder for item
                NbtIo.write(originalNbt, dataOutput);
            }
            dataOutput.close(); // Serialize that array
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void fromBase64(String data, ServerPlayerEntity player){

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
            ObjectInputStream dataInput = new ObjectInputStream(inputStream);
            int length =  dataInput.readInt();
            player.getInventory().clear();
            for (int i = 0; i < length; i++) {
                NbtCompound decodedNbt = NbtIo.read(dataInput);
                player.getInventory().insertStack(i, ItemStack.fromNbt(decodedNbt));
            }
            // Read the serialized inventory
            dataInput.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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


