package com.collegeadmin.dao;

import com.collegeadmin.model.Exam;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExamDAO {

    public List<Exam> findAll() {
        List<Exam> out = new ArrayList<>();
        try (Connection c = DBHelper.getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT id,name FROM exams ORDER BY id")) {
            while (rs.next()) {
                Exam e = new Exam();
                e.setId(rs.getInt(1));
                e.setName(rs.getString(2));
                out.add(e);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return out;
    }

    public void save(Exam e) {
        if (e.getId() > 0) {
            try (Connection c = DBHelper.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE exams SET name=? WHERE id=?")) {
                ps.setString(1, e.getName());
                ps.setInt(2, e.getId());
                ps.executeUpdate();
            } catch (SQLException ex) { ex.printStackTrace(); }
        } else {
            try (Connection c = DBHelper.getConnection(); PreparedStatement ps = c.prepareStatement("INSERT INTO exams(name) VALUES(?)")) {
                ps.setString(1, e.getName());
                ps.executeUpdate();
            } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    public void delete(int id) {
        try (Connection c = DBHelper.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM exams WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
