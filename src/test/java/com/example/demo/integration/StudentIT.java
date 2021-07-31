package com.example.demo.integration;

import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
@AutoConfigureMockMvc
public class StudentIT {

	@Autowired
  	private MockMvc mockMvc;

  	@Autowired
  	private ObjectMapper objectMapper;

  	@Autowired
  	private StudentRepository studentRepository;

  	private final Faker faker = new Faker();

	@Test
	void canGetStudents() throws Exception {
		//given
		String pathRequestMapping = "/api/v1/students";
		
		int registeredStudents = 5;
		for (int i = 0; i < registeredStudents; i++) {

			String name = String.format(
				"%s %s",
				faker.name().firstName(), 
				faker.name().lastName()
			);

			String email = String.format("%s@gmail.com", StringUtils.trimAllWhitespace(name.trim().toLowerCase()));

			Student student = new Student(name, email, LocalDate.now());

			this.mockMvc.perform(
				post(pathRequestMapping)
					.contentType(MediaType.APPLICATION_JSON)
					.content(this.objectMapper.writeValueAsString(student))
			)
			.andExpect(status().isOk());

		}
		//when
		ResultActions resultActions = this.mockMvc.perform(get(pathRequestMapping));

		//then
		resultActions.andExpect(status().isOk());

		MvcResult getStudenResult = resultActions.andReturn();

		String contentAsString = getStudenResult.getResponse().getContentAsString();

		List<Student> obtainedStudents =
			this.objectMapper.readValue(
            	contentAsString,
            	new TypeReference<>() {}
        	);

		List<Student> expectedStudents = this.studentRepository.findAll();

		System.out.println("expectedStudents: "+ expectedStudents);
		System.out.println("\n\n obtainedStudents: "+ obtainedStudents);

		assertThat(expectedStudents)
			.hasSize(expectedStudents.size())
			.usingFieldByFieldElementComparator()
			.containsAll(obtainedStudents);
	}

	@Test
	void canGetStudentById() throws Exception {
		// given
        String name = String.format(
            "%s %s",
            faker.name().firstName(),
            faker.name().lastName()
        );

        String email = String.format("%s@gmail.com", StringUtils.trimAllWhitespace(name.trim().toLowerCase()));

        Student student = new Student(name, email, LocalDate.now());

		String pathRequestMapping = "/api/v1/students";

		MvcResult postStudentResult =
        	this.mockMvc.perform(
				post(pathRequestMapping)
					.contentType(MediaType.APPLICATION_JSON)
					.content(this.objectMapper.writeValueAsString(student))
			)
			.andExpect(status().isOk())
			.andReturn();

        String contentAsString = postStudentResult.getResponse().getContentAsString();            						            						

        Student obtainedStudent = 
			this.objectMapper.readValue(
            	contentAsString,
            	new TypeReference<>() {}
        	);

        long id = obtainedStudent.getId();

        // when
        ResultActions resultActions = this.mockMvc.perform(get(pathRequestMapping + "/" + id));

        // then
        resultActions.andExpect(status().isOk());
		boolean existsStudent = this.studentRepository.existsById(id);
        assertThat(existsStudent).isTrue();
	}

	@Test
	void canRegisterNewStudent() throws Exception {
		// given
		String name = String.format(
			"%s %s",
			faker.name().firstName(),
			faker.name().lastName()
		);

		String email = String.format("%s@gmail.com", StringUtils.trimAllWhitespace(name.trim().toLowerCase()));

		Student student = new Student(name, email, LocalDate.now());
		
		String pathRequestMapping = "/api/v1/students";

		// when
		ResultActions resultActions = 
			this.mockMvc.perform(
				post(pathRequestMapping)
					.contentType(MediaType.APPLICATION_JSON)
					.content(this.objectMapper.writeValueAsString(student))
			);
			
		// then
		resultActions.andExpect(status().isOk());
		List<Student> students = this.studentRepository.findAll();
		assertThat(students).usingElementComparatorIgnoringFields("id").contains(student);
	}

	@Test
    void canDeleteStudent() throws Exception {
        // given
        String name = String.format(
            "%s %s",
            faker.name().firstName(),
            faker.name().lastName()
        );

        String email = String.format("%s@gmail.com", StringUtils.trimAllWhitespace(name.trim().toLowerCase()));

        Student student = new Student(name, email, LocalDate.now());
			
		String pathRequestMapping = "/api/v1/students";

        MvcResult postStudentResult =
        	this.mockMvc.perform(
				post(pathRequestMapping)
					.contentType(MediaType.APPLICATION_JSON)
					.content(this.objectMapper.writeValueAsString(student))
			)
			.andExpect(status().isOk())
			.andReturn();

        String contentAsString = postStudentResult.getResponse().getContentAsString();            						            						

        Student obtainedStudent = 
			this.objectMapper.readValue(
            	contentAsString,
            	new TypeReference<>() {}
        	);

        long id = obtainedStudent.getId();                                             

        // when
        ResultActions resultActions = this.mockMvc.perform(delete(pathRequestMapping + "/" + id));

        // then
        resultActions.andExpect(status().isOk());
        boolean exists = this.studentRepository.existsById(id);
        assertThat(exists).isFalse();
    }
}
