package com.drs.BookMyShow.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private Data timestamp;
    private int Status;
    private String error;
    private String message;
    private String path;
}
