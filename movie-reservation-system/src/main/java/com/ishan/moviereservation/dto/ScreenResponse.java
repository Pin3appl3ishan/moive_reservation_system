package com.ishan.moviereservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScreenResponse {
    private Long id;
    private String name;
    private Integer numberOfRows;
    private Integer seatsPerRow;
    private Integer totalSeats;
    private String description;
    private Long theaterId;
    private String theaterName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
