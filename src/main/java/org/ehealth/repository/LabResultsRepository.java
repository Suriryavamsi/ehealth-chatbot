package org.ehealth.repository;

import org.ehealth.model.Doctor;
import org.ehealth.model.LabResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LabResultsRepository extends JpaRepository<LabResult, Long> {

    // Find all lab results for a specific patient
    List<LabResult> findByPatientId(Long patientId);

    @Query("SELECT lr FROM LabResult lr WHERE lr.verifiedBy.id = :doctorId")
    List<LabResult> findByVerifiedById(@Param("doctorId") Long doctorId);

}
