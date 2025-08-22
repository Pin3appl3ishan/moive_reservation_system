package com.ishan.moviereservation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "seat_reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class SeatReservation extends BaseEntity {

    @NotNull(message = "Reservation is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @NotNull(message = "Seat is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @NotNull(message = "Showtime is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @NotNull(message = "Status is required")
    @Size(max = 20, message = "Status must not exceed 20 characters")
    @Column(name = "status", nullable = false, length = 20)
    private String status; // HELD, PAID, CANCELLED

    // Business logic methods
    public boolean isHeld() {
        return "HELD".equals(status);
    }

    public boolean isPaid() {
        return "PAID".equals(status);
    }

    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }
}
