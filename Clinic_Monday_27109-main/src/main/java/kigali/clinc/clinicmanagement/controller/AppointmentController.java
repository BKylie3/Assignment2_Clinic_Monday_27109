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

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;
import kigali.clinc.clinicmanagement.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // =========================
    // QUIZ 2 - B1
    // Safe booking
    // =========================

    @PostMapping(
            value = "/save",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveAppointment(
            @RequestBody Appointment appointment) {

        String message =
                appointmentService
                        .saveAppointment(appointment);

        if (message.equals(
                "Appointment is saved Successfully")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.CREATED);
        }

        if (message.equals(
                "Patient not found")
                ||
                message.equals(
                "Doctor not found")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.NOT_FOUND);
        }

        if (message.equals(
                "Appointment date cannot be in the past")
                ||
                message.equals(
                "Appointment date is required")
                ||
                message.equals(
                "Patient is required")
                ||
                message.equals(
                "Doctor is required")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.BAD_REQUEST);
        }

        if (message.equals(
                "Doctor is already booked on that date")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(
                message,
                HttpStatus.BAD_REQUEST);
    }

    // =========================
    // READ ALL
    // =========================

    @GetMapping("/all")
    public ResponseEntity<?> getAllAppointments() {

        return ResponseEntity.ok(
                appointmentService.findAll());
    }

    // =========================
    // READ ONE
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(
            @PathVariable Long id) {

        Optional<Appointment> appointment =
                appointmentService.findById(id);

        if (appointment.isEmpty()) {

            return new ResponseEntity<>(
                    "Appointment not found",
                    HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(
                appointment.get());
    }

    // =========================
    // GENERAL UPDATE
    // Status is NOT changed here
    // =========================

    @PutMapping(
            value = "/update/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAppointment(
            @PathVariable Long id,
            @RequestBody Appointment appointment) {

        String message =
                appointmentService
                        .updateAppointment(
                                id,
                                appointment);

        if (message.equals(
                "Appointment is updated Successfully")) {

            return ResponseEntity.ok(
                    message);
        }

        return new ResponseEntity<>(
                message,
                HttpStatus.NOT_FOUND);
    }

    // =========================
    // QUIZ 2 - C4
    // Protected appointment delete
    // =========================

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAppointment(
            @PathVariable Long id) {

        String message =
                appointmentService
                        .deleteAppointment(id);

        if (message.equals(
                "Appointment is deleted Successfully")) {

            return ResponseEntity.ok(
                    message);
        }

        if (message.equals(
                "That appointment has prescriptions, delete them first")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(
                message,
                HttpStatus.NOT_FOUND);
    }

    // =========================
    // QUIZ 2 - B2
    // Change appointment status
    // =========================

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status) {

        String message =
                appointmentService
                        .changeStatus(
                                id,
                                status);

        if (message.equals(
                "The appointment with that id does not exist")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.NOT_FOUND);
        }

        if (message.startsWith(
                "Cannot change status from")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(
                message);
    }

    // =========================
    // QUIZ 2 - B3
    // Reschedule appointment
    // =========================

    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<?> rescheduleAppointment(
            @PathVariable Long id,
            @RequestParam String newDate) {

        LocalDate date =
                LocalDate.parse(newDate);

        String message =
                appointmentService
                        .rescheduleAppointment(
                                id,
                                date);

        if (message.equals(
                "The appointment with that id does not exist")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.NOT_FOUND);
        }

        if (message.equals(
                "A COMPLETED appointment cannot be rescheduled")
                ||
                message.equals(
                "A CANCELLED appointment cannot be rescheduled")
                ||
                message.equals(
                "Appointment date cannot be in the past")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.BAD_REQUEST);
        }

        if (message.equals(
                "Doctor is already booked on that date")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.CONFLICT);
        }

        return ResponseEntity.ok(
                message);
    }

    // =========================
    // QUIZ 1 METHODS RETAINED
    // =========================

    @GetMapping("/by-status")
    public ResponseEntity<?> byStatus(
            @RequestParam AppointmentStatus status) {

        return ResponseEntity.ok(
                appointmentService
                        .findByStatus(status));
    }

    @GetMapping("/between")
    public ResponseEntity<?> between(
            @RequestParam String start,
            @RequestParam String end) {

        return ResponseEntity.ok(
                appointmentService
                        .findBetween(
                                LocalDate.parse(start),
                                LocalDate.parse(end)));
    }

    @GetMapping("/stats/by-status")
    public ResponseEntity<?> statsByStatus() {

        return ResponseEntity.ok(
                appointmentService
                        .appointmentsByStatus());
    }

    @PatchMapping("/cancel-day")
    public ResponseEntity<?> cancelDay(
            @RequestParam Long doctorId,
            @RequestParam String date) {

        return ResponseEntity.ok(
                appointmentService
                        .cancelDoctorDay(
                                doctorId,
                                LocalDate.parse(date)));
    }
}