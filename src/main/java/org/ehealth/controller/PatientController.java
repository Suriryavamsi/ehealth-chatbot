package org.ehealth.controller;

import org.ehealth.model.*;
import org.ehealth.repository.AppointmentRepository;
import org.ehealth.repository.LabResultsRepository;
import org.ehealth.repository.PatientRepository;
import org.ehealth.repository.PrescriptionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    private final PatientRepository patientRepo;
    private final AppointmentRepository appointmentRepo;
    private final LabResultsRepository labRepo;
    private final PrescriptionRepository prescriptionRepo;

    public PatientController(PatientRepository patientRepo,
                             AppointmentRepository appointmentRepo,
                             LabResultsRepository labRepo, PrescriptionRepository prescriptionRepo){
        this.patientRepo = patientRepo;
        this.appointmentRepo = appointmentRepo;
        this.labRepo = labRepo;
        this.prescriptionRepo = prescriptionRepo;
    }

    // ---------------- Patient Profile ----------------
    @GetMapping("/profile")
    public ResponseEntity<Patient> getProfile(Authentication auth){
        Long userId = getUserIdFromAuth(auth);
        Patient patient = patientRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return ResponseEntity.ok(patient);
    }

    // ---------------- Patient Appointments ----------------
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAppointments(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Long userId = getUserIdFromAuth(auth);
        Patient patient = patientRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        List<Appointment> appointments = appointmentRepo.findByPatientId(patient.getId());
        return ResponseEntity.ok(appointments);
    }

    // ---------------- Patient Lab Results ----------------
    @GetMapping("/lab-results")
    public ResponseEntity<List<LabResult>> getLabResults(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Long userId = getUserIdFromAuth(auth);
        Patient patient = patientRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        List<LabResult> results = labRepo.findByPatientId(patient.getId());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/prescriptions")
    public ResponseEntity<List<Prescription>> getPrescriptions(Authentication auth) {
        Long userId = getUserIdFromAuth(auth);
        Patient patient = patientRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        List<Prescription> prescriptions = prescriptionRepo.findByPatientId(patient.getId());
        return ResponseEntity.ok(prescriptions);
    }

    // ---------------- Helper Method ----------------
    private Long getUserIdFromAuth(Authentication auth){
        User user = (User) auth.getPrincipal();
        return user.getId();
    }
}
