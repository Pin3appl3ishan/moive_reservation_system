package com.ishan.moviereservation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "screens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"seats", "showtimes"})
@EqualsAndHashCode(callSuper = true, exclude = {"seats", "showtimes"})
public class Screen extends BaseEntity {

    @NotNull(message = "Theater is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    @Size(max = 100, message = "Screen name must not exceed 100 characters")
    @Column(name = "name", length = 100)
    private String name;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be positive")
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    // Relationships
    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Seat> seats = new ArrayList<>();

    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Showtime> showtimes = new ArrayList<>();

    // Custom constructor for basic fields
    public Screen(Theater theater, String name, Integer capacity) {
        this.theater = theater;
        this.name = name;
        this.capacity = capacity;
    }

    // Helper methods
    public void addSeat(Seat seat) {
        seats.add(seat);
        seat.setScreen(this);
    }

    public void removeSeat(Seat seat) {
        seats.remove(seat);
        seat.setScreen(null);
    }

    public void addShowtime(Showtime showtime) {
        showtimes.add(showtime);
        showtime.setScreen(this);
    }

    public void removeShowtime(Showtime showtime) {
        showtimes.remove(showtime);
        showtime.setScreen(null);
    }
}
