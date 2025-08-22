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
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"seatReservations"})
@EqualsAndHashCode(callSuper = true, exclude = {"seatReservations"})
public class Reservation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull(message = "Showtime is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be positive")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @NotNull(message = "Status is required")
    @Column(name = "status", nullable = false, length = 20)
    private String status; // HELD, CONFIRMED, CANCELLED

    @Column(name = "hold_expiry")
    private LocalDateTime holdExpiry;

    // Relationships
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SeatReservation> seatReservations = new ArrayList<>();

    // Custom constructor for basic fields
    public Reservation(User user, Showtime showtime, BigDecimal totalAmount, String status) {
        this.user = user;
        this.showtime = showtime;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Helper methods
    public void addSeatReservation(SeatReservation seatReservation) {
        seatReservations.add(seatReservation);
        seatReservation.setReservation(this);
    }

    public void removeSeatReservation(SeatReservation seatReservation) {
        seatReservations.remove(seatReservation);
        seatReservation.setReservation(null);
    }

    // Business logic methods
    public boolean isExpired() {
        return holdExpiry != null && LocalDateTime.now().isAfter(holdExpiry);
    }

    public boolean isConfirmed() {
        return "CONFIRMED".equals(status);
    }

    public boolean isHeld() {
        return "HELD".equals(status);
    }

    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }
}
