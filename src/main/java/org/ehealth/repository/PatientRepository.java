package org.ehealth.repository;

import org.ehealth.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Optional: find patients assigned to a doctor
//    List<Patient> findByDoctorId(Long doctorId);

    // Optional: find patient by user ID
//    Patient findByUserId(Long userId);

    @Query("SELECT p FROM Patient p WHERE p.user.id = :userId")
    Optional<Patient> findByUserId(@Param("userId") Long userId);
}
