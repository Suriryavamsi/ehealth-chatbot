package org.ehealth.controller;

import org.ehealth.model.Appointment;
import org.ehealth.model.Doctor;
import org.ehealth.model.LabResult;
import org.ehealth.model.Patient;
import org.ehealth.repository.AppointmentRepository;
import org.ehealth.repository.DoctorRepository;
import org.ehealth.repository.LabResultsRepository;
import org.ehealth.repository.PatientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private final DoctorRepository doctorRepo;
    private final AppointmentRepository appointmentRepo;
    private final PatientRepository patientRepo;
    private final LabResultsRepository labRepo;

    public DoctorController(DoctorRepository doctorRepo,
                            AppointmentRepository appointmentRepo,
                            PatientRepository patientRepo,
                            LabResultsRepository labRepo){
        this.doctorRepo = doctorRepo;
        this.appointmentRepo = appointmentRepo;
        this.patientRepo = patientRepo;
        this.labRepo = labRepo;
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAppointments(Authentication auth){
        String username = auth.getName();
        Doctor doctor = doctorRepo.findByUserId(getUserIdFromAuth(auth))
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<Appointment> appointments = appointmentRepo.findByDoctorId(doctor.getId());
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/patients/{patientId}")
    public ResponseEntity<Patient> getPatient(@PathVariable Long patientId){
        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getDoctorPatients(Authentication auth){
        Doctor doctor = doctorRepo.findByUserId(getUserIdFromAuth(auth))
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<Patient> patients = patientRepo.findByDoctorsId(doctor.getId());
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/patients/{patientId}/lab-results")
    public ResponseEntity<List<LabResult>> getPatientLabResults(@PathVariable Long patientId){
        List<LabResult> results = labRepo.findByPatientId(patientId);
        return ResponseEntity.ok(results);
    }

    private Long getUserIdFromAuth(Authentication auth){
        // Implement a method to fetch User ID from Authentication principal
        return ((org.ehealth.model.User) auth.getPrincipal()).getId();
    }
}
