package kigali.clinc.clinicmanagement.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinc.clinicmanagement.domain.Office;
import kigali.clinc.clinicmanagement.service.OfficeService;

@RestController
@RequestMapping("/api/offices")
public class OfficeController {

    @Autowired
    private OfficeService officeService;

    // =========================
    // EXISTING CRUD
    // =========================

    @PostMapping(
            value = "/save",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveOffice(
            @RequestBody Office office) {

        String message =
                officeService.saveOffice(office);

        if (message.equals(
                "Office is saved Successfully")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.CREATED);
        }

        return new ResponseEntity<>(
                message,
                HttpStatus.CONFLICT);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllOffices() {

        return ResponseEntity.ok(
                officeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOfficeById(
            @PathVariable Long id) {

        Optional<Office> office =
                officeService.findById(id);

        if (office.isEmpty()) {

            return new ResponseEntity<>(
                    "Office not found",
                    HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(
                office.get());
    }

    @PutMapping(
            value = "/update/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateOffice(
            @PathVariable Long id,
            @RequestBody Office office) {

        String message =
                officeService.updateOffice(
                        id,
                        office);

        if (message.equals(
                "Office is updated Successfully")) {

            return ResponseEntity.ok(
                    message);
        }

        if (message.equals(
                "Office not found")) {

            return new ResponseEntity<>(
                    message,
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                message,
                HttpStatus.CONFLICT);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOffice(
            @PathVariable Long id) {

        String message =
                officeService.deleteOffice(id);

        if (message.equals(
                "Office is deleted Successfully")) {

            return ResponseEntity.ok(
                    message);
        }

        return new ResponseEntity<>(
                message,
                HttpStatus.NOT_FOUND);
    }

    // =========================
    // QUIZ 2 - A3
    // Free offices
    // =========================

    @GetMapping("/free")
    public ResponseEntity<?> freeOffices() {

        return ResponseEntity.ok(
                officeService.findFreeOffices());
    }

    // =========================
    // QUIZ 1 METHOD RETAINED
    // =========================

    @GetMapping("/busiest")
    public ResponseEntity<?> busiestOffice() {

        Optional<Object[]> result =
                officeService.findBusiestOffice();

        if (result.isEmpty()) {

            return ResponseEntity.ok(
                    "No appointments yet");
        }

        return ResponseEntity.ok(
                result.get());
    }
}