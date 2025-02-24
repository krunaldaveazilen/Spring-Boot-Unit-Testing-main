package net.fernandosalas.springboot.repository;

import net.fernandosalas.springboot.entity.Student;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class StudentRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;

    // JUnit test for save student operation | given-when-then
    @DisplayName("JUnit test for save student operation")
    @Test
    public void givenStudentObject_whenSavingStudent_thenReturnStudentObject() {
        //given - precondition or setup
        Student student = Student.builder()
                .firstName("Fernando")
                .lastName("Salas")
                .email("fernando@gmail.com")
                .build();
        // when - action or the behavior we are going to test
        Student savedStudent = studentRepository.save(student);

        // then - verify the output
        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getId()).isGreaterThan(0);
        assertThat(savedStudent.getFirstName()).isEqualTo("Fernando");
    }

    //JUnit test for getting al students operation
    @DisplayName("JUnit test for getting al students operation")
    @Test
    public void givenStudentList_whenFindAllStudents_thenReturnStudentList() {
        //given - precondition or setup
        Student student = Student.builder()
                .firstName("Fernando")
                .lastName("Salas")
                .email("fernando@gmail.com")
                .build();

        Student student2 = Student.builder()
                .firstName("Claudia")
                .lastName("Ramos")
                .email("claudia@gmail.com")
                .build();

        List<Student> studentList = Arrays.asList(student, student2);
        studentRepository.saveAll(studentList);

        // when - action or the behavior we are going to test
        List<Student> retrievedStudents = studentRepository.findAll();

        // then - verify the output
        assertThat(retrievedStudents.size()).isGreaterThan(0);
        assertThat(retrievedStudents).contains(student, student2);
        assertThat(retrievedStudents).isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(student, student2);
    }


    //JUnit test to get student by email operation
    @DisplayName("JUnit test to get student by email operation")
    @Test
    public void givenStudentEmail_whenFindStudentByEmail_thenReturnStudentObject() {
        //given - precondition or setup
        Student student = Student.builder()
                .id(1L)
                .firstName("Fernando")
                .lastName("Salas")
                .email("fernando@gmail.com")
                .build();

        // when - action or the behavior we are going to test
        studentRepository.save(student);
        Student savedStudent = studentRepository.findByEmail(student.getEmail()).get();

        // then - verify the output
        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getEmail()).isEqualTo(student.getEmail());
    }

    //JUnit test for deleting student
    @DisplayName("JUnit test for deleting student")
    @Test
    public void givenStudentObject_whenDeletingStudent_thenReturnStudentObject() {
        //given - precondition or setup
        Student student = Student.builder()
                .id(1L)
                .firstName("Fernando")
                .lastName("Salas")
                .email("fernando@gmail.com")
                .build();
        studentRepository.save(student);
        // when - action or the behavior we are going to test
        studentRepository.delete(student);
        Optional<Student> optionalStudent = studentRepository.findById(student.getId());

        // then - verify the output
        assertThat(optionalStudent)
                .as("Deleted student should not be present")
                .isEmpty();
    }

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
    @DisplayName("JUnit test for custom query using JPQL with index")
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
        Student student = Student.builder()
                .firstName("Fernando")
                .lastName("Salas")
                .email("fernando@gmail.com")
                .build();
        studentRepository.save(student);

        String name = "Fernando";
        String lastName = "Salas";
        Student studentDB = studentRepository.findByNativeIndexParams(name, lastName);

        assertThat(studentDB.getFirstName())
                .as("First name should match")
                .isEqualTo(name);

        assertThat(studentDB.getLastName())
                .as("Last name should match")
                .isEqualTo(lastName);
    }

    @DisplayName("JUnit test for custom native query using name param")
    @Test
    public void givenFirstNameAndLastName_whenUsingNativeNamedQuery_thenReturnStudentObject() {
        Student student = Student.builder()
                .firstName("Fernando")
                .lastName("Salas")
                .email("fernando@gmail.com")
                .build();
        studentRepository.save(student);

        String name = "Fernando";
        String lastName = "Salas";
        Student studentDB = studentRepository.findByNativeNamedParams(name, lastName);

        assertThat(studentDB.getFirstName())
                .as("First name should match")
                .isEqualTo(name);

        assertThat(studentDB.getLastName())
                .as("Last name should match")
                .isEqualTo(lastName);
    }



}
