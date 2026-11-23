package kigali.clinc.clinicmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kigali.clinc.clinicmanagement.domain.Specialization;

@Repository
public interface SpecializationRepository
        extends JpaRepository<Specialization, Long> {

    Specialization findByName(String name);

    // B3 - JPQL
    @Query("SELECT s "
            + "FROM Specialization s "
            + "WHERE s.doctors IS EMPTY")
    List<Specialization> findUnusedSpecializations();
}