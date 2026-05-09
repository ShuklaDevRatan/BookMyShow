package com.drs.BookMyShow.Controller;

import com.drs.BookMyShow.Dto.SeatDto;
import com.drs.BookMyShow.Service.SeatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @PostMapping
    public ResponseEntity<SeatDto> createSeat(
            @Valid @RequestBody SeatDto seatDto){

        SeatDto createdSeat = seatService.createSeat(seatDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdSeat);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatDto> getSeatById(
            @PathVariable Long id){

        SeatDto seat = seatService.getSeatById(id);

        return ResponseEntity.ok(seat);
    }

    @GetMapping
    public ResponseEntity<List<SeatDto>> getAllSeats(){

        List<SeatDto> seats = seatService.getAllSeats();

        return ResponseEntity.ok(seats);
    }

    @GetMapping("/screen/{screenId}")
    public ResponseEntity<List<SeatDto>> getSeatsByScreenId(
            @PathVariable Long screenId){

        List<SeatDto> seats =
                seatService.getSeatsByScreenId(screenId);

        return ResponseEntity.ok(seats);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatDto> updateSeat(
            @PathVariable Long id,
            @Valid @RequestBody SeatDto seatDto){

        SeatDto updatedSeat =
                seatService.updateSeat(id, seatDto);

        return ResponseEntity.ok(updatedSeat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSeat(
            @PathVariable Long id){

        seatService.deleteSeat(id);

        return ResponseEntity.ok("Seat deleted successfully");
    }
}
