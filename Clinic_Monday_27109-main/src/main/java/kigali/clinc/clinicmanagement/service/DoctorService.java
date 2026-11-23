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
import kigali.clinc.clinicmanagement.domain.Office;
import kigali.clinc.clinicmanagement.domain.Specialization;
import kigali.clinc.clinicmanagement.repository.AppointmentRepository;
import kigali.clinc.clinicmanagement.repository.DoctorRepository;
import kigali.clinc.clinicmanagement.repository.OfficeRepository;
import kigali.clinc.clinicmanagement.repository.SpecializationRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private OfficeRepository officeRepo;

    @Autowired
    private SpecializationRepository specializationRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    // =========================
    // EXISTING CRUD
    // =========================

    public String saveDoctor(Doctor doctor) {

        if (doctor.getOffice() != null
                && doctor.getOffice().getId() != null) {

            Optional<Office> office =
                    officeRepo.findById(
                            doctor.getOffice().getId());

            if (office.isEmpty()) {
                return "Office not found";
            }

            doctor.setOffice(
                    office.get());
        }

        doctorRepo.save(
                doctor);

        return "Doctor is saved Successfully";
    }

    public List<Doctor> findAll() {

        return doctorRepo.findAll();
    }

    public Optional<Doctor> findById(
            Long id) {

        return doctorRepo.findById(id);
    }

    public String updateDoctor(
            Long id,
            Doctor doctor) {

        Optional<Doctor> existing =
                doctorRepo.findById(id);

        if (existing.isEmpty()) {

            return "Doctor not found";
        }

        Doctor dbDoctor =
                existing.get();

        dbDoctor.setFirstName(
                doctor.getFirstName());

        dbDoctor.setLastName(
                doctor.getLastName());

        dbDoctor.setDateOfBirth(
                doctor.getDateOfBirth());

        doctorRepo.save(
                dbDoctor);

        return "Doctor is updated Successfully";
    }

    @Transactional
    public String deleteDoctor(
            Long id) {

        Optional<Doctor> existing =
                doctorRepo.findById(id);

        if (existing.isEmpty()) {

            return "Doctor not found";
        }

        Doctor doctor =
                existing.get();

        doctor.setOffice(
                null);

        doctorRepo.save(
                doctor);

        doctorRepo.delete(
                doctor);

        return "Doctor is deleted Successfully";
    }

    // =========================
    // QUIZ 2 - A1
    // Add specialization
    // =========================

    @Transactional
    public String addOneSpecialization(
            Long doctorId,
            Long specializationId) {

        Optional<Doctor> doctorOpt =
                doctorRepo.findById(
                        doctorId);

        if (doctorOpt.isEmpty()) {

            return "The doctor with that id does not exist";
        }

        Optional<Specialization> specOpt =
                specializationRepo.findById(
                        specializationId);

        if (specOpt.isEmpty()) {

            return "That specialization does not exist";
        }

        Doctor doctor =
                doctorOpt.get();

        Specialization specialization =
                specOpt.get();

        if (doctor.getSpecializations()
                .contains(
                        specialization)) {

            return "The doctor already has that specialization";
        }

        doctor.getSpecializations()
                .add(
                        specialization);

        doctorRepo.save(
                doctor);

        return "Specialization added to the doctor";
    }

    // =========================
    // QUIZ 2 - A2
    // Remove specialization
    // =========================

    @Transactional
    public String removeOneSpecialization(
            Long doctorId,
            Long specializationId) {

        Optional<Doctor> doctorOpt =
                doctorRepo.findById(
                        doctorId);

        if (doctorOpt.isEmpty()) {

            return "The doctor with that id does not exist";
        }

        Optional<Specialization> specOpt =
                specializationRepo.findById(
                        specializationId);

        if (specOpt.isEmpty()) {

            return "That specialization does not exist";
        }

        Doctor doctor =
                doctorOpt.get();

        Specialization specialization =
                specOpt.get();

        if (!doctor.getSpecializations()
                .contains(
                        specialization)) {

            return "The doctor does not have that specialization";
        }

        doctor.getSpecializations()
                .remove(
                        specialization);

        doctorRepo.save(
                doctor);

        return "Specialization removed from the doctor";
    }

    // =========================
    // QUIZ 2 - A4
    // Change office
    // =========================

    @Transactional
    public String changeOffice(
            Long doctorId,
            int officeNumber) {

        Optional<Doctor> doctorOpt =
                doctorRepo.findById(
                        doctorId);

        if (doctorOpt.isEmpty()) {

            return "The doctor with that id does not exist";
        }

        Office office =
                officeRepo.findByOfficeNumber(
                        officeNumber);

        if (office == null) {

            return "The office with that office number does not exist";
        }

        boolean occupied =
                doctorRepo
                        .existsByOffice_OfficeNumber(
                                officeNumber);

        Doctor doctor =
                doctorOpt.get();

        if (occupied) {

            if (doctor.getOffice() == null
                    || doctor.getOffice()
                    .getOfficeNumber()
                    != officeNumber) {

                return "That office is taken by other doctor";
            }
        }

        doctor.setOffice(
                office);

        doctorRepo.save(
                doctor);

        return "Office changed successfully";
    }

    // =========================
    // QUIZ 2 - D1
    // Doctor's agenda
    // =========================

    public List<Appointment> getDoctorAgenda(
            Long doctorId,
            LocalDate date) {

        return appointmentRepo
                .findByDoctor_IdAndAppointmentDateAndStatusNot(
                        doctorId,
                        date,
                        AppointmentStatus.CANCELLED);
    }

    // =========================
    // QUIZ 2 - D3
    // Search doctors by name
    // =========================

    public List<Doctor> searchDoctors(
            String name) {

        return doctorRepo
                .searchDoctorsByName(
                        name);
    }

    // =========================
    // QUIZ 1 METHODS RETAINED
    // =========================

    public List<Doctor> findBySpecialization(
            String name) {

        return doctorRepo
                .findDoctorsBySpecializationName(
                        name);
    }

    public List<Doctor> findWithoutOffice() {

        return doctorRepo
                .findDoctorsWithoutOffice();
    }
}