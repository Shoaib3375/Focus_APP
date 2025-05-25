package com.example.demo;

import java.sql.*;

public class DatabaseHelper {
	private static final String DB_URL = "jdbc:sqlite:pomodoro.db";

	public static Connection connect() throws SQLException {
		return DriverManager.getConnection(DB_URL);
	}

	public static void createTableIfNotExists() {
		String sql = "CREATE TABLE IF NOT EXISTS settings (id INTEGER PRIMARY KEY, focus_minutes INTEGER)";
		try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void saveFocusTime(int minutes) {
		String sql = "INSERT INTO settings (id, focus_minutes) VALUES (1, ?) " +
				"ON CONFLICT(id) DO UPDATE SET focus_minutes=excluded.focus_minutes";
		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, minutes);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int loadFocusTime() {
		String sql = "SELECT focus_minutes FROM settings WHERE id = 1";
		try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
			if (rs.next()) {
				return rs.getInt("focus_minutes");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 25; // Default to 25 minutes
	}
}
