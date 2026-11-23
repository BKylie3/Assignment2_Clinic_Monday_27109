package kigali.clinc.clinicmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;
import kigali.clinc.clinicmanagement.domain.Doctor;
import kigali.clinc.clinicmanagement.domain.Patient;
import kigali.clinc.clinicmanagement.repository.AppointmentRepository;
import kigali.clinc.clinicmanagement.repository.DoctorRepository;
import kigali.clinc.clinicmanagement.repository.PatientRepository;
import kigali.clinc.clinicmanagement.repository.PrescriptionRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PrescriptionRepository prescriptionRepo;

    // =========================
    // QUIZ 2 - B1
    // Safe booking
    // =========================

    public String saveAppointment(
            Appointment appointment) {

        if (appointment.getAppointmentDate() == null) {
            return "Appointment date is required";
        }

        if (appointment.getAppointmentDate()
                .isBefore(LocalDate.now())) {

            return "Appointment date cannot be in the past";
        }

        if (appointment.getPatient() == null
                || appointment.getPatient().getId() == null) {

            return "Patient is required";
        }

        if (appointment.getDoctor() == null
                || appointment.getDoctor().getId() == null) {

            return "Doctor is required";
        }

        Optional<Patient> patient =
                patientRepo.findById(
                        appointment.getPatient().getId());

        if (patient.isEmpty()) {
            return "Patient not found";
        }

        Optional<Doctor> doctor =
                doctorRepo.findById(
                        appointment.getDoctor().getId());

        if (doctor.isEmpty()) {
            return "Doctor not found";
        }

        boolean alreadyBooked =
                appointmentRepo
                .existsByDoctor_IdAndAppointmentDateAndStatusNot(
                        doctor.get().getId(),
                        appointment.getAppointmentDate(),
                        AppointmentStatus.CANCELLED);

        if (alreadyBooked) {
            return "Doctor is already booked on that date";
        }

        appointment.setPatient(
                patient.get());

        appointment.setDoctor(
                doctor.get());

        // Every new appointment starts as SCHEDULED
        appointment.setStatus(
                AppointmentStatus.SCHEDULED);

        appointmentRepo.save(
                appointment);

        return "Appointment is saved Successfully";
    }

    // =========================
    // READ METHODS
    // =========================

    public List<Appointment> findAll() {
        return appointmentRepo.findAll();
    }

    public Optional<Appointment> findById(
            Long id) {

        return appointmentRepo.findById(id);
    }

    // =========================
    // GENERAL UPDATE
    // Status is NOT changed here
    // =========================

    public String updateAppointment(
            Long id,
            Appointment appointment) {

        Optional<Appointment> existing =
                appointmentRepo.findById(id);

        if (existing.isEmpty()) {
            return "Appointment not found";
        }

        Appointment dbAppointment =
                existing.get();

        dbAppointment.setAppointmentDate(
                appointment.getAppointmentDate());

        dbAppointment.setReason(
                appointment.getReason());

        if (appointment.getPatient() != null
                && appointment.getPatient().getId() != null) {

            Optional<Patient> patient =
                    patientRepo.findById(
                            appointment
                            .getPatient()
                            .getId());

            if (patient.isEmpty()) {
                return "Patient not found";
            }

            dbAppointment.setPatient(
                    patient.get());
        }

        if (appointment.getDoctor() != null
                && appointment.getDoctor().getId() != null) {

            Optional<Doctor> doctor =
                    doctorRepo.findById(
                            appointment
                            .getDoctor()
                            .getId());

            if (doctor.isEmpty()) {
                return "Doctor not found";
            }

            dbAppointment.setDoctor(
                    doctor.get());
        }

        appointmentRepo.save(
                dbAppointment);

        return "Appointment is updated Successfully";
    }

    // =========================
    // QUIZ 2 - C4
    // Protect appointment deletion
    // =========================

    public String deleteAppointment(
            Long id) {

        Optional<Appointment> appointmentOpt =
                appointmentRepo.findById(id);

        if (appointmentOpt.isEmpty()) {

            return "Appointment not found";
        }

        Appointment appointment =
                appointmentOpt.get();

        boolean hasPrescriptions =
                prescriptionRepo
                .existsByAppointment(
                        appointment);

        if (hasPrescriptions) {

            return "That appointment has prescriptions, delete them first";
        }

        appointmentRepo.delete(
                appointment);

        return "Appointment is deleted Successfully";
    }

    // =========================
    // QUIZ 2 - B2
    // Appointment status flow
    // =========================

    public String changeStatus(
            Long id,
            AppointmentStatus newStatus) {

        Optional<Appointment> appointmentOpt =
                appointmentRepo.findById(id);

        if (appointmentOpt.isEmpty()) {

            return "The appointment with that id does not exist";
        }

        Appointment appointment =
                appointmentOpt.get();

        AppointmentStatus currentStatus =
                appointment.getStatus();

        boolean allowed = false;

        if (currentStatus
                == AppointmentStatus.SCHEDULED) {

            if (newStatus
                    == AppointmentStatus.CONFIRMED
                    ||
                    newStatus
                    == AppointmentStatus.CANCELLED) {

                allowed = true;
            }
        }

        else if (currentStatus
                == AppointmentStatus.CONFIRMED) {

            if (newStatus
                    == AppointmentStatus.COMPLETED
                    ||
                    newStatus
                    == AppointmentStatus.CANCELLED) {

                allowed = true;
            }
        }

        if (!allowed) {

            return "Cannot change status from "
                    + currentStatus
                    + " to "
                    + newStatus;
        }

        appointment.setStatus(
                newStatus);

        appointmentRepo.save(
                appointment);

        return "Appointment status updated successfully";
    }

    // =========================
    // QUIZ 2 - B3
    // Reschedule appointment
    // =========================

    public String rescheduleAppointment(
            Long id,
            LocalDate newDate) {

        Optional<Appointment> appointmentOpt =
                appointmentRepo.findById(id);

        if (appointmentOpt.isEmpty()) {

            return "The appointment with that id does not exist";
        }

        Appointment appointment =
                appointmentOpt.get();

        if (appointment.getStatus()
                == AppointmentStatus.COMPLETED) {

            return "A COMPLETED appointment cannot be rescheduled";
        }

        if (appointment.getStatus()
                == AppointmentStatus.CANCELLED) {

            return "A CANCELLED appointment cannot be rescheduled";
        }

        if (newDate.isBefore(
                LocalDate.now())) {

            return "Appointment date cannot be in the past";
        }

        boolean alreadyBooked =
                appointmentRepo
                .existsByDoctor_IdAndAppointmentDateAndStatusNotAndIdNot(
                        appointment
                        .getDoctor()
                        .getId(),
                        newDate,
                        AppointmentStatus.CANCELLED,
                        appointment.getId());

        if (alreadyBooked) {

            return "Doctor is already booked on that date";
        }

        appointment.setAppointmentDate(
                newDate);

        appointment.setStatus(
                AppointmentStatus.SCHEDULED);

        appointmentRepo.save(
                appointment);

        return "Appointment rescheduled successfully";
    }

    // =========================
    // QUIZ 1 METHODS RETAINED
    // =========================

    public List<Appointment> findByStatus(
            AppointmentStatus status) {

        return appointmentRepo
                .findByStatusOrderByAppointmentDateAsc(
                        status);
    }

    public List<Appointment> findBetween(
            LocalDate start,
            LocalDate end) {

        return appointmentRepo
                .findByAppointmentDateBetweenOrderByAppointmentDateAsc(
                        start,
                        end);
    }

    public List<Object[]>
            appointmentsByStatus() {

        return appointmentRepo
                .countAppointmentsByStatus();
    }

    @Transactional
    public String cancelDoctorDay(
            Long doctorId,
            LocalDate date) {

        int count =
                appointmentRepo
                .cancelDoctorAppointmentsForDay(
                        doctorId,
                        date,
                        AppointmentStatus.CANCELLED,
                        AppointmentStatus.COMPLETED);

        return count
                + " appointments cancelled";
    }
}