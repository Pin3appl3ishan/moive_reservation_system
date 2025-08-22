package com.ishan.moviereservation.repository;

import com.ishan.moviereservation.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    // Find by movie ID
    List<Showtime> findByMovieId(Long movieId);

    // Find by screen ID
    List<Showtime> findByScreenId(Long screenId);

    // Find by movie ID and screen ID
    List<Showtime> findByMovieIdAndScreenId(Long movieId, Long screenId);

    // Find by start time range
    List<Showtime> findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    // Find by start time after specified time
    List<Showtime> findByStartTimeAfter(LocalDateTime startTime);

    // Find by start time before specified time
    List<Showtime> findByStartTimeBefore(LocalDateTime startTime);

    // Find by ticket price range
    List<Showtime> findByTicketPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Find by ticket price less than
    List<Showtime> findByTicketPriceLessThan(BigDecimal price);

    // Find by ticket price greater than
    List<Showtime> findByTicketPriceGreaterThan(BigDecimal price);

    // Find showtimes ordered by start time
    List<Showtime> findAllByOrderByStartTimeAsc();

    // Find showtimes ordered by start time (newest first)
    List<Showtime> findAllByOrderByStartTimeDesc();

    // Find showtimes by movie ordered by start time
    List<Showtime> findByMovieIdOrderByStartTimeAsc(Long movieId);

    // Find showtimes by screen ordered by start time
    List<Showtime> findByScreenIdOrderByStartTimeAsc(Long screenId);

    // Custom query to find showtimes with their reservations
    @Query("SELECT s FROM Showtime s LEFT JOIN FETCH s.reservations WHERE s.id = :showtimeId")
    Optional<Showtime> findByIdWithReservations(@Param("showtimeId") Long showtimeId);

    // Custom query to find showtimes with their seat reservations
    @Query("SELECT s FROM Showtime s LEFT JOIN FETCH s.seatReservations WHERE s.id = :showtimeId")
    Optional<Showtime> findByIdWithSeatReservations(@Param("showtimeId") Long showtimeId);

    // Find showtimes that have reservations
    @Query("SELECT DISTINCT s FROM Showtime s WHERE s.reservations IS NOT EMPTY")
    List<Showtime> findShowtimesWithReservations();

    // Find showtimes that have seat reservations
    @Query("SELECT DISTINCT s FROM Showtime s WHERE s.seatReservations IS NOT EMPTY")
    List<Showtime> findShowtimesWithSeatReservations();

    // Custom query to find showtimes with reservation count
    @Query("SELECT s, COUNT(r) as reservationCount FROM Showtime s LEFT JOIN s.reservations r GROUP BY s ORDER BY reservationCount DESC")
    List<Object[]> findShowtimesWithReservationCount();

    // Find showtimes with no reservations
    @Query("SELECT s FROM Showtime s WHERE s.reservations IS EMPTY")
    List<Showtime> findShowtimesWithoutReservations();

    // Find showtimes with no seat reservations
    @Query("SELECT s FROM Showtime s WHERE s.seatReservations IS EMPTY")
    List<Showtime> findShowtimesWithoutSeatReservations();

    // Find upcoming showtimes (after current time)
    @Query("SELECT s FROM Showtime s WHERE s.startTime > :currentTime ORDER BY s.startTime ASC")
    List<Showtime> findUpcomingShowtimes(@Param("currentTime") LocalDateTime currentTime);

    // Find showtimes for a specific date
    @Query("SELECT s FROM Showtime s WHERE DATE(s.startTime) = DATE(:date) ORDER BY s.startTime ASC")
    List<Showtime> findShowtimesByDate(@Param("date") LocalDateTime date);

    // Find showtimes for a specific date range
    @Query("SELECT s FROM Showtime s WHERE DATE(s.startTime) BETWEEN DATE(:startDate) AND DATE(:endDate) ORDER BY s.startTime ASC")
    List<Showtime> findShowtimesByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Find showtimes by movie and date range
    @Query("SELECT s FROM Showtime s WHERE s.movie.id = :movieId AND s.startTime BETWEEN :startTime AND :endTime ORDER BY s.startTime ASC")
    List<Showtime> findByMovieIdAndStartTimeBetween(@Param("movieId") Long movieId, 
                                                   @Param("startTime") LocalDateTime startTime, 
                                                   @Param("endTime") LocalDateTime endTime);

    // Find showtimes by screen and date range
    @Query("SELECT s FROM Showtime s WHERE s.screen.id = :screenId AND s.startTime BETWEEN :startTime AND :endTime ORDER BY s.startTime ASC")
    List<Showtime> findByScreenIdAndStartTimeBetween(@Param("screenId") Long screenId, 
                                                    @Param("startTime") LocalDateTime startTime, 
                                                    @Param("endTime") LocalDateTime endTime);

    // Find conflicting showtimes for a screen (overlapping time periods)
    @Query("SELECT s FROM Showtime s WHERE s.screen.id = :screenId AND " +
           "((s.startTime <= :startTime AND s.endTime > :startTime) OR " +
           "(s.startTime < :endTime AND s.endTime >= :endTime) OR " +
           "(s.startTime >= :startTime AND s.endTime <= :endTime))")
    List<Showtime> findConflictingShowtimes(@Param("screenId") Long screenId, 
                                           @Param("startTime") LocalDateTime startTime, 
                                           @Param("endTime") LocalDateTime endTime);
}
