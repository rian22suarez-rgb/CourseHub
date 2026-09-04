package edu.coursehub.persistence.enrollment;

import edu.coursehub.persistence.course.Course;
import edu.coursehub.persistence.student.Student;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "enrollment",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_enrollment_student_course",
                columnNames = {"student_id", "course_id"}
        )
)
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "enrolled_at", nullable = false)
    private Instant enrolledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnrollmentStatus status;

    @Column(name = "final_grade", precision = 3, scale = 2)
    private BigDecimal finalGrade;

    protected Enrollment() {
    }

    private Enrollment(Student student, Course course, Instant enrolledAt) {
        this.student = student;
        this.course = course;
        this.enrolledAt = enrolledAt;
        this.status = EnrollmentStatus.ACTIVE;
    }

    public static Enrollment enroll(Student student, Course course) {
        return new Enrollment(student, course, Instant.now());
    }

    public void complete(BigDecimal grade) {
        requireValidGrade(grade);
        if (status == EnrollmentStatus.CANCELLED) {
            throw new IllegalStateException("A cancelled enrollment cannot receive a final grade");
        }
        this.finalGrade = grade;
        this.status = EnrollmentStatus.COMPLETED;
    }

    public void cancel() {
        this.status = EnrollmentStatus.CANCELLED;
        this.finalGrade = null;
    }

    private static void requireValidGrade(BigDecimal grade) {
        if (grade == null || grade.compareTo(BigDecimal.ZERO) < 0 || grade.compareTo(new BigDecimal("5.00")) > 0) {
            throw new IllegalArgumentException("Final grade must be between 0.00 and 5.00");
        }
    }

    public Long getId() { return id; }
    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public Instant getEnrolledAt() { return enrolledAt; }
    public EnrollmentStatus getStatus() { return status; }
    public BigDecimal getFinalGrade() { return finalGrade; }
}
