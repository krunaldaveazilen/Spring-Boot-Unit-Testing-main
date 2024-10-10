package net.fernandosalas.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fernandosalas.springboot.entity.Student;
import net.fernandosalas.springboot.service.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.BDDMockito.*;

@WebMvcTest
public class StudentControllerTest {

    private static final String API_PATH = "/api/students";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentService studentService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenStudentObject_whenCreateStudent_thenReturnSavedStudent() throws Exception {
        //given - precondition or setup
        Student student = Student.builder()
                .firstName("Fernando")
                .lastName("Salas")
                .email("fernando@gmail.com")
                .build();

        // Mocking the behavior of studentService.saveStudent() method
        given(studentService.saveStudent(ArgumentMatchers.any(Student.class)))
                .willAnswer((invocationOnMock) -> invocationOnMock.getArgument(0));

        // when - action or the behavior we are going to test
        // Simulating an HTTP POST request to "/api/students"
        ResultActions response = mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));

        // then - verify the output
        // Asserting the response status code is 201 Created
        // Verifying response content using JSONPath
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(student.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(student.getLastName())))
                .andExpect(jsonPath("$.email", is(student.getEmail())));
    }

    @Test
    public void whenGetAllStudents_thenReturnStudentList() throws Exception {
        // given - precondition or setup
        List<Student> students = new ArrayList<>();
        students.add(Student.builder().firstName("John").lastName("Doe").email("john.doe@example.com").build());
        students.add(Student.builder().firstName("Jane").lastName("Doe").email("jane.doe@example.com").build());

        given(studentService.getAllStudents()).willReturn(students);

        // when - action or the behavior we are going to test
        ResultActions response = mockMvc.perform(get(API_PATH)
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(students.size())));
    }

    @Test
    public void givenStudentId_whenGetStudentById_thenReturnStudentObject() throws Exception {
        // given - precondition or setup
        long studentId = 1L;
        Student student = Student.builder().firstName("John").lastName("Doe").email("john.doe@example.com").build();

        given(studentService.getStudentById(studentId)).willReturn(Optional.of(student));

        // when - action or the behavior we are going to test
        ResultActions response = mockMvc.perform(get(API_PATH + "/{id}", studentId)
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(student.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(student.getLastName())))
                .andExpect(jsonPath("$.email", is(student.getEmail())));
    }

    @Test
    public void givenInvalidStudentId_whenGetStudentById_thenReturnEmpty() throws Exception {
        // given - precondition or setup
        long studentId = 1L;

        given(studentService.getStudentById(studentId)).willReturn(Optional.empty());

        // when - action or the behavior we are going to test
        ResultActions response = mockMvc.perform(get(API_PATH + "/{id}", studentId)
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenUpdatedStudent_whenUpdateStudent_thenReturnUpdatedStudentObject() throws Exception {
        // given - precondition or setup
        long studentId = 1L;
        Student savedStudent = Student.builder().firstName("John").lastName("Doe").email("john.doe@example.com").build();
        Student updatedStudent = Student.builder().firstName("Johnny").lastName("Doe").email("johnny.doe@example.com").build();

        given(studentService.getStudentById(studentId)).willReturn(Optional.of(savedStudent));
        given(studentService.updateStudent(ArgumentMatchers.any(Student.class))).willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or the behavior we are going to test
        ResultActions response = mockMvc.perform(put(API_PATH + "/{id}", studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStudent)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updatedStudent.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedStudent.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedStudent.getEmail())));
    }

    @Test
    public void givenInvalidStudentId_whenUpdateStudent_thenReturn404() throws Exception {
        // given - precondition or setup
        long studentId = 1L;
        Student updatedStudent = Student.builder().firstName("Johnny").lastName("Doe").email("johnny.doe@example.com").build();

        given(studentService.getStudentById(studentId)).willReturn(Optional.empty());

        // when - action or the behavior we are going to test
        ResultActions response = mockMvc.perform(put(API_PATH + "/{id}", studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStudent)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenStudentId_whenDeleteStudent_thenReturn200() throws Exception {
        // given - precondition or setup
        long studentId = 1L;
        willDoNothing().given(studentService).deleteStudent(studentId);

        // when - action or the behavior we are going to test
        ResultActions response = mockMvc.perform(delete(API_PATH + "/{id}", studentId)
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Student Deleted Successfully"));
    }
}
