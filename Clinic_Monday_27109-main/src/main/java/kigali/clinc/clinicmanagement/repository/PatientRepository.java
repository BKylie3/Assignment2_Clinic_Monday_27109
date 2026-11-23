package kigali.clinc.clinicmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinc.clinicmanagement.domain.Patient;

@Repository
public interface PatientRepository
        extends JpaRepository<Patient, Long> {

    // =========================
    // QUIZ 1 - A1
    // =========================

    List<Patient>
            findByLastNameIgnoreCaseOrderByFirstNameAsc(
                    String lastName);

    // =========================
    // QUIZ 1 - B4
    // =========================

    @Query("SELECT DISTINCT a.patient "
            + "FROM Appointment a "
            + "WHERE a.doctor.id = :doctorId")
    List<Patient>
            findPatientsOfDoctor(
                    @Param("doctorId")
                    Long doctorId);

    // =========================
    // QUIZ 1 - C2
    // =========================

    @Query("SELECT a.patient "
            + "FROM Appointment a "
            + "GROUP BY a.patient "
            + "HAVING COUNT(a) >= :min "
            + "ORDER BY COUNT(a) DESC")
    List<Patient>
            findFrequentPatients(
                    @Param("min")
                    long min);

    // =========================
    // QUIZ 2 - D2
    // Find patient by ssNumber
    // =========================

    Optional<Patient>
            findBySsNumber(
                    String ssNumber);

    boolean existsBySsNumber(
            String ssNumber);
}