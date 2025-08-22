package com.ishan.moviereservation.controller;

import com.ishan.moviereservation.dto.ScreenRequest;
import com.ishan.moviereservation.dto.ScreenResponse;
import com.ishan.moviereservation.service.ScreenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/screens")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminScreenController {

    private final ScreenService screenService;

    @PostMapping("/theater/{theaterId}")
    public ResponseEntity<ScreenResponse> createScreen(@PathVariable Long theaterId, @Valid @RequestBody ScreenRequest request) {
        log.info("Creating new screen for theater: {}", theaterId);
        ScreenResponse response = screenService.createScreen(theaterId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScreenResponse> updateScreen(@PathVariable Long id, @Valid @RequestBody ScreenRequest request) {
        log.info("Updating screen with id: {}", id);
        ScreenResponse response = screenService.updateScreen(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScreen(@PathVariable Long id) {
        log.info("Deleting screen with id: {}", id);
        screenService.deleteScreen(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ScreenResponse>> getAllScreens(Pageable pageable) {
        log.info("Fetching all screens with pagination");
        Page<ScreenResponse> screens = screenService.getAllScreens(pageable);
        return ResponseEntity.ok(screens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScreenResponse> getScreenById(@PathVariable Long id) {
        log.info("Fetching screen with id: {}", id);
        ScreenResponse screen = screenService.getScreenById(id);
        return ResponseEntity.ok(screen);
    }

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<ScreenResponse>> getScreensByTheater(@PathVariable Long theaterId) {
        log.info("Fetching screens for theater: {}", theaterId);
        List<ScreenResponse> screens = screenService.getScreensByTheater(theaterId);
        return ResponseEntity.ok(screens);
    }
}
