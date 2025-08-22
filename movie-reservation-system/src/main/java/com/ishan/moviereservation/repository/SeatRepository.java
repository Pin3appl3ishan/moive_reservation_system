package com.ishan.moviereservation.repository;

import com.ishan.moviereservation.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    // Find by label (exact match)
    Optional<Seat> findByLabel(String label);

    // Find by label containing (for search functionality)
    List<Seat> findByLabelContainingIgnoreCase(String label);

    // Find by screen ID
    List<Seat> findByScreenId(Long screenId);

    // Find by screen ID and label
    List<Seat> findByScreenIdAndLabelContainingIgnoreCase(Long screenId, String label);

    // Find by row label
    List<Seat> findByRowLabel(String rowLabel);

    // Find by row label containing
    List<Seat> findByRowLabelContainingIgnoreCase(String rowLabel);

    // Find by column number
    List<Seat> findByCol(Integer col);

    // Find by column range
    List<Seat> findByColBetween(Integer minCol, Integer maxCol);

    // Find by screen ID and row label
    List<Seat> findByScreenIdAndRowLabel(Long screenId, String rowLabel);

    // Find by screen ID and column
    List<Seat> findByScreenIdAndCol(Long screenId, Integer col);

    // Find by screen ID, row label, and column
    List<Seat> findByScreenIdAndRowLabelAndCol(Long screenId, String rowLabel, Integer col);

    // Find seats ordered by row label and column
    List<Seat> findByScreenIdOrderByRowLabelAscColAsc(Long screenId);

    // Find seats ordered by label
    List<Seat> findByScreenIdOrderByLabelAsc(Long screenId);

    // Custom query to find seats with their reservations
    @Query("SELECT s FROM Seat s LEFT JOIN FETCH s.seatReservations WHERE s.id = :seatId")
    Optional<Seat> findByIdWithSeatReservations(@Param("seatId") Long seatId);

    // Find seats that have reservations
    @Query("SELECT DISTINCT s FROM Seat s WHERE s.seatReservations IS NOT EMPTY")
    List<Seat> findSeatsWithReservations();

    // Find seats with no reservations
    @Query("SELECT s FROM Seat s WHERE s.seatReservations IS EMPTY")
    List<Seat> findSeatsWithoutReservations();

    // Custom query to find seats with reservation count
    @Query("SELECT s, COUNT(sr) as reservationCount FROM Seat s LEFT JOIN s.seatReservations sr GROUP BY s ORDER BY reservationCount DESC")
    List<Object[]> findSeatsWithReservationCount();

    // Find seats by screen with pagination support
    @Query("SELECT s FROM Seat s WHERE s.screen.id = :screenId ORDER BY s.rowLabel ASC, s.col ASC")
    List<Seat> findByScreenIdOrdered(@Param("screenId") Long screenId);

    // Find seats by screen and row
    @Query("SELECT s FROM Seat s WHERE s.screen.id = :screenId AND s.rowLabel = :rowLabel ORDER BY s.col ASC")
    List<Seat> findByScreenIdAndRowLabelOrdered(@Param("screenId") Long screenId, @Param("rowLabel") String rowLabel);

    // Find available seats for a specific showtime (not reserved)
    @Query("SELECT s FROM Seat s WHERE s.screen.id = :screenId AND s.id NOT IN " +
           "(SELECT sr.seat.id FROM SeatReservation sr WHERE sr.showtime.id = :showtimeId AND sr.status IN ('HELD', 'PAID')) " +
           "ORDER BY s.rowLabel ASC, s.col ASC")
    List<Seat> findAvailableSeatsForShowtime(@Param("screenId") Long screenId, @Param("showtimeId") Long showtimeId);
}
