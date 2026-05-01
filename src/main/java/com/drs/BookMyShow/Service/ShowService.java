package com.drs.BookMyShow.Service;

import com.drs.BookMyShow.Dto.*;
import com.drs.BookMyShow.Exception.ResourcesNotFoundException;
import com.drs.BookMyShow.Model.Movie;
import com.drs.BookMyShow.Model.Screen;
import com.drs.BookMyShow.Model.Show;
import com.drs.BookMyShow.Model.ShowSeat;
import com.drs.BookMyShow.Repository.MovieRepository;
import com.drs.BookMyShow.Repository.ScreenRepository;
import com.drs.BookMyShow.Repository.ShowRepository;
import com.drs.BookMyShow.Repository.ShowSeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;


    public ShowDto createShow(ShowDto showDto){
        Show show = new Show();
        Movie movie = movieRepository.findById(showDto.getMovie().getId())
                .orElseThrow(()-> new ResourcesNotFoundException("Movie not found"));

        Screen screen = screenRepository.findById(showDto.getScreen().getId())
                .orElseThrow(()-> new ResourcesNotFoundException("Screen not found"));

        show.setMovie(movie);
        show.setScreen(screen);
        show.setStartTime(showDto.getStartTime());
        show.setEndTime(showDto.getEndTime());

        Show savedShow = showRepository.save(show);
        List<ShowSeat> availableSeats =
                showSeatRepository.findByShowIdAndStatus(savedShow.getId(), "AVAILABLE");

        return mapToDto(savedShow,availableSeats);
    }



    public ShowDto getShowById(Long id){
        Show show = showRepository.findById(id)
                .orElseThrow(()-> new ResourcesNotFoundException("Show not found with id: " + id));
        List<ShowSeat> availableSeats =
                showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");

        return mapToDto(show,availableSeats);
    }




    public List<ShowDto> getAllShows(){
        List<Show> shows = showRepository.findAll();
        return shows.stream()
                .map(show -> {
                List<ShowSeat> availableSeats=showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                return mapToDto(show,availableSeats);
                })
                .collect(Collectors.toList());
    }



    public List<ShowDto> getShowByMovie(Long movieId){
        List<Show> shows = showRepository.findByMovieId(movieId);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats=showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show,availableSeats);
                })
                .collect(Collectors.toList());
    }




    public List<ShowDto> getShowByMovieAndCity(Long movieId, String city){
        List<Show> shows = showRepository.findByMovie_IdAndScreen_Theater_City(movieId, city);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats=showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show,availableSeats);
                })
                .collect(Collectors.toList());
    }




    public List<ShowDto> getShowByDateRange(LocalDateTime startDate, LocalDateTime endDate){
        List<Show> shows = showRepository.findByStartTimeBetween(startDate, endDate);
        return shows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats=showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
                    return mapToDto(show,availableSeats);
                })
                .collect(Collectors.toList());
    }





    public ShowDto mapToDto(Show show , List<ShowSeat>availableSeats){
        ShowDto showDto = new ShowDto();
        showDto.setId(show.getId());
        showDto.setStartTime(show.getStartTime());
        showDto.setEndTime(show.getEndTime());
        showDto.setMovie(new MovieDto(
                show.getMovie().getId(),
                show.getMovie().getTitle(),
                show.getMovie().getDiscription(),
                show.getMovie().getLanguage(),
                show.getMovie().getGenre(),
                show.getMovie().getDurationMins(),
                show.getMovie().getReleaseDate(),
                show.getMovie().getPosterUrl()
        ));

        TheaterDto theaterDto = new TheaterDto(
                show.getScreen().getTheater().getId(),
                show.getScreen().getTheater().getName(),
                show.getScreen().getTheater().getAddress(),
                show.getScreen().getTheater().getCity(),
                show.getScreen().getTheater().getTotalScreen()
        );
        showDto.setScreen(new ScreenDto(
                show.getScreen().getId(),
                show.getScreen().getName(),
                show.getScreen().getTotalSeats(),
                theaterDto
        ));

        List<ShowSeatDto> seatDtos = availableSeats.stream().map(seat->{
            ShowSeatDto seatDto = new ShowSeatDto();
            seatDto.setId(seat.getId());
            seatDto.setStatus(seat.getStatus());
            seatDto.setPrice(seat.getPrice());

            SeatDto baseSeatDto = new SeatDto();
            baseSeatDto.setId(seat.getSeat().getId());
            baseSeatDto.setSeatType(seat.getSeat().getSeatType());
            baseSeatDto.setBasePrice(seat.getSeat().getBasePrice());
            baseSeatDto.setSeatNumber(seat.getSeat().getSeatNumber());
            seatDto.setSeat(baseSeatDto);
            return seatDto;
        }).collect(Collectors.toList());

        showDto.setAvailableSeats(seatDtos);
        return showDto;
    }

}
