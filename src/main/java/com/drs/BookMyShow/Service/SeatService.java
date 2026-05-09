package com.drs.BookMyShow.Service;

import com.drs.BookMyShow.Dto.ScreenDto;
import com.drs.BookMyShow.Dto.SeatDto;
import com.drs.BookMyShow.Exception.ResourcesNotFoundException;
import com.drs.BookMyShow.Model.Screen;
import com.drs.BookMyShow.Model.Seat;
import com.drs.BookMyShow.Repository.ScreenRepository;
import com.drs.BookMyShow.Repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ScreenRepository screenRepository;

    public SeatDto createSeat(SeatDto seatDto){

        Screen screen = screenRepository.findById(
                        seatDto.getScreen().getId())
                .orElseThrow(() ->
                        new ResourcesNotFoundException("Screen not found"));

        Seat seat = new Seat();

        seat.setSeatNumber(seatDto.getSeatNumber());
        seat.setSeatType(seatDto.getSeatType());
        seat.setBasePrice(seatDto.getBasePrice());
        seat.setScreen(screen);

        Seat savedSeat = seatRepository.save(seat);

        return mapToDto(savedSeat);
    }

    public SeatDto getSeatById(Long id){

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() ->
                        new ResourcesNotFoundException("Seat not found"));

        return mapToDto(seat);
    }

    public List<SeatDto> getAllSeats(){

        List<Seat> seats = seatRepository.findAll();

        return seats.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<SeatDto> getSeatsByScreenId(Long screenId){

        List<Seat> seats = seatRepository.findByScreenId(screenId);

        return seats.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void deleteSeat(Long id){

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() ->
                        new ResourcesNotFoundException("Seat not found"));

        seatRepository.delete(seat);
    }

    public SeatDto updateSeat(Long id, SeatDto seatDto){

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() ->
                        new ResourcesNotFoundException("Seat not found"));

        Screen screen = screenRepository.findById(
                        seatDto.getScreen().getId())
                .orElseThrow(() ->
                        new ResourcesNotFoundException("Screen not found"));

        seat.setSeatNumber(seatDto.getSeatNumber());
        seat.setSeatType(seatDto.getSeatType());
        seat.setBasePrice(seatDto.getBasePrice());
        seat.setScreen(screen);

        Seat updatedSeat = seatRepository.save(seat);

        return mapToDto(updatedSeat);
    }
    private SeatDto mapToDto(Seat seat){

        SeatDto seatDto = new SeatDto();

        seatDto.setId(seat.getId());
        seatDto.setSeatNumber(seat.getSeatNumber());
        seatDto.setSeatType(seat.getSeatType());
        seatDto.setBasePrice(seat.getBasePrice());

        ScreenDto screenDto = new ScreenDto();

        screenDto.setId(seat.getScreen().getId());
        screenDto.setName(seat.getScreen().getName());

        seatDto.setScreen(screenDto);

        return seatDto;
    }

}
