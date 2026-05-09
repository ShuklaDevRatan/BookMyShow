package com.drs.BookMyShow.Controller;

import com.drs.BookMyShow.Dto.TheaterDto;
import com.drs.BookMyShow.Service.TheaterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theaters")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    @PostMapping
    public ResponseEntity<TheaterDto> addTheater( @Valid @RequestBody TheaterDto theaterDto){

        TheaterDto theater = theaterService.createTheater(theaterDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(theater);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheaterDto> getTheaterById(@PathVariable Long id){

        TheaterDto theater = theaterService.getTheaterById(id);

        return ResponseEntity.ok(theater);
    }

    @GetMapping
    public ResponseEntity<List<TheaterDto>> getAllTheaters(){

        List<TheaterDto> theaters = theaterService.getAllTheaters();

        return ResponseEntity.ok(theaters);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTheater(@PathVariable Long id){

        theaterService.deleteTheater(id);

        return ResponseEntity.ok("Theater deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<TheaterDto> updateTheater(
            @PathVariable Long id,
            @Valid @RequestBody TheaterDto theaterDto){

        TheaterDto updatedTheater =
                theaterService.updateTheater(id, theaterDto);

        return ResponseEntity.ok(updatedTheater);
    }
}
