package org.ehealth.controller;

import org.ehealth.model.*;
import org.ehealth.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nurse")
public class NurseController {

    private final AppointmentRepository appointmentRepo;
    private final PatientRepository patientRepo;
    private final NurseRepository nurseRepository;
    private final LabResultsRepository labRepo;
    private final PrescriptionRepository prescriptionRepo;

    public NurseController(AppointmentRepository appointmentRepo,
                           PatientRepository patientRepo,
                           NurseRepository nurseRepository, LabResultsRepository labRepo, PrescriptionRepository prescriptionRepo){
        this.appointmentRepo = appointmentRepo;
        this.patientRepo = patientRepo;
        this.nurseRepository = nurseRepository;
        this.labRepo = labRepo;
        this.prescriptionRepo = prescriptionRepo;
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(@RequestParam Long doctorId){
        List<Appointment> appointments = appointmentRepo.findByDoctorId(doctorId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/patients/{patientId}")
    public ResponseEntity<Patient> getPatient(@PathVariable Long patientId){
        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getAllPatients(Authentication auth) {
        String username = auth.getName();
        Nurse nurse = nurseRepository.findByUserId(getUserIdFromAuth(auth))
                .orElseThrow(() -> new RuntimeException("Nurse not found"));
        List<Patient> patients = nurseRepository.findPatientsByNurseId(nurse.getId());
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/list-all")
    public ResponseEntity<List<Doctor>> getDoctors(Authentication auth){
        Nurse nurse = nurseRepository.findByUserId(getUserIdFromAuth(auth))
                .orElseThrow(() -> new RuntimeException("Nurse not found"));

        return ResponseEntity.ok(nurseRepository.findDoctorsByNurseId(nurse.getId()));
    }

    @GetMapping("/patients/{patientId}/lab-results")
    public ResponseEntity<List<LabResult>> getPatientLabResults(@PathVariable Long patientId){
        List<LabResult> results = labRepo.findByPatientId(patientId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/patients/{patientId}/prescriptions")
    public ResponseEntity<List<Prescription>> getPrescriptions(@PathVariable Long patientId) {
        List<Prescription> prescriptions = prescriptionRepo.findByPatientId(patientId);
        return ResponseEntity.ok(prescriptions);
    }

    private Long getUserIdFromAuth(Authentication auth){
        // Implement a method to fetch User ID from Authentication principal
        return ((org.ehealth.model.User) auth.getPrincipal()).getId();
    }


}
