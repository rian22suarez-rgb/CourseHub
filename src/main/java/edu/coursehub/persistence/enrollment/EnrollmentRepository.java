package edu.coursehub.persistence.enrollment;

import edu.coursehub.persistence.student.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByStudent_IdAndCourse_Id(Long studentId, Long courseId);

    List<Enrollment> findByStudent_Id(Long studentId);

    long countByStatus(EnrollmentStatus status);

    @Query("""
            select e.student
            from Enrollment e
            where e.course.id = :courseId
              and e.status = :status
              and e.student.active = true
            order by e.student.name
            """)
    List<Student> findActiveStudentsByCourseAndStatus(
            @Param("courseId") Long courseId,
            @Param("status") EnrollmentStatus status
    );

    @Query("""
            select avg(e.finalGrade)
            from Enrollment e
            where e.course.id = :courseId
              and e.finalGrade is not null
            """)
    Double calculateAverageGrade(@Param("courseId") Long courseId);

    @EntityGraph(attributePaths = {"student", "course", "course.department"})
    List<Enrollment> findByCourse_Id(Long courseId);
}
