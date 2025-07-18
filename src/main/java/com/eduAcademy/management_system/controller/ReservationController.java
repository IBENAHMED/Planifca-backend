package com.eduAcademy.management_system.controller;

import com.eduAcademy.management_system.dto.*;
import com.eduAcademy.management_system.entity.Reservation;
import com.eduAcademy.management_system.exception.ConflictException;
import com.eduAcademy.management_system.exception.NotFoundException;
import com.eduAcademy.management_system.service.PDFReservationService;
import com.eduAcademy.management_system.service.ReservationService;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/reservation")
public class ReservationController {
    private final ReservationService reservationService;
    private final PDFReservationService pdfReservationService;

    @PostMapping("new/{terrainId}")
    public ResponseEntity<?> createReservation(@RequestBody ReservationDto reservationDto, @PathVariable String terrainId,@RequestHeader String clubRef) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createReservation(reservationDto, clubRef,terrainId));
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/cancel/{reservationId}")
    public ResponseEntity<String> cancelReservation(@PathVariable String reservationId, @RequestBody CancelReservationDto cancelReservationDto) {
        try {
            reservationService.cancelReservation(reservationId, cancelReservationDto);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/update/{reservationId}")
    public ResponseEntity<?> updateReservation(@PathVariable String reservationId,
                                                                    @RequestBody ReservationUpdateDto updatedReservationDto) {
        try {
            ReservationResponseDto updatedReservation = reservationService.updateReservation(reservationId, updatedReservationDto);
            return ResponseEntity.ok(updatedReservation);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<?> getReservationById(@PathVariable String reservationId) {
        try {
            ReservationResponseDto updatedReservation = reservationService.getReservation(reservationId);
            return ResponseEntity.ok(updatedReservation);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
    }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public Page<ReservationResponseDto> getClubs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,@RequestHeader String clubRef) {
        return reservationService.getReservationsClub(page, size,clubRef);
    }

    @PutMapping("/start/{reservationId}")
    public ResponseEntity<?> startReservation(@PathVariable String reservationId) {
        try {
              reservationService.startReservation(reservationId);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/download-receipt/{reservationId}")
    public ResponseEntity<ByteArrayResource> generateReceipt(@PathVariable String reservationId) throws IOException, DocumentException {
        byte[] pdfContent = pdfReservationService.generateReservationReceipt(reservationId);

        ByteArrayResource resource = new ByteArrayResource(pdfContent);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=receipt.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfContent.length)
                .body(resource);
    }

    @GetMapping("/statistics/{clubId}")
    public ResponseEntity<Map<String, Long>> getReservationStatisticsByClub(@PathVariable String clubId) {
        Map<String, Long> statistics = reservationService.getReservationStatisticsByClub(clubId);
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/timeslots/{clubRef}")
    public ResponseEntity<?> getAllTimeSlotsForClub(
            @PathVariable String clubRef,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {


        if (date == null) {
            return ResponseEntity.badRequest().body("Invalid date format. Please use yyyy-MM-dd'");
        }

        try {
            List<TimeSlotDto> timeSlots = reservationService.getAllTimeSlotsForClub(clubRef, date);
            return ResponseEntity.ok(timeSlots);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Club not found: " + clubRef);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

}
