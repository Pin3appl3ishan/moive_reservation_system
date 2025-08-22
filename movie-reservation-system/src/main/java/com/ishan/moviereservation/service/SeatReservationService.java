package com.ishan.moviereservation.service;

import com.ishan.moviereservation.entity.SeatReservation;
import com.ishan.moviereservation.repository.SeatReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SeatReservationService {

    private final SeatReservationRepository seatReservationRepository;

    /**
     * Create a new seat reservation
     */
    public SeatReservation createSeatReservation(SeatReservation seatReservation) {
        log.info("Creating new seat reservation for seat: {} and showtime: {}", 
            seatReservation.getSeat().getLabel(), seatReservation.getShowtime().getId());
        
        // Validate seat is not null
        if (seatReservation.getSeat() == null || seatReservation.getSeat().getId() == null) {
            throw new RuntimeException("Seat reservation must be associated with a seat");
        }
        
        // Validate showtime is not null
        if (seatReservation.getShowtime() == null || seatReservation.getShowtime().getId() == null) {
            throw new RuntimeException("Seat reservation must be associated with a showtime");
        }
        
        // Validate reservation is not null
        if (seatReservation.getReservation() == null || seatReservation.getReservation().getId() == null) {
            throw new RuntimeException("Seat reservation must be associated with a reservation");
        }
        
        // Validate seat and showtime are from the same screen
        if (!seatReservation.getSeat().getScreen().getId().equals(seatReservation.getShowtime().getScreen().getId())) {
            throw new RuntimeException("Seat and showtime must be from the same screen");
        }
        
        // Check if seat is already reserved for this showtime
        List<SeatReservation> existingReservations = seatReservationRepository
            .findBySeatIdAndShowtimeId(seatReservation.getSeat().getId(), seatReservation.getShowtime().getId());
        if (!existingReservations.isEmpty()) {
            throw new RuntimeException("Seat is already reserved for this showtime");
        }
        
        // Set initial status
        seatReservation.setStatus("RESERVED");
        
        SeatReservation savedSeatReservation = seatReservationRepository.save(seatReservation);
        log.info("Seat reservation created successfully with ID: {}", savedSeatReservation.getId());
        return savedSeatReservation;
    }

    /**
     * Find seat reservation by ID
     */
    @Transactional(readOnly = true)
    public Optional<SeatReservation> findById(Long id) {
        log.debug("Finding seat reservation by ID: {}", id);
        return seatReservationRepository.findById(id);
    }



    /**
     * Get all seat reservations
     */
    @Transactional(readOnly = true)
    public List<SeatReservation> findAll() {
        log.debug("Finding all seat reservations");
        return seatReservationRepository.findAll();
    }

    /**
     * Find seat reservations by seat ID
     */
    @Transactional(readOnly = true)
    public List<SeatReservation> findBySeatId(Long seatId) {
        log.debug("Finding seat reservations by seat ID: {}", seatId);
        return seatReservationRepository.findBySeatId(seatId);
    }

    /**
     * Find seat reservations by showtime ID
     */
    @Transactional(readOnly = true)
    public List<SeatReservation> findByShowtimeId(Long showtimeId) {
        log.debug("Finding seat reservations by showtime ID: {}", showtimeId);
        return seatReservationRepository.findByShowtimeId(showtimeId);
    }

    /**
     * Find seat reservations by reservation ID
     */
    @Transactional(readOnly = true)
    public List<SeatReservation> findByReservationId(Long reservationId) {
        log.debug("Finding seat reservations by reservation ID: {}", reservationId);
        return seatReservationRepository.findByReservationId(reservationId);
    }

    /**
     * Find seat reservations by status
     */
    @Transactional(readOnly = true)
    public List<SeatReservation> findByStatus(String status) {
        log.debug("Finding seat reservations by status: {}", status);
        return seatReservationRepository.findByStatus(status);
    }

    /**
     * Find seat reservations by seat and showtime
     */
    @Transactional(readOnly = true)
    public List<SeatReservation> findBySeatIdAndShowtimeId(Long seatId, Long showtimeId) {
        log.debug("Finding seat reservations by seat ID: {} and showtime ID: {}", seatId, showtimeId);
        return seatReservationRepository.findBySeatIdAndShowtimeId(seatId, showtimeId);
    }

    /**
     * Find seat reservations by seat and status
     */
    @Transactional(readOnly = true)
    public List<SeatReservation> findBySeatIdAndStatus(Long seatId, String status) {
        log.debug("Finding seat reservations by seat ID: {} and status: {}", seatId, status);
        return seatReservationRepository.findBySeatIdAndStatus(seatId, status);
    }

    /**
     * Find seat reservations by showtime and status
     */
    @Transactional(readOnly = true)
    public List<SeatReservation> findByShowtimeIdAndStatus(Long showtimeId, String status) {
        log.debug("Finding seat reservations by showtime ID: {} and status: {}", showtimeId, status);
        return seatReservationRepository.findByShowtimeIdAndStatus(showtimeId, status);
    }

    /**
     * Find seat reservations by reservation and status
     */
    @Transactional(readOnly = true)
    public List<SeatReservation> findByReservationIdAndStatus(Long reservationId, String status) {
        log.debug("Finding seat reservations by reservation ID: {} and status: {}", reservationId, status);
        return seatReservationRepository.findByReservationIdAndStatus(reservationId, status);
    }

    /**
     * Find active seat reservations
     */
    @Transactional(readOnly = true)
    public List<SeatReservation> findActiveSeatReservations() {
        log.debug("Finding active seat reservations");
        return seatReservationRepository.findActiveSeatReservations();
    }



    /**
     * Find cancelled seat reservations
     */
    @Transactional(readOnly = true)
    public List<SeatReservation> findCancelledSeatReservations() {
        log.debug("Finding cancelled seat reservations");
        return seatReservationRepository.findCancelledSeatReservations();
    }



    /**
     * Check if seat is available for showtime
     */
    @Transactional(readOnly = true)
    public boolean isSeatAvailableForShowtime(Long seatId, Long showtimeId) {
        log.debug("Checking if seat: {} is available for showtime: {}", seatId, showtimeId);
        long activeReservations = seatReservationRepository
            .countActiveReservationsForSeatAndShowtime(seatId, showtimeId);
        return activeReservations == 0;
    }

    /**
     * Get seat availability status for showtime
     */
    @Transactional(readOnly = true)
    public String getSeatAvailabilityStatus(Long seatId, Long showtimeId) {
        log.debug("Getting seat availability status for seat: {} and showtime: {}", seatId, showtimeId);
        List<SeatReservation> reservations = seatReservationRepository
            .findBySeatIdAndShowtimeId(seatId, showtimeId);
        
        if (reservations.isEmpty()) {
            return "AVAILABLE";
        }
        
        // Check if any reservation is active
        boolean hasActiveReservation = reservations.stream()
            .anyMatch(sr -> "RESERVED".equals(sr.getStatus()) || "CONFIRMED".equals(sr.getStatus()));
        
        return hasActiveReservation ? "RESERVED" : "AVAILABLE";
    }

    /**
     * Cancel a seat reservation
     */
    public SeatReservation cancelSeatReservation(Long seatReservationId) {
        log.info("Cancelling seat reservation with ID: {}", seatReservationId);
        
        Optional<SeatReservation> seatReservationOpt = seatReservationRepository.findById(seatReservationId);
        if (seatReservationOpt.isEmpty()) {
            throw new RuntimeException("Seat reservation not found with ID: " + seatReservationId);
        }
        
        SeatReservation seatReservation = seatReservationOpt.get();
        
        // Check if seat reservation can be cancelled
        if ("CANCELLED".equals(seatReservation.getStatus()) || "COMPLETED".equals(seatReservation.getStatus())) {
            throw new RuntimeException("Cannot cancel seat reservation with status: " + seatReservation.getStatus());
        }
        
        // Check if showtime has already started
        if (seatReservation.getShowtime().getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot cancel seat reservation for showtime that has already started");
        }
        
        seatReservation.setStatus("CANCELLED");
        
        SeatReservation cancelledSeatReservation = seatReservationRepository.save(seatReservation);
        log.info("Seat reservation cancelled successfully with ID: {}", cancelledSeatReservation.getId());
        return cancelledSeatReservation;
    }

    /**
     * Complete a seat reservation
     */
    public SeatReservation completeSeatReservation(Long seatReservationId) {
        log.info("Completing seat reservation with ID: {}", seatReservationId);
        
        Optional<SeatReservation> seatReservationOpt = seatReservationRepository.findById(seatReservationId);
        if (seatReservationOpt.isEmpty()) {
            throw new RuntimeException("Seat reservation not found with ID: " + seatReservationId);
        }
        
        SeatReservation seatReservation = seatReservationOpt.get();
        
        // Check if seat reservation is in RESERVED or CONFIRMED status
        if (!"RESERVED".equals(seatReservation.getStatus()) && !"CONFIRMED".equals(seatReservation.getStatus())) {
            throw new RuntimeException("Can only complete RESERVED or CONFIRMED seat reservations. Current status: " + seatReservation.getStatus());
        }
        
        seatReservation.setStatus("COMPLETED");
        
        SeatReservation completedSeatReservation = seatReservationRepository.save(seatReservation);
        log.info("Seat reservation completed successfully with ID: {}", completedSeatReservation.getId());
        return completedSeatReservation;
    }

    /**
     * Update seat reservation
     */
    public SeatReservation updateSeatReservation(SeatReservation seatReservation) {
        log.info("Updating seat reservation with ID: {}", seatReservation.getId());
        
        if (!seatReservationRepository.existsById(seatReservation.getId())) {
            throw new RuntimeException("Seat reservation not found with ID: " + seatReservation.getId());
        }
        
        // Validate seat is not null
        if (seatReservation.getSeat() == null || seatReservation.getSeat().getId() == null) {
            throw new RuntimeException("Seat reservation must be associated with a seat");
        }
        
        // Validate showtime is not null
        if (seatReservation.getShowtime() == null || seatReservation.getShowtime().getId() == null) {
            throw new RuntimeException("Seat reservation must be associated with a showtime");
        }
        
        // Validate reservation is not null
        if (seatReservation.getReservation() == null || seatReservation.getReservation().getId() == null) {
            throw new RuntimeException("Seat reservation must be associated with a reservation");
        }
        
        SeatReservation updatedSeatReservation = seatReservationRepository.save(seatReservation);
        log.info("Seat reservation updated successfully with ID: {}", updatedSeatReservation.getId());
        return updatedSeatReservation;
    }

    /**
     * Delete seat reservation by ID
     */
    public void deleteById(Long id) {
        log.info("Deleting seat reservation with ID: {}", id);
        
        if (!seatReservationRepository.existsById(id)) {
            throw new RuntimeException("Seat reservation not found with ID: " + id);
        }
        
        seatReservationRepository.deleteById(id);
        log.info("Seat reservation deleted successfully with ID: {}", id);
    }

    /**
     * Get seat reservation count
     */
    @Transactional(readOnly = true)
    public long count() {
        log.debug("Getting total seat reservation count");
        return seatReservationRepository.count();
    }

    /**
     * Get seat reservation count by seat
     */
    @Transactional(readOnly = true)
    public long countBySeat(Long seatId) {
        log.debug("Getting seat reservation count by seat ID: {}", seatId);
        return seatReservationRepository.findBySeatId(seatId).size();
    }

    /**
     * Get seat reservation count by showtime
     */
    @Transactional(readOnly = true)
    public long countByShowtime(Long showtimeId) {
        log.debug("Getting seat reservation count by showtime ID: {}", showtimeId);
        return seatReservationRepository.findByShowtimeId(showtimeId).size();
    }

    /**
     * Get seat reservation count by reservation
     */
    @Transactional(readOnly = true)
    public long countByReservation(Long reservationId) {
        log.debug("Getting seat reservation count by reservation ID: {}", reservationId);
        return seatReservationRepository.findByReservationId(reservationId).size();
    }

    /**
     * Get seat reservation count by status
     */
    @Transactional(readOnly = true)
    public long countByStatus(String status) {
        log.debug("Getting seat reservation count by status: {}", status);
        return seatReservationRepository.findByStatus(status).size();
    }

    /**
     * Get active seat reservation count
     */
    @Transactional(readOnly = true)
    public long countActiveSeatReservations() {
        log.debug("Getting active seat reservation count");
        return seatReservationRepository.findActiveSeatReservations().size();
    }

    /**
     * Get cancelled seat reservation count
     */
    @Transactional(readOnly = true)
    public long countCancelledSeatReservations() {
        log.debug("Getting cancelled seat reservation count");
        return seatReservationRepository.findCancelledSeatReservations().size();
    }



    /**
     * Find seat reservations ordered by creation date
     */
    @Transactional(readOnly = true)
    public List<SeatReservation> findAllOrderByCreatedAt() {
        log.debug("Finding all seat reservations ordered by creation date");
        return seatReservationRepository.findAllByOrderByCreatedAtDesc();
    }



    /**
     * Find seat reservations by showtime ordered by creation date
     */
    @Transactional(readOnly = true)
    public List<SeatReservation> findByShowtimeIdOrderByCreatedAt(Long showtimeId) {
        log.debug("Finding seat reservations by showtime ID ordered by creation date: {}", showtimeId);
        return seatReservationRepository.findByShowtimeIdOrderByCreatedAtDesc(showtimeId);
    }

    /**
     * Find seat reservations by reservation ordered by creation date
     */
    @Transactional(readOnly = true)
    public List<SeatReservation> findByReservationIdOrderByCreatedAt(Long reservationId) {
        log.debug("Finding seat reservations by reservation ID ordered by creation date: {}", reservationId);
        return seatReservationRepository.findByReservationIdOrderByCreatedAtDesc(reservationId);
    }

    
    /**
     * Find seat reservations by status ordered by creation date
     */
    @Transactional(readOnly = true)
    public List<SeatReservation> findByStatusOrderByCreatedAt(String status) {
        log.debug("Finding seat reservations by status ordered by creation date: {}", status);
        return seatReservationRepository.findByStatusOrderByCreatedAtDesc(status);
    }
}
