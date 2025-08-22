package com.ishan.moviereservation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"seatReservations"})
@EqualsAndHashCode(callSuper = true, exclude = {"seatReservations"})
public class Seat extends BaseEntity {

    @NotNull(message = "Screen is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;

    @NotBlank(message = "Seat label is required")
    @Size(max = 16, message = "Seat label must not exceed 16 characters")
    @Column(name = "label", nullable = false, length = 16)
    private String label;

    @Size(max = 8, message = "Row label must not exceed 8 characters")
    @Column(name = "row_label", length = 8)
    private String rowLabel;

    @Column(name = "col")
    private Integer col;

    // Relationships
    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SeatReservation> seatReservations = new ArrayList<>();

    // Custom constructor for basic fields
    public Seat(Screen screen, String label, String rowLabel, Integer col) {
        this.screen = screen;
        this.label = label;
        this.rowLabel = rowLabel;
        this.col = col;
    }

    // Helper methods
    public void addSeatReservation(SeatReservation seatReservation) {
        seatReservations.add(seatReservation);
        seatReservation.setSeat(this);
    }

    public void removeSeatReservation(SeatReservation seatReservation) {
        seatReservations.remove(seatReservation);
        seatReservation.setSeat(null);
    }
}
