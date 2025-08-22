package com.ishan.moviereservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScreenRequest {
    
    @NotBlank(message = "Screen name is required")
    @Size(max = 100, message = "Screen name must not exceed 100 characters")
    private String name;
    
    @NotNull(message = "Number of rows is required")
    @Positive(message = "Number of rows must be positive")
    private Integer numberOfRows;
    
    @NotNull(message = "Number of seats per row is required")
    @Positive(message = "Number of seats per row must be positive")
    private Integer seatsPerRow;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}
