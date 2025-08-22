package com.ishan.moviereservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String error;
    private LocalDateTime timestamp = LocalDateTime.now();
}