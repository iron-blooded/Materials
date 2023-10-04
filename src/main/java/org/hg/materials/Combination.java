package org.hg.materials;

import com.google.gson.Gson;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.hg.materials.attributes.Attributes;
import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Combination {
    Materials plugin;
    public List<SerializeItem> items = new ArrayList<>();
    public Attributes attributes = new Attributes();
    public Combination(Materials plugin){
        this.plugin = plugin;
        items = new ArrayList<>();
        attributes = new Attributes();
    }
    public Combination(Materials plugin, List<SerializeItem> items, Attributes attributes){
        this.plugin = plugin;
        this.attributes = attributes;
        this.items = items;
        sortItems();
    }
    public Combination(Materials plugin, String serialize_items, String serialize_attributes){
        this.plugin = plugin;
        this.attributes = new Attributes(serialize_attributes);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serialize_items.getBytes(StandardCharsets.ISO_8859_1));
        try {
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            this.items = (List<SerializeItem>) dataInput.readObject();
            this.sortItems();
            dataInput.close();
        } catch (Exception e){e.printStackTrace();}
    }
    public String serializeItems(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = null;
        sortItems();
        try {
            dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(items);
            dataOutput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(outputStream.toByteArray(), StandardCharsets.ISO_8859_1);
    }
    public String hashItems(){
        String input = serializeItems();
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
    public void sortItems(){
        List<SerializeItem> list = new ArrayList<>();
        for (SerializeItem item: items){
            list.add(new SerializeItem(item.getItem()));
        }
        this.items = list;
        Collections.sort(items, SerializeItem::compareTo);
    }
    public String serializeAttributes(){
        return this.attributes.serialize();
    }

}
