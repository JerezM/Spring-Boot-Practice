package com.example.demo.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/students")
public class StudentController {
    
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    

    @GetMapping
    public List<Student> getStudents() {
        List<Student> studentList = studentService.getStudents();

        return studentList;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student){
        Student createdStudent = studentService.createStudent(student);

        return createdStudent;
    }
}
