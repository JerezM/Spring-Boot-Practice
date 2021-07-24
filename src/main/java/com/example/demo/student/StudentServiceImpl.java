package com.example.demo.student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Override
    public List<Student> getStudents() {
        List<Student> studentList = new ArrayList<Student>();

        Student student1 = new Student(1L, "Martin", "jerez149@gmail.com",
                                            LocalDate.of(1998, 4, 18), 23);
        studentList.add(student1);

        Student student2 = new Student(2L, "Franco", "jfranco@gmail.com",
                                            LocalDate.of(1995, 9, 13), 25);
        studentList.add(student2);

        return studentList;
    }
    
}
