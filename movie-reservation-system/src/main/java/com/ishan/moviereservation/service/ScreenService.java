package com.ishan.moviereservation.service;

import com.ishan.moviereservation.dto.ScreenRequest;
import com.ishan.moviereservation.dto.ScreenResponse;
import com.ishan.moviereservation.entity.Screen;
import com.ishan.moviereservation.entity.Theater;
import com.ishan.moviereservation.repository.ScreenRepository;
import com.ishan.moviereservation.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ScreenService {

    private final ScreenRepository screenRepository;
    private final TheaterRepository theaterRepository;

    /**
     * Create a new screen
     */
    public Screen createScreen(Screen screen) {
        log.info("Creating new screen with name: {} for theater: {}", screen.getName(), screen.getTheater().getName());
        
        // Validate capacity is positive
        if (screen.getCapacity() == null || screen.getCapacity() <= 0) {
            throw new RuntimeException("Screen capacity must be positive");
        }
        
        // Validate theater is not null
        if (screen.getTheater() == null || screen.getTheater().getId() == null) {
            throw new RuntimeException("Screen must be associated with a theater");
        }
        
        // Check if screen with same name already exists in the same theater
        List<Screen> existingScreens = screenRepository.findByTheaterIdAndNameContainingIgnoreCase(
            screen.getTheater().getId(), screen.getName());
        if (!existingScreens.isEmpty()) {
            throw new RuntimeException("Screen with name already exists in theater: " + screen.getName());
        }
        
        Screen savedScreen = screenRepository.save(screen);
        log.info("Screen created successfully with ID: {}", savedScreen.getId());
        return savedScreen;
    }

    /**
     * Find screen by ID
     */
    @Transactional(readOnly = true)
    public Optional<Screen> findById(Long id) {
        log.debug("Finding screen by ID: {}", id);
        return screenRepository.findById(id);
    }

    /**
     * Find screen by name (exact match)
     */
    @Transactional(readOnly = true)
    public Optional<Screen> findByName(String name) {
        log.debug("Finding screen by name: {}", name);
        return screenRepository.findByName(name);
    }

    /**
     * Find screen by ID with seats
     */
    @Transactional(readOnly = true)
    public Optional<Screen> findByIdWithSeats(Long id) {
        log.debug("Finding screen by ID with seats: {}", id);
        return screenRepository.findByIdWithSeats(id);
    }

    /**
     * Find screen by ID with showtimes
     */
    @Transactional(readOnly = true)
    public Optional<Screen> findByIdWithShowtimes(Long id) {
        log.debug("Finding screen by ID with showtimes: {}", id);
        return screenRepository.findByIdWithShowtimes(id);
    }

    /**
     * Get all screens
     */
    @Transactional(readOnly = true)
    public List<Screen> findAll() {
        log.debug("Finding all screens");
        return screenRepository.findAll();
    }

    /**
     * Get all screens ordered by capacity
     */
    @Transactional(readOnly = true)
    public List<Screen> findAllOrderByCapacity() {
        log.debug("Finding all screens ordered by capacity");
        return screenRepository.findAllByOrderByCapacityAsc();
    }

    /**
     * Get all screens ordered by name
     */
    @Transactional(readOnly = true)
    public List<Screen> findAllOrderByName() {
        log.debug("Finding all screens ordered by name");
        return screenRepository.findAllByOrderByNameAsc();
    }

    /**
     * Find screens by theater ID
     */
    @Transactional(readOnly = true)
    public List<Screen> findByTheaterId(Long theaterId) {
        log.debug("Finding screens by theater ID: {}", theaterId);
        return screenRepository.findByTheaterId(theaterId);
    }

    /**
     * Find screens by theater ID and name
     */
    @Transactional(readOnly = true)
    public List<Screen> findByTheaterIdAndName(Long theaterId, String name) {
        log.debug("Finding screens by theater ID: {} and name: {}", theaterId, name);
        return screenRepository.findByTheaterIdAndNameContainingIgnoreCase(theaterId, name);
    }

    /**
     * Search screens by name
     */
    @Transactional(readOnly = true)
    public List<Screen> searchByName(String name) {
        log.debug("Searching screens by name: {}", name);
        return screenRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Find screens by capacity range
     */
    @Transactional(readOnly = true)
    public List<Screen> findByCapacityBetween(Integer minCapacity, Integer maxCapacity) {
        log.debug("Finding screens with capacity between: {} and {}", minCapacity, maxCapacity);
        return screenRepository.findByCapacityBetween(minCapacity, maxCapacity);
    }

    /**
     * Find screens with capacity greater than specified
     */
    @Transactional(readOnly = true)
    public List<Screen> findByCapacityGreaterThan(Integer capacity) {
        log.debug("Finding screens with capacity greater than: {}", capacity);
        return screenRepository.findByCapacityGreaterThan(capacity);
    }

    /**
     * Find screens with capacity less than specified
     */
    @Transactional(readOnly = true)
    public List<Screen> findByCapacityLessThan(Integer capacity) {
        log.debug("Finding screens with capacity less than: {}", capacity);
        return screenRepository.findByCapacityLessThan(capacity);
    }

    /**
     * Find screens by theater and capacity range
     */
    @Transactional(readOnly = true)
    public List<Screen> findByTheaterIdAndCapacityBetween(Long theaterId, Integer minCapacity, Integer maxCapacity) {
        log.debug("Finding screens by theater ID: {} with capacity between: {} and {}", theaterId, minCapacity, maxCapacity);
        return screenRepository.findByTheaterIdAndCapacityBetween(theaterId, minCapacity, maxCapacity);
    }

    /**
     * Find screens that have seats
     */
    @Transactional(readOnly = true)
    public List<Screen> findScreensWithSeats() {
        log.debug("Finding screens with seats");
        return screenRepository.findScreensWithSeats();
    }

    /**
     * Find screens that have showtimes
     */
    @Transactional(readOnly = true)
    public List<Screen> findScreensWithShowtimes() {
        log.debug("Finding screens with showtimes");
        return screenRepository.findScreensWithShowtimes();
    }

    /**
     * Find screens with no seats (for cleanup)
     */
    @Transactional(readOnly = true)
    public List<Screen> findScreensWithoutSeats() {
        log.debug("Finding screens without seats");
        return screenRepository.findScreensWithoutSeats();
    }

    /**
     * Find screens with no showtimes (for cleanup)
     */
    @Transactional(readOnly = true)
    public List<Screen> findScreensWithoutShowtimes() {
        log.debug("Finding screens without showtimes");
        return screenRepository.findScreensWithoutShowtimes();
    }

    /**
     * Get screens with seat count
     */
    @Transactional(readOnly = true)
    public List<Object[]> findScreensWithSeatCount() {
        log.debug("Finding screens with seat count");
        return screenRepository.findScreensWithSeatCount();
    }

    /**
     * Update screen
     */
    public Screen updateScreen(Screen screen) {
        log.info("Updating screen with ID: {}", screen.getId());
        
        if (!screenRepository.existsById(screen.getId())) {
            throw new RuntimeException("Screen not found with ID: " + screen.getId());
        }
        
        // Validate capacity is positive
        if (screen.getCapacity() == null || screen.getCapacity() <= 0) {
            throw new RuntimeException("Screen capacity must be positive");
        }
        
        // Validate theater is not null
        if (screen.getTheater() == null || screen.getTheater().getId() == null) {
            throw new RuntimeException("Screen must be associated with a theater");
        }
        
        // Check if name is being changed and if new name already exists in the same theater
        Optional<Screen> existingScreen = screenRepository.findById(screen.getId());
        if (existingScreen.isPresent() && !existingScreen.get().getName().equals(screen.getName())) {
            List<Screen> existingScreens = screenRepository.findByTheaterIdAndNameContainingIgnoreCase(
                screen.getTheater().getId(), screen.getName());
            if (!existingScreens.isEmpty()) {
                throw new RuntimeException("Screen with name already exists in theater: " + screen.getName());
            }
        }
        
        Screen updatedScreen = screenRepository.save(screen);
        log.info("Screen updated successfully with ID: {}", updatedScreen.getId());
        return updatedScreen;
    }

    /**
     * Delete screen by ID
     */
    public void deleteById(Long id) {
        log.info("Deleting screen with ID: {}", id);
        
        if (!screenRepository.existsById(id)) {
            throw new RuntimeException("Screen not found with ID: " + id);
        }
        
        // Check if screen has seats (business rule: can't delete screen with seats)
        Optional<Screen> screen = screenRepository.findByIdWithSeats(id);
        if (screen.isPresent() && !screen.get().getSeats().isEmpty()) {
            throw new RuntimeException("Cannot delete screen with seats. Screen ID: " + id);
        }
        
        // Check if screen has showtimes (business rule: can't delete screen with showtimes)
        Optional<Screen> screenWithShowtimes = screenRepository.findByIdWithShowtimes(id);
        if (screenWithShowtimes.isPresent() && !screenWithShowtimes.get().getShowtimes().isEmpty()) {
            throw new RuntimeException("Cannot delete screen with showtimes. Screen ID: " + id);
        }
        
        screenRepository.deleteById(id);
        log.info("Screen deleted successfully with ID: {}", id);
    }

    /**
     * Get screen count
     */
    @Transactional(readOnly = true)
    public long count() {
        log.debug("Getting total screen count");
        return screenRepository.count();
    }

    /**
     * Get screen count by theater
     */
    @Transactional(readOnly = true)
    public long countByTheater(Long theaterId) {
        log.debug("Getting screen count by theater ID: {}", theaterId);
        return screenRepository.findByTheaterId(theaterId).size();
    }

    /**
     * Get screen count with seats
     */
    @Transactional(readOnly = true)
    public long countWithSeats() {
        log.debug("Getting screen count with seats");
        return screenRepository.findScreensWithSeats().size();
    }

    /**
     * Get screen count with showtimes
     */
    @Transactional(readOnly = true)
    public long countWithShowtimes() {
        log.debug("Getting screen count with showtimes");
        return screenRepository.findScreensWithShowtimes().size();
    }

    /**
     * Get screen count without seats
     */
    @Transactional(readOnly = true)
    public long countWithoutSeats() {
        log.debug("Getting screen count without seats");
        return screenRepository.findScreensWithoutSeats().size();
    }

    /**
     * Get screen count without showtimes
     */
    @Transactional(readOnly = true)
    public long countWithoutShowtimes() {
        log.debug("Getting screen count without showtimes");
        return screenRepository.findScreensWithoutShowtimes().size();
    }

    /**
     * Find screens by theater ordered by capacity
     */
    @Transactional(readOnly = true)
    public List<Screen> findByTheaterIdOrderByCapacity(Long theaterId) {
        log.debug("Finding screens by theater ID ordered by capacity: {}", theaterId);
        List<Screen> screens = screenRepository.findByTheaterId(theaterId);
        screens.sort((s1, s2) -> Integer.compare(s1.getCapacity(), s2.getCapacity()));
        return screens;
    }

    /**
     * Find screens by theater ordered by name
     */
    @Transactional(readOnly = true)
    public List<Screen> findByTheaterIdOrderByName(Long theaterId) {
        log.debug("Finding screens by theater ID ordered by name: {}", theaterId);
        List<Screen> screens = screenRepository.findByTheaterId(theaterId);
        screens.sort((s1, s2) -> {
            String name1 = s1.getName() != null ? s1.getName() : "";
            String name2 = s2.getName() != null ? s2.getName() : "";
            return name1.compareToIgnoreCase(name2);
        });
        return screens;
    }

    // ========== DTO-based methods for API controllers ==========

    /**
     * Create screen from DTO
     */
    public ScreenResponse createScreen(Long theaterId, ScreenRequest request) {
        log.info("Creating new screen from DTO for theater: {}", theaterId);
        
        // Find the theater
        Theater theater = theaterRepository.findById(theaterId)
            .orElseThrow(() -> new RuntimeException("Theater not found with ID: " + theaterId));
        
        // Calculate total capacity
        int totalCapacity = request.getNumberOfRows() * request.getSeatsPerRow();
        
        Screen screen = new Screen();
        screen.setName(request.getName());
        screen.setCapacity(totalCapacity);
        screen.setTheater(theater);
        
        Screen savedScreen = createScreen(screen);
        return convertToResponse(savedScreen);
    }

    /**
     * Update screen from DTO
     */
    public ScreenResponse updateScreen(Long id, ScreenRequest request) {
        log.info("Updating screen from DTO with ID: {}", id);
        
        Screen existingScreen = findById(id)
            .orElseThrow(() -> new RuntimeException("Screen not found with ID: " + id));
        
        // Calculate total capacity
        int totalCapacity = request.getNumberOfRows() * request.getSeatsPerRow();
        
        existingScreen.setName(request.getName());
        existingScreen.setCapacity(totalCapacity);
        
        Screen updatedScreen = updateScreen(existingScreen);
        return convertToResponse(updatedScreen);
    }

    /**
     * Delete screen by ID
     */
    public void deleteScreen(Long id) {
        log.info("Deleting screen with ID: {}", id);
        deleteById(id);
    }

    /**
     * Get all screens with pagination
     */
    @Transactional(readOnly = true)
    public Page<ScreenResponse> getAllScreens(Pageable pageable) {
        log.debug("Getting all screens with pagination");
        return screenRepository.findAll(pageable)
            .map(this::convertToResponse);
    }

    /**
     * Get screen by ID
     */
    @Transactional(readOnly = true)
    public ScreenResponse getScreenById(Long id) {
        log.debug("Getting screen by ID: {}", id);
        Screen screen = findById(id)
            .orElseThrow(() -> new RuntimeException("Screen not found with ID: " + id));
        return convertToResponse(screen);
    }

    /**
     * Get screens by theater ID
     */
    @Transactional(readOnly = true)
    public List<ScreenResponse> getScreensByTheater(Long theaterId) {
        log.debug("Getting screens by theater ID: {}", theaterId);
        return findByTheaterId(theaterId)
            .stream()
            .map(this::convertToResponse)
            .toList();
    }

    /**
     * Convert Screen entity to ScreenResponse DTO
     */
    private ScreenResponse convertToResponse(Screen screen) {
        // Calculate rows and seats per row from capacity
        int totalSeats = screen.getCapacity();
        int seatsPerRow = 10; // Default assumption, could be stored separately
        int numberOfRows = totalSeats / seatsPerRow;
        if (totalSeats % seatsPerRow != 0) {
            numberOfRows++; // Round up for partial rows
        }
        
        return new ScreenResponse(
            screen.getId(),
            screen.getName(),
            numberOfRows,
            seatsPerRow,
            totalSeats,
            null, // description not available in entity
            screen.getTheater().getId(),
            screen.getTheater().getName(),
            screen.getCreatedAt(),
            screen.getUpdatedAt()
        );
    }
}
