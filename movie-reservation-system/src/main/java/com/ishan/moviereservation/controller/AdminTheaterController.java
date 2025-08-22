package com.ishan.moviereservation.controller;

import com.ishan.moviereservation.dto.TheaterRequest;
import com.ishan.moviereservation.dto.TheaterResponse;
import com.ishan.moviereservation.service.TheaterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/theaters")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminTheaterController {

    private final TheaterService theaterService;

    @PostMapping
    public ResponseEntity<TheaterResponse> createTheater(@Valid @RequestBody TheaterRequest request) {
        log.info("Creating new theater: {}", request.getName());
        TheaterResponse response = theaterService.createTheater(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TheaterResponse> updateTheater(@PathVariable Long id, @Valid @RequestBody TheaterRequest request) {
        log.info("Updating theater with id: {}", id);
        TheaterResponse response = theaterService.updateTheater(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheater(@PathVariable Long id) {
        log.info("Deleting theater with id: {}", id);
        theaterService.deleteTheater(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<TheaterResponse>> getAllTheaters(Pageable pageable) {
        log.info("Fetching all theaters with pagination");
        Page<TheaterResponse> theaters = theaterService.getAllTheaters(pageable);
        return ResponseEntity.ok(theaters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheaterResponse> getTheaterById(@PathVariable Long id) {
        log.info("Fetching theater with id: {}", id);
        TheaterResponse theater = theaterService.getTheaterById(id);
        return ResponseEntity.ok(theater);
    }
}
