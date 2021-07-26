package com.example.demo.student;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

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
    public List<Student> getStudents() {
        List<Student> studentList = this.studentRepository.findAll();

        return studentList;
    }
    
    @Override
    public Student getStudentById(Long id){
        Student studentFinded = this.studentRepository.findById(id)
                                .orElseThrow(() -> new IllegalStateException("student with id: "+ id +" does not exist"));

        return studentFinded;
    }

    @Transactional
    @Override
    public void updateStudent(Long studentId, String name, String email) {
        Student student = this.studentRepository.findById(studentId)
                            .orElseThrow(() -> new IllegalStateException("student with id: "+ studentId +" does not exist"));

        if( name != null && name.length() > 0 && !Objects.equals(student.getName(), name) ) {
            student.setName(name);
        }

        if( email != null && email.length() > 0 && !Objects.equals(student.getEmail(), email) ){
            boolean emailExist = this.studentRepository.existsStudentByEmail(email);

            if(emailExist) {
                throw new IllegalStateException("email taken");
            }
            else {
                student.setEmail(email);
            }
        }

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
