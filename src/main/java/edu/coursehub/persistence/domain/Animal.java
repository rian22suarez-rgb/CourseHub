package edu.coursehub.persistence.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "animal_code", nullable = false, unique = true, length = 50)
    private String animalCode;

    @Column(name = "common_name", nullable = false, length = 100)
    private String commonName;

    @Column(name = "scientific_name", length = 150)
    private String scientificName;

    @Column(name = "tracking_device_code", unique = true, length = 50)
    private String trackingDeviceCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AnimalSex sex;

    // Relación 1:1 con RescueCase
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rescue_case_id", nullable = false, unique = true)
    private RescueCase rescueCase;

    // Relación 1:1 con MedicalRecord
    @OneToOne(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private MedicalRecord medicalRecord;

    // Relación 1:N con Treatment
    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Treatment> treatments = new ArrayList<>();

    protected Animal() {
    }

    public Animal(String animalCode, String commonName, String scientificName, AnimalSex sex) {
        this.animalCode = animalCode;
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.sex = sex;
    }

    // Métodos de sincronización
    public void setRescueCase(RescueCase rescueCase) {
        this.rescueCase = rescueCase;
    }

    public void assignMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
        if (medicalRecord != null) {
            medicalRecord.setAnimal(this);
        }
    }

    // Getters
    public Long getId() { return id; }
    public String getAnimalCode() { return animalCode; }
    public String getCommonName() { return commonName; }
    public String getScientificName() { return scientificName; }
    public AnimalSex getSex() { return sex; }
    public RescueCase getRescueCase() { return rescueCase; }
    public MedicalRecord getMedicalRecord() { return medicalRecord; }
    public List<Treatment> getTreatments() { return treatments; }
}