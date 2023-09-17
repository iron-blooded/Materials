package org.hg.materials;

import com.google.gson.Gson;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class SerializeItem implements Serializable {
    String itemMeta;
    Material material;
    int amount;
    public SerializeItem(String serialize){
        SerializeItem item = new Gson().fromJson(serialize, SerializeItem.class);
        amount = item.amount;
        material = item.material;
        itemMeta = item.itemMeta;
    }
    public SerializeItem(ItemStack item){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = null;
        try {
            dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(item.getItemMeta());
            dataOutput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        itemMeta = new String(outputStream.toByteArray(), StandardCharsets.ISO_8859_1);
        material = item.getType();
        amount = item.getAmount();
    }
    public String serialize(){
        return new Gson().toJson(this);
    }
    public String hash(){
        String input = serialize();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                hexString.append(String.format("%02x", b));
            }
            String hashedString = hexString.toString();
            hashedString = hashedString.replaceAll("[^a-z0-9/._-]", "_");

            return hashedString;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public ItemStack getItem(){
        ItemStack itemStack = new ItemStack(material);
        itemStack.setAmount(amount);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(itemMeta.getBytes(StandardCharsets.ISO_8859_1));
        try {
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemMeta itemMeta = (ItemMeta) dataInput.readObject();
            itemStack.setItemMeta(itemMeta);
            dataInput.close();
        } catch (Exception e){}
        return itemStack;
    }

    public int compareTo(SerializeItem o) {
        int length1 = this.itemMeta.length();
        int length2 = o.itemMeta.length();
        return Integer.compare(length1, length2);
    }
}
