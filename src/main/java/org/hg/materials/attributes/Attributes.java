package org.hg.materials.attributes;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Attributes {
    public Multimap<Attribute, AttributeModifier> attribute = ArrayListMultimap.create();
    public HashMap<Enchantment, Integer> enchantment = new HashMap<>();
    public Attributes(){
    }
    public Attributes(SerializerAttributes serializerAttributes){
        Attributes attributes = serializerAttributes.getAttributes();
        this.attribute = attributes.attribute;
        this.enchantment = attributes.enchantment;
    }
    public Attributes(String serialize){
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(serialize.getBytes(StandardCharsets.ISO_8859_1));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            SerializerAttributes serializerAttributes = (SerializerAttributes) dataInput.readObject();
            dataInput.close();
            Attributes attributes = serializerAttributes.getAttributes();
            this.attribute = attributes.attribute;
            this.enchantment = attributes.enchantment;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
//        Attributes attributes = new Yaml().loadAs(serialize, SerializerAttributes.class).getAttributes();
    }
    public String serialize(){
        SerializerAttributes serializerAttributes = new SerializerAttributes(this);
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(serializerAttributes);
            dataOutput.close();
            return new String(outputStream.toByteArray(), StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
