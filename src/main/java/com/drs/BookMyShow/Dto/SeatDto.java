package com.drs.BookMyShow.Dto;

import com.drs.BookMyShow.Model.Screen;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatDto {

    private Long id;
    private String seatNumber;
    private String seatType;
    private Double basePrice;
    private ScreenDto screen;
}
