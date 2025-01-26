package com.eduAcademy.management_system.controller;

import com.eduAcademy.management_system.dto.StaffRequestDto;
import com.eduAcademy.management_system.dto.StaffResponseDto;
import com.eduAcademy.management_system.entity.Staff;
import com.eduAcademy.management_system.mapper.StaffMapper;
import com.eduAcademy.management_system.service.StaffServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/staff")
@RequiredArgsConstructor
public class StaffController {
    private final StaffServiceImpl staffService;
    private final StaffMapper staffMapper;

    @PostMapping("newStaff")
    public ResponseEntity<?> addStaff(@RequestBody StaffRequestDto staffRequestDto, @RequestHeader String clubRef) {
        try {

            staffService.addStaff(staffRequestDto,clubRef);
            return ResponseEntity.status(HttpStatus.CREATED).body(staffRequestDto);
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error while creating the staff : " + e.getMessage());
        }
    }

    @DeleteMapping("remove/{objectId}")
    public ResponseEntity<String> deleteStaff(@PathVariable String objectId) {
        try {
            staffService.deleteStaff(objectId);
            return ResponseEntity.ok("Staff successfully deleted: " + objectId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error while deleted the staff : " + e.getMessage());
        }
    }

    @PutMapping("update/{email}")
    public ResponseEntity<?> updateStaff(@PathVariable String email, @RequestBody StaffRequestDto staffRequestDto,@RequestHeader String clubRef) {
        try {

            StaffResponseDto responseDto=staffService.updateStaff(staffRequestDto,email,clubRef);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseDto);
        }  catch (Exception e) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Error while updated the staff : " + e.getMessage());
        }
    }

    @GetMapping("{email}")
    public ResponseEntity<?> getStaffByEmail(@PathVariable String email, @RequestHeader String clubRef) {
        try {
            StaffResponseDto staffResponseDto = staffService.getStaffByEmail(email,clubRef);
            return ResponseEntity.ok(staffResponseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
