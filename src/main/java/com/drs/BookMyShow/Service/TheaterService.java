package com.drs.BookMyShow.Service;

import com.drs.BookMyShow.Dto.TheaterDto;
import com.drs.BookMyShow.Exception.ResourcesNotFoundException;
import com.drs.BookMyShow.Model.Theater;
import com.drs.BookMyShow.Repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheaterService {

    @Autowired
    private TheaterRepository theaterRepository;

    public TheaterDto createTheater(TheaterDto theaterDto){
        Theater theater = mapToEntity(theaterDto);
        Theater savedTheater = theaterRepository.save(theater);
        return mapToDto(savedTheater);


    }

    private TheaterDto mapToDto(Theater theater) {
        TheaterDto theaterDto = new TheaterDto();
        theaterDto.setId(theater.getId());
        theaterDto.setName(theater.getName());
        theaterDto.setCity(theater.getCity());
        theaterDto.setAddress(theater.getAddress());
        theaterDto.setTotalScreen(theater.getTotalScreen());
        return theaterDto;
    }

    public TheaterDto getTheaterById(Long id){
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(()->new ResourcesNotFoundException("Theater not found with id :" + id));
        return mapToDto(theater);
    }

    public List<TheaterDto> getAllTheaters(){
        List<Theater>theaters = theaterRepository.findAll();
        return theaters.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<TheaterDto> getAllTheaterByCity(String city){
        List<Theater>theaters = theaterRepository.findByCity(city);
        return theaters.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public TheaterDto updateTheater(Long id , TheaterDto theaterDto){
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(()->new ResourcesNotFoundException("Theater not found with id :" + id));
        theater.setName(theaterDto.getName());
        theater.setCity(theaterDto.getCity());
        theater.setAddress(theaterDto.getAddress());
        theater.setTotalScreen(theaterDto.getTotalScreen());
        Theater updateTheater = theaterRepository.save(theater);
        return mapToDto(updateTheater);
    }

    public void deleteTheater(Long id){
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(()->new ResourcesNotFoundException("Theater not found with id :" + id));
        theaterRepository.delete(theater);
    }

    private Theater mapToEntity(TheaterDto theaterDto) {
        Theater theater = new Theater();
        theater.setName(theaterDto.getName());
        theater.setCity(theaterDto.getCity());
        theater.setAddress(theaterDto.getAddress());
        theater.setTotalScreen(theaterDto.getTotalScreen());
        return theater;
    }

}
