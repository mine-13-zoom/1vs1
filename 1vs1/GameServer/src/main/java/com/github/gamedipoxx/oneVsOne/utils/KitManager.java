package com.github.gamedipoxx.oneVsOne.utils;

import com.github.gamedipoxx.oneVsOne.OneVsOne;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class KitManager {

    public static void savePlayerKit(Player player, String kitName) {
        UUID playerUUID = player.getUniqueId();
        String inventoryToBase64 = toBase64(player.getInventory());

        try (Connection connection = MySQLManager.getConnection()) {
            String sql = "INSERT INTO player_kits (uuid, username, kit_name, kit_data) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE kit_data = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, playerUUID.toString());
                statement.setString(2, player.getName());
                statement.setString(3, kitName);
                statement.setString(4, inventoryToBase64);
                statement.setString(5, inventoryToBase64);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Inventory getPlayerKit(Player player, String kitName) {
        UUID playerUUID = player.getUniqueId();
        try (Connection connection = MySQLManager.getConnection()) {
            String sql = "SELECT kit_data FROM player_kits WHERE uuid = ? AND kit_name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, playerUUID.toString());
                statement.setString(2, kitName);
                try (ResultSet resultSet = statement.executeQuery()) {
                            if (resultSet.next()) {
                                return fromBase64(resultSet.getString("kit_data"));
                            }
                        }
                    }
                } catch (SQLException | IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }

            public static String toBase64(PlayerInventory inventory) {
                try {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

                    dataOutput.writeObject(inventory.getContents());

                    dataOutput.close();
                    return Base64Coder.encodeLines(outputStream.toByteArray());
                } catch (Exception e) {
                    throw new IllegalStateException("Could not convert inventory to base64.", e);
                }
            }

            public static Inventory fromBase64(String data) throws IOException, ClassNotFoundException {
                try {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
                    BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

                    Inventory inventory = Bukkit.createInventory(null, InventoryType.PLAYER);
                    inventory.setContents((ItemStack[]) dataInput.readObject());

                    dataInput.close();
                    return inventory;
                } catch (Exception e) {
                    throw new IOException("Could not decode inventory.", e);
                }
            }

            public static void createKitDatabase() {
                try (Connection connection = MySQLManager.getConnection()) {
                    String sqlDrop = "DROP TABLE IF EXISTS player_kits";
                    try (PreparedStatement statement = connection.prepareStatement(sqlDrop)) {
                        statement.executeUpdate();
                    }
                    String sql = "CREATE TABLE IF NOT EXISTS player_kits (uuid VARCHAR(36), username VARCHAR(16), kit_name VARCHAR(32), kit_data TEXT, PRIMARY KEY (uuid, kit_name))";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        