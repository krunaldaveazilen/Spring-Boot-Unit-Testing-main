package net.fernandosalas.springboot.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    @Test
    public void testStudentBuilder() {
        Student student = Student.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        assertNotNull(student);
        assertEquals("John", student.getFirstName());
        assertEquals("Doe", student.getLastName());
        assertEquals("john.doe@example.com", student.getEmail());
    }

    @Test
    public void testStudentGettersAndSetters() {
        Student student = new Student();
        student.setFirstName("Jane");
        student.setLastName("Doe");
        student.setEmail("jane.doe@example.com");

        assertEquals("Jane", student.getFirstName());
        assertEquals("Doe", student.getLastName());
        assertEquals("jane.doe@example.com", student.getEmail());
    }

    @Test
    public void testStudentConstructor() {
        Student student = new Student(1L, "Alice", "Smith", "alice.smith@example.com");

        assertEquals(1L, student.getId());
        assertEquals("Alice", student.getFirstName());
        assertEquals("Smith", student.getLastName());
        assertEquals("alice.smith@example.com", student.getEmail());
    }
}
