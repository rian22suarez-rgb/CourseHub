package edu.coursehub.persistence.enrollment;

public class DuplicateEnrollmentException extends RuntimeException {
    public DuplicateEnrollmentException(Long studentId, Long courseId) {
        super("Student %d is already enrolled in course %d".formatted(studentId, courseId));
    }
}
