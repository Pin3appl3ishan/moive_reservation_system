package com.ishan.moviereservation.repository;

import com.ishan.moviereservation.entity.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {

    // Find by name (exact match)
    Optional<Screen> findByName(String name);

    // Find by name containing (for search functionality)
    List<Screen> findByNameContainingIgnoreCase(String name);

    // Find by theater ID
    List<Screen> findByTheaterId(Long theaterId);

    // Find by theater ID and name
    List<Screen> findByTheaterIdAndNameContainingIgnoreCase(Long theaterId, String name);

    // Find by capacity range
    List<Screen> findByCapacityBetween(Integer minCapacity, Integer maxCapacity);

    // Find screens with capacity greater than specified
    List<Screen> findByCapacityGreaterThan(Integer capacity);

    // Find screens with capacity less than specified
    List<Screen> findByCapacityLessThan(Integer capacity);

    // Find screens ordered by capacity
    List<Screen> findAllByOrderByCapacityAsc();

    // Find screens ordered by name
    List<Screen> findAllByOrderByNameAsc();

    // Custom query to find screens with their seats
    @Query("SELECT s FROM Screen s LEFT JOIN FETCH s.seats WHERE s.id = :screenId")
    Optional<Screen> findByIdWithSeats(@Param("screenId") Long screenId);

    // Custom query to find screens with their showtimes
    @Query("SELECT s FROM Screen s LEFT JOIN FETCH s.showtimes WHERE s.id = :screenId")
    Optional<Screen> findByIdWithShowtimes(@Param("screenId") Long screenId);

    // Find screens that have seats
    @Query("SELECT DISTINCT s FROM Screen s WHERE s.seats IS NOT EMPTY")
    List<Screen> findScreensWithSeats();

    // Find screens that have showtimes
    @Query("SELECT DISTINCT s FROM Screen s WHERE s.showtimes IS NOT EMPTY")
    List<Screen> findScreensWithShowtimes();

    // Custom query to find screens with seat count
    @Query("SELECT s, COUNT(st) as seatCount FROM Screen s LEFT JOIN s.seats st GROUP BY s ORDER BY seatCount DESC")
    List<Object[]> findScreensWithSeatCount();

    // Find screens with no seats (for cleanup)
    @Query("SELECT s FROM Screen s WHERE s.seats IS EMPTY")
    List<Screen> findScreensWithoutSeats();

    // Find screens with no showtimes (for cleanup)
    @Query("SELECT s FROM Screen s WHERE s.showtimes IS EMPTY")
    List<Screen> findScreensWithoutShowtimes();

    // Find screens by theater and capacity range
    @Query("SELECT s FROM Screen s WHERE s.theater.id = :theaterId AND s.capacity BETWEEN :minCapacity AND :maxCapacity")
    List<Screen> findByTheaterIdAndCapacityBetween(@Param("theaterId") Long theaterId, 
                                                   @Param("minCapacity") Integer minCapacity, 
                                                   @Param("maxCapacity") Integer maxCapacity);
}
