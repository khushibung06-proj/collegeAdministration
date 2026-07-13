package com.collegeadmin.config;

import com.collegeadmin.dao.ExamDAO;
import com.collegeadmin.dao.RoomDAO;
import com.collegeadmin.dao.StudentDAO;
import com.collegeadmin.model.Exam;
import com.collegeadmin.model.Room;
import com.collegeadmin.model.Student;

import java.util.List;

public class DataLoader {
    public static void loadDemoData() {
        StudentDAO sd = new StudentDAO();
        ExamDAO ed = new ExamDAO();
        RoomDAO rd = new RoomDAO();

        List<Student> students = sd.findAll();
        if (students.isEmpty()) {
            for (int i = 1; i <= 30; i++) {
                Student s = new Student();
                s.setName("Student " + i);
                s.setRoll(String.format("R%03d", i));
                sd.save(s);
            }
        }

        List<Exam> exams = ed.findAll();
        if (exams.isEmpty()) {
            Exam ex = new Exam();
            ex.setName("Midterm - Computer Science");
            ed.save(ex);
            Exam ex2 = new Exam();
            ex2.setName("Final - Mathematics");
            ed.save(ex2);
        }

        List<Room> rooms = rd.findAll();
        if (rooms.isEmpty()) {
            Room r1 = new Room(); r1.setName("Auditorium A"); r1.setCapacity(10); rd.save(r1);
            Room r2 = new Room(); r2.setName("Lab 101"); r2.setCapacity(10); rd.save(r2);
            Room r3 = new Room(); r3.setName("Lecture Hall 2"); r3.setCapacity(10); rd.save(r3);
        }
    }
}
