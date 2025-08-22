package com.ishan.moviereservation.service;

import com.ishan.moviereservation.entity.Reservation;
import com.ishan.moviereservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;

    /**
     * Create a new reservation
     */
    public Reservation createReservation(Reservation reservation) {
        log.info("Creating new reservation for user: {} and showtime: {}", 
            reservation.getUser().getUsername(), reservation.getShowtime().getId());
        
        // Validate user is not null
        if (reservation.getUser() == null || reservation.getUser().getId() == null) {
            throw new RuntimeException("Reservation must be associated with a user");
        }
        
        // Validate showtime is not null
        if (reservation.getShowtime() == null || reservation.getShowtime().getId() == null) {
            throw new RuntimeException("Reservation must be associated with a showtime");
        }
        
        // Validate showtime is in the future
        if (reservation.getShowtime().getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot create reservation for past showtime");
        }
        
        // Validate total amount is positive
        if (reservation.getTotalAmount() == null || reservation.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Reservation total amount must be positive");
        }
        
        // Set initial status
        reservation.setStatus("PENDING");
        
        Reservation savedReservation = reservationRepository.save(reservation);
        log.info("Reservation created successfully with ID: {}", savedReservation.getId());
        return savedReservation;
    }

    /**
     * Find reservation by ID
     */
    @Transactional(readOnly = true)
    public Optional<Reservation> findById(Long id) {
        log.debug("Finding reservation by ID: {}", id);
        return reservationRepository.findById(id);
    }

    /**
     * Find reservation by ID with seat reservations
     */
    @Transactional(readOnly = true)
    public Optional<Reservation> findByIdWithSeatReservations(Long id) {
        log.debug("Finding reservation by ID with seat reservations: {}", id);
        return reservationRepository.findByIdWithSeatReservations(id);
    }

    /**
     * Get all reservations
     */
    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        log.debug("Finding all reservations");
        return reservationRepository.findAll();
    }

    /**
     * Find reservations by user ID
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByUserId(Long userId) {
        log.debug("Finding reservations by user ID: {}", userId);
        return reservationRepository.findByUserId(userId);
    }

    /**
     * Find reservations by showtime ID
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByShowtimeId(Long showtimeId) {
        log.debug("Finding reservations by showtime ID: {}", showtimeId);
        return reservationRepository.findByShowtimeId(showtimeId);
    }

    /**
     * Find reservations by user and showtime
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByUserIdAndShowtimeId(Long userId, Long showtimeId) {
        log.debug("Finding reservations by user ID: {} and showtime ID: {}", userId, showtimeId);
        return reservationRepository.findByUserIdAndShowtimeId(userId, showtimeId);
    }

    /**
     * Find reservations by status
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByStatus(String status) {
        log.debug("Finding reservations by status: {}", status);
        return reservationRepository.findByStatus(status);
    }

    /**
     * Find reservations by user and status
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByUserIdAndStatus(Long userId, String status) {
        log.debug("Finding reservations by user ID: {} and status: {}", userId, status);
        return reservationRepository.findByUserIdAndStatus(userId, status);
    }

    /**
     * Find reservations by showtime and status
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByShowtimeIdAndStatus(Long showtimeId, String status) {
        log.debug("Finding reservations by showtime ID: {} and status: {}", showtimeId, status);
        return reservationRepository.findByShowtimeIdAndStatus(showtimeId, status);
    }

    /**
     * Find reservations by total amount range
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByTotalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount) {
        log.debug("Finding reservations with total amount between: {} and {}", minAmount, maxAmount);
        return reservationRepository.findByTotalAmountBetween(minAmount, maxAmount);
    }

    /**
     * Find reservations by total amount greater than
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByTotalAmountGreaterThan(BigDecimal amount) {
        log.debug("Finding reservations with total amount greater than: {}", amount);
        return reservationRepository.findByTotalAmountGreaterThan(amount);
    }

    /**
     * Find reservations by total amount less than
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByTotalAmountLessThan(BigDecimal amount) {
        log.debug("Finding reservations with total amount less than: {}", amount);
        return reservationRepository.findByTotalAmountLessThan(amount);
    }

    /**
     * Find reservations by hold expiry before
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByHoldExpiryBefore(LocalDateTime time) {
        log.debug("Finding reservations with hold expiry before: {}", time);
        return reservationRepository.findByHoldExpiryBefore(time);
    }

    /**
     * Find reservations by hold expiry after
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByHoldExpiryAfter(LocalDateTime time) {
        log.debug("Finding reservations with hold expiry after: {}", time);
        return reservationRepository.findByHoldExpiryAfter(time);
    }

    /**
     * Find reservations by hold expiry between
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByHoldExpiryBetween(LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Finding reservations with hold expiry between: {} and {}", startTime, endTime);
        return reservationRepository.findByHoldExpiryBetween(startTime, endTime);
    }

    /**
     * Find expired reservations
     */
    @Transactional(readOnly = true)
    public List<Reservation> findExpiredReservations() {
        log.debug("Finding expired reservations");
        return reservationRepository.findExpiredReservations(LocalDateTime.now());
    }

    /**
     * Find active reservations
     */
    @Transactional(readOnly = true)
    public List<Reservation> findActiveReservations() {
        log.debug("Finding active reservations");
        return reservationRepository.findActiveReservations(LocalDateTime.now());
    }

    /**
     * Find confirmed reservations
     */
    @Transactional(readOnly = true)
    public List<Reservation> findConfirmedReservations() {
        log.debug("Finding confirmed reservations");
        return reservationRepository.findConfirmedReservations();
    }

    /**
     * Find held reservations
     */
    @Transactional(readOnly = true)
    public List<Reservation> findHeldReservations() {
        log.debug("Finding held reservations");
        return reservationRepository.findHeldReservations();
    }

    /**
     * Find cancelled reservations
     */
    @Transactional(readOnly = true)
    public List<Reservation> findCancelledReservations() {
        log.debug("Finding cancelled reservations");
        return reservationRepository.findCancelledReservations();
    }

    /**
     * Find reservations by date range
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Finding reservations between: {} and {}", startDate, endDate);
        return reservationRepository.findByCreatedAtBetween(startDate, endDate);
    }

    /**
     * Find reservations by user and date range
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Finding reservations by user ID: {} between: {} and {}", userId, startDate, endDate);
        return reservationRepository.findByUserIdAndCreatedAtBetween(userId, startDate, endDate);
    }

    /**
     * Find reservations by showtime and date range
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByShowtimeIdAndCreatedAtBetween(Long showtimeId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Finding reservations by showtime ID: {} between: {} and {}", showtimeId, startDate, endDate);
        return reservationRepository.findByShowtimeIdAndCreatedAtBetween(showtimeId, startDate, endDate);
    }

    /**
     * Find reservations with seat reservations
     */
    @Transactional(readOnly = true)
    public List<Reservation> findReservationsWithSeatReservations() {
        log.debug("Finding reservations with seat reservations");
        return reservationRepository.findReservationsWithSeatReservations();
    }

    /**
     * Find reservations without seat reservations
     */
    @Transactional(readOnly = true)
    public List<Reservation> findReservationsWithoutSeatReservations() {
        log.debug("Finding reservations without seat reservations");
        return reservationRepository.findReservationsWithoutSeatReservations();
    }

    /**
     * Find reservations with seat reservation count
     */
    @Transactional(readOnly = true)
    public List<Object[]> findReservationsWithSeatReservationCount() {
        log.debug("Finding reservations with seat reservation count");
        return reservationRepository.findReservationsWithSeatReservationCount();
    }

    /**
     * Find total revenue by date range
     */
    @Transactional(readOnly = true)
    public BigDecimal findTotalRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Finding total revenue between: {} and {}", startDate, endDate);
        return reservationRepository.findTotalRevenueByDateRange(startDate, endDate);
    }

    /**
     * Find reservation count by status
     */
    @Transactional(readOnly = true)
    public List<Object[]> findReservationCountByStatus() {
        log.debug("Finding reservation count by status");
        return reservationRepository.findReservationCountByStatus();
    }

    /**
     * Confirm a reservation
     */
    public Reservation confirmReservation(Long reservationId) {
        log.info("Confirming reservation with ID: {}", reservationId);
        
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isEmpty()) {
            throw new RuntimeException("Reservation not found with ID: " + reservationId);
        }
        
        Reservation reservation = reservationOpt.get();
        
        // Check if reservation is in PENDING status
        if (!"PENDING".equals(reservation.getStatus())) {
            throw new RuntimeException("Can only confirm PENDING reservations. Current status: " + reservation.getStatus());
        }
        
        // Check if showtime is still in the future
        if (reservation.getShowtime().getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot confirm reservation for past showtime");
        }
        
        reservation.setStatus("CONFIRMED");
        
        Reservation confirmedReservation = reservationRepository.save(reservation);
        log.info("Reservation confirmed successfully with ID: {}", confirmedReservation.getId());
        return confirmedReservation;
    }

    /**
     * Cancel a reservation
     */
    public Reservation cancelReservation(Long reservationId) {
        log.info("Cancelling reservation with ID: {}", reservationId);
        
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isEmpty()) {
            throw new RuntimeException("Reservation not found with ID: " + reservationId);
        }
        
        Reservation reservation = reservationOpt.get();
        
        // Check if reservation can be cancelled
        if ("CANCELLED".equals(reservation.getStatus()) || "COMPLETED".equals(reservation.getStatus())) {
            throw new RuntimeException("Cannot cancel reservation with status: " + reservation.getStatus());
        }
        
        // Check if showtime has already started
        if (reservation.getShowtime().getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot cancel reservation for showtime that has already started");
        }
        
        reservation.setStatus("CANCELLED");
        
        Reservation cancelledReservation = reservationRepository.save(reservation);
        log.info("Reservation cancelled successfully with ID: {}", cancelledReservation.getId());
        return cancelledReservation;
    }

    /**
     * Complete a reservation
     */
    public Reservation completeReservation(Long reservationId) {
        log.info("Completing reservation with ID: {}", reservationId);
        
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isEmpty()) {
            throw new RuntimeException("Reservation not found with ID: " + reservationId);
        }
        
        Reservation reservation = reservationOpt.get();
        
        // Check if reservation is in CONFIRMED status
        if (!"CONFIRMED".equals(reservation.getStatus())) {
            throw new RuntimeException("Can only complete CONFIRMED reservations. Current status: " + reservation.getStatus());
        }
        
        // Check if showtime has started
        if (reservation.getShowtime().getStartTime().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Cannot complete reservation for future showtime");
        }
        
        reservation.setStatus("COMPLETED");
        
        Reservation completedReservation = reservationRepository.save(reservation);
        log.info("Reservation completed successfully with ID: {}", completedReservation.getId());
        return completedReservation;
    }

    /**
     * Update reservation
     */
    public Reservation updateReservation(Reservation reservation) {
        log.info("Updating reservation with ID: {}", reservation.getId());
        
        if (!reservationRepository.existsById(reservation.getId())) {
            throw new RuntimeException("Reservation not found with ID: " + reservation.getId());
        }
        
        // Validate user is not null
        if (reservation.getUser() == null || reservation.getUser().getId() == null) {
            throw new RuntimeException("Reservation must be associated with a user");
        }
        
        // Validate showtime is not null
        if (reservation.getShowtime() == null || reservation.getShowtime().getId() == null) {
            throw new RuntimeException("Reservation must be associated with a showtime");
        }
        
        // Validate total amount is positive
        if (reservation.getTotalAmount() == null || reservation.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Reservation total amount must be positive");
        }
        

        
        Reservation updatedReservation = reservationRepository.save(reservation);
        log.info("Reservation updated successfully with ID: {}", updatedReservation.getId());
        return updatedReservation;
    }

    /**
     * Delete reservation by ID
     */
    public void deleteById(Long id) {
        log.info("Deleting reservation with ID: {}", id);
        
        if (!reservationRepository.existsById(id)) {
            throw new RuntimeException("Reservation not found with ID: " + id);
        }
        
        // Check if reservation has seat reservations (business rule: can't delete reservation with seat reservations)
        Optional<Reservation> reservation = reservationRepository.findByIdWithSeatReservations(id);
        if (reservation.isPresent() && !reservation.get().getSeatReservations().isEmpty()) {
            throw new RuntimeException("Cannot delete reservation with seat reservations. Reservation ID: " + id);
        }
        
        reservationRepository.deleteById(id);
        log.info("Reservation deleted successfully with ID: {}", id);
    }

    /**
     * Get reservation count
     */
    @Transactional(readOnly = true)
    public long count() {
        log.debug("Getting total reservation count");
        return reservationRepository.count();
    }

    /**
     * Get reservation count by user
     */
    @Transactional(readOnly = true)
    public long countByUser(Long userId) {
        log.debug("Getting reservation count by user ID: {}", userId);
        return reservationRepository.findByUserId(userId).size();
    }

    /**
     * Get reservation count by showtime
     */
    @Transactional(readOnly = true)
    public long countByShowtime(Long showtimeId) {
        log.debug("Getting reservation count by showtime ID: {}", showtimeId);
        return reservationRepository.findByShowtimeId(showtimeId).size();
    }

    /**
     * Get reservation count by status
     */
    @Transactional(readOnly = true)
    public long countByStatus(String status) {
        log.debug("Getting reservation count by status: {}", status);
        return reservationRepository.findByStatus(status).size();
    }

    /**
     * Get active reservation count
     */
    @Transactional(readOnly = true)
    public long countActiveReservations() {
        log.debug("Getting active reservation count");
        return reservationRepository.findActiveReservations(LocalDateTime.now()).size();
    }

    /**
     * Get confirmed reservation count
     */
    @Transactional(readOnly = true)
    public long countConfirmedReservations() {
        log.debug("Getting confirmed reservation count");
        return reservationRepository.findConfirmedReservations().size();
    }

    /**
     * Get held reservation count
     */
    @Transactional(readOnly = true)
    public long countHeldReservations() {
        log.debug("Getting held reservation count");
        return reservationRepository.findHeldReservations().size();
    }

    /**
     * Get cancelled reservation count
     */
    @Transactional(readOnly = true)
    public long countCancelledReservations() {
        log.debug("Getting cancelled reservation count");
        return reservationRepository.findCancelledReservations().size();
    }

    /**
     * Find reservations ordered by creation date
     */
    @Transactional(readOnly = true)
    public List<Reservation> findAllOrderByCreatedAt() {
        log.debug("Finding all reservations ordered by creation date");
        return reservationRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Find reservations by user ordered by creation date
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByUserIdOrderByCreatedAt(Long userId) {
        log.debug("Finding reservations by user ID ordered by creation date: {}", userId);
        return reservationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Find reservations by showtime ordered by creation date
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByShowtimeIdOrderByCreatedAt(Long showtimeId) {
        log.debug("Finding reservations by showtime ID ordered by creation date: {}", showtimeId);
        return reservationRepository.findByShowtimeIdOrderByCreatedAtDesc(showtimeId);
    }

    /**
     * Find reservations by status ordered by creation date
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByStatusOrderByCreatedAt(String status) {
        log.debug("Finding reservations by status ordered by creation date: {}", status);
        return reservationRepository.findByStatusOrderByCreatedAtDesc(status);
    }
}
