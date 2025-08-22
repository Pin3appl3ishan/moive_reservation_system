package com.ishan.moviereservation.service;

import com.ishan.moviereservation.dto.TheaterRequest;
import com.ishan.moviereservation.dto.TheaterResponse;
import com.ishan.moviereservation.entity.Theater;
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
public class TheaterService {

    private final TheaterRepository theaterRepository;

    /**
     * Create a new theater
     */
    public Theater createTheater(Theater theater) {
        log.info("Creating new theater with name: {}", theater.getName());
        
        // Check if theater with same name already exists
        if (theaterRepository.findByName(theater.getName()).isPresent()) {
            throw new RuntimeException("Theater with name already exists: " + theater.getName());
        }
        
        // Validate name is not empty
        if (theater.getName() == null || theater.getName().trim().isEmpty()) {
            throw new RuntimeException("Theater name cannot be empty");
        }
        
        Theater savedTheater = theaterRepository.save(theater);
        log.info("Theater created successfully with ID: {}", savedTheater.getId());
        return savedTheater;
    }

    /**
     * Find theater by ID
     */
    @Transactional(readOnly = true)
    public Optional<Theater> findById(Long id) {
        log.debug("Finding theater by ID: {}", id);
        return theaterRepository.findById(id);
    }

    /**
     * Find theater by name (exact match)
     */
    @Transactional(readOnly = true)
    public Optional<Theater> findByName(String name) {
        log.debug("Finding theater by name: {}", name);
        return theaterRepository.findByName(name);
    }

    /**
     * Find theater by ID with screens
     */
    @Transactional(readOnly = true)
    public Optional<Theater> findByIdWithScreens(Long id) {
        log.debug("Finding theater by ID with screens: {}", id);
        return theaterRepository.findByIdWithScreens(id);
    }

    /**
     * Get all theaters
     */
    @Transactional(readOnly = true)
    public List<Theater> findAll() {
        log.debug("Finding all theaters");
        return theaterRepository.findAll();
    }

    /**
     * Get all theaters ordered by name
     */
    @Transactional(readOnly = true)
    public List<Theater> findAllOrderByName() {
        log.debug("Finding all theaters ordered by name");
        return theaterRepository.findAllByOrderByNameAsc();
    }

    /**
     * Get all theaters ordered by creation date (newest first)
     */
    @Transactional(readOnly = true)
    public List<Theater> findAllOrderByCreatedAtDesc() {
        log.debug("Finding all theaters ordered by creation date");
        return theaterRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Search theaters by name
     */
    @Transactional(readOnly = true)
    public List<Theater> searchByName(String name) {
        log.debug("Searching theaters by name: {}", name);
        return theaterRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Search theaters by address
     */
    @Transactional(readOnly = true)
    public List<Theater> searchByAddress(String address) {
        log.debug("Searching theaters by address: {}", address);
        return theaterRepository.findByAddressContainingIgnoreCase(address);
    }

    /**
     * Search theaters by name and address
     */
    @Transactional(readOnly = true)
    public List<Theater> searchByNameAndAddress(String name, String address) {
        log.debug("Searching theaters by name: {} and address: {}", name, address);
        return theaterRepository.findByNameContainingIgnoreCaseAndAddressContainingIgnoreCase(name, address);
    }

    /**
     * Search theaters by name or address
     */
    @Transactional(readOnly = true)
    public List<Theater> searchByNameOrAddress(String searchTerm) {
        log.debug("Searching theaters by name or address: {}", searchTerm);
        return theaterRepository.findByNameOrAddressContainingIgnoreCase(searchTerm);
    }

    /**
     * Find theaters that have screens
     */
    @Transactional(readOnly = true)
    public List<Theater> findTheatersWithScreens() {
        log.debug("Finding theaters with screens");
        return theaterRepository.findTheatersWithScreens();
    }

    /**
     * Find theaters with no screens (for cleanup)
     */
    @Transactional(readOnly = true)
    public List<Theater> findTheatersWithoutScreens() {
        log.debug("Finding theaters without screens");
        return theaterRepository.findTheatersWithoutScreens();
    }

    /**
     * Get theaters with screen count
     */
    @Transactional(readOnly = true)
    public List<Object[]> findTheatersWithScreenCount() {
        log.debug("Finding theaters with screen count");
        return theaterRepository.findTheatersWithScreenCount();
    }

    /**
     * Update theater
     */
    public Theater updateTheater(Theater theater) {
        log.info("Updating theater with ID: {}", theater.getId());
        
        if (!theaterRepository.existsById(theater.getId())) {
            throw new RuntimeException("Theater not found with ID: " + theater.getId());
        }
        
        // Check if name is being changed and if new name already exists
        Optional<Theater> existingTheater = theaterRepository.findById(theater.getId());
        if (existingTheater.isPresent() && !existingTheater.get().getName().equals(theater.getName())) {
            if (theaterRepository.findByName(theater.getName()).isPresent()) {
                throw new RuntimeException("Theater with name already exists: " + theater.getName());
            }
        }
        
        // Validate name is not empty
        if (theater.getName() == null || theater.getName().trim().isEmpty()) {
            throw new RuntimeException("Theater name cannot be empty");
        }
        
        Theater updatedTheater = theaterRepository.save(theater);
        log.info("Theater updated successfully with ID: {}", updatedTheater.getId());
        return updatedTheater;
    }

    /**
     * Delete theater by ID
     */
    public void deleteById(Long id) {
        log.info("Deleting theater with ID: {}", id);
        
        if (!theaterRepository.existsById(id)) {
            throw new RuntimeException("Theater not found with ID: " + id);
        }
        
        // Check if theater has screens (business rule: can't delete theater with screens)
        Optional<Theater> theater = theaterRepository.findByIdWithScreens(id);
        if (theater.isPresent() && !theater.get().getScreens().isEmpty()) {
            throw new RuntimeException("Cannot delete theater with screens. Theater ID: " + id);
        }
        
        theaterRepository.deleteById(id);
        log.info("Theater deleted successfully with ID: {}", id);
    }

    /**
     * Get theater count
     */
    @Transactional(readOnly = true)
    public long count() {
        log.debug("Getting total theater count");
        return theaterRepository.count();
    }

    /**
     * Get theater count with screens
     */
    @Transactional(readOnly = true)
    public long countWithScreens() {
        log.debug("Getting theater count with screens");
        return theaterRepository.findTheatersWithScreens().size();
    }

    /**
     * Get theater count without screens
     */
    @Transactional(readOnly = true)
    public long countWithoutScreens() {
        log.debug("Getting theater count without screens");
        return theaterRepository.findTheatersWithoutScreens().size();
    }

    /**
     * Check if theater name exists
     */
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        log.debug("Checking if theater name exists: {}", name);
        return theaterRepository.findByName(name).isPresent();
    }

    /**
     * Get theaters by location (address contains)
     */
    @Transactional(readOnly = true)
    public List<Theater> findByLocation(String location) {
        log.debug("Finding theaters by location: {}", location);
        return theaterRepository.findByAddressContainingIgnoreCase(location);
    }

    /**
     * Get theaters with screens ordered by screen count
     */
    @Transactional(readOnly = true)
    public List<Object[]> findTheatersOrderedByScreenCount() {
        log.debug("Finding theaters ordered by screen count");
        return theaterRepository.findTheatersWithScreenCount();
    }

    // ========== DTO-based methods for API controllers ==========

    /**
     * Create theater from DTO
     */
    public TheaterResponse createTheater(TheaterRequest request) {
        log.info("Creating new theater from DTO: {}", request.getName());
        
        Theater theater = new Theater();
        theater.setName(request.getName());
        theater.setAddress(request.getAddress());
        
        Theater savedTheater = createTheater(theater);
        return convertToResponse(savedTheater);
    }

    /**
     * Update theater from DTO
     */
    public TheaterResponse updateTheater(Long id, TheaterRequest request) {
        log.info("Updating theater from DTO with ID: {}", id);
        
        Theater existingTheater = findById(id)
            .orElseThrow(() -> new RuntimeException("Theater not found with ID: " + id));
        
        existingTheater.setName(request.getName());
        existingTheater.setAddress(request.getAddress());
        
        Theater updatedTheater = updateTheater(existingTheater);
        return convertToResponse(updatedTheater);
    }

    /**
     * Delete theater by ID
     */
    public void deleteTheater(Long id) {
        log.info("Deleting theater with ID: {}", id);
        deleteById(id);
    }

    /**
     * Get all theaters with pagination
     */
    @Transactional(readOnly = true)
    public Page<TheaterResponse> getAllTheaters(Pageable pageable) {
        log.debug("Getting all theaters with pagination");
        return theaterRepository.findAll(pageable)
            .map(this::convertToResponse);
    }

    /**
     * Get theater by ID
     */
    @Transactional(readOnly = true)
    public TheaterResponse getTheaterById(Long id) {
        log.debug("Getting theater by ID: {}", id);
        Theater theater = findById(id)
            .orElseThrow(() -> new RuntimeException("Theater not found with ID: " + id));
        return convertToResponse(theater);
    }

    /**
     * Convert Theater entity to TheaterResponse DTO
     */
    private TheaterResponse convertToResponse(Theater theater) {
        return new TheaterResponse(
            theater.getId(),
            theater.getName(),
            theater.getAddress(),
            null, // city not available in entity
            null, // state not available in entity
            null, // zipCode not available in entity
            null, // phoneNumber not available in entity
            null, // screens will be populated separately if needed
            theater.getCreatedAt(),
            theater.getUpdatedAt()
        );
    }
}
