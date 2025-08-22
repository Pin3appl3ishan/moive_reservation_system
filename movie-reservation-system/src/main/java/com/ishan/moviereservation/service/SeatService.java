package com.ishan.moviereservation.service;

import com.ishan.moviereservation.entity.Seat;
import com.ishan.moviereservation.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SeatService {

    private final SeatRepository seatRepository;

    /**
     * Create a new seat
     */
    public Seat createSeat(Seat seat) {
        log.info("Creating new seat with label: {} for screen: {}", seat.getLabel(), seat.getScreen().getName());
        
        // Validate screen is not null
        if (seat.getScreen() == null || seat.getScreen().getId() == null) {
            throw new RuntimeException("Seat must be associated with a screen");
        }
        
        // Validate label is not empty
        if (seat.getLabel() == null || seat.getLabel().trim().isEmpty()) {
            throw new RuntimeException("Seat label cannot be empty");
        }
        
        // Check if seat with same label already exists in the same screen
        List<Seat> existingSeats = seatRepository.findByScreenIdAndLabelContainingIgnoreCase(
            seat.getScreen().getId(), seat.getLabel());
        if (!existingSeats.isEmpty()) {
            throw new RuntimeException("Seat with label already exists in screen: " + seat.getLabel());
        }
        
        // Validate column number is positive if provided
        if (seat.getCol() != null && seat.getCol() <= 0) {
            throw new RuntimeException("Seat column number must be positive");
        }
        
        Seat savedSeat = seatRepository.save(seat);
        log.info("Seat created successfully with ID: {}", savedSeat.getId());
        return savedSeat;
    }

    /**
     * Find seat by ID
     */
    @Transactional(readOnly = true)
    public Optional<Seat> findById(Long id) {
        log.debug("Finding seat by ID: {}", id);
        return seatRepository.findById(id);
    }

    /**
     * Find seat by label (exact match)
     */
    @Transactional(readOnly = true)
    public Optional<Seat> findByLabel(String label) {
        log.debug("Finding seat by label: {}", label);
        return seatRepository.findByLabel(label);
    }

    /**
     * Find seat by ID with seat reservations
     */
    @Transactional(readOnly = true)
    public Optional<Seat> findByIdWithSeatReservations(Long id) {
        log.debug("Finding seat by ID with seat reservations: {}", id);
        return seatRepository.findByIdWithSeatReservations(id);
    }

    /**
     * Get all seats
     */
    @Transactional(readOnly = true)
    public List<Seat> findAll() {
        log.debug("Finding all seats");
        return seatRepository.findAll();
    }

    /**
     * Find seats by screen ID
     */
    @Transactional(readOnly = true)
    public List<Seat> findByScreenId(Long screenId) {
        log.debug("Finding seats by screen ID: {}", screenId);
        return seatRepository.findByScreenId(screenId);
    }

    /**
     * Find seats by screen ID ordered by row and column
     */
    @Transactional(readOnly = true)
    public List<Seat> findByScreenIdOrdered(Long screenId) {
        log.debug("Finding seats by screen ID ordered: {}", screenId);
        return seatRepository.findByScreenIdOrdered(screenId);
    }

    /**
     * Find seats by screen ID and row label
     */
    @Transactional(readOnly = true)
    public List<Seat> findByScreenIdAndRowLabel(Long screenId, String rowLabel) {
        log.debug("Finding seats by screen ID: {} and row label: {}", screenId, rowLabel);
        return seatRepository.findByScreenIdAndRowLabel(screenId, rowLabel);
    }

    /**
     * Find seats by screen ID and row label ordered
     */
    @Transactional(readOnly = true)
    public List<Seat> findByScreenIdAndRowLabelOrdered(Long screenId, String rowLabel) {
        log.debug("Finding seats by screen ID: {} and row label ordered: {}", screenId, rowLabel);
        return seatRepository.findByScreenIdAndRowLabelOrdered(screenId, rowLabel);
    }

    /**
     * Search seats by label
     */
    @Transactional(readOnly = true)
    public List<Seat> searchByLabel(String label) {
        log.debug("Searching seats by label: {}", label);
        return seatRepository.findByLabelContainingIgnoreCase(label);
    }

    /**
     * Find seats by row label
     */
    @Transactional(readOnly = true)
    public List<Seat> findByRowLabel(String rowLabel) {
        log.debug("Finding seats by row label: {}", rowLabel);
        return seatRepository.findByRowLabel(rowLabel);
    }

    /**
     * Search seats by row label
     */
    @Transactional(readOnly = true)
    public List<Seat> searchByRowLabel(String rowLabel) {
        log.debug("Searching seats by row label: {}", rowLabel);
        return seatRepository.findByRowLabelContainingIgnoreCase(rowLabel);
    }

    /**
     * Find seats by column number
     */
    @Transactional(readOnly = true)
    public List<Seat> findByCol(Integer col) {
        log.debug("Finding seats by column: {}", col);
        return seatRepository.findByCol(col);
    }

    /**
     * Find seats by column range
     */
    @Transactional(readOnly = true)
    public List<Seat> findByColBetween(Integer minCol, Integer maxCol) {
        log.debug("Finding seats by column range: {} to {}", minCol, maxCol);
        return seatRepository.findByColBetween(minCol, maxCol);
    }

    /**
     * Find seats by screen ID and column
     */
    @Transactional(readOnly = true)
    public List<Seat> findByScreenIdAndCol(Long screenId, Integer col) {
        log.debug("Finding seats by screen ID: {} and column: {}", screenId, col);
        return seatRepository.findByScreenIdAndCol(screenId, col);
    }

    /**
     * Find seats by screen ID, row label, and column
     */
    @Transactional(readOnly = true)
    public List<Seat> findByScreenIdAndRowLabelAndCol(Long screenId, String rowLabel, Integer col) {
        log.debug("Finding seats by screen ID: {}, row: {}, column: {}", screenId, rowLabel, col);
        return seatRepository.findByScreenIdAndRowLabelAndCol(screenId, rowLabel, col);
    }

    /**
     * Find seats that have reservations
     */
    @Transactional(readOnly = true)
    public List<Seat> findSeatsWithReservations() {
        log.debug("Finding seats with reservations");
        return seatRepository.findSeatsWithReservations();
    }

    /**
     * Find seats with no reservations
     */
    @Transactional(readOnly = true)
    public List<Seat> findSeatsWithoutReservations() {
        log.debug("Finding seats without reservations");
        return seatRepository.findSeatsWithoutReservations();
    }

    /**
     * Get seats with reservation count
     */
    @Transactional(readOnly = true)
    public List<Object[]> findSeatsWithReservationCount() {
        log.debug("Finding seats with reservation count");
        return seatRepository.findSeatsWithReservationCount();
    }

    /**
     * Find available seats for a specific showtime
     */
    @Transactional(readOnly = true)
    public List<Seat> findAvailableSeatsForShowtime(Long screenId, Long showtimeId) {
        log.debug("Finding available seats for showtime: {} in screen: {}", showtimeId, screenId);
        return seatRepository.findAvailableSeatsForShowtime(screenId, showtimeId);
    }

    /**
     * Check if seat is available for a showtime
     */
    @Transactional(readOnly = true)
    public boolean isSeatAvailableForShowtime(Long seatId, Long showtimeId) {
        log.debug("Checking if seat: {} is available for showtime: {}", seatId, showtimeId);
        Optional<Seat> seat = seatRepository.findById(seatId);
        if (seat.isEmpty()) {
            return false;
        }
        
        // Check if seat is in the available seats list for this showtime
        List<Seat> availableSeats = seatRepository.findAvailableSeatsForShowtime(
            seat.get().getScreen().getId(), showtimeId);
            
        return availableSeats.stream().anyMatch(s -> s.getId().equals(seatId));
    }

    /**
     * Update seat
     */
    public Seat updateSeat(Seat seat) {
        log.info("Updating seat with ID: {}", seat.getId());
        
        if (!seatRepository.existsById(seat.getId())) {
            throw new RuntimeException("Seat not found with ID: " + seat.getId());
        }
        
        // Validate screen is not null
        if (seat.getScreen() == null || seat.getScreen().getId() == null) {
            throw new RuntimeException("Seat must be associated with a screen");
        }
        
        // Validate label is not empty
        if (seat.getLabel() == null || seat.getLabel().trim().isEmpty()) {
            throw new RuntimeException("Seat label cannot be empty");
        }
        
        // Check if label is being changed and if new label already exists in the same screen
        Optional<Seat> existingSeat = seatRepository.findById(seat.getId());
        if (existingSeat.isPresent() && !existingSeat.get().getLabel().equals(seat.getLabel())) {
            List<Seat> existingSeats = seatRepository.findByScreenIdAndLabelContainingIgnoreCase(
                seat.getScreen().getId(), seat.getLabel());
            if (!existingSeats.isEmpty()) {
                throw new RuntimeException("Seat with label already exists in screen: " + seat.getLabel());
            }
        }
        
        // Validate column number is positive if provided
        if (seat.getCol() != null && seat.getCol() <= 0) {
            throw new RuntimeException("Seat column number must be positive");
        }
        
        Seat updatedSeat = seatRepository.save(seat);
        log.info("Seat updated successfully with ID: {}", updatedSeat.getId());
        return updatedSeat;
    }

    /**
     * Delete seat by ID
     */
    public void deleteById(Long id) {
        log.info("Deleting seat with ID: {}", id);
        
        if (!seatRepository.existsById(id)) {
            throw new RuntimeException("Seat not found with ID: " + id);
        }
        
        // Check if seat has reservations (business rule: can't delete seat with reservations)
        Optional<Seat> seat = seatRepository.findByIdWithSeatReservations(id);
        if (seat.isPresent() && !seat.get().getSeatReservations().isEmpty()) {
            throw new RuntimeException("Cannot delete seat with reservations. Seat ID: " + id);
        }
        
        seatRepository.deleteById(id);
        log.info("Seat deleted successfully with ID: {}", id);
    }

    /**
     * Get seat count
     */
    @Transactional(readOnly = true)
    public long count() {
        log.debug("Getting total seat count");
        return seatRepository.count();
    }

    /**
     * Get seat count by screen
     */
    @Transactional(readOnly = true)
    public long countByScreen(Long screenId) {
        log.debug("Getting seat count by screen ID: {}", screenId);
        return seatRepository.findByScreenId(screenId).size();
    }

    /**
     * Get seat count by row
     */
    @Transactional(readOnly = true)
    public long countByRow(String rowLabel) {
        log.debug("Getting seat count by row: {}", rowLabel);
        return seatRepository.findByRowLabel(rowLabel).size();
    }

    /**
     * Get seat count with reservations
     */
    @Transactional(readOnly = true)
    public long countWithReservations() {
        log.debug("Getting seat count with reservations");
        return seatRepository.findSeatsWithReservations().size();
    }

    /**
     * Get seat count without reservations
     */
    @Transactional(readOnly = true)
    public long countWithoutReservations() {
        log.debug("Getting seat count without reservations");
        return seatRepository.findSeatsWithoutReservations().size();
    }

    /**
     * Get available seat count for a showtime
     */
    @Transactional(readOnly = true)
    public long countAvailableSeatsForShowtime(Long screenId, Long showtimeId) {
        log.debug("Getting available seat count for showtime: {} in screen: {}", showtimeId, screenId);
        return seatRepository.findAvailableSeatsForShowtime(screenId, showtimeId).size();
    }

    /**
     * Find seats by screen ordered by row label and column
     */
    @Transactional(readOnly = true)
    public List<Seat> findByScreenIdOrderByRowLabelAndCol(Long screenId) {
        log.debug("Finding seats by screen ID ordered by row and column: {}", screenId);
        return seatRepository.findByScreenIdOrderByRowLabelAscColAsc(screenId);
    }

    /**
     * Find seats by screen ordered by label
     */
    @Transactional(readOnly = true)
    public List<Seat> findByScreenIdOrderByLabel(Long screenId) {
        log.debug("Finding seats by screen ID ordered by label: {}", screenId);
        return seatRepository.findByScreenIdOrderByLabelAsc(screenId);
    }

    /**
     * Get seat layout for a screen (organized by rows)
     */
    @Transactional(readOnly = true)
    public List<Seat> getSeatLayoutForScreen(Long screenId) {
        log.debug("Getting seat layout for screen: {}", screenId);
        return seatRepository.findByScreenIdOrdered(screenId);
    }
}

