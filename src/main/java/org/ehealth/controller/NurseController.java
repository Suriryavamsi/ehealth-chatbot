package org.ehealth.controller;

import org.ehealth.model.Appointment;
import org.ehealth.model.Patient;
import org.ehealth.repository.AppointmentRepository;
import org.ehealth.repository.PatientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nurse")
public class NurseController {

    private final AppointmentRepository appointmentRepo;
    private final PatientRepository patientRepo;

    public NurseController(AppointmentRepository appointmentRepo,
                           PatientRepository patientRepo){
        this.appointmentRepo = appointmentRepo;
        this.patientRepo = patientRepo;
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
}
