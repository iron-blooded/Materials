package org.hg.materials.attributes;

import com.google.gson.Gson;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;

public class Attributes {
    public HashMap<Attribute, Float> attribute;
    public HashMap<Enchantment, Integer> enchantment;
    public Attributes(){
        attribute = new HashMap<>();
        enchantment = new HashMap<>();
    }
    public Attributes(String serialize){
        Attributes attributes = new Gson().fromJson(serialize, Attributes.class);
        this.attribute = attributes.attribute;
        this.enchantment = attributes.enchantment;
    }
    public String serialize(){
        return new Gson().toJson(this);
    }
}
