package com.example.demo.student;

import java.util.List;
import java.util.Optional;

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
        Optional<Student> optionalStudent = studentRepository
            .findStudentByEmail(student.getEmail());

        if(optionalStudent.isPresent()) {
            throw new IllegalStateException("email taken");
        }    

        Student addedStudent = studentRepository.save(student);

        return addedStudent;
    }

    
    @Override
    public Student getStudentById(Long id){
        Optional<Student> studentFinded = null;
        try {
            studentFinded = studentRepository.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(!studentFinded.isPresent()) {
            throw new IllegalStateException("student not founded");
        }

        return studentFinded.get();
    }
    
}
