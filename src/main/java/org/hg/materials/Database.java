package org.hg.materials;

import org.bukkit.inventory.ItemStack;
import org.hg.materials.attributes.Attributes;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class Database {
    private static Materials plugin;
    public Connection connection;
    public Database(Materials plugin){
        this.plugin = plugin;
        setupDatabase();
    }
    public void setupDatabase() {
        try {
            File folder = new File(plugin.getDataFolder() + File.separator);
            if (!folder.exists()) {
                folder.mkdir();
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + File.separator + "database.db");
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS materials (item STRING, attributes STRING, PRIMARY KEY (item))");
            statement.execute();
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS combining (items STRING, attributes STRING, PRIMARY KEY (items))");
            statement.execute();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void addValue(ItemStack item, Attributes attributes) {
        String sql = "INSERT OR REPLACE INTO materials (item, attributes) VALUES (?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, new SerializeItem(item).serialize());
            stmt.setObject(2, attributes.serialize());
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {e.printStackTrace();}
    }
    public void deleteValue(ItemStack item) {
        String sql = "DELETE FROM materials WHERE item=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, new SerializeItem(item).serialize());
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {e.printStackTrace();}
    }
    public Attributes getValue(ItemStack item){
        String sql = "SELECT attributes FROM materials WHERE item=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, new SerializeItem(item).serialize());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                return new Attributes(rs.getString("attributes"));
            }
            rs.close();
            stmt.close();
        } catch (Exception e){e.printStackTrace();}
        return null;
    }
    public ArrayList<SerializeItem> getAllValues(){
        ArrayList<SerializeItem> items = new ArrayList<>();
        try {
            String sql = "SELECT item FROM materials";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                items.add(new SerializeItem(rs.getString("item")));
            }
            rs.close();
            stmt.close();
        } catch (Exception e){e.printStackTrace();}
        return items;
    }
}
