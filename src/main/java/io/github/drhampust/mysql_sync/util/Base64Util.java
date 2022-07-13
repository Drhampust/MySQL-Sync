package io.github.drhampust.mysql_sync.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Base64Util {
    /**
     * A method to encode a ByteArrayOutputStream to base64 encoded String
     * @param data to encode
     * @return Base64 string of the provided ByteArrayOutputStream
     */
    public static String byteArrayStreamToBase64(@NotNull ByteArrayOutputStream data) {
        return Base64.getEncoder().encodeToString(data.toByteArray());
    }

    /**
     * A method to serialize an inventory to Base64 string.
     *
     * @param player which inventory should be converted to a base64 String
     * @return Base64 string of the provided inventory
     */
    public static String invToBase64(@NotNull ServerPlayerEntity player) throws IllegalStateException {

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
            return byteArrayStreamToBase64(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<ItemStack> invFromBase64(String data){
        List<ItemStack> inventory = new ArrayList<>();
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
            ObjectInputStream dataInput = new ObjectInputStream(inputStream);

            int length =  dataInput.readInt();
            for (int i = 0; i < length; i++) {
                NbtCompound decodedNbt = NbtIo.read(dataInput);
                inventory.add(i, ItemStack.fromNbt(decodedNbt));
            }

//            playerInfo = new PlayerInformation(inventory, health, saturation, exhaustion, food, xp);

            // Read the serialized inventory
            dataInput.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return inventory;
    }

    @SuppressWarnings("unused")
    public static String objectToBase64(Object object) throws IllegalStateException {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream dataOutput = new ObjectOutputStream(outputStream);

            dataOutput.writeObject(object);

            dataOutput.close(); // Serialize that array
            return byteArrayStreamToBase64(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @SuppressWarnings("unused")
    public static Object objectFromBase64(String data){
        Object output;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
            ObjectInputStream dataInput = new ObjectInputStream(inputStream);
            output =  dataInput.readObject();
            dataInput.close();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return output;
    }
}


