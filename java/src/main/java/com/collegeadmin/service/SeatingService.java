package com.collegeadmin.service;

import com.collegeadmin.dao.RoomDAO;
import com.collegeadmin.dao.StudentDAO;
import com.collegeadmin.model.Room;
import com.collegeadmin.model.Student;

import java.util.*;
import java.util.stream.Collectors;

public class SeatingService {
    private final StudentDAO studentDAO = new StudentDAO();
    private final RoomDAO roomDAO = new RoomDAO();

    /**
     * Very simple seating: fetch all students and all rooms, then distribute round-robin respecting capacity.
     */
    public Map<Room, List<Student>> generatePlan(int examId) {
        List<Student> students = studentDAO.findAll();
        List<Room> rooms = roomDAO.findAll().stream().filter(r -> r.getCapacity() > 0).collect(Collectors.toList());
        Map<Room, List<Student>> plan = new LinkedHashMap<>();
        if (rooms.isEmpty()) return plan;
        for (Room r : rooms) plan.put(r, new ArrayList<>());

        int roomIndex = 0;
        for (Student s : students) {
            // find next room with space
            int attempts = 0;
            while (attempts < rooms.size()) {
                Room r = rooms.get(roomIndex);
                List<Student> assigned = plan.get(r);
                if (assigned.size() < r.getCapacity()) {
                    assigned.add(s);
                    break;
                }
                roomIndex = (roomIndex + 1) % rooms.size();
                attempts++;
            }
            roomIndex = (roomIndex + 1) % rooms.size();
        }
        return plan;
    }
}
