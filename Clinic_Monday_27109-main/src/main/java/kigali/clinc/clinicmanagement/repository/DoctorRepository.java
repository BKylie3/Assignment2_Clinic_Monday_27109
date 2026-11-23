package kigali.clinc.clinicmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinc.clinicmanagement.domain.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // QUIZ 1 - B1
    @Query("SELECT DISTINCT d "
            + "FROM Doctor d "
            + "JOIN d.specializations s "
            + "WHERE LOWER(s.name) = LOWER(:name)")
    List<Doctor> findDoctorsBySpecializationName(
            @Param("name") String name);

    // QUIZ 1 - B2
    @Query("SELECT d "
            + "FROM Doctor d "
            + "WHERE d.office IS NULL "
            + "ORDER BY d.lastName ASC")
    List<Doctor> findDoctorsWithoutOffice();

    // QUIZ 2 - A4
    // Checks whether another doctor already occupies an office.
    // This is a DERIVED query, as required by the quiz.
    boolean existsByOffice_OfficeNumber(int officeNumber);

    // QUIZ 2 - D3 will use this later
    @Query("SELECT d "
            + "FROM Doctor d "
            + "WHERE LOWER(d.firstName) LIKE LOWER(CONCAT('%', :name, '%')) "
            + "OR LOWER(d.lastName) LIKE LOWER(CONCAT('%', :name, '%')) "
            + "ORDER BY d.lastName ASC")
    List<Doctor> searchDoctorsByName(
            @Param("name") String name);
}