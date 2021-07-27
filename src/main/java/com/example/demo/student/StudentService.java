package com.example.demo.student;

import java.util.List;

public interface StudentService {

    /**
     * Create a new student using the parameterized one.
     * @param student must not had an id.
     * @return The created student.
     */
    public Student addNewStudent(Student student);

    /**
     * Get a collection of all the students.
     * @return a collection of all the actual students.
     */
    public List<Student> getStudents();

    /**
     * Get the student by his id.
     * @param id must not be null.
     * @return the student with the given id.
     */
    public Student getStudentById(Long id);

    /**
     * Update the name and the email of the user with the given id.
     * @param studentId must not be null.
     * @param name must not be null or empty string if you want to update this attribute.
     * @param email must not be null or empty string if you want to update this attribute.
     */
    public void updateStudent(Long studentId, String name, String email);

    /**
     * Delete the student by his id.
     * @param studentId must not be null.
     */
    public void deleteStudentById(Long studentId);
}
