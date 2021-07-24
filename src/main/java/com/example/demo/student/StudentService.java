package com.example.demo.student;

import java.util.List;

public interface StudentService {
    
    /**
     * Get a collection of all the students.
     * @return a collection of all the actual students.
     */
    public List<Student> getStudents();
}
