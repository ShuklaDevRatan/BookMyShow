package com.drs.BookMyShow.Service;

import com.drs.BookMyShow.Dto.*;
import com.drs.BookMyShow.Exception.ResourcesNotFoundException;
import com.drs.BookMyShow.Exception.SeatUnavailableException;
import com.drs.BookMyShow.Model.*;
import com.drs.BookMyShow.Repository.BookingRepository;
import com.drs.BookMyShow.Repository.ShowRepository;
import com.drs.BookMyShow.Repository.ShowSeatRepository;
import com.drs.BookMyShow.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private ShowSeatRepository showSeatRepository;
    @Autowired
    private BookingRepository bookingRepository;




    @Transactional
    public BookingDto createBooking(BookingRequestDto bookingRequest){

        User user = userRepository.findById(bookingRequest.getUserId())
                .orElseThrow(()->new ResourcesNotFoundException("User Not Found"));

        Show show = showRepository.findById(bookingRequest.getShowId())
                .orElseThrow(()->new ResourcesNotFoundException("Show Not Found"));

        List<ShowSeat>selectedSeats = showSeatRepository.findAllById(bookingRequest.getSeatIds());

        for(ShowSeat seat : selectedSeats){
            if(!"AVAILABLE".equals(seat.getStatus())){
                throw new SeatUnavailableException("Seat " + seat.getSeat().getSeatNumber() + " is not Available");
            }
            seat.setStatus("LOCKED");
        }
        showSeatRepository.saveAll(selectedSeats);
        Double totalAmount = selectedSeats.stream()
                .mapToDouble(ShowSeat::getPrice)
                .sum();

        // Payment
        Payment payment = new Payment();
        payment.setAmount(totalAmount);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setPaymentMethod(bookingRequest.getPaymentMethod());
        payment.setStatus("SUCCESS");
        payment.setTransactionId(UUID.randomUUID().toString());

        //Booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setBookkingTime(LocalDateTime.now());
        booking.setStatus("CONFIRMED");
        booking.setTotelAmount(totalAmount);
        booking.setBookingNumber(UUID.randomUUID().toString());
        booking.setPayment(payment);

        Booking saveBooking = bookingRepository.save(booking);
        selectedSeats.forEach(seat ->{
            seat.setStatus("BOOKED");
            seat.setBooking(saveBooking);
        });
        showSeatRepository.saveAll(selectedSeats);
        return mapToBookingDto(saveBooking,selectedSeats);
    }



    public BookingDto getBookingById(Long id){
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()->new ResourcesNotFoundException("Booking Not Found"));
        List<ShowSeat> seats=showSeatRepository.findAll().
                stream().
                filter(seat->seat.getBooking() != null && seat.getBooking().getId().equals(booking.getId()))
                .collect(Collectors.toList());

        return mapToBookingDto(booking,seats);

    }



    public BookingDto getBookingByNumber(String bookingNumber){
        Booking booking = bookingRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(()->new ResourcesNotFoundException("Booking Not Found"));
        List<ShowSeat> seats=showSeatRepository.findAll().
                stream().
                filter(seat->seat.getBooking() != null && seat.getBooking().getId().equals(booking.getId()))
                .collect(Collectors.toList());

        return mapToBookingDto(booking,seats);
    }



    public List<BookingDto> getBookingByUserId(Long userId){
      List<Booking> bookings = bookingRepository.findByUserId(userId);
      return bookings.stream()
              .map(booking -> {
                  List<ShowSeat> seats = showSeatRepository.findAll()
                          .stream()
                          .filter(seat->seat.getBooking() != null && seat.getBooking().getId().equals(booking.getId()))
                          .collect(Collectors.toList());

                  return mapToBookingDto(booking , seats);
              })
              .collect(Collectors.toList());
    }



    @Transactional
    public BookingDto cancelBooking(Long id){
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()-> new ResourcesNotFoundException("Booking Not Found"));

        booking.setStatus("CANCELLED");
        List<ShowSeat> seats = showSeatRepository.findAll()
                .stream()
                .filter(seat->seat.getBooking() != null && seat.getBooking().getId().equals(booking.getId()))
                .collect(Collectors.toList());

        seats.forEach(seat->{
            seat.setStatus("AVAILABLE");
            seat.setBooking(null);
        });

        if (booking.getPayment()!=null){
            booking.getPayment().setStatus("REFUNDED");
        }
        Booking updateBooking= bookingRepository.save(booking);
        showSeatRepository.saveAll(seats);

        return mapToBookingDto(updateBooking , seats);
    }



    private BookingDto mapToBookingDto(Booking booking , List<ShowSeat>seats) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setBookingNumber(booking.getBookingNumber());
        bookingDto.setBookingTime(booking.getBookkingTime());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setTotalAmount(booking.getTotelAmount());

        //user
        UserDto userDto = new UserDto();
        userDto.setId(booking.getUser().getId());
        userDto.setName(booking.getUser().getName());
        userDto.setEmail(booking.getUser().getEmail());
        userDto.setPhoneNumber(booking.getUser().getPhoneNumber());
        bookingDto.setUser(userDto);

        //show
        ShowDto showDto = new ShowDto();
        showDto.setId(booking.getShow().getId());
        showDto.setStartTime(booking.getShow().getStartTime());
        showDto.setEndTime(booking.getShow().getEndTime());


        //movie
        MovieDto movieDto = new MovieDto();
        movieDto.setId(booking.getShow().getMovie().getId());
        movieDto.setTitle(booking.getShow().getMovie().getTitle());
        movieDto.setDiscription(booking.getShow().getMovie().getDiscription());
        movieDto.setGenre(booking.getShow().getMovie().getGenre());
        movieDto.setLangague(booking.getShow().getMovie().getLanguage());
        movieDto.setDurationMins(booking.getShow().getMovie().getDurationMins());
        movieDto.setReleaseDate(booking.getShow().getMovie().getReleaseDate());
        movieDto.setPosterUrl(booking.getShow().getMovie().getPosterUrl());
        showDto.setMovie(movieDto);

        //screen
        ScreenDto screenDto = new ScreenDto();
        screenDto.setId(booking.getShow().getScreen().getId());
        screenDto.setName(booking.getShow().getScreen().getName());
        screenDto.setTotalSeats(booking.getShow().getScreen().getTotalSeats());

        //theater
        TheaterDto theaterDto = new TheaterDto();
        theaterDto.setId(booking.getShow().getScreen().getTheater().getId());
        theaterDto.setName(booking.getShow().getScreen().getTheater().getName());
        theaterDto.setCity(booking.getShow().getScreen().getTheater().getCity());
        theaterDto.setAddress(booking.getShow().getScreen().getTheater().getAddress());
        theaterDto.setTotalScreen(booking.getShow().getScreen().getTheater().getTotalScreen());

        screenDto.setTheater(theaterDto);
        showDto.setScreen(screenDto);
        bookingDto.setShow(showDto);

        //seat
        List<ShowSeatDto> seatDto = seats.stream()
                .map(seat ->{
                    ShowSeatDto seatDtos = new ShowSeatDto();
                    seatDtos.setId(seat.getId());
                    seatDtos.setStatus(seat.getStatus());
                    seatDtos.setPrice(seat.getPrice());

                    SeatDto baseSeatDto = new SeatDto();
                    baseSeatDto.setId(seat.getSeat().getId());
                    baseSeatDto.setBasePrice(seat.getSeat().getBasePrice());
                    baseSeatDto.setSeatType(seat.getSeat().getSeatType());
                    baseSeatDto.setSeatNumber(seat.getSeat().getSeatNumber());
                    seatDtos.setSeat(baseSeatDto);
                    return seatDtos;
                })
                .collect(Collectors.toList());
        bookingDto.setSeats(seatDto);

        //payment
        if(booking.getPayment() != null){
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setId(booking.getPayment().getId());
            paymentDto.setPaymentMethod(booking.getPayment().getPaymentMethod());
            paymentDto.setPaymentTime(booking.getPayment().getPaymentTime());
            paymentDto.setStatus(booking.getPayment().getStatus());
            paymentDto.setAmount(booking.getPayment().getAmount());
            paymentDto.setTransactionId(booking.getPayment().getTransactionId());
            bookingDto.setPayment(paymentDto);
        }

        return bookingDto;
    }
}
