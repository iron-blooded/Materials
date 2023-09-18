package org.hg.materials;

import org.bukkit.inventory.ItemStack;
import org.hg.materials.attributes.Attributes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DatabaseComb {
    Materials plugin;
    Connection connection;
    public DatabaseComb(Materials plugin){
        this.plugin = plugin;
        this.connection = plugin.database.connection;
    }
    public void setValue(Combination combination) {
        String sql = "INSERT OR REPLACE INTO combining (items, attributes) VALUES (?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, combination.serializeItems());
            stmt.setString(2, combination.serializeAttributes());
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {e.printStackTrace();}
    }
    public void deleteValue(Combination combination) {
        String sql = "DELETE FROM combining WHERE items=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, combination.serializeItems());
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {e.printStackTrace();}
    }
    public void deleteValue(ItemStack item1, ItemStack item2) {
        Combination combination = new Combination(plugin);
        combination.items.add(new SerializeItem(item1));
        combination.items.add(new SerializeItem(item2));
        String sql = "DELETE FROM combining WHERE items=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, combination.serializeItems());
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {e.printStackTrace();}
    }
    public List<Combination> getValues(SerializeItem item){
        List<Combination> list = new ArrayList<>();
        for (Combination combination : getAllValues()){
            for (SerializeItem serializeItem : combination.items){
                if (serializeItem.getItem().equals(item.getItem())) {
                    list.add(combination);
                }
            }
        }
        return list;
    }
    public Attributes getAttribute(Combination combination){
        try {
            String sql = "SELECT attributes FROM combining WHERE items=?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, combination.serializeItems());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                return new Attributes(rs.getString("attributes"));
            }
            rs.close();
            stmt.close();
        } catch (Exception e){e.printStackTrace();}
        return new Attributes();
    }
    public ArrayList<Combination> getAllValues(){
        ArrayList<Combination> items = new ArrayList<>();
        try {
            String sql = "SELECT * FROM combining";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                items.add(new Combination(plugin, rs.getString("items"), rs.getString("attributes")));
            }
            rs.close();
            stmt.close();
        } catch (Exception e){e.printStackTrace();}
        return items;
    }
    public void replaceValues(Combination combination1, Combination combination2, boolean ignore_attributes){
        combination1.sortItems();
        combination2.sortItems();
        if (ignore_attributes) {
            for (Combination c : getAllValues()) {
                if (c.items.equals(combination1.items)) {
                    combination2.attributes = c.attributes;
                }
            }
        }
        deleteValue(combination1);
        setValue(combination2);
    }
}
