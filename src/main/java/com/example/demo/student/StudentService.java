package com.example.demo.student;

import java.util.List;

public interface StudentService {
    
    /**
     * Get a collection of all the students.
     * @return a collection of all the actual students.
     */
    public List<Student> getStudents();

    /**
     * Get the student by his id.
     * @param id must not be null.
     * @return the student with the given id.
     *
    public Student getStudentById(Long id) throws StudentNotFoundException;
    */

    /**
     * Create a new student using the parameterized one.
     * @param student must not had an id.
     * @return The created student.
     */
    public Student addNewStudent(Student student);
}
