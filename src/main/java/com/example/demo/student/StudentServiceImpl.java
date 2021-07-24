package com.example.demo.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @Override
    public List<Student> getStudents() {
        List<Student> studentList = studentRepository.findAll();

        return studentList;
    }


    @Override
    public Student createStudent(Student student) {
        Student createdStudent = studentRepository.save(student);

        return createdStudent;
    }
    
}
