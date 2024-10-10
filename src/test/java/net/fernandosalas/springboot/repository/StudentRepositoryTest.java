package net.fernandosalas.springboot.repository;

import net.fernandosalas.springboot.entity.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StudentRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;

    // Existing tests...

    //JUnit test for custom query using JPQL with index
    @DisplayName("JUnit test for custom query using JPQL with index")
    @Test
    public void givenFirstNameAndLastName_whenUsingIndexJPQL_thenReturnStudentObject() {
        //given - precondition or setup
        Student student = Student.builder()
                .firstName("Fernando")
                .lastName("Salas")
                .email("fernando@gmail.com")
                .build();
        studentRepository.save(student);

        // when - action or the behavior we are going to test
        String name = "Fernando";
        String lastName = "Salas";
        Student savedStudent = studentRepository.findByJPQLIndexParams(name, lastName);

        // then - verify the output
        assertThat(savedStudent.getFirstName()).isEqualTo(student.getFirstName());
        assertThat(savedStudent.getLastName()).isEqualTo(student.getLastName());
    }

    //JUnit test for custom query using JPQL with named param
    @DisplayName("JUnit test for custom query using JPQL with named param")
    @Test
    public void givenFirstNameAndLastName_whenUsingNamedJPQL_thenReturnStudentObject() {
        //given - precondition or setup
        Student student = Student.builder()
                .firstName("Fernando")
                .lastName("Salas")
                .email("fernando@gmail.com")
                .build();
        studentRepository.save(student);

        // when - action or the behavior we are going to test
        String name = "Fernando";
        String lastName = "Salas";
        Student savedStudent = studentRepository.findByJPQLNamedParams(name, lastName);

        // then - verify the output
        assertThat(savedStudent.getFirstName()).isEqualTo(student.getFirstName());
        assertThat(savedStudent.getLastName()).isEqualTo(student.getLastName());
    }

    //JUnit test for custom query using NATIVE SQL with index params
    @DisplayName("JUnit test for custom native query using index param")
    @Test
    public void givenFirstNameAndLastName_whenUsingNativeIndexQuery_thenReturnStudentObject() {
        //given - precondition or setup
        Student student = Student.builder()
                .firstName("Fernando")
                .lastName("Salas")
                .email("fernando@gmail.com")
                .build();
        studentRepository.save(student);

        // when - action or the behavior we are going to test
        String name = "Fernando";
        String lastName = "Salas";
        Student studentDB = studentRepository.findByNativeIndexParams(name, lastName);

        // then - verify the output
        assertThat(studentDB.getFirstName()).isEqualTo(name);
        assertThat(studentDB.getLastName()).isEqualTo(lastName);
    }

    //JUnit test for custom query using NATIVE SQL with named params
    @DisplayName("JUnit test for custom native query using name param")
    @Test
    public void givenFirstNameAndLastName_whenUsingNativeNamedQuery_thenReturnStudentObject() {
        //given - precondition or setup
        Student student = Student.builder()
                .firstName("Fernando")
                .lastName("Salas")
                .email("fernando@gmail.com")
                .build();
        studentRepository.save(student);

        // when - action or the behavior we are going to test
        String name = "Fernando";
        String lastName = "Salas";
        Student studentDB = studentRepository.findByNativeNamedParams(name, lastName);

        // then - verify the output
        assertThat(studentDB.getFirstName()).isEqualTo(name);
        assertThat(studentDB.getLastName()).isEqualTo(lastName);
    }
}
