package kigali.clinc.clinicmanagement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.Prescription;

@Repository
public interface PrescriptionRepository
        extends JpaRepository<Prescription, UUID> {

    // =========================
    // QUIZ 2 - C2
    // DERIVED query
    // Find prescriptions of one appointment
    // =========================

    List<Prescription>
            findByAppointment(
                    Appointment appointment);

    // =========================
    // QUIZ 2 - C4
    // Used to protect appointment deletion
    // =========================

    boolean existsByAppointment(
            Appointment appointment);

    // =========================
    // QUIZ 2 - C3
    // JPQL query
    // Patient prescription history
    // =========================

    @Query("SELECT p "
            + "FROM Prescription p "
            + "WHERE p.appointment.patient.ssNumber = :ss "
            + "ORDER BY p.issuedDate DESC")
    List<Prescription>
            findPatientHistory(
                    @Param("ss") String ssNumber);
}