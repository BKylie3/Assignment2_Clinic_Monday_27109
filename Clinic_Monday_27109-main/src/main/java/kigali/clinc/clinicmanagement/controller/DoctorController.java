package kigali.clinc.clinicmanagement.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinc.clinicmanagement.domain.Doctor;
import kigali.clinc.clinicmanagement.service.DoctorService;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // =========================
    // EXISTING CRUD
    // =========================

    @PostMapping(
            value = "/save",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveDoctor(
            @RequestBody Doctor doctor) {

        String message =
                doctorService.saveDoctor(doctor);

        if (message.equals(
                "Doctor is saved Successfully")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.CREATED);
        }

        if (message.equals(
                "Office not found")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                message,
                HttpStatus.CONFLICT);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllDoctors() {

        return ResponseEntity.ok(
                doctorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(
            @PathVariable Long id) {

        Optional<Doctor> doctor =
                doctorService.findById(id);

        if (doctor.isEmpty()) {

            return new ResponseEntity<>(
                    "Doctor not found",
                    HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(
                doctor.get());
    }

    @PutMapping(
            value = "/update/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateDoctor(
            @PathVariable Long id,
            @RequestBody Doctor doctor) {

        String message =
                doctorService.updateDoctor(
                        id,
                        doctor);

        if (message.equals(
                "Doctor is updated Successfully")) {

            return ResponseEntity.ok(
                    message);
        }

        return new ResponseEntity<>(
                message,
                HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDoctor(
            @PathVariable Long id) {

        String message =
                doctorService.deleteDoctor(id);

        if (message.equals(
                "Doctor is deleted Successfully")) {

            return ResponseEntity.ok(
                    message);
        }

        return new ResponseEntity<>(
                message,
                HttpStatus.NOT_FOUND);
    }

    // =========================
    // QUIZ 2 - A1
    // Add specialization
    // =========================

    @PutMapping(
            "/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<?> addSpecialization(
            @PathVariable Long doctorId,
            @PathVariable Long specializationId) {

        String message =
                doctorService
                        .addOneSpecialization(
                                doctorId,
                                specializationId);

        if (message.equals(
                "The doctor with that id does not exist")
                ||
                message.equals(
                "That specialization does not exist")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.NOT_FOUND);
        }

        if (message.equals(
                "The doctor already has that specialization")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.CONFLICT);
        }

        return ResponseEntity.ok(
                message);
    }

    // =========================
    // QUIZ 2 - A2
    // Remove specialization
    // =========================

    @DeleteMapping(
            "/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<?> removeSpecialization(
            @PathVariable Long doctorId,
            @PathVariable Long specializationId) {

        String message =
                doctorService
                        .removeOneSpecialization(
                                doctorId,
                                specializationId);

        if (message.equals(
                "The doctor with that id does not exist")
                ||
                message.equals(
                "That specialization does not exist")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.NOT_FOUND);
        }

        if (message.equals(
                "The doctor does not have that specialization")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.CONFLICT);
        }

        return ResponseEntity.ok(
                message);
    }

    // =========================
    // QUIZ 2 - A4
    // Change office
    // =========================

    @PatchMapping("/{doctorId}/office")
    public ResponseEntity<?> changeOffice(
            @PathVariable Long doctorId,
            @RequestParam int officeNumber) {

        String message =
                doctorService.changeOffice(
                        doctorId,
                        officeNumber);

        if (message.equals(
                "The doctor with that id does not exist")
                ||
                message.equals(
                "The office with that office number does not exist")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.NOT_FOUND);
        }

        if (message.equals(
                "That office is taken by other doctor")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.CONFLICT);
        }

        return ResponseEntity.ok(
                message);
    }

    // =========================
    // QUIZ 2 - D1
    // Doctor agenda for a day
    // =========================

    @GetMapping("/{doctorId}/agenda")
    public ResponseEntity<?> getDoctorAgenda(
            @PathVariable Long doctorId,
            @RequestParam String date) {

        if (doctorService
                .findById(doctorId)
                .isEmpty()) {

            return new ResponseEntity<>(
                    "The doctor with that id does not exist",
                    HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(
                doctorService
                        .getDoctorAgenda(
                                doctorId,
                                LocalDate.parse(date)));
    }

    // =========================
    // QUIZ 2 - D3
    // Search doctor by name
    // =========================

    @GetMapping("/search")
    public ResponseEntity<?> searchDoctors(
            @RequestParam String name) {

        return ResponseEntity.ok(
                doctorService
                        .searchDoctors(name));
    }

    // =========================
    // QUIZ 1 METHODS RETAINED
    // =========================

    @GetMapping("/by-specialization")
    public ResponseEntity<?> bySpecialization(
            @RequestParam String name) {

        return ResponseEntity.ok(
                doctorService
                        .findBySpecialization(name));
    }

    @GetMapping("/without-office")
    public ResponseEntity<?> withoutOffice() {

        return ResponseEntity.ok(
                doctorService
                        .findWithoutOffice());
    }
}