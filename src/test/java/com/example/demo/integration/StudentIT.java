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

		MvcResult getStudentResult =
        	this.mockMvc.perform(
				post(pathRequestMapping)
					.contentType(MediaType.APPLICATION_JSON)
					.content(this.objectMapper.writeValueAsString(student))
			)
			.andExpect(status().isOk())
			.andReturn();

        String contentAsString = getStudentResult.getResponse().getContentAsString();            						            						

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

        MvcResult getStudentResult =
        	this.mockMvc.perform(
				post(pathRequestMapping)
					.contentType(MediaType.APPLICATION_JSON)
					.content(this.objectMapper.writeValueAsString(student))
			)
			.andExpect(status().isOk())
			.andReturn();

        String contentAsString = getStudentResult.getResponse().getContentAsString();            						            						

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
