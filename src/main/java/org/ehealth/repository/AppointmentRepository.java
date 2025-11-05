package org.ehealth.repository;

import org.ehealth.model.Appointment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Find all appointments for a doctor on a specific date
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND FUNCTION('DATE', a.datetime) = :date")
    List<Appointment> findByDoctorAndDate(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);

    // Find upcoming appointments for a patient, ordered by datetime
    @Query("SELECT a FROM Appointment a WHERE a.patient.user.id = :userId AND a.datetime >= CURRENT_TIMESTAMP ORDER BY a.datetime ASC")
    List<Appointment> findUpcomingByPatientUserId(@Param("userId") Long userId, Pageable pageable);

    // Optional: find all appointments for a doctor
    List<Appointment> findByDoctorId(Long doctorId);

    // Optional: find all appointments for a patient
    List<Appointment> findByPatientId(Long patientId);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.user.id = :userId ORDER BY a.datetime ASC")
    List<Appointment> findByDoctorUserId(@Param("userId") Long userId);

}
