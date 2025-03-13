package com.eduAcademy.management_system.controller;

import com.eduAcademy.management_system.dto.ClubResponseDto;
import com.eduAcademy.management_system.dto.SportRequest;
import com.eduAcademy.management_system.dto.StadiumDto;
import com.eduAcademy.management_system.dto.StadiumResponse;
import com.eduAcademy.management_system.exception.NotFoundException;
import com.eduAcademy.management_system.service.StadiumService;
import com.eduAcademy.management_system.service.serviceImpl.StadiumImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internal/stadium")
@RequiredArgsConstructor
public class StadiumController {

    private final StadiumService stadiumService;

    @PostMapping("new")
    public ResponseEntity<?> newStadium(@RequestBody StadiumDto stadiumDto, @RequestHeader String clubRef) {
        try {
            StadiumResponse stadium = stadiumService.addStadium(stadiumDto, clubRef);
            return ResponseEntity.status(HttpStatus.CREATED).body(stadium);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("{clubRef}/stadiums")
    public ResponseEntity<?> getStadiumBySport(@PathVariable String clubRef, @RequestBody SportRequest typeSport){
        try {
            List<StadiumResponse> stadiums = stadiumService.getStadiumsBySportAndClub(clubRef, typeSport.getTypeSport());
            return ResponseEntity.ok(stadiums);
        }catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
}
