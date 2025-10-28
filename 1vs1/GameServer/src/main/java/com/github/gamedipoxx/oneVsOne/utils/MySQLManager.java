package com.github.gamedipoxx.oneVsOne.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import com.github.gamedipoxx.oneVsOne.OneVsOne;
import com.github.gamedipoxx.oneVsOne.arena.Arena;
import com.github.gamedipoxx.oneVsOne.utils.stats.GlobalStatsGUI;
import com.github.gamedipoxx.oneVsOne.utils.stats.StatsObject;
import com.mysql.cj.jdbc.MysqlDataSource;

public class MySQLManager {

	private static MysqlDataSource datasource;
	private static FileConfiguration config;
	private static InputStream setupFile;
	private static JavaPlugin plugin;
	private static String version = "1_2_1";

	public static Boolean init() {

		plugin.getLogger().info("§2Starting Database Setup");
		try {
			connect();
		} catch (SQLException e) {
			plugin.getLogger().severe("Database connection failed: " + e.getMessage());
			plugin.getLogger().severe("Please check your database configuration in config.yml");
			plugin.getLogger().severe("Make sure MySQL server is running and credentials are correct");
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			plugin.getLogger().severe("Database initialization failed: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean isInitialized() {
		return datasource != null;
	}

	private static void connect() throws SQLException {

		if (plugin.getConfig().getBoolean("debug") == true) {
			plugin.getLogger().info(
					"MySqlCredentails [Host: " + config.getString("Database.Host") + " Port: " + config.getInt("Database.Port") + " Database: " + config.getString("Database.Database") + " User: " + config.getString("Database.User") + " Password: " + config.getString("Database.Password") + "]");
		}
		
		// Explicitly load MySQL driver to avoid class loading issues
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			plugin.getLogger().info("MySQL driver loaded successfully");
		} catch (ClassNotFoundException e) {
			plugin.getLogger().severe("Failed to load MySQL driver: " + e.getMessage());
			throw new SQLException("MySQL driver not found", e);
		}
		
		// Validate configuration
		String host = config.getString("Database.Host");
		Integer port = config.getInt("Database.Port");
		String database = config.getString("Database.Database");
		String user = config.getString("Database.User");
		String password = config.getString("Database.Password");
		
		if (host == null || database == null || user == null || password == null) {
			throw new SQLException("Database configuration is incomplete. Please check your config.yml file.");
		}
		
		try {
			datasource = new MysqlDataSource();
		} catch (Exception e) {
			plugin.getLogger().severe("Failed to create MySQL datasource: " + e.getMessage());
			throw new SQLException("Failed to initialize MySQL connection", e);
		}

		datasource.setServerName(host);
		datasource.setPortNumber(port);
		datasource.setDatabaseName(database);
		datasource.setUser(user);
		datasource.setPassword(password);
		plugin.getLogger().info("§2Connecting to Database");
		try (Connection connection = datasource.getConnection()) {
			if (!connection.isValid(1000)) {
				throw new SQLException("Could not establish database connection.");
			}
		}

		try {
			plugin.getLogger().info("§2Initialize Tables");
			setupDB();
		} catch (IOException e) {
			throw new SQLException("Failed to read database setup file: " + e.getMessage(), e);
		} catch (SQLException e) {
			throw new SQLException("Failed to initialize database tables: " + e.getMessage(), e);
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
		setup = setup.replace("${mysql.version}", version);
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

	public static Connection getConnection() throws SQLException {
        return datasource.getConnection();
    }

	public static void updateArena(Arena arena) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("REPLACE "+ version +"_Arenas(ArenaName, ArenaState, Kit, Players, Server) VALUES (?, ?, ?, ?, ?)")) {

					stmt.setString(1, arena.getArenaName());
					stmt.setString(2, arena.getGameState().toString());
					stmt.setString(3, arena.getArenaMap().getKitName());
					stmt.setInt(4, arena.getPlayerCount());
					stmt.setString(5, OneVsOne.getServername());
					stmt.execute();

				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});
	}

public static void addArena(Arena arena) {
		// Don't add arena to database if plugin is disabled
		if (!OneVsOne.getPlugin().isEnabled()) {
			return;
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(OneVsOne.getPlugin(), new Runnable() {

			@Override
			public void run() {
				// Double-check plugin is still enabled before accessing database
				if (!OneVsOne.getPlugin().isEnabled()) {
					return;
				}
				
				try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("REPLACE "+ version +"_Arenas(ArenaName, ArenaState, Kit, Players, Server) VALUES (?, ?, ?, ?, ?)")) {

					stmt.setString(1, arena.getArenaName());
					stmt.setString(2, arena.getGameState().toString());
					stmt.setString(3, arena.getArenaMap().getKitName());
					stmt.setInt(4, arena.getPlayerCount());
					stmt.setString(5, OneVsOne.getServername());
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
				try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("DELETE FROM "+ version +"_Arenas WHERE ArenaName = ?;")) {

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
		try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("DELETE FROM "+ version +"_Arenas WHERE Server = ?;")) {
			stmt.setString(1, OneVsOne.getServername());
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("DELETE FROM "+ version +"_Teleport WHERE Server = ?;")) {
			stmt.setString(1, OneVsOne.getServername());
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<SimpleArenaDatabaseObject> readArenas() {

		try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT ArenaName, ArenaState, Players, Kit, Server FROM "+ version +"_Arenas;")) {
			ResultSet resultSet = stmt.executeQuery();

			ArrayList<SimpleArenaDatabaseObject> sado = new ArrayList<>();

			while (resultSet.next()) {
				String arenaName = resultSet.getString("ArenaName");
				GameState gameState = GameState.valueOf(resultSet.getString("ArenaState"));
				int players = resultSet.getInt("Players");
				String kit = resultSet.getString("Kit");
				String server = resultSet.getString("Server");
				sado.add(new SimpleArenaDatabaseObject(arenaName, players, gameState, kit, server));
			}
			return sado;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static @Nullable String readPlayerTeleport(Player player) {
		try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT ArenaName FROM "+ version +"_Teleport WHERE PlayerName = ?;")) {
			stmt.setString(1, player.getName());
			ResultSet resultSet = stmt.executeQuery();

			String arenaname = null;
			if (resultSet.next()) {
				arenaname = resultSet.getString("ArenaName");
			}
			if (arenaname == null) {
				return null;
			} else {
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
				try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("DELETE FROM "+ version +"_Teleport WHERE PlayerName = ?;")) {

					stmt.setString(1, player);
					stmt.execute();

				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});
	}

	public static void addPlayerToTeleport(String player, String arena, String server) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO "+ version +"_Teleport(PlayerName, ArenaName, Server) VALUES (?, ?, ?)")) {

					stmt.setString(1, player);
					stmt.setString(2, arena);
					stmt.setString(3, server);
					stmt.execute();

				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});
	}

	public static void increateGamesPlayedByArena(Arena arena) {
		ArrayList<Player> players = new ArrayList<>(arena.getPlayers());
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				for (Player player : players) {
					UUID uuid = player.getUniqueId();
					try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO Stats (PlayerUUID, PlayerName, GamesPlayed, GamesWon) VALUES (?, ?, 1, 0) ON DUPLICATE KEY UPDATE GamesPlayed = GamesPlayed + 1, PlayerName = ?;")) {
						stmt.setString(1, uuid.toString());
						stmt.setString(2, player.getName());
						stmt.setString(3, player.getName());
						stmt.execute();

} catch (SQLException e) {
					plugin.getLogger().severe("Failed to add arena to database: " + e.getMessage());
					e.printStackTrace();
				} catch (Exception e) {
					plugin.getLogger().severe("Unexpected error adding arena to database: " + e.getMessage());
					e.printStackTrace();
				}
				}
			}
		});
	}

	public static void increateWinsBy1(Player player) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				UUID uuid = player.getUniqueId();
				try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO Stats (PlayerUUID, PlayerName, GamesPlayed, GamesWon) VALUES (?, ?, 1, 1) ON DUPLICATE KEY UPDATE GamesWon = GamesWon + 1, GamesPlayed = GamesPlayed, PlayerUUID = ?, PlayerName = ?;")) {
					stmt.setString(1, uuid.toString());
					stmt.setString(2, player.getName());
					stmt.setString(3, uuid.toString());
					stmt.setString(4, player.getName());
					stmt.execute();

				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});
	}

	public static void updateStats() {

		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT PlayerUUID, PlayerName, GamesPlayed, GamesWon FROM Stats;")) {
					ResultSet resultSet = stmt.executeQuery();

					HashMap<UUID, StatsObject> stats = new HashMap<>();
					HashMap<String, StatsObject> statsByName = new HashMap<>();

					while (resultSet.next()) {
						UUID uuid = UUID.fromString(resultSet.getString("PlayerUUID"));
						int gamesPlayed = resultSet.getInt("GamesPlayed");
						int gamesWon = resultSet.getInt("GamesWon");
						String playername = resultSet.getString("PlayerName");
						StatsObject statsobject = new StatsObject(uuid, gamesPlayed, gamesWon, playername);
						stats.put(uuid, statsobject);
						statsByName.put(playername, statsobject);
					}
					StatsObject.setStatsCach(stats);
					StatsObject.setStatsCachByName(statsByName);

				} catch (SQLException e) {
					e.printStackTrace();
					return;
				}

			}
		});

	}

	public static void updateTopPlayers() {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT PlayerUUID, PlayerName, GamesPlayed, GamesWon FROM Stats ORDER BY GamesWon DESC LIMIT 53;")) {
					ResultSet resultSet = stmt.executeQuery();
					
					ArrayList<StatsObject> top = new ArrayList<>();

					while (resultSet.next()) {
						UUID uuid = UUID.fromString(resultSet.getString("PlayerUUID"));
						String playername = resultSet.getString("PlayerName");
						int gamesPlayed = resultSet.getInt("GamesPlayed");
						int gamesWon = resultSet.getInt("GamesWon");
						top.add(new StatsObject(uuid, gamesPlayed, gamesWon, playername));
					}
					GlobalStatsGUI.setTopPlayers(top);
					GlobalStatsGUI.updateGui();

				} catch (SQLException e) {
					e.printStackTrace();
					return;
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
