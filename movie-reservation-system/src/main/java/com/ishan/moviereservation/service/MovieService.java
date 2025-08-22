package com.ishan.moviereservation.service;

import com.ishan.moviereservation.dto.MovieRequest;
import com.ishan.moviereservation.dto.MovieResponse;
import com.ishan.moviereservation.entity.Movie;
import com.ishan.moviereservation.repository.MovieRepository;
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
public class MovieService {

    private final MovieRepository movieRepository;

    /**
     * Create a new movie
     */
    public Movie createMovie(Movie movie) {
        log.info("Creating new movie with title: {}", movie.getTitle());
        
        // Check if movie with same title already exists
        if (movieRepository.findByTitle(movie.getTitle()).isPresent()) {
            throw new RuntimeException("Movie with title already exists: " + movie.getTitle());
        }
        
        // Validate duration
        if (movie.getDurationMinutes() != null && movie.getDurationMinutes() <= 0) {
            throw new RuntimeException("Movie duration must be positive");
        }
        
        Movie savedMovie = movieRepository.save(movie);
        log.info("Movie created successfully with ID: {}", savedMovie.getId());
        return savedMovie;
    }

    /**
     * Find movie by ID
     */
    @Transactional(readOnly = true)
    public Optional<Movie> findById(Long id) {
        log.debug("Finding movie by ID: {}", id);
        return movieRepository.findById(id);
    }

    /**
     * Find movie by title (exact match)
     */
    @Transactional(readOnly = true)
    public Optional<Movie> findByTitle(String title) {
        log.debug("Finding movie by title: {}", title);
        return movieRepository.findByTitle(title);
    }

    /**
     * Find movie by ID with showtimes
     */
    @Transactional(readOnly = true)
    public Optional<Movie> findByIdWithShowtimes(Long id) {
        log.debug("Finding movie by ID with showtimes: {}", id);
        return movieRepository.findByIdWithShowtimes(id);
    }

    /**
     * Get all movies
     */
    @Transactional(readOnly = true)
    public List<Movie> findAll() {
        log.debug("Finding all movies");
        return movieRepository.findAll();
    }

    /**
     * Get all movies ordered by title
     */
    @Transactional(readOnly = true)
    public List<Movie> findAllOrderByTitle() {
        log.debug("Finding all movies ordered by title");
        return movieRepository.findAllByOrderByTitleAsc();
    }

    /**
     * Get all movies ordered by creation date (newest first)
     */
    @Transactional(readOnly = true)
    public List<Movie> findAllOrderByCreatedAtDesc() {
        log.debug("Finding all movies ordered by creation date");
        return movieRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Search movies by title
     */
    @Transactional(readOnly = true)
    public List<Movie> searchByTitle(String title) {
        log.debug("Searching movies by title: {}", title);
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }

    /**
     * Find movies by genre
     */
    @Transactional(readOnly = true)
    public List<Movie> findByGenre(String genre) {
        log.debug("Finding movies by genre: {}", genre);
        return movieRepository.findByGenre(genre);
    }

    /**
     * Search movies by genre
     */
    @Transactional(readOnly = true)
    public List<Movie> searchByGenre(String genre) {
        log.debug("Searching movies by genre: {}", genre);
        return movieRepository.findByGenreContainingIgnoreCase(genre);
    }

    /**
     * Find movies by title and genre
     */
    @Transactional(readOnly = true)
    public List<Movie> searchByTitleAndGenre(String title, String genre) {
        log.debug("Searching movies by title: {} and genre: {}", title, genre);
        return movieRepository.findByTitleContainingIgnoreCaseAndGenreContainingIgnoreCase(title, genre);
    }

    /**
     * Find movies with duration less than specified minutes
     */
    @Transactional(readOnly = true)
    public List<Movie> findByDurationLessThan(Integer durationMinutes) {
        log.debug("Finding movies with duration less than: {} minutes", durationMinutes);
        return movieRepository.findByDurationMinutesLessThan(durationMinutes);
    }

    /**
     * Find movies with duration greater than specified minutes
     */
    @Transactional(readOnly = true)
    public List<Movie> findByDurationGreaterThan(Integer durationMinutes) {
        log.debug("Finding movies with duration greater than: {} minutes", durationMinutes);
        return movieRepository.findByDurationMinutesGreaterThan(durationMinutes);
    }

    /**
     * Find movies by duration range
     */
    @Transactional(readOnly = true)
    public List<Movie> findByDurationBetween(Integer minDuration, Integer maxDuration) {
        log.debug("Finding movies with duration between: {} and {} minutes", minDuration, maxDuration);
        return movieRepository.findByDurationMinutesBetween(minDuration, maxDuration);
    }

    /**
     * Find movies that have showtimes scheduled
     */
    @Transactional(readOnly = true)
    public List<Movie> findMoviesWithShowtimes() {
        log.debug("Finding movies with showtimes");
        return movieRepository.findMoviesWithShowtimes();
    }

    /**
     * Find movies with no showtimes (for cleanup)
     */
    @Transactional(readOnly = true)
    public List<Movie> findMoviesWithoutShowtimes() {
        log.debug("Finding movies without showtimes");
        return movieRepository.findMoviesWithoutShowtimes();
    }

    /**
     * Get movies with showtime count
     */
    @Transactional(readOnly = true)
    public List<Object[]> findMoviesWithShowtimeCount() {
        log.debug("Finding movies with showtime count");
        return movieRepository.findMoviesWithShowtimeCount();
    }

    /**
     * Update movie
     */
    public Movie updateMovie(Movie movie) {
        log.info("Updating movie with ID: {}", movie.getId());
        
        if (!movieRepository.existsById(movie.getId())) {
            throw new RuntimeException("Movie not found with ID: " + movie.getId());
        }
        
        // Check if title is being changed and if new title already exists
        Optional<Movie> existingMovie = movieRepository.findById(movie.getId());
        if (existingMovie.isPresent() && !existingMovie.get().getTitle().equals(movie.getTitle())) {
            if (movieRepository.findByTitle(movie.getTitle()).isPresent()) {
                throw new RuntimeException("Movie with title already exists: " + movie.getTitle());
            }
        }
        
        // Validate duration
        if (movie.getDurationMinutes() != null && movie.getDurationMinutes() <= 0) {
            throw new RuntimeException("Movie duration must be positive");
        }
        
        Movie updatedMovie = movieRepository.save(movie);
        log.info("Movie updated successfully with ID: {}", updatedMovie.getId());
        return updatedMovie;
    }

    /**
     * Delete movie by ID
     */
    public void deleteById(Long id) {
        log.info("Deleting movie with ID: {}", id);
        
        if (!movieRepository.existsById(id)) {
            throw new RuntimeException("Movie not found with ID: " + id);
        }
        
        // Check if movie has showtimes (business rule: can't delete movie with scheduled showtimes)
        Optional<Movie> movie = movieRepository.findByIdWithShowtimes(id);
        if (movie.isPresent() && !movie.get().getShowtimes().isEmpty()) {
            throw new RuntimeException("Cannot delete movie with scheduled showtimes. Movie ID: " + id);
        }
        
        movieRepository.deleteById(id);
        log.info("Movie deleted successfully with ID: {}", id);
    }

    /**
     * Get movie count
     */
    @Transactional(readOnly = true)
    public long count() {
        log.debug("Getting total movie count");
        return movieRepository.count();
    }

    /**
     * Get movie count by genre
     */
    @Transactional(readOnly = true)
    public long countByGenre(String genre) {
        log.debug("Getting movie count by genre: {}", genre);
        return movieRepository.findByGenre(genre).size();
    }

    /**
     * Get movie count with showtimes
     */
    @Transactional(readOnly = true)
    public long countWithShowtimes() {
        log.debug("Getting movie count with showtimes");
        return movieRepository.findMoviesWithShowtimes().size();
    }

    /**
     * Get movie count without showtimes
     */
    @Transactional(readOnly = true)
    public long countWithoutShowtimes() {
        log.debug("Getting movie count without showtimes");
        return movieRepository.findMoviesWithoutShowtimes().size();
    }

    // ========== DTO-based methods for API controllers ==========

    /**
     * Create movie from DTO
     */
    public MovieResponse createMovie(MovieRequest request) {
        log.info("Creating new movie from DTO: {}", request.getTitle());
        
        Movie movie = new Movie();
        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setGenre(request.getGenre());
        movie.setDurationMinutes(request.getDurationMinutes());
        
        Movie savedMovie = createMovie(movie);
        return convertToResponse(savedMovie);
    }

    /**
     * Update movie from DTO
     */
    public MovieResponse updateMovie(Long id, MovieRequest request) {
        log.info("Updating movie from DTO with ID: {}", id);
        
        Movie existingMovie = findById(id)
            .orElseThrow(() -> new RuntimeException("Movie not found with ID: " + id));
        
        existingMovie.setTitle(request.getTitle());
        existingMovie.setDescription(request.getDescription());
        existingMovie.setPosterUrl(request.getPosterUrl());
        existingMovie.setGenre(request.getGenre());
        existingMovie.setDurationMinutes(request.getDurationMinutes());
        
        Movie updatedMovie = updateMovie(existingMovie);
        return convertToResponse(updatedMovie);
    }

    /**
     * Delete movie by ID
     */
    public void deleteMovie(Long id) {
        log.info("Deleting movie with ID: {}", id);
        deleteById(id);
    }

    /**
     * Get all movies with pagination
     */
    @Transactional(readOnly = true)
    public Page<MovieResponse> getAllMovies(Pageable pageable) {
        log.debug("Getting all movies with pagination");
        return movieRepository.findAll(pageable)
            .map(this::convertToResponse);
    }

    /**
     * Get movie by ID
     */
    @Transactional(readOnly = true)
    public MovieResponse getMovieById(Long id) {
        log.debug("Getting movie by ID: {}", id);
        Movie movie = findById(id)
            .orElseThrow(() -> new RuntimeException("Movie not found with ID: " + id));
        return convertToResponse(movie);
    }

    /**
     * Convert Movie entity to MovieResponse DTO
     */
    private MovieResponse convertToResponse(Movie movie) {
        return new MovieResponse(
            movie.getId(),
            movie.getTitle(),
            movie.getDescription(),
            movie.getPosterUrl(),
            movie.getGenre(),
            movie.getDurationMinutes(),
            movie.getCreatedAt(),
            movie.getUpdatedAt()
        );
    }
}
