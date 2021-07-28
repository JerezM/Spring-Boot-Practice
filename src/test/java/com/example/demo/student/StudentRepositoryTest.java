package com.example.demo.student;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class StudentRepositoryTest {
    
    @Autowired
    private StudentRepository repositoryUnderTest;

    @AfterEach
    void tearDown() {
        this.repositoryUnderTest.deleteAll();
    }

    @Test
    public void itShouldFindStudentByEmail() {
        //given
        String email = "juanperez73@gmail.com";
        Student student = new Student("Juan", email, LocalDate.of(1973, 3, 2));
        this.repositoryUnderTest.save(student);

        //when
        Optional<Student> optinalStudent = this.repositoryUnderTest.findStudentByEmail(email);
        boolean wasFinded = optinalStudent.isPresent();

        //then
        assertThat(wasFinded).isTrue();
    }

    @Test
    public void itShouldNotFindStudentByEmail() {
        //given
        String email = "juanperez73@gmail.com";
        Student student = new Student("Juan", email, LocalDate.of(1973, 3, 2));
        this.repositoryUnderTest.save(student);

        //when
        String wrongEmail = "lucasp@gmail.com";
        Optional<Student> optinalStudent = this.repositoryUnderTest.findStudentByEmail(wrongEmail);
        boolean wasNotFinded = optinalStudent.isPresent();

        //then
        assertThat(wasNotFinded).isFalse();
    }

    @Test
    public void itShouldCheckIfStudentExistByEmail() {
        //given
        String email = "juanperez73@gmail.com";
        Student student = new Student("Juan", email, LocalDate.of(1973, 3, 2));
        this.repositoryUnderTest.save(student);

        //when
        boolean exists = this.repositoryUnderTest.existsStudentByEmail(email);

        //then
        assertThat(exists).isTrue();
    }

    @Test
    public void itShouldCheckIfStudentDoesNotExistByEmail() {
        //given
        String email = "juanperez73@gmail.com";
        Student student = new Student("Juan", email, LocalDate.of(1973, 3, 2));
        this.repositoryUnderTest.save(student);

        //when
        String wrongEmail = "mateo63@gmail.com";
        boolean notExists = this.repositoryUnderTest.existsStudentByEmail(wrongEmail);

        //then
        assertThat(notExists).isFalse();
        
    }
}
