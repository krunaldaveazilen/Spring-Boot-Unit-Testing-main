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
        Student student = Student.builder()
                .firstName("Fernando")
                .lastName("Salas")
                .email("fernando@gmail.com")
                .build();

        given(studentService.saveStudent(ArgumentMatchers.any(Student.class)))
                .willAnswer((invocationOnMock) -> invocationOnMock.getArgument(0));

        ResultActions response = mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(student.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(student.getLastName())))
                .andExpect(jsonPath("$.email", is(student.getEmail())));
    }

    @Test
    public void whenGetAllStudents_thenReturnListOfStudents() throws Exception {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "Fernando", "Salas", "fernando@gmail.com"));
        students.add(new Student(2, "John", "Doe", "john.doe@gmail.com"));

        given(studentService.getAllStudents()).willReturn(students);

        ResultActions response = mockMvc.perform(get(API_PATH)
                .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(students.size())));
    }

    @Test
    public void whenGetStudentById_thenReturnStudent() throws Exception {
        long studentId = 1;
        Student student = new Student(studentId, "Fernando", "Salas", "fernando@gmail.com");

        given(studentService.getStudentById(studentId)).willReturn(Optional.of(student));

        ResultActions response = mockMvc.perform(get(API_PATH + "/" + studentId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(student.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(student.getLastName())))
                .andExpect(jsonPath("$.email", is(student.getEmail())));
    }

    @Test
    public void whenGetStudentById_notFound() throws Exception {
        long studentId = 1;

        given(studentService.getStudentById(studentId)).willReturn(Optional.empty());

        ResultActions response = mockMvc.perform(get(API_PATH + "/" + studentId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUpdateStudent_thenReturnUpdatedStudent() throws Exception {
        long studentId = 1;
        Student existingStudent = new Student(studentId, "Fernando", "Salas", "fernando@gmail.com");
        Student updatedStudent = new Student(studentId, "Fernando", "Updated", "fernando.updated@gmail.com");

        given(studentService.getStudentById(studentId)).willReturn(Optional.of(existingStudent));
        given(studentService.updateStudent(ArgumentMatchers.any(Student.class))).willReturn(updatedStudent);

        ResultActions response = mockMvc.perform(put(API_PATH + "/" + studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStudent)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updatedStudent.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedStudent.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedStudent.getEmail())));
    }

    @Test
    public void whenUpdateStudent_notFound() throws Exception {
        long studentId = 1;
        Student updatedStudent = new Student(studentId, "Fernando", "Updated", "fernando.updated@gmail.com");

        given(studentService.getStudentById(studentId)).willReturn(Optional.empty());

        ResultActions response = mockMvc.perform(put(API_PATH + "/" + studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStudent)));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteStudent_thenReturnSuccessMessage() throws Exception {
        long studentId = 1;

        doNothing().when(studentService).deleteStudent(studentId);

        ResultActions response = mockMvc.perform(delete(API_PATH + "/" + studentId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Student Deleted Successfully"));
    }
}
