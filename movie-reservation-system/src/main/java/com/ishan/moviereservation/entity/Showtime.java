package com.ishan.moviereservation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "showtimes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"reservations", "seatReservations"})
@EqualsAndHashCode(callSuper = true, exclude = {"reservations", "seatReservations"})
public class Showtime extends BaseEntity {

    @NotNull(message = "Movie is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @NotNull(message = "Screen is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;

    @NotNull(message = "Start time is required")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @NotNull(message = "Ticket price is required")
    @Positive(message = "Ticket price must be positive")
    @Column(name = "ticket_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal ticketPrice;

    // Relationships
    @OneToMany(mappedBy = "showtime", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "showtime", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SeatReservation> seatReservations = new ArrayList<>();

    // Custom constructor for basic fields
    public Showtime(Movie movie, Screen screen, LocalDateTime startTime, LocalDateTime endTime, BigDecimal ticketPrice) {
        this.movie = movie;
        this.screen = screen;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ticketPrice = ticketPrice;
    }

    // Helper methods
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        reservation.setShowtime(this);
    }

    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
        reservation.setShowtime(null);
    }

    public void addSeatReservation(SeatReservation seatReservation) {
        seatReservations.add(seatReservation);
        seatReservation.setShowtime(this);
    }

    public void removeSeatReservation(SeatReservation seatReservation) {
        seatReservations.remove(seatReservation);
        seatReservation.setShowtime(null);
    }
}
