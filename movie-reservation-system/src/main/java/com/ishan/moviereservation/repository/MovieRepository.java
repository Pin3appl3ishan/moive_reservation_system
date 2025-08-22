package com.ishan.moviereservation.repository;

import com.ishan.moviereservation.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Find by title (exact match)
    Optional<Movie> findByTitle(String title);

    // Find by title containing (for search functionality)
    List<Movie> findByTitleContainingIgnoreCase(String title);

    // Find by genre
    List<Movie> findByGenre(String genre);

    // Find by genre containing (for search functionality)
    List<Movie> findByGenreContainingIgnoreCase(String genre);

    // Find movies with duration less than specified minutes
    List<Movie> findByDurationMinutesLessThan(Integer durationMinutes);

    // Find movies with duration greater than specified minutes
    List<Movie> findByDurationMinutesGreaterThan(Integer durationMinutes);

    // Find movies by duration range
    List<Movie> findByDurationMinutesBetween(Integer minDuration, Integer maxDuration);

    // Find movies ordered by title
    List<Movie> findAllByOrderByTitleAsc();

    // Find movies ordered by creation date (newest first)
    List<Movie> findAllByOrderByCreatedAtDesc();

    // Custom query to find movies with their showtimes
    @Query("SELECT m FROM Movie m LEFT JOIN FETCH m.showtimes WHERE m.id = :movieId")
    Optional<Movie> findByIdWithShowtimes(@Param("movieId") Long movieId);

    // Find movies that have showtimes scheduled
    @Query("SELECT DISTINCT m FROM Movie m WHERE m.showtimes IS NOT EMPTY")
    List<Movie> findMoviesWithShowtimes();

    // Find movies by title and genre
    List<Movie> findByTitleContainingIgnoreCaseAndGenreContainingIgnoreCase(String title, String genre);

    // Custom query to find movies with showtime count
    @Query("SELECT m, COUNT(s) as showtimeCount FROM Movie m LEFT JOIN m.showtimes s GROUP BY m ORDER BY showtimeCount DESC")
    List<Object[]> findMoviesWithShowtimeCount();

    // Find movies with no showtimes (for cleanup)
    @Query("SELECT m FROM Movie m WHERE m.showtimes IS EMPTY")
    List<Movie> findMoviesWithoutShowtimes();
}
