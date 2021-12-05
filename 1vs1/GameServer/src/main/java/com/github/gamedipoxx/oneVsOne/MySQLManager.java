package com.github.gamedipoxx.oneVsOne;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import com.github.gamedipoxx.oneVsOne.arena.Arena;
import com.mysql.cj.jdbc.MysqlDataSource;

public class MySQLManager {

	private static MysqlDataSource datasource;

	public static Boolean init() {
		try {
			connect();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static void connect() throws SQLException {

		datasource = new MysqlDataSource();
		FileConfiguration config = OneVsOne.getPlugin().getConfig();

		datasource.setServerName(config.getString("Database.Host"));
		datasource.setPortNumber(config.getInt("Database.Port"));
		datasource.setDatabaseName(config.getString("Database.Database"));
		datasource.setUser(config.getString("Database.User"));
		datasource.setPassword(config.getString("Database.Password"));

		try (Connection connection = datasource.getConnection()) {
			if (!connection.isValid(1000)) {
				throw new SQLException("Could not establish database connection.");
			}
		}

		try {
			setupDB();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void setupDB() throws IOException, SQLException {
		String setup;
		try (InputStream in = OneVsOne.getPlugin().getResource("dbsetup.sql")) {
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
		OneVsOne.getPlugin().getLogger().info("§2Database setup complete.");
	}

	public static MysqlDataSource getDatasource() {
		return datasource;
	}

	public static void updateArena(Arena arena) {
		Bukkit.getScheduler().runTaskAsynchronously(OneVsOne.getPlugin(), new Runnable() {

			@Override
			public void run() {
				try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("REPLACE Arenas(ArenaName, ArenaState, Kit, Players) VALUES (?, ?, ?, ?)")) {

					stmt.setString(1, arena.getArenaName());
					stmt.setString(2, arena.getGameState().toString());
					stmt.setString(3, arena.getKit().toString());
					stmt.setInt(4, arena.getPlayerCount());
					stmt.execute();

				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});
	}

	public static void addArena(Arena arena) {
		Bukkit.getScheduler().runTaskAsynchronously(OneVsOne.getPlugin(), new Runnable() {

			@Override
			public void run() {
				try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO Arenas(ArenaName, ArenaState, Kit, Players) VALUES (?, ?, ?, ?)")) {

					stmt.setString(1, arena.getArenaName());
					stmt.setString(2, arena.getGameState().toString());
					stmt.setString(3, arena.getKit().toString());
					stmt.setInt(4, arena.getPlayerCount());
					stmt.execute();

				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});
	}

	public static void deleteArena(Arena arena) {
		Bukkit.getScheduler().runTaskAsynchronously(OneVsOne.getPlugin(), new Runnable() {

			@Override
			public void run() {
				try (Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("DELETE FROM Arenas WHERE ArenaName = ?;")) {

					stmt.setString(1, arena.getArenaName());
					stmt.execute();

				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		});
	}
	
	public static void purgeDatabase() {
		try(Connection conn = datasource.getConnection(); PreparedStatement stmt = conn.prepareStatement("DELETE FROM Arenas")){
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
