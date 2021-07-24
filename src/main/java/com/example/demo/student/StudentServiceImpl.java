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
    public Student addNewStudent(Student student) {
        Student addedStudent = studentRepository.save(student);

        return addedStudent;
    }

    /*
    @Override
    public Student getStudentById(Long id) throws StudentNotFoundException {
        Optional<Student> studentFinded = null;
        try {
            studentFinded = studentRepository.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(!studentFinded.isPresent()) {
            throw new StudentNotFoundException("The student with id: "+id+", were not found.");
        }

        return studentFinded.get();
    }*/
    
}
