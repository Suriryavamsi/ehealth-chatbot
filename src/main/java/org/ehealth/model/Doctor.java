package org.ehealth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private String specialization;

    @Column(unique = true)
    private String regNo;

    private String contact;
    private String availability;

    @ManyToMany
    @JoinTable(
            name = "patient_doctors",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id")
    )
    private List<Patient> patients;

    @ManyToMany
    @JoinTable(
            name = "doctor_nurses",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "nurse_id")
    )
    private List<Nurse> nurses;

}
