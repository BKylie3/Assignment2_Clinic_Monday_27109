package kigali.clinc.clinicmanagement.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinc.clinicmanagement.domain.AppointmentStatus;
import kigali.clinc.clinicmanagement.repository.AppointmentRepository;
import kigali.clinc.clinicmanagement.repository.DoctorRepository;
import kigali.clinc.clinicmanagement.repository.PatientRepository;

@Service
public class DashboardService {

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private PrescriptionService prescriptionService;

    // =========================
    // QUIZ 2 - D4
    // Dashboard summary
    // =========================

    public Map<String, Long> getSummary() {

        Map<String, Long> summary =
                new LinkedHashMap<>();

        // Total patients
        summary.put(
                "patients",
                patientRepo.count());

        // Total doctors
        summary.put(
                "doctors",
                doctorRepo.count());

        // Number of free offices
        summary.put(
                "freeOffices",
                (long) officeService
                        .findFreeOffices()
                        .size());

        // Appointments scheduled for today
        summary.put(
                "appointmentsToday",
                appointmentRepo
                        .countByAppointmentDate(
                                LocalDate.now()));

        // Total SCHEDULED appointments
        summary.put(
                "scheduled",
                appointmentRepo
                        .countByStatus(
                                AppointmentStatus.SCHEDULED));

        // Total prescriptions
        summary.put(
                "prescriptions",
                prescriptionService.count());

        return summary;
    }
}