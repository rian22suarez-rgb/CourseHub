package edu.coursehub.persistence.enrollment;

import edu.coursehub.persistence.course.Course;
import edu.coursehub.persistence.course.CourseRepository;
import edu.coursehub.persistence.course.Department;
import edu.coursehub.persistence.course.DepartmentRepository;
import edu.coursehub.persistence.student.Student;
import edu.coursehub.persistence.student.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@SpringBootTest
class EnrollmentServiceIT {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18-alpine");

    @Autowired StudentRepository studentRepository;
    @Autowired DepartmentRepository departmentRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired EnrollmentService enrollmentService;

    @Test
    void shouldEnrollActiveStudent() {
        Department department = departmentRepository.saveAndFlush(new Department("Software Architecture"));
        Course course = courseRepository.saveAndFlush(new Course("ARCH-01", "Software Architecture", 4, department));
        Student student = studentRepository.saveAndFlush(new Student("Sofía", "sofia.arch@coursehub.edu", LocalDate.of(2002, 3, 3)));

        Enrollment enrollment = enrollmentService.enroll(student.getId(), course.getId());

        assertThat(enrollment.getId()).isNotNull();
        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.ACTIVE);
    }

    @Test
    void shouldRejectDuplicateEnrollmentAtBusinessLayer() {
        Department department = departmentRepository.saveAndFlush(new Department("Computer Science Duplicate"));
        Course course = courseRepository.saveAndFlush(new Course("CS-DUP", "Algorithms", 4, department));
        Student student = studentRepository.saveAndFlush(new Student("Mateo", "mateo.dup@coursehub.edu", LocalDate.of(2001, 4, 4)));
        enrollmentService.enroll(student.getId(), course.getId());

        assertThatThrownBy(() -> enrollmentService.enroll(student.getId(), course.getId()))
                .isInstanceOf(DuplicateEnrollmentException.class);
    }
}
