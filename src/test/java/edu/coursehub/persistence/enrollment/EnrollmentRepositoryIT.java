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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class EnrollmentRepositoryIT {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18-alpine");

    @Autowired StudentRepository studentRepository;
    @Autowired DepartmentRepository departmentRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired EnrollmentRepository enrollmentRepository;

    @Test
    void shouldCalculateAverageGradeUsingJpql() {
        Department department = departmentRepository.saveAndFlush(new Department("Engineering AVG"));
        Course course = courseRepository.saveAndFlush(new Course("JAVA-AVG", "Modern Java", 4, department));
        Student one = studentRepository.saveAndFlush(new Student("Student One", "one.avg@coursehub.edu", LocalDate.of(2000, 1, 1)));
        Student two = studentRepository.saveAndFlush(new Student("Student Two", "two.avg@coursehub.edu", LocalDate.of(2001, 1, 1)));

        Enrollment enrollmentOne = enrollmentRepository.saveAndFlush(Enrollment.enroll(one, course));
        enrollmentOne.complete(new BigDecimal("4.00"));
        enrollmentRepository.saveAndFlush(enrollmentOne);

        Enrollment enrollmentTwo = enrollmentRepository.saveAndFlush(Enrollment.enroll(two, course));
        enrollmentTwo.complete(new BigDecimal("5.00"));
        enrollmentRepository.saveAndFlush(enrollmentTwo);

        Double average = enrollmentRepository.calculateAverageGrade(course.getId());

        assertThat(average).isEqualTo(4.5d);
    }

    @Test
    void shouldFetchStudentsForCourseUsingEntityGraph() {
        Department department = departmentRepository.saveAndFlush(new Department("Science Fetch"));
        Course course = courseRepository.saveAndFlush(new Course("DB-FETCH", "Databases", 3, department));
        Student student = studentRepository.saveAndFlush(new Student("Fetch Student", "fetch@coursehub.edu", LocalDate.of(2002, 2, 2)));
        enrollmentRepository.saveAndFlush(Enrollment.enroll(student, course));

        List<Enrollment> result = enrollmentRepository.findByCourse_Id(course.getId());

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getStudent().getName()).isEqualTo("Fetch Student");
        assertThat(result.getFirst().getCourse().getDepartment().getName()).isEqualTo("Science Fetch");
    }
}
