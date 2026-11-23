package kigali.clinc.clinicmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;
import kigali.clinc.clinicmanagement.domain.Prescription;
import kigali.clinc.clinicmanagement.repository.AppointmentRepository;
import kigali.clinc.clinicmanagement.repository.PrescriptionRepository;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    // =========================
    // QUIZ 2 - C1
    // Save prescription
    // =========================

    public String savePrescription(
            Long appointmentId,
            Prescription prescription) {

        Optional<Appointment> appointmentOpt =
                appointmentRepo.findById(
                        appointmentId);

        if (appointmentOpt.isEmpty()) {

            return "The appointment with that id does not exist";
        }

        Appointment appointment =
                appointmentOpt.get();

        if (appointment.getStatus()
                != AppointmentStatus.COMPLETED) {

            return "Prescriptions are only for COMPLETED appointments";
        }

        // Service sets issuedDate automatically
        prescription.setIssuedDate(
                LocalDate.now());

        // Link prescription to appointment
        prescription.setAppointment(
                appointment);

        prescriptionRepo.save(
                prescription);

        return "Prescription saved successfully";
    }

    // =========================
    // QUIZ 2 - C2
    // Prescriptions for one appointment
    // =========================

    public List<Prescription>
            findByAppointment(
                    Long appointmentId) {

        Optional<Appointment> appointmentOpt =
                appointmentRepo.findById(
                        appointmentId);

        if (appointmentOpt.isEmpty()) {
            return null;
        }

        return prescriptionRepo
                .findByAppointment(
                        appointmentOpt.get());
    }

    public boolean appointmentExists(
            Long appointmentId) {

        return appointmentRepo
                .existsById(appointmentId);
    }

    // =========================
    // QUIZ 2 - C3
    // Patient prescription history
    // =========================

    public List<Prescription>
            findPatientHistory(
                    String ssNumber) {

        return prescriptionRepo
                .findPatientHistory(
                        ssNumber);
    }

    // =========================
    // QUIZ 2 - C4
    // Update prescription
    // =========================

    public String updatePrescription(
            UUID id,
            Prescription prescription) {

        Optional<Prescription> existing =
                prescriptionRepo.findById(id);

        if (existing.isEmpty()) {

            return "The prescription with that id does not exist";
        }

        Prescription dbPrescription =
                existing.get();

        // Only these 3 fields can change
        dbPrescription.setMedicineName(
                prescription.getMedicineName());

        dbPrescription.setDosage(
                prescription.getDosage());

        dbPrescription.setDurationDays(
                prescription.getDurationDays());

        prescriptionRepo.save(
                dbPrescription);

        return "Prescription updated successfully";
    }

    // =========================
    // QUIZ 2 - C4
    // Delete prescription
    // =========================

    public String deletePrescription(
            UUID id) {

        if (!prescriptionRepo.existsById(id)) {

            return "The prescription with that id does not exist";
        }

        prescriptionRepo.deleteById(id);

        return "Prescription deleted successfully";
    }

    // Used later in D4 dashboard
    public long count() {

        return prescriptionRepo.count();
    }
}