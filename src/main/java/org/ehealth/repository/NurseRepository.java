package org.ehealth.repository;

import org.ehealth.model.Appointment;
import org.ehealth.model.Nurse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface NurseRepository extends JpaRepository<Nurse, Long> {

    List<Nurse> findByPatientsId(Long patientId);
}
