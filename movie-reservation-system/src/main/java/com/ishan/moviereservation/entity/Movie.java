package com.ishan.moviereservation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"showtimes"})
@EqualsAndHashCode(callSuper = true, exclude = {"showtimes"})
public class Movie extends BaseEntity {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "poster_url", columnDefinition = "TEXT")
    private String posterUrl;

    @Positive(message = "Duration must be positive")
    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Size(max = 100, message = "Genre must not exceed 100 characters")
    @Column(name = "genre", length = 100)
    private String genre;

    // Relationships
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Showtime> showtimes = new ArrayList<>();

    // Custom constructor for basic fields
    public Movie(String title, String description, Integer durationMinutes, String genre) {
        this.title = title;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.genre = genre;
    }

    // Helper methods
    public void addShowtime(Showtime showtime) {
        showtimes.add(showtime);
        showtime.setMovie(this);
    }

    public void removeShowtime(Showtime showtime) {
        showtimes.remove(showtime);
        showtime.setMovie(null);
    }
}
