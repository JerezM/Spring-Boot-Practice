package com.example.demo.student;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    private StudentService studentServiceUnderTest;

    @BeforeEach
    void setUp() {
        this.studentServiceUnderTest = new StudentServiceImpl(this.studentRepository);
    }


    @Test
    void canAddNewStudent() {
        //given
        Student expectStudent = new Student("Juan", "juanperez73@gmail.com", LocalDate.of(1973, 3, 2));

        //when
        this.studentServiceUnderTest.addNewStudent(expectStudent);

        //then
        //it will capture the argument with the type Student used in a method 
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);

        //verify that the mock(studentRepository) use the method save when the StudentService use the method addNewStudent
        verify(this.studentRepository).save(studentArgumentCaptor.capture());//The argumentCaptor capture the student used in the save method
        Student capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(expectStudent);
        
    }

    @Test
    void willThrowWhenEmailIsTakenUsingAddNewStudent() {
        //given
        String email = "juanperez73@gmail.com";
        Student student = new Student("Juan", email, LocalDate.of(1973, 3, 2));

        Student studentStored = new Student("Juan Manuel", email, LocalDate.of(1998, 5, 22));
        Optional<Student> returnedOptional = Optional.of(studentStored);

        given(this.studentRepository.findStudentByEmail(email))
            .willReturn(returnedOptional);//this makes that when the method findStudentByEmail is used by the mock
                                         //this one will always return the returnedOptinal object

        //when
        //then
        //this check that, when i use the method addNewStudent(in this case), it will throws an IllegalStateException
        assertThatThrownBy(() -> this.studentServiceUnderTest.addNewStudent(student))
                            .isInstanceOf(IllegalStateException.class)
                            .hasMessageContaining("email taken");

        verify(this.studentRepository, never()).save(any());//this verify that the mock not used the method save in this case
          
    }

    @Test
    void canGetStudents() {
        //when
        this.studentServiceUnderTest.getStudents();

        //then
        verify(this.studentRepository).findAll();
    }

    @Test
    void canGetStudentById() {
        //given
        Long id = 1L;
        Student student = new Student(id, "Juan", "juanperez73@gmail.com", LocalDate.of(1973, 3, 2));
        Optional<Student> returnedStudentOptional = Optional.of(student);

        given(this.studentRepository.findById(id)).willReturn(returnedStudentOptional);

        //when
        this.studentServiceUnderTest.getStudentById(id);

        //then
        ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        verify(this.studentRepository).findById(idArgumentCaptor.capture());
        Long idCaptured = idArgumentCaptor.getValue();

        assertThat(idCaptured).isEqualTo(id);
    }

    @Test
    void willThrowWhenIdIsNotFoundedUsingGetStudentById(){
        //given
        Long wrongId = 2L;
        Optional<Student> emptyStudentOptional = Optional.empty();

        given(this.studentRepository.findById(wrongId))
            .willReturn(emptyStudentOptional);

        //when
        //then
        assertThatThrownBy(() -> this.studentServiceUnderTest.getStudentById(wrongId))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("student with id: "+ wrongId +" does not exist");

        verify(this.studentRepository).findById(wrongId);
    }

    @Test
    @Disabled
    void updateStudent() {

    }

    @Test
    @Disabled
    void deleteStudentById() {

    }
}
