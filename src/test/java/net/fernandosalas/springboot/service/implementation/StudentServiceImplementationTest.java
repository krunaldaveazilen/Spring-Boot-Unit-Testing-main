package net.fernandosalas.springboot.service.implementation;

import net.fernandosalas.springboot.entity.Student;
import net.fernandosalas.springboot.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceImplementationTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImplementation studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        student = new Student(1L, "John", "Doe", "john.doe@example.com");
    }

    @Test
    void saveStudent() {
        when(studentRepository.save(student)).thenReturn(student);
        Student savedStudent = studentService.saveStudent(student);
        assertEquals(student, savedStudent);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void getAllStudents() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student));
        List<Student> students = studentService.getAllStudents();
        assertEquals(1, students.size());
        assertEquals(student, students.get(0));
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getStudentById() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        Optional<Student> foundStudent = studentService.getStudentById(1L);
        assertTrue(foundStudent.isPresent());
        assertEquals(student, foundStudent.get());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void updateStudent() {
        when(studentRepository.save(student)).thenReturn(student);
        Student updatedStudent = studentService.updateStudent(student);
        assertEquals(student, updatedStudent);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void deleteStudent() {
        doNothing().when(studentRepository).deleteById(1L);
        studentService.deleteStudent(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }
}
