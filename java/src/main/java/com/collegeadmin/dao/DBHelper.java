package com.collegeadmin.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper {
    private static final String DB_FOLDER = "java/data";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FOLDER + "/college.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initDatabase() {
        try (Connection c = getConnection(); Statement s = c.createStatement()) {
            s.execute("PRAGMA foreign_keys = ON;");

            s.execute("CREATE TABLE IF NOT EXISTS students (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, roll TEXT);");
            s.execute("CREATE TABLE IF NOT EXISTS exams (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL);");
            s.execute("CREATE TABLE IF NOT EXISTS rooms (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, capacity INTEGER NOT NULL);");
            s.execute("CREATE TABLE IF NOT EXISTS seating (id INTEGER PRIMARY KEY AUTOINCREMENT, exam_id INTEGER, room_id INTEGER, student_id INTEGER, seat_no INTEGER,\n                    FOREIGN KEY(exam_id) REFERENCES exams(id),\n                    FOREIGN KEY(room_id) REFERENCES rooms(id),\n                    FOREIGN KEY(student_id) REFERENCES students(id));");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
