package org.ehealth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "prescription_items")
public class PrescriptionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prescription_id")
    @JsonIgnore
    private Prescription prescription;

    @ManyToOne
    @JoinColumn(name = "drug_id")
    private Medication medication;

    private String dosage;
    private String frequency;
    private String duration;

}
