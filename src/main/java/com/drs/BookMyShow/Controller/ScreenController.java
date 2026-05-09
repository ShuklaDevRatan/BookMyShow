package com.drs.BookMyShow.Controller;

import com.drs.BookMyShow.Dto.ScreenDto;
import com.drs.BookMyShow.Service.ScreenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screens")
public class ScreenController {

    @Autowired
    private ScreenService screenService;

    @PostMapping
    public ResponseEntity<ScreenDto> createScreen(@Valid @RequestBody ScreenDto screenDto){

        ScreenDto createdScreen = screenService.createScreen(screenDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdScreen);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScreenDto> getScreenById(
            @PathVariable Long id){

        ScreenDto screen = screenService.getScreenById(id);

        return ResponseEntity.ok(screen);
    }

    @GetMapping
    public ResponseEntity<List<ScreenDto>> getAllScreens(){

        List<ScreenDto> screens = screenService.getAllScreens();

        return ResponseEntity.ok(screens);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteScreen(
            @PathVariable Long id){

        screenService.deleteScreen(id);

        return ResponseEntity.ok("Screen deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScreenDto> updateScreen(
            @PathVariable Long id,
            @Valid @RequestBody ScreenDto screenDto){

        ScreenDto updatedScreen =
                screenService.updateScreen(id, screenDto);

        return ResponseEntity.ok(updatedScreen);
    }
}
