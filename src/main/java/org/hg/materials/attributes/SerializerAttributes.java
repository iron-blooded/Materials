package org.hg.materials.attributes;

import com.google.common.collect.Multimap;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SerializerAttributes implements Serializable {
    public HashMap<Map<Attribute, EquipmentSlot>, Double> attribute;
    public HashMap<String, Integer> enchantment;
    public int limit;
    public SerializerAttributes (Attributes attributes) {
        this.limit = attributes.limit;
        HashMap<String, Integer> enchants = new HashMap<>();
        for (Enchantment enchantment1: attributes.enchantment.keySet()){
            enchants.put(enchantment1.getName(), attributes.enchantment.get(enchantment1));
        }
        this.enchantment = enchants;
        this.attribute = new HashMap<>();
        for (Attribute attrib :attributes.attribute.keySet()){
            for (AttributeModifier attributeModifier : attributes.attribute.get(attrib)){
                HashMap<Attribute, EquipmentSlot> map = new HashMap<>();
                map.put(attrib, attributeModifier.getSlot());
                attribute.put(map, attributeModifier.getAmount());
            }
        }
    }
    public Attributes getAttributes(){
        Attributes attributes = new Attributes();
        attributes.limit = this.limit;
        HashMap<Enchantment, Integer> enchantmentIntegerHashMap = new HashMap<>();
        for (String ench: this.enchantment.keySet()){
            try {
                enchantmentIntegerHashMap.put(Enchantment.getByName(ench), this.enchantment.get(ench));
            } catch (Exception e){e.printStackTrace();}
        }
        attributes.enchantment = enchantmentIntegerHashMap;
        for (Map<Attribute, EquipmentSlot> attributeEquipmentSlotHashMap: this.attribute.keySet()){
            for (Attribute attrib: attributeEquipmentSlotHashMap.keySet()){
                attributes.attribute.put(attrib, new AttributeModifier(UUID.fromString("22fa46b8-1326-4a32-a971-5dc39bd5dfa4"), attrib.name(), this.attribute.get(attributeEquipmentSlotHashMap), AttributeModifier.Operation.ADD_NUMBER, attributeEquipmentSlotHashMap.get(attrib)));
            }
        }
        return attributes;
    }
}
