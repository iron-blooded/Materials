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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Combination {
    Materials plugin;
    public ArrayList<SerializeItem> items = new ArrayList<>();
    Attributes attributes = new Attributes();
    public Combination(Materials plugin){
        this.plugin = plugin;
    }
    public Combination(Materials plugin, ArrayList<SerializeItem> items, Attributes attributes){
        this.plugin = plugin;
        this.attributes = attributes;
        this.items = items;
        sortItems();
    }
    public Combination(Materials plugin, String serialize_items, String serialize_attributes){
        this.plugin = plugin;
        this.attributes = new Attributes(serialize_attributes);
        sortItems();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serialize_items.getBytes(StandardCharsets.ISO_8859_1));
        try {
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            this.items = (ArrayList<SerializeItem>) dataInput.readObject();
            dataInput.close();
        } catch (Exception e){}
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
    public void sortItems(){
        Collections.sort(items, SerializeItem::compareTo);
    }
    public String serializeAttributes(){
        return this.attributes.serialize();
    }

}
