package kigali.clinc.clinicmanagement.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import kigali.clinc.clinicmanagement.domain.Patient;
import kigali.clinc.clinicmanagement.service.DoctorService;
import kigali.clinc.clinicmanagement.service.PatientService;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    // =========================
    // EXISTING CRUD
    // =========================

    @PostMapping(
            value = "/save",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> savePatient(
            @RequestBody Patient patient) {

        String message =
                patientService.savePatient(patient);

        return new ResponseEntity<>(
                message,
                HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPatients() {

        return ResponseEntity.ok(
                patientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(
            @PathVariable Long id) {

        Optional<Patient> patient =
                patientService.findById(id);

        if (patient.isEmpty()) {

            return new ResponseEntity<>(
                    "Patient not found",
                    HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(
                patient.get());
    }

    @PutMapping(
            value = "/update/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePatient(
            @PathVariable Long id,
            @RequestBody Patient patient) {

        String message =
                patientService
                        .updatePatient(
                                id,
                                patient);

        if (message.equals(
                "Patient is updated Successfully")) {

            return ResponseEntity.ok(
                    message);
        }

        return new ResponseEntity<>(
                message,
                HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePatient(
            @PathVariable Long id) {

        String message =
                patientService
                        .deletePatient(id);

        if (message.equals(
                "Patient is deleted Successfully")) {

            return ResponseEntity.ok(
                    message);
        }

        return new ResponseEntity<>(
                message,
                HttpStatus.NOT_FOUND);
    }

    // =========================
    // QUIZ 2 - D2
    // Upcoming appointments
    // =========================

    @GetMapping("/{ssNumber}/upcoming")
    public ResponseEntity<?> upcomingAppointments(
            @PathVariable String ssNumber) {

        boolean exists =
                patientService
                        .patientExistsBySsNumber(
                                ssNumber);

        if (!exists) {

            return new ResponseEntity<>(
                    "That patient with that ssnumber does not exist",
                    HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(
                patientService
                        .findUpcomingAppointments(
                                ssNumber));
    }

    // =========================
    // QUIZ 1 METHODS RETAINED
    // =========================

    @GetMapping("/by-last-name")
    public ResponseEntity<?> findByLastName(
            @RequestParam String lastName) {

        return ResponseEntity.ok(
                patientService
                        .findPatientsByLastName(
                                lastName));
    }

    @GetMapping("/of-doctor/{doctorId}")
    public ResponseEntity<?> patientsOfDoctor(
            @PathVariable Long doctorId) {

        if (doctorService
                .findById(doctorId)
                .isEmpty()) {

            return new ResponseEntity<>(
                    "The doctor with that id does not exist",
                    HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(
                patientService
                        .findPatientsOfDoctor(
                                doctorId));
    }

    @GetMapping("/frequent")
    public ResponseEntity<?> frequentPatients(
            @RequestParam long min) {

        return ResponseEntity.ok(
                patientService
                        .findFrequentPatients(
                                min));
    }
}