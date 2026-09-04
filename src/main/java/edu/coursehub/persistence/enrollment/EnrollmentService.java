package edu.coursehub.persistence.enrollment;

import edu.coursehub.persistence.course.Course;
import edu.coursehub.persistence.course.CourseRepository;
import edu.coursehub.persistence.student.Student;
import edu.coursehub.persistence.student.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public Enrollment enroll(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found: " + studentId));

        if (!student.isActive()) {
            throw new IllegalStateException("Inactive students cannot enroll");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + courseId));

        if (enrollmentRepository.existsByStudent_IdAndCourse_Id(studentId, courseId)) {
            throw new DuplicateEnrollmentException(studentId, courseId);
        }

        return enrollmentRepository.save(Enrollment.enroll(student, course));
    }

    @Transactional
    public Enrollment complete(Long enrollmentId, BigDecimal grade) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found: " + enrollmentId));
        enrollment.complete(grade);
        return enrollment;
    }
}
