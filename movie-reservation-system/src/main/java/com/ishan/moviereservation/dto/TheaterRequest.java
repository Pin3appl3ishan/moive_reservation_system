package com.ishan.moviereservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheaterRequest {
    
    @NotBlank(message = "Theater name is required")
    @Size(max = 255, message = "Theater name must not exceed 255 characters")
    private String name;
    
    @NotBlank(message = "Address is required")
    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;
    
    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;
    
    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;
    
    @Size(max = 20, message = "Zip code must not exceed 20 characters")
    private String zipCode;
    
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;
}
