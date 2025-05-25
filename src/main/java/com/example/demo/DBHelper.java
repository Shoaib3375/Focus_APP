package com.example.demo;



import java.sql.*;

public class DBHelper {
	private static final String DB_URL = "jdbc:sqlite:demo.db";

	public static Connection connect() {
		try {
			return DriverManager.getConnection(DB_URL);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void initDatabase() {
		String createTable = """
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                completed BOOLEAN DEFAULT 0
            );
        """;
		try (Connection conn = connect()) {
			assert conn != null;
			try (Statement stmt = conn.createStatement()) {
				stmt.execute(createTable);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
