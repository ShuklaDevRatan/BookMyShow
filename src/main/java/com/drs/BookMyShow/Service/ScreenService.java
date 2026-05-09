package com.drs.BookMyShow.Service;

import com.drs.BookMyShow.Dto.ScreenDto;
import com.drs.BookMyShow.Dto.TheaterDto;
import com.drs.BookMyShow.Exception.ResourcesNotFoundException;
import com.drs.BookMyShow.Model.Screen;
import com.drs.BookMyShow.Model.Theater;
import com.drs.BookMyShow.Repository.ScreenRepository;
import com.drs.BookMyShow.Repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScreenService {

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    public ScreenDto createScreen(ScreenDto screenDto){
        Screen screen = mapToEntity(screenDto);
        Screen savedScreen = screenRepository.save(screen);
        return mapToDto(savedScreen);
    }



    public ScreenDto getScreenById(Long id){
        Screen screen = screenRepository.findById(id)
                .orElseThrow(() ->
                        new ResourcesNotFoundException("Screen not found"));
        return mapToDto(screen);
    }



    public List<ScreenDto> getAllScreens(){

        List<Screen> screens = screenRepository.findAll();

        return screens.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }



    public void deleteScreen(Long id){

        Screen screen = screenRepository.findById(id)
                .orElseThrow(() ->
                        new ResourcesNotFoundException("Screen not found"));

        screenRepository.delete(screen);
    }
    public ScreenDto updateScreen(
            Long id,
            ScreenDto screenDto){

        Screen screen = screenRepository.findById(id)
                .orElseThrow(() ->
                        new ResourcesNotFoundException("Screen not found"));

        Theater theater = theaterRepository.findById(screenDto.getTheater().getId())
                .orElseThrow(() ->
                        new ResourcesNotFoundException("Theater not found"));

        screen.setName(screenDto.getName());
        screen.setTotalSeats(screenDto.getTotalSeats());
        screen.setTheater(theater);

        Screen updatedScreen = screenRepository.save(screen);

        return mapToDto(updatedScreen);
    }

    private ScreenDto mapToDto(Screen screen) {
        ScreenDto screenDto = new ScreenDto();
        screenDto.setId(screen.getId());
        screenDto.setName(screen.getName());
        screenDto.setTotalSeats(screen.getTotalSeats());

        TheaterDto theaterDto = new TheaterDto();

        theaterDto.setId(screen.getTheater().getId());
        theaterDto.setName(screen.getTheater().getName());
        theaterDto.setCity(screen.getTheater().getCity());
        theaterDto.setAddress(screen.getTheater().getAddress());

        screenDto.setTheater(theaterDto);
        return screenDto;
    }

    private Screen mapToEntity(ScreenDto screenDto) {
        Theater theater = theaterRepository.findById(screenDto.getTheater().getId())
                .orElseThrow(() ->
                        new ResourcesNotFoundException("Theater not found"));
        Screen screen = new Screen();
        screen.setName(screenDto.getName());
        screen.setTotalSeats(screenDto.getTotalSeats());
        screen.setTheater(theater);
        return screen;
    }
}
