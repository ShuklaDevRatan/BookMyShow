package com.drs.BookMyShow.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScreenDto {
    private Long id;
    public String name;
    private Integer totalSeats;
    private TheaterDto theater;

}
