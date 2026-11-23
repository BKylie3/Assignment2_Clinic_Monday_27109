package kigali.clinc.clinicmanagement.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;

@Repository
public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    // =========================
    // QUIZ 1 - A2
    // =========================

    List<Appointment>
            findByStatusOrderByAppointmentDateAsc(
                    AppointmentStatus status);

    // =========================
    // QUIZ 1 - A3
    // =========================

    List<Appointment>
            findByAppointmentDateBetweenOrderByAppointmentDateAsc(
                    LocalDate start,
                    LocalDate end);

    // =========================
    // QUIZ 1 - A4
    // =========================

    boolean
            existsByDoctor_IdAndAppointmentDateAndStatusNot(
                    Long doctorId,
                    LocalDate appointmentDate,
                    AppointmentStatus status);

    // =========================
    // QUIZ 1 - C1
    // =========================

    @Query("SELECT a.status, COUNT(a) "
            + "FROM Appointment a "
            + "GROUP BY a.status")
    List<Object[]> countAppointmentsByStatus();

    // =========================
    // QUIZ 1 - C4
    // =========================

    @Modifying
    @Query("UPDATE Appointment a "
            + "SET a.status = :cancelled "
            + "WHERE a.doctor.id = :doctorId "
            + "AND a.appointmentDate = :date "
            + "AND a.status <> :completed")
    int cancelDoctorAppointmentsForDay(
            @Param("doctorId")
            Long doctorId,

            @Param("date")
            LocalDate date,

            @Param("cancelled")
            AppointmentStatus cancelled,

            @Param("completed")
            AppointmentStatus completed);

    // =========================
    // QUIZ 2 - B3
    // DERIVED
    // Check another appointment
    // on the same date
    // =========================

    boolean
            existsByDoctor_IdAndAppointmentDateAndStatusNotAndIdNot(
                    Long doctorId,
                    LocalDate appointmentDate,
                    AppointmentStatus status,
                    Long appointmentId);

    // =========================
    // QUIZ 2 - D1
    // Doctor agenda for one day
    // Exclude CANCELLED
    // =========================

    List<Appointment>
            findByDoctor_IdAndAppointmentDateAndStatusNot(
                    Long doctorId,
                    LocalDate appointmentDate,
                    AppointmentStatus status);

    // =========================
    // QUIZ 2 - D2
    // Upcoming appointments
    // DERIVED QUERY
    // =========================

    List<Appointment>
            findByPatient_SsNumberAndAppointmentDateGreaterThanEqualAndStatusInOrderByAppointmentDateAsc(
                    String ssNumber,
                    LocalDate appointmentDate,
                    List<AppointmentStatus> statuses);

    // =========================
    // QUIZ 2 - D4
    // Dashboard counts
    // =========================

    long countByStatus(
            AppointmentStatus status);

    long countByAppointmentDate(
            LocalDate appointmentDate);
}