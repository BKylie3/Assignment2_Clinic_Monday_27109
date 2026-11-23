package kigali.clinc.clinicmanagement.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinc.clinicmanagement.domain.Appointment;
import kigali.clinc.clinicmanagement.domain.AppointmentStatus;
import kigali.clinc.clinicmanagement.domain.Patient;
import kigali.clinc.clinicmanagement.repository.AppointmentRepository;
import kigali.clinc.clinicmanagement.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    // =========================
    // EXISTING CRUD
    // =========================

    public String savePatient(
            Patient patient) {

        patientRepo.save(patient);

        return "Patient is saved Successfully";
    }

    public List<Patient> findAll() {

        return patientRepo.findAll();
    }

    public Optional<Patient> findById(
            Long id) {

        return patientRepo.findById(id);
    }

    public String updatePatient(
            Long id,
            Patient patient) {

        Optional<Patient> existing =
                patientRepo.findById(id);

        if (existing.isEmpty()) {

            return "Patient not found";
        }

        Patient dbPatient =
                existing.get();

        dbPatient.setFirstName(
                patient.getFirstName());

        dbPatient.setLastName(
                patient.getLastName());

        dbPatient.setDateOfBirth(
                patient.getDateOfBirth());

        dbPatient.setSsNumber(
                patient.getSsNumber());

        patientRepo.save(
                dbPatient);

        return "Patient is updated Successfully";
    }

    public String deletePatient(
            Long id) {

        if (!patientRepo.existsById(id)) {

            return "Patient not found";
        }

        patientRepo.deleteById(id);

        return "Patient is deleted Successfully";
    }

    // =========================
    // QUIZ 1 METHODS RETAINED
    // =========================

    public List<Patient>
            findPatientsByLastName(
                    String lastName) {

        return patientRepo
                .findByLastNameIgnoreCaseOrderByFirstNameAsc(
                        lastName);
    }

    public List<Patient>
            findPatientsOfDoctor(
                    Long doctorId) {

        return patientRepo
                .findPatientsOfDoctor(
                        doctorId);
    }

    public List<Patient>
            findFrequentPatients(
                    long min) {

        return patientRepo
                .findFrequentPatients(
                        min);
    }

    // =========================
    // QUIZ 2 - D2
    // Check patient by ssNumber
    // =========================

    public boolean patientExistsBySsNumber(
            String ssNumber) {

        return patientRepo
                .existsBySsNumber(
                        ssNumber);
    }

    // =========================
    // QUIZ 2 - D2
    // Upcoming appointments
    // =========================

    public List<Appointment>
            findUpcomingAppointments(
                    String ssNumber) {

        List<AppointmentStatus> statuses =
                Arrays.asList(
                        AppointmentStatus.SCHEDULED,
                        AppointmentStatus.CONFIRMED);

        return appointmentRepo
                .findByPatient_SsNumberAndAppointmentDateGreaterThanEqualAndStatusInOrderByAppointmentDateAsc(
                        ssNumber,
                        LocalDate.now(),
                        statuses);
    }
}