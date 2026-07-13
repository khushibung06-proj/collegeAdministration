package com.collegeadmin.dao;

import com.collegeadmin.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public List<Student> findAll() {
        List<Student> out = new ArrayList<>();
        try (Connection c = DBHelper.getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT id,name,roll FROM students ORDER BY id")) {
            while (rs.next()) {
                Student st = new Student();
                st.setId(rs.getInt(1));
                st.setName(rs.getString(2));
                st.setRoll(rs.getString(3));
                out.add(st);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return out;
    }

    public void save(Student s) {
        if (s.getId() > 0) {
            try (Connection c = DBHelper.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE students SET name=?, roll=? WHERE id=?")) {
                ps.setString(1, s.getName());
                ps.setString(2, s.getRoll());
                ps.setInt(3, s.getId());
                ps.executeUpdate();
            } catch (SQLException e) { e.printStackTrace(); }
        } else {
            try (Connection c = DBHelper.getConnection(); PreparedStatement ps = c.prepareStatement("INSERT INTO students(name,roll) VALUES(?,?)")) {
                ps.setString(1, s.getName());
                ps.setString(2, s.getRoll());
                ps.executeUpdate();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void delete(int id) {
        try (Connection c = DBHelper.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM students WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
