package com.example.demo.student;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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


    // ---- addNewStudent method test ----

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

    // ---- getStudents method test ----

    @Test
    void canGetStudents() {
        //when
        this.studentServiceUnderTest.getStudents();

        //then
        verify(this.studentRepository).findAll();
    }

    // ---- getStudentById method test ----

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
    void willThrowWhenIdIsNotFoundedUsingGetStudentById() {
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

    // ---- updateStudent method test ----

    @Test
    void canUpdateStudentName() {
        //given
        Long id = 1L;
        String studentName = "Juan";
        String studentEmail = "juan@gmail.com";
        Student student = new Student(studentName, studentEmail, LocalDate.now());

        Optional<Student> studentOptional = Optional.of(student);

        given(this.studentRepository.findById(id))
            .willReturn(studentOptional);

        String renovedStudentName = "Juan Gomez";

        //when
        this.studentServiceUnderTest.updateStudent(id, renovedStudentName, studentEmail);

        //then
        verify(this.studentRepository).findById(id);

        assertThat(renovedStudentName).isNotNull();
        assertThat(renovedStudentName).isNotEmpty();
        assertThat(renovedStudentName).isNotEqualTo(studentName);

        assertThat(renovedStudentName).isEqualTo(student.getName());
    }

    @Test
    void canUpdateStudentEmail() {
        //given
        Long id = 1L;
        String studentName = "Juan";
        String studentEmail = "juan@gmail.com";
        Student student = new Student(studentName, studentEmail, LocalDate.now());

        Optional<Student> studentOptional = Optional.of(student);

        given(this.studentRepository.findById(id))
            .willReturn(studentOptional);

        String renovedStudentEmail = "juan-gomez@gmail.com";

        given(this.studentRepository.existsStudentByEmail(renovedStudentEmail))
            .willReturn(false);

        //when
        this.studentServiceUnderTest.updateStudent(id, studentName, renovedStudentEmail);

        //then
        verify(this.studentRepository).findById(id);

        assertThat(renovedStudentEmail).isNotNull();
        assertThat(renovedStudentEmail).isNotEmpty();
        assertThat(renovedStudentEmail).isNotEqualTo(studentEmail);

        verify(this.studentRepository).existsStudentByEmail(renovedStudentEmail);

        assertThat(renovedStudentEmail).isEqualTo(student.getEmail());
    }

    @Test
    void canUpdateStudentNameAndEmail() {
        //given
        Long id = 1L;
        String studentName = "Juan";
        String studentEmail = "juan@gmail.com";
        Student student = new Student(studentName, studentEmail, LocalDate.now());

        Optional<Student> studentOptional = Optional.of(student);

        given(this.studentRepository.findById(id))
            .willReturn(studentOptional);

        String renovedStudentName = "Juan Gomez";
        String renovedStudentEmail = "juan-gomez@gmail.com";

        given(this.studentRepository.existsStudentByEmail(renovedStudentEmail))
            .willReturn(false);

        //when
        this.studentServiceUnderTest.updateStudent(id, renovedStudentName, renovedStudentEmail);

        //then
        verify(this.studentRepository).findById(id);

        assertThat(renovedStudentName).isNotNull();
        assertThat(renovedStudentName).isNotEmpty();
        assertThat(renovedStudentName).isNotEqualTo(studentName);

        assertThat(renovedStudentName).isEqualTo(student.getName());

        assertThat(renovedStudentEmail).isNotNull();
        assertThat(renovedStudentEmail).isNotEmpty();
        assertThat(renovedStudentEmail).isNotEqualTo(studentEmail);

        verify(this.studentRepository).existsStudentByEmail(renovedStudentEmail);

        assertThat(renovedStudentEmail).isEqualTo(student.getEmail());
    }

    @Test
    void willThrowWhenIdIsNotFoundedUsingUpdateStudent() {
        //given
        String name = "foo";
        String email = "foo@gmail.com";
        Long wrongId = 2L;
        Optional<Student> emptyStudentOptional = Optional.empty();

        given(this.studentRepository.findById(wrongId))
            .willReturn(emptyStudentOptional);

        //when
        //then
        assertThatThrownBy(() -> this.studentServiceUnderTest.updateStudent(wrongId, name, email))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("student with id: "+ wrongId +" does not exist");

        verify(this.studentRepository).findById(wrongId);
        verify(this.studentRepository, never()).existsStudentByEmail(any());
    }

    @Test
    void willThrowWhenEmailIsTakenUsingUpdateStudent() {
        //given
        Long id = 1L;
        String name = "Juan";
        String email = "juan@gmail.com";
        Student student = new Student(name, email, LocalDate.now());

        Optional<Student> studentOptional = Optional.of(student);

        given(this.studentRepository.findById(id))
            .willReturn(studentOptional);   

        String renovedEmail = "juan-new-email@gmail.com";

        given(this.studentRepository.existsStudentByEmail(renovedEmail))
            .willReturn(true);

        //when
        //then
        assertThatThrownBy(() -> this.studentServiceUnderTest.updateStudent(id, name, renovedEmail))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("email taken");

        assertThat(name).isEqualTo(student.getName());//in this case the name is equal to the current name 
                                                      //and the name of the student is not updated

        assertThat(renovedEmail).isNotNull();
        assertThat(renovedEmail).isNotEmpty();
        assertThat(renovedEmail).isNotEqualTo(email);

        verify(this.studentRepository).existsStudentByEmail(renovedEmail);
    }

    // ---- deleteStudentById method test ----

    @Test
    void canDeleteStudentById() {
        //given
        Long id = 1L;

        given(this.studentRepository.existsById(id))
            .willReturn(true);

        //when
        this.studentServiceUnderTest.deleteStudentById(id);

        //then
        verify(this.studentRepository).existsById(id);
        verify(this.studentRepository).deleteById(id);
    }

    @Test
    void willThrowWhenIdIsNotFoundedUsingDeleteStudentById() {
        //given
        Long id = 1L;

        given(this.studentRepository.existsById(id))
            .willReturn(false);

        //when
        //then
        assertThatThrownBy(() -> this.studentServiceUnderTest.deleteStudentById(id))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("student with id: "+ id +" does not exist");

        verify(this.studentRepository).existsById(id);

        verify(this.studentRepository, never()).deleteById(any());
    }
}
