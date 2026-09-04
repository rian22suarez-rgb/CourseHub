package edu.coursehub.persistence.student;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@SpringBootTest
class StudentRepositoryIT {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18-alpine");

    @Autowired
    StudentRepository studentRepository;

    @Test
    void shouldPersistAndRetrieveStudent() {
        Student saved = studentRepository.saveAndFlush(
                new Student("Laura Gómez", "laura@coursehub.edu", LocalDate.of(2003, 5, 12))
        );

        Optional<Student> result = studentRepository.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.orElseThrow().getEmail()).isEqualTo("laura@coursehub.edu");
    }

    @Test
    void shouldFindStudentByEmailUsingDerivedQuery() {
        studentRepository.saveAndFlush(
                new Student("Carlos Pérez", "carlos@coursehub.edu", LocalDate.of(2002, 8, 15))
        );

        Optional<Student> result = studentRepository.findByEmail("carlos@coursehub.edu");

        assertThat(result).isPresent();
        assertThat(result.orElseThrow().getName()).isEqualTo("Carlos Pérez");
    }

    @Test
    void shouldRejectDuplicatedEmailAtDatabaseLevel() {
        studentRepository.saveAndFlush(
                new Student("Ana Uno", "duplicate@coursehub.edu", LocalDate.of(2000, 1, 1))
        );

        assertThatThrownBy(() -> studentRepository.saveAndFlush(
                new Student("Ana Dos", "duplicate@coursehub.edu", LocalDate.of(2001, 1, 1))
        )).isInstanceOf(DataIntegrityViolationException.class);
    }
}
