package com.ishan.moviereservation.repository;

import com.ishan.moviereservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Find by user ID
    List<Reservation> findByUserId(Long userId);

    // Find by showtime ID
    List<Reservation> findByShowtimeId(Long showtimeId);

    // Find by user ID and showtime ID
    List<Reservation> findByUserIdAndShowtimeId(Long userId, Long showtimeId);

    // Find by status
    List<Reservation> findByStatus(String status);

    // Find by user ID and status
    List<Reservation> findByUserIdAndStatus(Long userId, String status);

    // Find by showtime ID and status
    List<Reservation> findByShowtimeIdAndStatus(Long showtimeId, String status);

    // Find by total amount range
    List<Reservation> findByTotalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);

    // Find by total amount greater than
    List<Reservation> findByTotalAmountGreaterThan(BigDecimal amount);

    // Find by total amount less than
    List<Reservation> findByTotalAmountLessThan(BigDecimal amount);

    // Find by hold expiry before specified time (expired reservations)
    List<Reservation> findByHoldExpiryBefore(LocalDateTime time);

    // Find by hold expiry after specified time (active reservations)
    List<Reservation> findByHoldExpiryAfter(LocalDateTime time);

    // Find by hold expiry between
    List<Reservation> findByHoldExpiryBetween(LocalDateTime startTime, LocalDateTime endTime);

    // Find reservations ordered by creation date (newest first)
    List<Reservation> findAllByOrderByCreatedAtDesc();

    // Find reservations by user ordered by creation date
    List<Reservation> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Find reservations by showtime ordered by creation date
    List<Reservation> findByShowtimeIdOrderByCreatedAtDesc(Long showtimeId);

    // Find reservations by status ordered by creation date
    List<Reservation> findByStatusOrderByCreatedAtDesc(String status);

    // Custom query to find reservations with their seat reservations
    @Query("SELECT r FROM Reservation r LEFT JOIN FETCH r.seatReservations WHERE r.id = :reservationId")
    Optional<Reservation> findByIdWithSeatReservations(@Param("reservationId") Long reservationId);

    // Find reservations that have seat reservations
    @Query("SELECT DISTINCT r FROM Reservation r WHERE r.seatReservations IS NOT EMPTY")
    List<Reservation> findReservationsWithSeatReservations();

    // Find reservations with no seat reservations
    @Query("SELECT r FROM Reservation r WHERE r.seatReservations IS EMPTY")
    List<Reservation> findReservationsWithoutSeatReservations();

    // Custom query to find reservations with seat reservation count
    @Query("SELECT r, COUNT(sr) as seatReservationCount FROM Reservation r LEFT JOIN r.seatReservations sr GROUP BY r ORDER BY seatReservationCount DESC")
    List<Object[]> findReservationsWithSeatReservationCount();

    // Find expired reservations (hold expiry before current time)
    @Query("SELECT r FROM Reservation r WHERE r.holdExpiry < :currentTime AND r.status = 'HELD'")
    List<Reservation> findExpiredReservations(@Param("currentTime") LocalDateTime currentTime);

    // Find active reservations (not expired and not cancelled)
    @Query("SELECT r FROM Reservation r WHERE (r.holdExpiry IS NULL OR r.holdExpiry > :currentTime) AND r.status != 'CANCELLED'")
    List<Reservation> findActiveReservations(@Param("currentTime") LocalDateTime currentTime);

    // Find confirmed reservations
    @Query("SELECT r FROM Reservation r WHERE r.status = 'CONFIRMED'")
    List<Reservation> findConfirmedReservations();

    // Find held reservations
    @Query("SELECT r FROM Reservation r WHERE r.status = 'HELD'")
    List<Reservation> findHeldReservations();

    // Find cancelled reservations
    @Query("SELECT r FROM Reservation r WHERE r.status = 'CANCELLED'")
    List<Reservation> findCancelledReservations();

    // Find reservations by date range
    @Query("SELECT r FROM Reservation r WHERE r.createdAt BETWEEN :startDate AND :endDate ORDER BY r.createdAt DESC")
    List<Reservation> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Find reservations by user and date range
    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.createdAt BETWEEN :startDate AND :endDate ORDER BY r.createdAt DESC")
    List<Reservation> findByUserIdAndCreatedAtBetween(@Param("userId") Long userId, 
                                                     @Param("startDate") LocalDateTime startDate, 
                                                     @Param("endDate") LocalDateTime endDate);

    // Find reservations by showtime and date range
    @Query("SELECT r FROM Reservation r WHERE r.showtime.id = :showtimeId AND r.createdAt BETWEEN :startDate AND :endDate ORDER BY r.createdAt DESC")
    List<Reservation> findByShowtimeIdAndCreatedAtBetween(@Param("showtimeId") Long showtimeId, 
                                                         @Param("startDate") LocalDateTime startDate, 
                                                         @Param("endDate") LocalDateTime endDate);

    // Find total revenue by date range
    @Query("SELECT SUM(r.totalAmount) FROM Reservation r WHERE r.status = 'CONFIRMED' AND r.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal findTotalRevenueByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Find reservation count by status
    @Query("SELECT r.status, COUNT(r) FROM Reservation r GROUP BY r.status")
    List<Object[]> findReservationCountByStatus();
}
