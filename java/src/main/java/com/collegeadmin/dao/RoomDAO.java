package com.collegeadmin.dao;

import com.collegeadmin.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    public List<Room> findAll() {
        List<Room> out = new ArrayList<>();
        try (Connection c = DBHelper.getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT id,name,capacity FROM rooms ORDER BY id")) {
            while (rs.next()) {
                Room r = new Room();
                r.setId(rs.getInt(1));
                r.setName(rs.getString(2));
                r.setCapacity(rs.getInt(3));
                out.add(r);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return out;
    }

    public void save(Room r) {
        if (r.getId() > 0) {
            try (Connection c = DBHelper.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE rooms SET name=?, capacity=? WHERE id=?")) {
                ps.setString(1, r.getName());
                ps.setInt(2, r.getCapacity());
                ps.setInt(3, r.getId());
                ps.executeUpdate();
            } catch (SQLException e) { e.printStackTrace(); }
        } else {
            try (Connection c = DBHelper.getConnection(); PreparedStatement ps = c.prepareStatement("INSERT INTO rooms(name,capacity) VALUES(?,?)")) {
                ps.setString(1, r.getName());
                ps.setInt(2, r.getCapacity());
                ps.executeUpdate();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void delete(int id) {
        try (Connection c = DBHelper.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM rooms WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
