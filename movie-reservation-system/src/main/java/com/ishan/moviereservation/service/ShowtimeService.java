package com.ishan.moviereservation.service;

import com.ishan.moviereservation.entity.Showtime;
import com.ishan.moviereservation.repository.ShowtimeRepository;
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
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;

    /**
     * Create a new showtime
     */
    public Showtime createShowtime(Showtime showtime) {
        log.info("Creating new showtime for movie: {} at screen: {}", 
            showtime.getMovie().getTitle(), showtime.getScreen().getName());
        
        // Validate movie is not null
        if (showtime.getMovie() == null || showtime.getMovie().getId() == null) {
            throw new RuntimeException("Showtime must be associated with a movie");
        }
        
        // Validate screen is not null
        if (showtime.getScreen() == null || showtime.getScreen().getId() == null) {
            throw new RuntimeException("Showtime must be associated with a screen");
        }
        
        // Validate start time is not null and in the future
        if (showtime.getStartTime() == null) {
            throw new RuntimeException("Showtime start time cannot be null");
        }
        
        if (showtime.getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Showtime start time must be in the future");
        }
        
        // Validate ticket price is positive
        if (showtime.getTicketPrice() == null || showtime.getTicketPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Showtime ticket price must be positive");
        }
        
        // Check for scheduling conflicts in the same screen
        List<Showtime> conflictingShowtimes = showtimeRepository.findConflictingShowtimes(
            showtime.getScreen().getId(), 
            showtime.getStartTime(), 
            showtime.getStartTime().plusMinutes(showtime.getMovie().getDurationMinutes())
        );
        
        if (!conflictingShowtimes.isEmpty()) {
            throw new RuntimeException("Scheduling conflict detected. Screen is already booked for this time period");
        }
        
        Showtime savedShowtime = showtimeRepository.save(showtime);
        log.info("Showtime created successfully with ID: {}", savedShowtime.getId());
        return savedShowtime;
    }

    /**
     * Find showtime by ID
     */
    @Transactional(readOnly = true)
    public Optional<Showtime> findById(Long id) {
        log.debug("Finding showtime by ID: {}", id);
        return showtimeRepository.findById(id);
    }

    /**
     * Find showtime by ID with reservations
     */
    @Transactional(readOnly = true)
    public Optional<Showtime> findByIdWithReservations(Long id) {
        log.debug("Finding showtime by ID with reservations: {}", id);
        return showtimeRepository.findByIdWithReservations(id);
    }

    /**
     * Get all showtimes
     */
    @Transactional(readOnly = true)
    public List<Showtime> findAll() {
        log.debug("Finding all showtimes");
        return showtimeRepository.findAll();
    }

    /**
     * Find showtimes by movie ID
     */
    @Transactional(readOnly = true)
    public List<Showtime> findByMovieId(Long movieId) {
        log.debug("Finding showtimes by movie ID: {}", movieId);
        return showtimeRepository.findByMovieId(movieId);
    }

    /**
     * Find showtimes by screen ID
     */
    @Transactional(readOnly = true)
    public List<Showtime> findByScreenId(Long screenId) {
        log.debug("Finding showtimes by screen ID: {}", screenId);
        return showtimeRepository.findByScreenId(screenId);
    }

    /**
     * Find showtimes by start time range
     */
    @Transactional(readOnly = true)
    public List<Showtime> findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Finding showtimes between: {} and {}", startTime, endTime);
        return showtimeRepository.findByStartTimeBetween(startTime, endTime);
    }

    /**
     * Find showtimes by ticket price range
     */
    @Transactional(readOnly = true)
    public List<Showtime> findByTicketPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        log.debug("Finding showtimes with ticket price between: {} and {}", minPrice, maxPrice);
        return showtimeRepository.findByTicketPriceBetween(minPrice, maxPrice);
    }

    /**
     * Find upcoming showtimes
     */
    @Transactional(readOnly = true)
    public List<Showtime> findUpcomingShowtimes() {
        log.debug("Finding upcoming showtimes");
        return showtimeRepository.findUpcomingShowtimes(LocalDateTime.now());
    }

    /**
     * Find showtimes for today
     */
    @Transactional(readOnly = true)
    public List<Showtime> findTodayShowtimes() {
        log.debug("Finding today's showtimes");
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime tomorrow = today.plusDays(1);
        return showtimeRepository.findByStartTimeBetween(today, tomorrow);
    }

    /**
     * Find showtimes for a specific date
     */
    @Transactional(readOnly = true)
    public List<Showtime> findShowtimesByDate(LocalDateTime date) {
        log.debug("Finding showtimes for date: {}", date);
        return showtimeRepository.findShowtimesByDate(date);
    }

    /**
     * Find showtimes by movie and date range
     */
    @Transactional(readOnly = true)
    public List<Showtime> findByMovieIdAndDateRange(Long movieId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Finding showtimes for movie: {} between: {} and {}", movieId, startDate, endDate);
        return showtimeRepository.findByMovieIdAndStartTimeBetween(movieId, startDate, endDate);
    }

    /**
     * Find showtimes by screen and date range
     */
    @Transactional(readOnly = true)
    public List<Showtime> findByScreenIdAndDateRange(Long screenId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Finding showtimes for screen: {} between: {} and {}", screenId, startDate, endDate);
        return showtimeRepository.findByScreenIdAndStartTimeBetween(screenId, startDate, endDate);
    }

    /**
     * Find showtimes that have reservations
     */
    @Transactional(readOnly = true)
    public List<Showtime> findShowtimesWithReservations() {
        log.debug("Finding showtimes with reservations");
        return showtimeRepository.findShowtimesWithReservations();
    }

    /**
     * Find showtimes with no reservations
     */
    @Transactional(readOnly = true)
    public List<Showtime> findShowtimesWithoutReservations() {
        log.debug("Finding showtimes without reservations");
        return showtimeRepository.findShowtimesWithoutReservations();
    }

    /**
     * Find showtimes with seat reservations
     */
    @Transactional(readOnly = true)
    public List<Showtime> findShowtimesWithSeatReservations() {
        log.debug("Finding showtimes with seat reservations");
        return showtimeRepository.findShowtimesWithSeatReservations();
    }

    /**
     * Find showtimes with no seat reservations
     */
    @Transactional(readOnly = true)
    public List<Showtime> findShowtimesWithoutSeatReservations() {
        log.debug("Finding showtimes without seat reservations");
        return showtimeRepository.findShowtimesWithoutSeatReservations();
    }

    /**
     * Check for scheduling conflicts
     */
    @Transactional(readOnly = true)
    public List<Showtime> findConflictingShowtimes(Long screenId, LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Checking for conflicts in screen: {} between: {} and {}", screenId, startTime, endTime);
        return showtimeRepository.findConflictingShowtimes(screenId, startTime, endTime);
    }

    /**
     * Update showtime
     */
    public Showtime updateShowtime(Showtime showtime) {
        log.info("Updating showtime with ID: {}", showtime.getId());
        
        if (!showtimeRepository.existsById(showtime.getId())) {
            throw new RuntimeException("Showtime not found with ID: " + showtime.getId());
        }
        
        // Validate movie is not null
        if (showtime.getMovie() == null || showtime.getMovie().getId() == null) {
            throw new RuntimeException("Showtime must be associated with a movie");
        }
        
        // Validate screen is not null
        if (showtime.getScreen() == null || showtime.getScreen().getId() == null) {
            throw new RuntimeException("Showtime must be associated with a screen");
        }
        
        // Validate start time is not null and in the future
        if (showtime.getStartTime() == null) {
            throw new RuntimeException("Showtime start time cannot be null");
        }
        
        if (showtime.getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Showtime start time must be in the future");
        }
        
        // Validate ticket price is positive
        if (showtime.getTicketPrice() == null || showtime.getTicketPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Showtime ticket price must be positive");
        }
        
        // Check for scheduling conflicts (excluding current showtime)
        List<Showtime> conflictingShowtimes = showtimeRepository.findConflictingShowtimes(
            showtime.getScreen().getId(), 
            showtime.getStartTime(), 
            showtime.getStartTime().plusMinutes(showtime.getMovie().getDurationMinutes())
        );
        
        // Remove current showtime from conflicts if it exists
        conflictingShowtimes.removeIf(st -> st.getId().equals(showtime.getId()));
        
        if (!conflictingShowtimes.isEmpty()) {
            throw new RuntimeException("Scheduling conflict detected. Screen is already booked for this time period");
        }
        
        Showtime updatedShowtime = showtimeRepository.save(showtime);
        log.info("Showtime updated successfully with ID: {}", updatedShowtime.getId());
        return updatedShowtime;
    }

    /**
     * Delete showtime by ID
     */
    public void deleteById(Long id) {
        log.info("Deleting showtime with ID: {}", id);
        
        if (!showtimeRepository.existsById(id)) {
            throw new RuntimeException("Showtime not found with ID: " + id);
        }
        
        // Check if showtime has reservations (business rule: can't delete showtime with reservations)
        Optional<Showtime> showtime = showtimeRepository.findByIdWithReservations(id);
        if (showtime.isPresent() && !showtime.get().getReservations().isEmpty()) {
            throw new RuntimeException("Cannot delete showtime with reservations. Showtime ID: " + id);
        }
        
        showtimeRepository.deleteById(id);
        log.info("Showtime deleted successfully with ID: {}", id);
    }

    /**
     * Get showtime count
     */
    @Transactional(readOnly = true)
    public long count() {
        log.debug("Getting total showtime count");
        return showtimeRepository.count();
    }

    /**
     * Get showtime count by movie
     */
    @Transactional(readOnly = true)
    public long countByMovie(Long movieId) {
        log.debug("Getting showtime count by movie ID: {}", movieId);
        return showtimeRepository.findByMovieId(movieId).size();
    }

    /**
     * Get showtime count by screen
     */
    @Transactional(readOnly = true)
    public long countByScreen(Long screenId) {
        log.debug("Getting showtime count by screen ID: {}", screenId);
        return showtimeRepository.findByScreenId(screenId).size();
    }

    /**
     * Get upcoming showtime count
     */
    @Transactional(readOnly = true)
    public long countUpcomingShowtimes() {
        log.debug("Getting upcoming showtime count");
        return showtimeRepository.findUpcomingShowtimes(LocalDateTime.now()).size();
    }

    /**
     * Get today's showtime count
     */
    @Transactional(readOnly = true)
    public long countTodayShowtimes() {
        log.debug("Getting today's showtime count");
        return findTodayShowtimes().size();
    }

    /**
     * Get showtime count with reservations
     */
    @Transactional(readOnly = true)
    public long countShowtimesWithReservations() {
        log.debug("Getting showtime count with reservations");
        return showtimeRepository.findShowtimesWithReservations().size();
    }

    /**
     * Get showtime count without reservations
     */
    @Transactional(readOnly = true)
    public long countShowtimesWithoutReservations() {
        log.debug("Getting showtime count without reservations");
        return showtimeRepository.findShowtimesWithoutReservations().size();
    }

    /**
     * Find showtimes ordered by start time
     */
    @Transactional(readOnly = true)
    public List<Showtime> findAllOrderByStartTime() {
        log.debug("Finding all showtimes ordered by start time");
        return showtimeRepository.findAllByOrderByStartTimeAsc();
    }

    /**
     * Find showtimes by movie ordered by start time
     */
    @Transactional(readOnly = true)
    public List<Showtime> findByMovieIdOrderByStartTime(Long movieId) {
        log.debug("Finding showtimes by movie ID ordered by start time: {}", movieId);
        return showtimeRepository.findByMovieIdOrderByStartTimeAsc(movieId);
    }

    /**
     * Find showtimes by screen ordered by start time
     */
    @Transactional(readOnly = true)
    public List<Showtime> findByScreenIdOrderByStartTime(Long screenId) {
        log.debug("Finding showtimes by screen ID ordered by start time: {}", screenId);
        return showtimeRepository.findByScreenIdOrderByStartTimeAsc(screenId);
    }

    /**
     * Find showtimes by ticket price less than
     */
    @Transactional(readOnly = true)
    public List<Showtime> findByTicketPriceLessThan(BigDecimal price) {
        log.debug("Finding showtimes with ticket price less than: {}", price);
        return showtimeRepository.findByTicketPriceLessThan(price);
    }

    /**
     * Find showtimes by ticket price greater than
     */
    @Transactional(readOnly = true)
    public List<Showtime> findByTicketPriceGreaterThan(BigDecimal price) {
        log.debug("Finding showtimes with ticket price greater than: {}", price);
        return showtimeRepository.findByTicketPriceGreaterThan(price);
    }

    /**
     * Find showtimes by start time after
     */
    @Transactional(readOnly = true)
    public List<Showtime> findByStartTimeAfter(LocalDateTime startTime) {
        log.debug("Finding showtimes after: {}", startTime);
        return showtimeRepository.findByStartTimeAfter(startTime);
    }

    /**
     * Find showtimes by start time before
     */
    @Transactional(readOnly = true)
    public List<Showtime> findByStartTimeBefore(LocalDateTime startTime) {
        log.debug("Finding showtimes before: {}", startTime);
        return showtimeRepository.findByStartTimeBefore(startTime);
    }

    /**
     * Find showtimes by movie and screen
     */
    @Transactional(readOnly = true)
    public List<Showtime> findByMovieIdAndScreenId(Long movieId, Long screenId) {
        log.debug("Finding showtimes by movie ID: {} and screen ID: {}", movieId, screenId);
        return showtimeRepository.findByMovieIdAndScreenId(movieId, screenId);
    }
}
