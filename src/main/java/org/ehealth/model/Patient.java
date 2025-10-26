package org.ehealth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Setter
@Getter
@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private String name;
    private java.sql.Date dob;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String contact;
    private String address;
    private String emergencyContact;

    public enum Gender {
        Male, Female, Other;
    }
}
