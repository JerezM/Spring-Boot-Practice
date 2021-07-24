package com.example.demo.student;

import java.util.List;

public interface StudentService {
    
    /**
     * Get a collection of all the students.
     * @return a collection of all the actual students.
     */
    public List<Student> getStudents();

    /**
     * Create a new student using the parameterized one.
     * @param student Student that will be created.
     * @return The student created.
     */
    public Student createStudent(Student student);
}
