package com.ishan.moviereservation.controller;

import com.ishan.moviereservation.dto.MovieResponse;
import com.ishan.moviereservation.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
@Slf4j
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<Page<MovieResponse>> getAllMovies(Pageable pageable) {
        log.info("Public request: Fetching all movies with pagination");
        Page<MovieResponse> movies = movieService.getAllMovies(pageable);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getMovieById(@PathVariable Long id) {
        log.info("Public request: Fetching movie with id: {}", id);
        MovieResponse movie = movieService.getMovieById(id);
        return ResponseEntity.ok(movie);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieResponse>> searchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre) {
        
        log.info("Public request: Searching movies with title: {}, genre: {}", title, genre);
        
        List<MovieResponse> movies;
        if (title != null && genre != null) {
            movies = movieService.searchByTitleAndGenre(title, genre)
                .stream()
                .map(this::convertToResponse)
                .toList();
        } else if (title != null) {
            movies = movieService.searchByTitle(title)
                .stream()
                .map(this::convertToResponse)
                .toList();
        } else if (genre != null) {
            movies = movieService.searchByGenre(genre)
                .stream()
                .map(this::convertToResponse)
                .toList();
        } else {
            movies = movieService.findAllOrderByTitle()
                .stream()
                .map(this::convertToResponse)
                .toList();
        }
        
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<MovieResponse>> getMoviesByGenre(@PathVariable String genre) {
        log.info("Public request: Fetching movies by genre: {}", genre);
        List<MovieResponse> movies = movieService.findByGenre(genre)
            .stream()
            .map(this::convertToResponse)
            .toList();
        return ResponseEntity.ok(movies);
    }

    /**
     * Convert Movie entity to MovieResponse DTO
     */
    private MovieResponse convertToResponse(com.ishan.moviereservation.entity.Movie movie) {
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
