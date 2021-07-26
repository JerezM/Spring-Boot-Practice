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
        List<Student> studentList = this.studentRepository.findAll();

        return studentList;
    }


    @Override
    public Student addNewStudent(Student student) {
        Optional<Student> optionalStudent = this.studentRepository
            .findStudentByEmail(student.getEmail());

        if(optionalStudent.isPresent()) {
            throw new IllegalStateException("email taken");
        }    

        Student addedStudent = this.studentRepository.save(student);

        return addedStudent;
    }

    
    @Override
    public Student getStudentById(Long id){
        Optional<Student> optionalStudent = null;
        try {
            optionalStudent = this.studentRepository.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(!optionalStudent.isPresent()) {
            throw new IllegalStateException("student with id: "+ id +" does not exist");
        }

        Student studentFinded = optionalStudent.get();

        return studentFinded;
    }


    @Override
    public void deleteStudentById(Long studentId) {
        boolean studentExist = this.studentRepository.existsById(studentId);

        if(!studentExist) {
            throw new IllegalStateException("student with id: "+ studentId +" does not exist");
        }

        studentRepository.deleteById(studentId);
    }
    
}
