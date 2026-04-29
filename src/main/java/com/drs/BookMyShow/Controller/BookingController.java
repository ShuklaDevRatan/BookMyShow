package com.drs.BookMyShow.Controller;

import com.drs.BookMyShow.Dto.BookingDto;
import com.drs.BookMyShow.Dto.BookingRequestDto;
import com.drs.BookMyShow.Service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    BookingService bookingService;

    @PostMapping("/")
    public ResponseEntity<BookingDto>createBooking(@Valid @RequestBody BookingRequestDto bookingRequest){
        return new ResponseEntity<>(bookingService.createBooking(bookingRequest), HttpStatus.CREATED);
    }
}
