package com.ishan.moviereservation.repository;

import com.ishan.moviereservation.entity.SeatReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatReservationRepository extends JpaRepository<SeatReservation, Long> {

    // Find by reservation ID
    List<SeatReservation> findByReservationId(Long reservationId);

    // Find by seat ID
    List<SeatReservation> findBySeatId(Long seatId);

    // Find by showtime ID
    List<SeatReservation> findByShowtimeId(Long showtimeId);

    // Find by status
    List<SeatReservation> findByStatus(String status);

    // Find by reservation ID and status
    List<SeatReservation> findByReservationIdAndStatus(Long reservationId, String status);

    // Find by seat ID and status
    List<SeatReservation> findBySeatIdAndStatus(Long seatId, String status);

    // Find by showtime ID and status
    List<SeatReservation> findByShowtimeIdAndStatus(Long showtimeId, String status);

    // Find by seat ID and showtime ID
    List<SeatReservation> findBySeatIdAndShowtimeId(Long seatId, Long showtimeId);

    // Find by reservation ID and seat ID
    List<SeatReservation> findByReservationIdAndSeatId(Long reservationId, Long seatId);

    // Find by reservation ID and showtime ID
    List<SeatReservation> findByReservationIdAndShowtimeId(Long reservationId, Long showtimeId);

    // Find seat reservations ordered by creation date
    List<SeatReservation> findAllByOrderByCreatedAtDesc();

    // Find by reservation ordered by creation date
    List<SeatReservation> findByReservationIdOrderByCreatedAtDesc(Long reservationId);

    // Find by showtime ordered by creation date
    List<SeatReservation> findByShowtimeIdOrderByCreatedAtDesc(Long showtimeId);

    // Find by status ordered by creation date
    List<SeatReservation> findByStatusOrderByCreatedAtDesc(String status);

    // Find held seat reservations
    @Query("SELECT sr FROM SeatReservation sr WHERE sr.status = 'HELD'")
    List<SeatReservation> findHeldSeatReservations();

    // Find paid seat reservations
    @Query("SELECT sr FROM SeatReservation sr WHERE sr.status = 'PAID'")
    List<SeatReservation> findPaidSeatReservations();

    // Find cancelled seat reservations
    @Query("SELECT sr FROM SeatReservation sr WHERE sr.status = 'CANCELLED'")
    List<SeatReservation> findCancelledSeatReservations();

    // Find active seat reservations (not cancelled)
    @Query("SELECT sr FROM SeatReservation sr WHERE sr.status != 'CANCELLED'")
    List<SeatReservation> findActiveSeatReservations();



    // Find seat reservations for a specific seat and showtime with status
    @Query("SELECT sr FROM SeatReservation sr WHERE sr.seat.id = :seatId AND sr.showtime.id = :showtimeId AND sr.status = :status")
    List<SeatReservation> findBySeatIdAndShowtimeIdAndStatus(@Param("seatId") Long seatId, 
                                                            @Param("showtimeId") Long showtimeId, 
                                                            @Param("status") String status);

    // Check if a seat is available for a showtime (no active reservations)
    @Query("SELECT COUNT(sr) FROM SeatReservation sr WHERE sr.seat.id = :seatId AND sr.showtime.id = :showtimeId AND sr.status IN ('HELD', 'PAID')")
    long countActiveReservationsForSeatAndShowtime(@Param("seatId") Long seatId, @Param("showtimeId") Long showtimeId);

    // Find all seat reservations for a showtime with seat details
    @Query("SELECT sr FROM SeatReservation sr JOIN FETCH sr.seat WHERE sr.showtime.id = :showtimeId ORDER BY sr.seat.rowLabel ASC, sr.seat.col ASC")
    List<SeatReservation> findByShowtimeIdWithSeatDetails(@Param("showtimeId") Long showtimeId);

    // Find all seat reservations for a showtime with reservation details
    @Query("SELECT sr FROM SeatReservation sr JOIN FETCH sr.reservation WHERE sr.showtime.id = :showtimeId ORDER BY sr.createdAt DESC")
    List<SeatReservation> findByShowtimeIdWithReservationDetails(@Param("showtimeId") Long showtimeId);

    // Find seat reservations by date range
    @Query("SELECT sr FROM SeatReservation sr WHERE sr.createdAt BETWEEN :startDate AND :endDate ORDER BY sr.createdAt DESC")
    List<SeatReservation> findByCreatedAtBetween(@Param("startDate") java.time.LocalDateTime startDate, 
                                                @Param("endDate") java.time.LocalDateTime endDate);

    // Find seat reservations by showtime and date range
    @Query("SELECT sr FROM SeatReservation sr WHERE sr.showtime.id = :showtimeId AND sr.createdAt BETWEEN :startDate AND :endDate ORDER BY sr.createdAt DESC")
    List<SeatReservation> findByShowtimeIdAndCreatedAtBetween(@Param("showtimeId") Long showtimeId, 
                                                             @Param("startDate") java.time.LocalDateTime startDate, 
                                                             @Param("endDate") java.time.LocalDateTime endDate);

    // Find seat reservations by reservation and date range
    @Query("SELECT sr FROM SeatReservation sr WHERE sr.reservation.id = :reservationId AND sr.createdAt BETWEEN :startDate AND :endDate ORDER BY sr.createdAt DESC")
    List<SeatReservation> findByReservationIdAndCreatedAtBetween(@Param("reservationId") Long reservationId, 
                                                                @Param("startDate") java.time.LocalDateTime startDate, 
                                                                @Param("endDate") java.time.LocalDateTime endDate);

    // Find seat reservation count by status
    @Query("SELECT sr.status, COUNT(sr) FROM SeatReservation sr GROUP BY sr.status")
    List<Object[]> findSeatReservationCountByStatus();

    // Find seat reservation count by showtime
    @Query("SELECT sr.showtime.id, COUNT(sr) FROM SeatReservation sr WHERE sr.status IN ('HELD', 'PAID') GROUP BY sr.showtime.id")
    List<Object[]> findSeatReservationCountByShowtime();
}
