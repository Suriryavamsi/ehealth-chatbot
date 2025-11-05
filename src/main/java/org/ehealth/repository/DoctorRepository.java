package org.ehealth.repository;

import org.ehealth.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // Find doctor by user ID
    @Query("SELECT d FROM Doctor d WHERE d.user.id = :userId")
    Optional<Doctor> findByUserId(@Param("userId") Long userId);

    // Find doctors by specialization
    @Query("SELECT d FROM Doctor d WHERE d.specialization = :specialization")
    List<Doctor> findBySpecialization(@Param("specialization") String specialization);

    // Search doctor by name
    @Query("SELECT d FROM Doctor d WHERE d.user.username LIKE %:name% OR d.user.email LIKE %:name%")
    List<Doctor> searchByNameOrEmail(@Param("name") String name);
}
