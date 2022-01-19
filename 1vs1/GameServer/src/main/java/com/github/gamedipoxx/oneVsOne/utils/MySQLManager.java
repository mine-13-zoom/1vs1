package com.github.gamedipoxx.oneVsOne.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import com.github.gamedipoxx.oneVsOne.arena.Arena;
import com.mysql.cj.jdbc.MysqlDataSource;

public class MySQLManager {

	private static MysqlDataSource datasource;
	private static FileConfiguration config;
	private static InputStream setupFile;
	private static JavaPlugin plugin;

	public static Boolean init() {

		plugin.getLogger().info("§2Starting Database Setup");

		try {
			connect();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static void connect() throws SQLException {

		if(plugin.getConfig().getBoolean("debug") == true) {
			plugin.getLogger().info("MySqlCredentails [Host: " + config.getString("Database.Host") + " Port: " + config.getInt("Database.Port") + " Database: " + config.getString("Database.Database") + " User: " +  config.getString("Database.User") + " Password: " + config.getString("Database.Password") + "]");
		}
		datasource = new MysqlDataSource();

		datasource.setServerName(config.getString("Database.Host"));
		datasource.setPortNumber(config.getInt("Database.Port"));
		datasource.setDatabaseName(config.getString("Database.Database"));
		datasource.setUser(config.getString("Database.User"));
		datasource.setPassword(config.getString("Database.Password"));
		plugin.getLogger().info("§2Connecting to Database");
		try (Connection connection = datasource.getConnection()) {
			if (!connection.isValid(1000)) {
				disableplugin();
				throw new SQLException("Could not establish database connection.");	
			}
		}

		try {
			plugin.getLogger().info("§2Initialize Tables");
			setupDB();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			disableplugin();
		}

	}

	private static void setupDB() throws IOException, SQLException {
		String setup;
		try (InputStream in = setupFile) {
			setup = new String(in.readAllBytes());
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		String[] queries = setup.split(";");
		for (String query : queries) {
			if (query.isBlank())
				continue;
			try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.execute();
			}
		}
		plugin.getLogger().info("§2Database setup complete.");
	}

	public static MysqlDataSource getDatasource() {
		return datasource;
	}

	public static void updateArena(Arena arena) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("REPLACE Arenas(ArenaName, ArenaState, Kit, Players) VALUES (?, ?, ?, ?)")) {

					stmt.setString(1, arena.getArenaName());
					stmt.setString(2, arena.getGameState().toString());
					stmt.setString(3, arena.getArenaMap().getKitName());
					stmt.setInt(4, arena.getPlayerCount());
					stmt.execute();

				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});
	}

	public static void addArena(Arena arena) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO Arenas(ArenaName, ArenaState, Kit, Players) VALUES (?, ?, ?, ?)")) {

					stmt.setString(1, arena.getArenaName());
					stmt.setString(2, arena.getGameState().toString());
					stmt.setString(3, arena.getArenaMap().getKitName());
					stmt.setInt(4, arena.getPlayerCount());
					stmt.execute();

				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});
	}

	public static void deleteArena(String arenaUUID) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("DELETE FROM Arenas WHERE ArenaName = ?;")) {

					stmt.setString(1, arenaUUID);
					stmt.execute();

				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});
	}

	// Purge database but NOT Async
	public static void purgeDatabase() {
		try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("DELETE FROM Arenas")) {
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("DELETE FROM Teleport")) {
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<SimpleArenaDatabaseObject> readArenas() {

		try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT ArenaName, ArenaState, Players, Kit FROM Arenas;")) {
			ResultSet resultSet = stmt.executeQuery();

			ArrayList<SimpleArenaDatabaseObject> sado = new ArrayList<>();

			while (resultSet.next()) {
				String arenaName = resultSet.getString("ArenaName");
				GameState gameState = GameState.valueOf(resultSet.getString("ArenaState"));
				int players = resultSet.getInt("Players");
				String kit = resultSet.getString("Kit");
				sado.add(new SimpleArenaDatabaseObject(arenaName, players, gameState, kit));
			}
			return sado;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static @Nullable String readPlayerTeleport(Player player) {
		try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT ArenaName FROM Teleport WHERE PlayerName = ?;")) {
			stmt.setString(1, player.getName());
			ResultSet resultSet = stmt.executeQuery();
			
			String arenaname = null;
			if(resultSet.next()) {
				arenaname = resultSet.getString("ArenaName");
			}
			if(arenaname == null) {
				return null;
			}
			else {
				return arenaname;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static void deletePlayerFromTeleport(String player) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("DELETE FROM Teleport WHERE PlayerName = ?;")) {

					stmt.setString(1, player);
					stmt.execute();

				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});
	}
	
	public static void addPlayerToTeleport(String player, String arena) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO Teleport(PlayerName, ArenaName) VALUES (?, ?)")) {

					stmt.setString(1, player);
					stmt.setString(2, arena);
					stmt.execute();

				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});
	}

	public static void setConfig(FileConfiguration config) {
		MySQLManager.config = config;
	}

	public static void setSetupFile(InputStream setupFile) {
		MySQLManager.setupFile = setupFile;
	}

	public static void setPlugin(JavaPlugin plugin) {
		MySQLManager.plugin = plugin;
	}
	
	private static void disableplugin() {
		plugin.getLogger().warning("Disableing plugins because of a SQL-Exception!");
		Bukkit.broadcastMessage("§2Disable 1vs1 caused by an Exception!");
		Bukkit.getPluginManager().disablePlugin(plugin);
	}
}
