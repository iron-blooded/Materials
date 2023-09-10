package org.hg.materials;

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
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS materials (item OBJECT, attributes OBJECT, PRIMARY KEY (item))");
            statement.execute();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void addValue(Object item, Object attributes) {
        String sql = "INSERT OR REPLACE INTO materials (item, attributes) VALUES (?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setObject(1, item);
            stmt.setObject(2, attributes);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {}
    }
    public Object getValue(Object item){
        String sql = "SELECT attributes FROM materials WHERE item=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setObject(1, item);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                rs.close();
                stmt.close();
                return rs.getInt("attributes");
            }
            rs.close();
            stmt.close();
        } catch (Exception e){}
        return null;
    }
    public ArrayList<Object> getAllValues(){
        ArrayList<Object> items = new ArrayList<>();
        try {
            String sql = "SELECT item FROM materials";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                items.add(rs.getObject("item"));
            }
            rs.close();
            stmt.close();
        } catch (Exception e){}
        return items;
    }
}
