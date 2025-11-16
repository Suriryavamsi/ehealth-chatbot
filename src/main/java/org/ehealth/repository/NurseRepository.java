package org.ehealth.repository;

import org.ehealth.model.Appointment;
import org.ehealth.model.Doctor;
import org.ehealth.model.Nurse;
import org.ehealth.model.Patient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NurseRepository extends JpaRepository<Nurse, Long> {

    @Query("SELECT n FROM Nurse n WHERE n.user.id = :userId")
    Optional<Nurse> findByUserId(@Param("userId") Long userId);

    // Get patients assigned to a nurse
    @Query("SELECT p FROM Patient p JOIN p.nurses n WHERE n.id = :nurseId")
    List<Patient> findPatientsByNurseId(@Param("nurseId") Long nurseId);

    // Get doctors assigned to a nurse
    @Query("SELECT d FROM Doctor d JOIN d.nurses n WHERE n.id = :nurseId")
    List<Doctor> findDoctorsByNurseId(@Param("nurseId") Long nurseId);
}
