package com.drs.BookMyShow.Controller;

import com.drs.BookMyShow.Dto.ShowDto;
import com.drs.BookMyShow.Service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    @Autowired
    private ShowService showService;

    @PostMapping
    public ResponseEntity<ShowDto> createShow(
            @RequestBody ShowDto showDto){

        ShowDto createdShow = showService.createShow(showDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdShow);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ShowDto> getShowById(
            @PathVariable Long id){

        return ResponseEntity.ok(showService.getShowById(id));
    }

    @GetMapping
    public ResponseEntity<List<ShowDto>> getAllShows(){

        return ResponseEntity.ok(showService.getAllShows());
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowDto>> getShowsByMovie(
            @PathVariable Long movieId){

        return ResponseEntity.ok(
                showService.getShowByMovie(movieId));
    }

    @GetMapping("/movie/{movieId}/city/{city}")
    public ResponseEntity<List<ShowDto>> getShowsByMovieAndCity(
            @PathVariable Long movieId,
            @PathVariable String city){

        return ResponseEntity.ok(
                showService.getShowByMovieAndCity(movieId, city));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<ShowDto>> getShowsByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate){

        return ResponseEntity.ok(
                showService.getShowByDateRange(startDate, endDate));
    }
}
