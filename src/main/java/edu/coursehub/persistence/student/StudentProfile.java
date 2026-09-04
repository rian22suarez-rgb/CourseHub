package edu.coursehub.persistence.student;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "student_profile")
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    private Student student;

    @Column(length = 1000)
    private String biography;

    @Column(name = "github_url", length = 250)
    private String githubUrl;

    @Column(name = "linkedin_url", length = 250)
    private String linkedinUrl;

    protected StudentProfile() {
    }

    public StudentProfile(Student student, String biography, String githubUrl, String linkedinUrl) {
        this.student = student;
        this.biography = biography;
        this.githubUrl = githubUrl;
        this.linkedinUrl = linkedinUrl;
    }

    public Long getId() { return id; }
    public Student getStudent() { return student; }
    public String getBiography() { return biography; }
    public String getGithubUrl() { return githubUrl; }
    public String getLinkedinUrl() { return linkedinUrl; }
}
