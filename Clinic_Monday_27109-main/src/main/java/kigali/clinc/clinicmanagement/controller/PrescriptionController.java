package kigali.clinc.clinicmanagement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinc.clinicmanagement.domain.Prescription;
import kigali.clinc.clinicmanagement.service.PrescriptionService;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    // =========================
    // QUIZ 2 - C1
    // Write prescription
    // =========================

    @PostMapping("/save")
    public ResponseEntity<?> savePrescription(
            @RequestParam Long appointmentId,
            @RequestBody Prescription prescription) {

        String message =
                prescriptionService.savePrescription(
                        appointmentId,
                        prescription);

        if (message.equals(
                "The appointment with that id does not exist")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.NOT_FOUND);
        }

        if (message.equals(
                "Prescriptions are only for COMPLETED appointments")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
                message,
                HttpStatus.CREATED);
    }

    // =========================
    // QUIZ 2 - C2
    // Prescriptions of appointment
    // =========================

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> prescriptionsOfAppointment(
            @PathVariable Long appointmentId) {

        if (!prescriptionService
                .appointmentExists(
                        appointmentId)) {

            return new ResponseEntity<>(
                    "The appointment with that id does not exist",
                    HttpStatus.NOT_FOUND);
        }

        List<Prescription> prescriptions =
                prescriptionService
                .findByAppointment(
                        appointmentId);

        return ResponseEntity.ok(
                prescriptions);
    }

    // =========================
    // QUIZ 2 - C3
    // Patient prescription history
    // =========================

    @GetMapping("/patient/{ssNumber}")
    public ResponseEntity<?> patientHistory(
            @PathVariable String ssNumber) {

        return ResponseEntity.ok(
                prescriptionService
                .findPatientHistory(
                        ssNumber));
    }

    // =========================
    // QUIZ 2 - C4
    // Update prescription
    // =========================

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePrescription(
            @PathVariable UUID id,
            @RequestBody Prescription prescription) {

        String message =
                prescriptionService
                .updatePrescription(
                        id,
                        prescription);

        if (message.equals(
                "The prescription with that id does not exist")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(
                message);
    }

    // =========================
    // QUIZ 2 - C4
    // Delete prescription
    // =========================

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePrescription(
            @PathVariable UUID id) {

        String message =
                prescriptionService
                .deletePrescription(id);

        if (message.equals(
                "The prescription with that id does not exist")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(
                message);
    }
}